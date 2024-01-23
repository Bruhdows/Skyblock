package me.bruhdows.skyblock.util;

import java.util.Random;

public class RandomUtil {

    public static int randomInt(int min, int max) {
        return min + (int) (new Random().nextFloat() * (max - min));
    }

    public static double randomDouble(double min, double max) {
        return min + new Random().nextDouble() * (max - min);
    }

    public static boolean chanceOf(double chance) {
        double random = RandomUtil.randomDouble(0, 100);
        return random < chance;
    }

}
