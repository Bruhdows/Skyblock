package me.bruhdows.skyblock.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    public static int randomInt(int min, int max) {
        return min + (int) (new Random().nextFloat() * (max - min));
    }

    public static double randomDouble(double min, double max) {
        return min + new Random().nextDouble() * (max - min);
    }

}
