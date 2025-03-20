package com.hopoong.post.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    public static long getRandomUserId() {
        return ThreadLocalRandom.current().nextLong(1, 11);
    }

    public static int getRandomIntValue() {
        Random random = new Random();
        return random.nextInt(100000) + 1;
    }

}
