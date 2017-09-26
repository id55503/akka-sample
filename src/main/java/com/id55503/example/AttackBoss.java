package com.id55503.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AttackBoss {
    public static void main(String[] args) {
        long lastTime = System.currentTimeMillis();
        List<Player> players = new ArrayList<>();
        Boss boss = new Boss();
        for (int i = 0; i < 10; i++) {
            players.add(new Player(i, i));
        }
        AtomicInteger stopPlayerNumber = new AtomicInteger(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (Player player : players) {
            executorService.execute(() -> {
                while (true) {
                    boolean b = player.attackBoss(boss);
                    if (!b) {
                        System.out.println("boss is dead playerId " + player.id);
                        break;
                    }
//                    try {
//                        Thread.sleep(0);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    Thread.yield();
                }

                System.out.println("stop playerId " + player.id);
                if (stopPlayerNumber.decrementAndGet() == 0) {
                    executorService.shutdown();
                    System.out.println(System.currentTimeMillis() - lastTime);
                }
            });
        }
    }
}

class Boss {
    AtomicLong hp = new AtomicLong(50000L);
}

class Player {

    private long attack;
    int id;

    Player(int id, long attack) {
        this.id = id;
        this.attack = attack;
    }

    boolean attackBoss(Boss boss) {
        long current = boss.hp.getAndAdd(-attack);
        if (current <= attack) {
            if (current <= 0) {
                return false;
            } else {
                System.out.println("=================>kill boss playerId " + id);
            }
        }
        System.out.println("attack boss playerId " + id + " " + current + " -> " + (current - attack));
        return true;
    }
}
