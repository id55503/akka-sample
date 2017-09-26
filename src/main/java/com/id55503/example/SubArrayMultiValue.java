package com.id55503.example;

import java.math.BigInteger;
import java.util.Random;

/**
 * 获得子数值最大乘积
 */
public class SubArrayMultiValue {

    private static int[] getRandomShortArray(int arrayLength) {
        int[] ints = new int[arrayLength];
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < arrayLength; i++) {
            ints[i] = random.nextInt();
        }
        return ints;
    }

    private static int getMaxMultiArray(int[] ints) {
        BigInteger[] lefts = new BigInteger[ints.length];
        BigInteger[] rights = new BigInteger[ints.length];

        lefts[0] = BigInteger.valueOf(1);
        for (int i = 1; i < ints.length; i++) {
            lefts[i] = lefts[i - 1].multiply(BigInteger.valueOf(ints[i - 1]));
        }

        rights[ints.length - 1] = BigInteger.valueOf(1);
        for (int i = ints.length - 2; i >= 0; i--) {
            rights[i] = rights[i + 1].multiply(BigInteger.valueOf(ints[i + 1]));
        }

//        for (int i = 0; i < lefts.length; i++) {
//            System.out.println("left" + i + " = " + lefts[i]);
//        }
//
//        for (int i = 0; i < rights.length; i++) {
//            System.out.println("right" + i + " = " + rights[i]);
//        }

        BigInteger[] removeIndex = new BigInteger[]{BigInteger.valueOf(-1), BigInteger.valueOf(Long.MIN_VALUE)};
        for (int i = 0; i < ints.length; i++) {
            BigInteger value = lefts[i].multiply(rights[i]);
//            System.out.println("value " + i + " = " + value);
            if (removeIndex[1].compareTo(value) < 0) {
                removeIndex = new BigInteger[]{BigInteger.valueOf(i), value};
            }
        }
        return removeIndex[0].intValue();
    }

    private static BigInteger[] loop(int[] ints) {
        BigInteger[] values = new BigInteger[ints.length];
        for (int i = 0; i < ints.length; i++) {
            BigInteger value = BigInteger.valueOf(1);
            for (int j = 0; j < ints.length; j++) {
                if (j != i) {
                    value = BigInteger.valueOf(ints[j]).multiply(value);
                }

            }
            values[i] = value;
//            System.out.println("loop-value" + i + " = " + value);
        }
        BigInteger[] removeIndex = new BigInteger[]{BigInteger.valueOf(-1), BigInteger.valueOf(Long.MIN_VALUE)};
        for (int i = 0; i < values.length; i++) {
            BigInteger value = values[i];
//            System.out.println("value " + i + " = " + value);
            if (removeIndex[1].compareTo(value) < 0) {
                removeIndex = new BigInteger[]{BigInteger.valueOf(i), value};
            }
        }
        return removeIndex;
    }

    private static BigInteger[] getMaxMultiRemoveIndex(int[] ints) {
        int zeroNumberIndex = -1;
        int zeroNumber = 0;

        int negativeNumber = 0;
        int[] minNegative = new int[]{-1, Integer.MAX_VALUE};
        int[] maxNegative = new int[]{-1, Integer.MIN_VALUE};

        int[] minPositive = new int[]{-1, Integer.MAX_VALUE};

        for (int i = 0; i < ints.length; i++) {
            int value = ints[i];
            if (value == 0) {
                zeroNumber++;
                if (zeroNumber == 1) {
                    zeroNumberIndex = i;
                }
            }
            if (value < 0) {
                negativeNumber++;
                if (value < minNegative[1]) {
                    minNegative = new int[]{i, value};
                }
                if (value > maxNegative[1]) {
                    maxNegative = new int[]{i, value};
                }
            }
            if (value > 0 && value < minPositive[1]) {
                minPositive = new int[]{i, value};
            }
        }
        int removeIndex = 0;
        if (zeroNumber > 1) {
            removeIndex = 0;
        }
        if (zeroNumberIndex > -1) {
            if (negativeNumber % 2 == 0) {
                removeIndex = zeroNumberIndex;
            } else {
                removeIndex = maxNegative[0];
            }
        }
        if (negativeNumber % 2 == 0) {
            if (minPositive[0] == -1) {
                removeIndex = minNegative[0];
            } else {
                removeIndex = maxNegative[0];
            }
        } else if (negativeNumber > 0) {
            removeIndex = maxNegative[0];
        } else {
            removeIndex = minPositive[0];
        }

        BigInteger total = BigInteger.ONE;
        for (int i = 0; i < ints.length; i++) {
            if (i != removeIndex) {
                total = total.multiply(BigInteger.valueOf(ints[i]));
            }
        }
        return new BigInteger[]{BigInteger.valueOf(removeIndex), total};
    }

    private static BigInteger[] getMaxResultSubArray(int[] ints) {
        BigInteger[] bigIntegers = new BigInteger[ints.length];
        for (int i = 0; i < ints.length; i++) {
            bigIntegers[i] = BigInteger.valueOf(ints[i]);
        }
        BigInteger[] left = new BigInteger[bigIntegers.length];
        BigInteger[] right = new BigInteger[bigIntegers.length];

        left[0] = BigInteger.ONE;
        for (int i = 1; i < left.length; i++) {
            left[i] = bigIntegers[i - 1].multiply(left[i - 1]);
        }
        right[right.length - 1] = BigInteger.ONE;
        for (int i = right.length - 2; i >= 0; i--) {
            right[i] = bigIntegers[i + 1].multiply(right[i + 1]);
        }

        BigInteger[] result = new BigInteger[]{BigInteger.valueOf(-1), BigInteger.valueOf(Long.MIN_VALUE)};

        for (int i = 0; i < bigIntegers.length; i++) {
            BigInteger value = right[i].multiply(left[i]);
            if (value.compareTo(result[1]) > 0) {
                result = new BigInteger[]{BigInteger.valueOf(i), value};
            }
        }

        System.out.println("big result = " + result[1] + " remove index = " + result[0]);
        BigInteger[] subArray = new BigInteger[bigIntegers.length - 1];
        System.arraycopy(bigIntegers, 0, subArray, 0, result[0].intValue());
        System.arraycopy(bigIntegers, result[0].intValue() + 1, subArray, result[0].intValue(), bigIntegers.length - result[0].intValue() - 1);
        return subArray;
    }

    private static BigInteger[] getMaxReusltSubArray2(int[] ints) {
        int zeroNumber = 0;
        int zeroIndex = -1;
        int negativeNumber = 0;
        int[] maxNegativeNumber = new int[]{-1, Integer.MIN_VALUE};
        int[] minPositiveNumber = new int[]{-1, Integer.MAX_VALUE};
        int[] minAbsNumber = new int[]{-1, Integer.MAX_VALUE};
        for (int i = 0; i < ints.length; i++) {
            if (ints[i] == 0) {
                zeroNumber++;
                zeroIndex = i;
            } else if (ints[i] < 0) {
                negativeNumber++;
                if (ints[i] > maxNegativeNumber[1]) {
                    maxNegativeNumber = new int[]{i, ints[i]};
                }
                if (-ints[i] < minAbsNumber[1]) {
                    minAbsNumber = new int[]{i, -ints[i]};
                }
            } else {
                if (ints[i] < minPositiveNumber[1]) {
                    minPositiveNumber = new int[]{i, ints[i]};
                }
                if (ints[i] < minAbsNumber[1]) {
                    minAbsNumber = new int[]{i, ints[i]};
                }
            }

        }

        int removeIndex;
        if (zeroNumber > 1) {
            removeIndex = 0;
        } else if (zeroNumber == 1) {
            if (negativeNumber == 0) {
                removeIndex = minPositiveNumber[0];
            } else {
                if (negativeNumber % 2 == 0) {
                    removeIndex = zeroIndex;
                } else {
                    removeIndex = maxNegativeNumber[0];
                }
            }
        } else {
            if (negativeNumber == 0) {
                removeIndex = minPositiveNumber[0];
            } else {
                if (negativeNumber % 2 == 0) {
                    removeIndex = minPositiveNumber[0];
                } else {
                    removeIndex = maxNegativeNumber[0];
                }
            }
        }
        BigInteger bigInteger = BigInteger.ONE;
        for (int i = 0; i < ints.length; i++) {
            if (i != removeIndex) {
                bigInteger = bigInteger.multiply(BigInteger.valueOf(ints[i]));
            }
        }
        return new BigInteger[]{BigInteger.valueOf(removeIndex), bigInteger};
    }

    public static void main(String[] args) {
        int[] ints = new int[]{-2, 9, -9, -8, -9, -19, 99};
//        ints = getRandomShortArray(1000);
        long lastTime = System.currentTimeMillis();
        int removeIndex = getMaxMultiArray(ints);
        long time1 = System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        BigInteger[] removeIndexUseLoop = loop(ints);
        long time2 = System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
//        BigInteger[] removeIndexUse2 = getMaxMultiRemoveIndex(ints);
//        long time3 = System.currentTimeMillis() - lastTime;
        for (BigInteger bigInteger : getMaxResultSubArray(ints)) {
            System.out.println(bigInteger);
        }
        for (int number : SubArrayMultiMaxResult.getSubArrayMultMaxResult(ints)) {
            System.out.println(number);
        }
        for (int number : SubArrayMultiMaxResult.getMaxMultiSubArray(ints)) {
            System.out.println(number);
        }
        BigInteger[] subArray2 = getMaxReusltSubArray2(ints);
//        System.out.println("removeIndex " + removeIndex);
//        System.out.println("removeIndexUse " + removeIndexUse2[0] + " use time " + time3 + " " + removeIndexUse2[1]);
        System.out.println("removeIndexUseLoop " + removeIndexUseLoop[0] + " " + removeIndexUseLoop[1]);
        System.out.println("subArray2 " + subArray2[0] + " " + subArray2[1]);
//        System.out.println("subArray " + subArray[0] + " " + subArray[1]);
//        System.out.println(Integer.MAX_VALUE * Integer.MIN_VALUE);


    }

}
