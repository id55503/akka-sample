package cn.qgame.example.akka.streams;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.ThrottleMode;
import akka.stream.javadsl.*;
import akka.util.ByteString;
import scala.concurrent.duration.Duration;

import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * Created by DongLei on 2016/11/15.
 */
public class SinkExample1 {

    public Sink<String, CompletionStage<IOResult>> lineSink(String filename) {
        return Flow.of(String.class)
                .map(s -> ByteString.fromString(s + "\n"))
                .toMat(FileIO.toPath(Paths.get(filename)), Keep.right());
    }

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("SinkExample1");

        Materializer materializer = ActorMaterializer.create(system);

        SinkExample1 sinkExample1 = new SinkExample1();
        Source<Integer, NotUsed> source = Source.range(1, 5);
//        source.runForeach(i -> System.out.println(i), materializer);
        Source<BigInteger, NotUsed> factorials =
                source
                        .scan(BigInteger.ONE, (acc, next) -> acc.multiply(BigInteger.valueOf(next)));

//        CompletionStage<IOResult> result =
//                factorials
//                        .map(num -> ByteString.fromString(num.toString() + "\n"))
//                        .runWith(FileIO.toPath(Paths.get("factorials.txt")), materializer);

        factorials.map(BigInteger::toString).runWith(sinkExample1.lineSink("factorial2.txt"), materializer);

        factorials
                .zipWith(Source.range(0,10),(num,idx)->String.format("%d! = %s",idx,num))
                .throttle(1, Duration.create(1, TimeUnit.SECONDS),1, ThrottleMode.shaping())
                .runForeach(s->System.out.println(s),materializer);

    }

}
