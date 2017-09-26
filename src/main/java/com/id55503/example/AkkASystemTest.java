package com.id55503.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.util.ArrayList;
import java.util.List;

public class AkkASystemTest {

    static class Tick {
        static final Tick TICK = new Tick();
    }

    static class Worker extends UntypedActor {
        @Override
        public void preStart() throws Exception {
            super.preStart();
            getContext().system().eventStream().subscribe(getSelf(), Tick.class);
        }

        @Override
        public void onReceive(Object message) throws Throwable {
//            System.out.println("worker" + message);
//            int number = ((String) message).length();
//            System.out.println("worker" + message);
//            System.currentTimeMillis();
//            message.getClass();
        }
    }

    static class Boss extends UntypedActor {
        List<ActorRef> workList = new ArrayList<>();

        @Override
        public void preStart() throws Exception {
            super.preStart();
            for (int i = 0; i < 100000; i++) {
                workList.add(getContext().actorOf(Props.create(Worker.class)));
            }
            for (ActorRef actorRef : workList) {
                getContext().watch(actorRef);
            }
        }

        @Override
        public void onReceive(Object message) throws Throwable {
            System.out.println("boss" + message);
            for (ActorRef worker : workList) {
                worker.tell(message, ActorRef.noSender());
            }
//            getContext().getChildren().forEach(actorRef -> actorRef.tell(message, ActorRef.noSender()));
        }
    }

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("AKKA");
        ActorRef root = system.actorOf(Props.create(Boss.class), "boss");
        System.out.println(root.path());
        for (int i = 0; i < 1000; i++) {
            root.tell(Tick.TICK, ActorRef.noSender());
//            system.eventStream().publish(Tick.TICK);
//            system.actorSelection("akka://AKKA/user/boss/*").tell(Tick.TICK,ActorRef.noSender());
//            system.actorSelection("akka://AKKA/user/boss").tell(Tick.TICK, ActorRef.noSender());
            try {
                Thread.sleep(1000);
//                System.out.println("===============================");
//                AkkASystemTest akkASystemTest = new AkkASystemTest();
//                System.out.println(akkASystemTest.getList(1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        system.shutdown();
    }

}
