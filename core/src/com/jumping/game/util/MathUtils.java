package com.jumping.game.util;

import java.util.concurrent.ThreadLocalRandom;

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
        return ThreadLocalRandom.current().nextInt(0, 100) < percent;
    }

    /**
     * Return random x position within world
     */
    public static float getRandomWorldX(int tileWidth) {
        return ThreadLocalRandom.current().nextInt(tileWidth/2, Values.WORLD_WIDTH-tileWidth/2 + 1);
    }

    public static int getRandomX(int from, int to) {
        return ThreadLocalRandom.current().nextInt(from, to + 1);
    }
}
