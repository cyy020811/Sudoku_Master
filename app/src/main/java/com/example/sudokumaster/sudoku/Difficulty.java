package com.example.sudokumaster.sudoku;

import androidx.annotation.NonNull;

public enum Difficulty {
    EASY(35, 40),
    MEDIUM(45, 50),
    HARD(55, 50),
    CUSTOM(0, 0);

    private final int min;
    private final int max;

    Difficulty(int min, int max) {
        this.min = min;
        this.max = max;
    }

    // Randomly choose a number between min and max
    public int getBetweenMinMax() {
        int gap = max - min;
        return min + (int) Math.floor(Math.random() * gap + 1);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
