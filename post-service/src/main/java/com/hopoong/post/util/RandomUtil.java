package com.hopoong.post.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    public static long getRandomUserId() {
        return ThreadLocalRandom.current().nextLong(1, 11);
    }

}
