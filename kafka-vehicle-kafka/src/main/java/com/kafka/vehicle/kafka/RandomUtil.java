package com.kafka.vehicle.kafka;

import java.util.Random;

public class RandomUtil {

    public static int getRandomNumberInRange(int min, int max) {

        Random r = new Random();
        return r.ints(min, (max + 1)).limit(1).findFirst().getAsInt();

    }
}
