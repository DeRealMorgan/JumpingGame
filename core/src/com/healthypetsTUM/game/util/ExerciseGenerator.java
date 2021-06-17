package com.healthypetsTUM.game.util;

public class ExerciseGenerator {
    public static void main(String[] args) {
        generateSimpleExercisesDiv();
    }

    public static void generateSimpleExercisesAdd() {
        for (int i = 1; i <= 500; i += 10) {
            final int finalI = i;
            if (MathUtils.getTrue(0.5f)) i += 2;
            if (MathUtils.getTrue(0.5f)) i += 3;
            if (MathUtils.getTrue(0.5f)) i += 2;

            for (int j = 1; j <= 50; j += 4) {
                final int finalJ = j;
                if (MathUtils.getTrue(0.5f)) ++j;
                if (MathUtils.getTrue(0.5f)) ++j;
                if (MathUtils.getTrue(0.5f)) ++j;

                System.out.println(i + "+" + j + "=" + (i + j));

                j = finalJ;
            }

            i = finalI;
        }
    }

    public static void generateSimpleExercisesSub() {
        for(int i = 1; i <= 500; i += 10) {
            final int finalI = i;

            if(MathUtils.getTrue(0.5f)) ++i;
            if(MathUtils.getTrue(0.5f)) ++i;
            if(MathUtils.getTrue(0.5f)) ++i;
            if(MathUtils.getTrue(0.5f)) ++i;

            for(int j = 1; j <= 50; j += 3) {
                final int finalJ = j;

                if(MathUtils.getTrue(0.5f)) ++j;
                if(MathUtils.getTrue(0.5f)) ++j;

                System.out.println(i + "-" + j + "=" + (i - j));
                j = finalJ;
            }

            i = finalI;
        }
        for(int i = 1; i <= 50; i += 3) {
            final int finalI = i;

            if(MathUtils.getTrue(0.5f)) ++i;
            if(MathUtils.getTrue(0.5f)) ++i;

            for(int j = 1; j <= 500; j += 10) {
                final int finalJ = j;

                if(MathUtils.getTrue(0.5f)) ++j;
                if(MathUtils.getTrue(0.5f)) ++j;
                if(MathUtils.getTrue(0.5f)) ++j;

                System.out.println(i + "-" + j + "=" + (i - j));

                j = finalJ;
            }

            i = finalI;
        }
    }

    public static void generateSimpleExercisesMul() {
        for(int i = 1; i <= 12; ++i) {
            for(int j = 1; j <= 12; ++j) {
                System.out.println(i + "*" + j + "=" + (i * j));
            }
        }

        for(int i = 12; i <= 25; ++i) {
            if(MathUtils.getTrue(0.5f)) ++i;
            for(int j = 1; j <= 10; ++j) {
                if (MathUtils.getTrue(0.5f)) ++j;
                System.out.println(i + "*" + j + "=" + (i * j));
            }
            System.out.println(i + "*" + i + "=" + (i * i));
        }
    }

    public static void generateSimpleExercisesDiv() {
        for(int i = 1; i <= 250; ++i) {
            for(int j = 2; j <= 50; ++j) {
                if(j > i) continue;
                if(i%j != 0) continue;
                System.out.println(i + "/" + j + "=" + (i / j));
            }
        }
    }
}
