package com.healthypetsTUM.game.util;

public class ExerciseGenerator {
    public static void main(String[] args) {
        generateSimpleExercisesDiv();
    }

    public static void generateSimpleExercisesAdd() {
        for (int i = 0; i <= 100; i += 1) {
            final int finalI = i;

            for (int j = 0; j <= 100; j += 1) {
                final int finalJ = j;

                if(i+j > 100) break;
                System.out.println(i + " + " + j + " = " + (i + j));

                j = finalJ;
            }

            i = finalI;
        }

        for (int i = 100; i <= 500; i += 50) {
            final int finalI = i;

            for (int j = 50; j <= 500; j += 50) {
                final int finalJ = j;

                if(i + j > 1000) break;
                System.out.println(i + " + " + j + " = " + (i + j));

                j = finalJ;
            }

            i = finalI;
        }
    }

    public static void generateSimpleExercisesSub() {
        for(int i = 100; i >= 0; i--) {
            final int finalI = i;

            for(int j = 0; j <= 100; j += 1) {
                final int finalJ = j;

                if(i - j < 0) break;

                System.out.println(i + " - " + j + " = " + (i - j));
                j = finalJ;
            }

            i = finalI;
        }

        for(int i = 150; i <= 1000; i += 50) {
            final int finalI = i;

            for(int j = 0; j <= 1000; j += 50) {
                final int finalJ = j;

                if(i-j < 0) break;
                System.out.println(i + " - " + j + " = " + (i - j));

                j = finalJ;
            }

            i = finalI;
        }
    }

    public static void generateSimpleExercisesMul() {
        for(int i = 0; i <= 10; ++i) {
            for(int j = 0; j <= 10; ++j) {
                System.out.println(i + " * " + j + " = " + (i * j));
            }
        }
    }

    public static void generateSimpleExercisesDiv() {
        for(int i = 0; i <= 100; ++i) {
            for(int j = 1; j <= 10; ++j) {
                if(j > i) continue;
                if(i%j != 0) continue;
                System.out.println(i + " / " + j + " = " + (i / j));
            }
        }
    }
}
