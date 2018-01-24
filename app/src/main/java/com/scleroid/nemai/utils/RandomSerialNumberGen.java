package com.scleroid.nemai.utils;

import java.util.Random;

/**
 * Created by Ganesh on 21-12-2017.
 */

public class RandomSerialNumberGen {
    public static long getRandomSerialNo() {
        return (long) new Random().nextInt(Integer.MAX_VALUE);
    }
}
