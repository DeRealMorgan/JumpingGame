package com.jumping.game.game.math;

public class MathExercise {
    private final String exercise;
    private final String solution;

    public MathExercise(String exercise) {
        String[] parts = exercise.split("=");
        this.exercise = parts[0];
        this.solution = parts[1].trim();
    }

    public String getExercise() {
        return exercise;
    }

    public String getExerciseQuestion() {
        return exercise + "=?";
    }

    public String getSolution() {
        return solution;
    }

    public boolean isCorrect(String other) {
        return solution.equals(other);
    }

    @Override
    public String toString() {
        return "Exercise: " + exercise + "=" + solution;
    }
}
