package com.id55503.example;

import java.util.LinkedList;
import java.util.List;

public class PrimeNumberRange {
    public static void main(String[] args) {
        long starting_number = 2L;
        long ending_number = 10000L;
        long totals = 0;
        long lastTime = System.currentTimeMillis();
        System.out.println("List of prime numbers between " + starting_number + " and " + ending_number);

        for (long current = starting_number; current <= ending_number; current++) {
            long sqr_root = (long) Math.sqrt(current);
            boolean is_prime = true;
            for (long i = 2; i <= sqr_root; i++) {
                if (current % i == 0) {
                    is_prime = false; // Current is not prime.
                }
            }
            if (is_prime) {
//                System.out.println(current);
                totals++;
            }
        }
        System.out.println("There are a total of " + totals + " prime numbers between " + starting_number + " and " + ending_number);
        System.out.println(System.currentTimeMillis() - lastTime);

        getPrimeNumber(1, 20).forEach(integer -> System.out.println("prime " + integer));
        PrimeNumberInRange.getPrimeArray(1,20).forEach(integer -> System.out.println("prime " + integer));
    }


    private static List<Integer> getPrimeNumber(int start, int end) {
        List<Integer> result = new LinkedList<>();

        for (int current = start; current < end; current++) {
            int number = (int) Math.sqrt(current);
            boolean isPrime = true;
            for (int i = 2; i <= number; i++) {
                if (current % i == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                result.add(current);
            }
        }

        return result;
    }
}
