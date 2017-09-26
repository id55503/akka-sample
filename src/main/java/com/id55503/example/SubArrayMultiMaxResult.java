package com.id55503.example;

import java.math.BigInteger;

public class SubArrayMultiMaxResult {

    static int[] getSubArrayMultMaxResult(int[] ints) {

        int zeroNumber = 0;
        int zeroIndex = -1;
        int neNumber = 0;
//        int[] minNeIndex = new int[]{-1, Integer.MAX_VALUE};
        int[] minPoIndex = new int[]{-1, Integer.MAX_VALUE};
        int[] maxNeIndex = new int[]{-1, Integer.MIN_VALUE};

        for (int i = 0; i < ints.length; i++) {
            int value = ints[i];
            if (value == 0) {
                zeroNumber++;
                zeroIndex = i;
            } else if (value < 0) {
                neNumber++;
                if (maxNeIndex[1] < value) {
                    maxNeIndex = new int[]{i, value};
                }
            } else {
                if (minPoIndex[1] > value) {
                    minPoIndex = new int[]{i, value};
                }
            }
        }

        int removeIndex;

        if (zeroNumber > 1) {
            removeIndex = 0;
        } else if (zeroNumber == 1) {
            if (neNumber % 2 == 0) {
                removeIndex = zeroIndex;
            } else {
                removeIndex = maxNeIndex[0];
            }
        } else {
            if (neNumber == 0) {
                removeIndex = minPoIndex[0];
            } else if (neNumber % 2 == 0) {
                removeIndex = minPoIndex[0];
            } else {
                removeIndex = maxNeIndex[0];
            }
        }
        int[] resultArray = new int[ints.length - 1];
        System.arraycopy(ints, 0, resultArray, 0, removeIndex);
        System.arraycopy(ints, removeIndex + 1, resultArray, removeIndex, ints.length - removeIndex - 1);
        BigInteger total = BigInteger.ONE;
        for (int value : resultArray) {
            total = total.multiply(BigInteger.valueOf(value));
        }
        System.out.println("total " + total + " removeIndex " + removeIndex);
        return resultArray;
    }


    static int[] getMaxMultiSubArray(int[] ints) {

        int zeroNumber = 0;
        int zeroIndex = -1;
        int neNumber = 0;
        int[] neMaxNumber = new int[]{-1, Integer.MIN_VALUE};
        int[] poMinNumber = new int[]{-1, Integer.MAX_VALUE};

        for (int i = 0; i < ints.length; i++) {
            int value = ints[i];
            if (value == 0) {
                zeroNumber++;
                zeroIndex = i;
            } else if (value < 0) {
                neNumber++;
                if (value > neMaxNumber[1]) {
                    neMaxNumber = new int[]{i, value};
                }
            } else {
                if (value < poMinNumber[1]) {
                    poMinNumber = new int[]{i, value};
                }
            }
        }

        int removeIndex = -1;
        if (zeroNumber > 1) {
            removeIndex = 0;
        } else if (zeroNumber == 1) {
            if (neNumber == 0) {
                removeIndex = zeroIndex;
            } else if (neNumber % 2 == 0) {
                removeIndex = zeroIndex;
            } else {
                removeIndex = neMaxNumber[0];
            }
        } else {
            if (neNumber == 0) {
                removeIndex = poMinNumber[0];
            } else if (neNumber % 2 == 0) {
                removeIndex = poMinNumber[0];
            } else {
                removeIndex = neMaxNumber[0];
            }
        }

        int[] subArray = new int[ints.length - 1];
        System.arraycopy(ints, 0, subArray, 0, removeIndex);
        System.arraycopy(ints, removeIndex, subArray, removeIndex, ints.length - removeIndex - 1);
        return ints;
    }

}
