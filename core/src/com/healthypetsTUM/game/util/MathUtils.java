package com.healthypetsTUM.game.util;

import java.util.Random;

public class MathUtils {
    public static boolean isInDelta(float fst, float snd, float delta) {
        return Math.abs(fst - snd) <= delta;
    }

    /**
     * Returns true for a given probability
     * Probability of 0.5 returns true every other time statistically
     */
    public static boolean getTrue(float probability) {
        int percent = (int)(probability*100);
        return new Random().nextInt(100) < percent;
    }

    /**
     * Return random x position within world
     */
    public static float getRandomWorldX(int tileWidth) {
        return tileWidth/2 + new Random().nextInt(Values.WORLD_WIDTH-tileWidth/2 + 1 - tileWidth/2);
    }

    public static int getRandomX(int from, int to) {
        return from + new Random().nextInt( to + 1 - from);
    }
}
