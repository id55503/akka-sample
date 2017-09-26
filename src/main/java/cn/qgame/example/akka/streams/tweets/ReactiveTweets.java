package cn.qgame.example.akka.streams.tweets;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.japi.function.Function;
import akka.stream.*;
import akka.stream.javadsl.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

/**
 * Created by DongLei on 2016/4/27.
 */
public class ReactiveTweets {

    public static class Author {
        final String handle;

        public Author(String handle) {
            this.handle = handle;
        }
    }

    public static class Hashtag {
        final String name;

        public Hashtag(String name) {
            this.name = name;
        }
    }

    public static class Tweet {
        public final Author author;
        public final long timestamp;
        public final String body;

        public Tweet(Author author, long timestamp, String body) {
            this.author = author;
            this.timestamp = timestamp;
            this.body = body;
        }

        public Set<Hashtag> hashtags() {
            return Arrays.stream(body.split(" "))
                    .filter(a -> a.startsWith("#"))
                    .map(Hashtag::new)
                    .collect(Collectors.toSet());
        }
    }

    public static final Hashtag AKKA = new Hashtag("#akka");
    public final ActorSystem system = ActorSystem.create("reactive-tweets");
    public final Materializer mat = ActorMaterializer.create(system);
    public Source<Tweet, NotUsed> tweets;
    final Source<Author, NotUsed> authors = tweets.filter(t -> t.hashtags().contains(AKKA)).map(t -> t.author);

    public void printlnSink() {
        CompletionStage<Done> completionStage = authors.runWith(Sink.foreach(System.out::println), mat);
        System.out.println(completionStage);
    }

    public void println() {
        CompletionStage<Done> completionStage = authors.runForeach(System.out::println, mat);
        System.out.println(completionStage);
    }

    public void broadcastStream() {
        Sink<Author, CompletionStage<Done>> writeAuthors = Sink.foreach(System.out::println);
        Sink<Hashtag, CompletionStage<Done>> writeHashtags = Sink.foreach(System.out::println);
        RunnableGraph.fromGraph(GraphDSL.create(b -> {
            final UniformFanOutShape<Tweet, Tweet> bcast = b.add(Broadcast.create(2));
            final FlowShape<Tweet, Author> toAuthors = b.add(Flow.of(Tweet.class).map((Function<Tweet, Author>) param -> param.author));
            final FlowShape<Tweet, Hashtag> toHashtags = b.add(Flow.of(Tweet.class).mapConcat((Function<Tweet, Iterable<Hashtag>>) param -> new ArrayList<Hashtag>(param.hashtags())));
            final SinkShape<Author> authors = b.add(writeAuthors);
            final SinkShape<Hashtag> hashtags = b.add(writeHashtags);
            b.from(b.add(tweets)).viaFanOut(bcast).via(toAuthors).to(authors);
            b.from(bcast).via(toHashtags).to(hashtags);
            return ClosedShape.getInstance();
        })).run(mat);
    }

    final Sink<Integer, CompletionStage<Integer>> sumSink = Sink.<Integer, Integer>fold(0, (acc, elem) -> acc + elem);
    final RunnableGraph<CompletionStage<Integer>> counter = tweets.map(t -> 1).toMat(sumSink, Keep.right());
    final CompletionStage<Integer> sum = counter.run(mat);


}
