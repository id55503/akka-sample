package com.id55503.example;

import java.util.ArrayList;
import java.util.List;

public class PrimeNumberInRange {

    static List<Integer> getPrimeArray(int start, int end) {
        List<Integer> primeList = new ArrayList<>();
        for (int current = start; current < end; current++) {
            int sqrtValue = (int) Math.sqrt(current);
            boolean isPrime = true;
            for (int i = 2; i < sqrtValue; i++) {
                if (current % i == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                primeList.add(current);
            }

        }
        return primeList;
    }
}
