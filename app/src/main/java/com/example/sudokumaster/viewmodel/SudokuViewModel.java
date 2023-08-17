package com.example.sudokumaster.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.sudokumaster.model.SudokuGame;
import com.example.sudokumaster.sudoku.Difficulty;

public class SudokuViewModel extends ViewModel {

    private SudokuGame sudokuGame;

    public void createSudokuGame(Difficulty difficulty) {
        sudokuGame = new SudokuGame(difficulty);
    }

    public SudokuGame getSudokuGame() {
        return sudokuGame;
    }
}
