package com.del.ministry.utils;

import com.del.ministry.db.Building;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.stream.StreamSupport;

public class DistrictGenerator {


    public static void main(String[] args) {
        BitSet bitSet = prepareAvailable(1, 93, new int[]{3, 4, 5, 6, 7, 15, 16, 22, 23}, 2, 5, 2);
        System.out.println(bitSet);
    }

    public static int rndDoor(Building building, int floorsMax, int dispersion, int[] excludes, List<Integer> prepared) {
        int[] numbers = prepareAvailable(1, building.getDoors(), excludes,
                Math.max(floorsMax, 1),
                Math.max(building.getFloors(), 1),
                Math.max(building.getEntrances(), 1)
        ).stream().toArray();
        return numbers[new Random().nextInt(numbers.length)];
    }

    public static BitSet prepareAvailable(int from, int to, int[] excludes, int floorsMax, int floors, int entrances) {
        BitSet countBS = new BitSet();
        countBS.set(from, to + 1);
        if (excludes != null) {
            BitSet excludesBS = new BitSet();
            StreamSupport.stream(Arrays.spliterator(excludes), false).forEach(excludesBS::set);
            excludesBS.and(countBS);
            countBS.andNot(excludesBS);
        }
        if (floorsMax < floors) {
            BitSet floorsBS = new BitSet();
            for (int enter = 1; enter <= entrances; enter++) {
                BigDecimal d1 = new BigDecimal(to - from + 1, MathContext.DECIMAL64);
                BigDecimal d2 = new BigDecimal(entrances * floors, MathContext.DECIMAL64);
                BigDecimal d3 = new BigDecimal(entrances, MathContext.DECIMAL64);
                int maxDoorsPerEnter = d1.divide(d2, 0, BigDecimal.ROUND_UP).intValue() * floorsMax;
                int doorsPerEnter = d1.divide(d3, 0, BigDecimal.ROUND_UP).intValue();
                int firstDoor = from + (doorsPerEnter * (enter - 1));
                System.out.println(firstDoor + " - " + (firstDoor + maxDoorsPerEnter - 1));
                floorsBS.set(firstDoor, firstDoor + maxDoorsPerEnter);
            }
            countBS.and(floorsBS);
        }
        return countBS;
    }

}
