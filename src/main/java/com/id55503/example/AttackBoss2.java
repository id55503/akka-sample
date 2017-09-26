package com.id55503.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AttackBoss2 {

    static class Player {
        final int attack;
        final int id;

        public Player(int attack, int id) {
            this.attack = attack;
            this.id = id;
        }

        boolean attackBoss(AttackBoss2.Boss boss) {
            long currentHp = boss.hp.getAndAdd(-attack);
            if (currentHp > 0) {
                if (currentHp <= attack) {
                    System.out.println("kill boss Player id = " + id);
                }
                System.out.println("success Player id = " + id + " " + currentHp + " -> " + (currentHp - attack));
                return true;
            } else {
                System.out.println("failed Player id = " + id + " " + currentHp + " -> " + (currentHp - attack));
                return false;
            }
        }
    }

    static class Boss {
        final AtomicLong hp;

        public Boss(AtomicLong hp) {
            this.hp = hp;
        }
    }

    public static void main(String[] args) {
        List<Player> playerList = new ArrayList<>();
        AttackBoss2.Boss boss = new AttackBoss2.Boss(new AtomicLong(100000L));
        for (int i = 0; i < 10; i++) {
            playerList.add(new Player(i, i));
        }
        AtomicInteger countDown = new AtomicInteger(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (Player player : playerList) {
            executorService.execute(() -> {
                while (true) {
                    boolean success = player.attackBoss(boss);
                    if (!success) {
                        break;
                    }
                }
                if (countDown.decrementAndGet() == 0) {
                    executorService.shutdown();
                }
            });
        }
    }
}


