package cn.qgame.example.akka.streams;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.ThrottleMode;
import akka.stream.javadsl.*;
import akka.util.ByteString;
import scala.concurrent.duration.Duration;

import java.io.File;
import java.math.BigInteger;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * Created by DongLei on 2016/4/27.
 */
public class AKKAStreams {

    final ActorSystem system = ActorSystem.create("QuickStart");

    final Materializer materializer = ActorMaterializer.create(system);

    void print() {
        final Source<Integer, NotUsed> source = Source.range(1, 100);
        source.runForeach(System.out::println, materializer);
    }

    void factorials() {
        final Source<Integer, NotUsed> source = Source.range(1, 2);
        final Source<BigInteger, NotUsed> factorials = source.scan(BigInteger.ONE, (acc, next) -> acc.multiply(BigInteger.valueOf(next)));
        final CompletionStage<IOResult> result = factorials.map(num -> ByteString.fromString(num.toString() + "\n")).runWith(FileIO.toFile(new File("factorials.txt")), materializer);
        result.whenCompleteAsync((ioResult, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            } else {
                System.out.println(ioResult);
            }

        });
    }

    public Sink<String, CompletionStage<IOResult>> lineSink(String filename) {
        return Flow.of(String.class).map(s -> ByteString.fromString(s.toString() + "\n"))
                .toMat(FileIO.toFile(new File(filename)), Keep.right());
    }

    void factorials2() {
        final Source<Integer, NotUsed> source = Source.range(1, 2);
        final Source<BigInteger, NotUsed> factorials = source.scan(BigInteger.ONE, (acc, next) -> acc.multiply(BigInteger.valueOf(next)));
        factorials.map(acc -> acc.toString() + " -> ").runWith(lineSink("factorial2.txt"), materializer);
    }

    void timeBaseProcessing() {
        final Source<Integer, NotUsed> source = Source.range(1, 20);
        final Source<BigInteger, NotUsed> factorials = source.scan(BigInteger.ONE, (acc, next) -> acc.multiply(BigInteger.valueOf(next)));
        final CompletionStage<Done> done = factorials.zipWith(Source.range(0, 5), (num, idx) -> String.format("%d! = %s", idx, num))
                .throttle(1, Duration.create(1, TimeUnit.SECONDS), 1, ThrottleMode.shaping())
                .runForeach(System.out::println, materializer);
        done.whenCompleteAsync((d, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            } else {
                System.out.println(d);
            }

        });
    }

    public static void main(String[] args) {
//        System.out.println(new File("factorials.txt").getPath());
        AKKAStreams akkaStreams = new AKKAStreams();
        akkaStreams.print();
        akkaStreams.factorials();
        akkaStreams.factorials2();
        akkaStreams.timeBaseProcessing();
        try {
            Thread.sleep(10000);
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
