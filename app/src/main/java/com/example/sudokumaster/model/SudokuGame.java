package com.example.sudokumaster.model;

import android.util.Pair;

import androidx.lifecycle.MutableLiveData;

import com.example.sudokumaster.sudoku.Board;
import com.example.sudokumaster.sudoku.Cell;
import com.example.sudokumaster.sudoku.Difficulty;
import com.example.sudokumaster.sudoku.Generator;

import java.util.ArrayList;

public class SudokuGame {

    private final int SIZE = 9;
    private final int SQRT_SIZE = 3;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private final MutableLiveData<Pair<Integer, Integer>> selectedCellLiveData;
    private final MutableLiveData<Cell[][]> cellsLiveData;
    private final MutableLiveData<Boolean> isCompleted;
    private final Board board;
    private final Board answerBoard;
    private final Generator generator = Generator.getInstance();

    public SudokuGame(Difficulty difficulty) {
        selectedCellLiveData = new MutableLiveData<>();
        cellsLiveData = new MutableLiveData<>();
        isCompleted = new MutableLiveData<>();
        board = new Board(SIZE);
        answerBoard = new Board(SIZE);
        board.setCells();
        answerBoard.setCells();
        copyBoard(answerBoard, generator.generateSudoku(difficulty));
        copyBoard(board, generator.removeCells(difficulty));
        selectedCellLiveData.postValue(new Pair(selectedRow, selectedCol));
        cellsLiveData.postValue(board.getCells());
        isCompleted.postValue(false);
    }

    // Update the selectedRow and selectedCol and post the values back to update the view
    public void updateSelectedCellData(int row, int col) {
        if (row == -1 || col == -1 || !board.getCell(row, col).isStartingCell()) {
            selectedRow = row;
            selectedCol = col;
            selectedCellLiveData.postValue(new Pair(selectedRow, selectedCol));
        }
    }

    // Update the view with the new user input data
    public void handleInput(int data) {
        if (selectedRow == -1 && selectedCol == -1) return;
        board.getCell(selectedRow, selectedCol).setData(data);
        isCompleted.postValue(isSudokuCompleted());
        cellsLiveData.postValue(board.getCells());
    }

    public MutableLiveData<Pair<Integer, Integer>> getSelectedCellLiveData() {
        return selectedCellLiveData;
    }

    public MutableLiveData<Cell[][]> getCellsLiveData() {
        return cellsLiveData;
    }

    public MutableLiveData<Boolean> getIsCompleted() {
        return isCompleted;
    }

    public Cell[][] getCells() {
        return board.getCells();
    }

    public void copyBoard(Board myBoard, Board targetBoard) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                myBoard.getCell(i, j).copyCell(targetBoard.getCell(i, j));
            }
        }
    }

    public void showHint() {
        ArrayList<Cell> emptyCells = new ArrayList<>();
        ArrayList<Cell> answeredCells = new ArrayList<>();
        Cell cell, hint;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cell = board.getCell(i, j);
                if (cell.getData() == Board.EMPTY) {
                    emptyCells.add(answerBoard.getCell(i, j));
                } else if (!cell.isStartingCell()) {
                    answeredCells.add(answerBoard.getCell(i, j));
                }
            }
        }
        if (emptyCells.size() != 0) {
            hint = emptyCells.get((int) Math.floor(Math.random() * emptyCells.size()));
        } else {
            hint = answeredCells.get((int) Math.floor(Math.random() * answeredCells.size()));
        }
        selectedRow = hint.getRow();
        selectedCol = hint.getCol();
        handleInput(hint.getData());
    }

    public void showAnswer() {
        Cell answerCell;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                answerCell = answerBoard.getCell(i, j);
                if (!board.getCell(i, j).isStartingCell()) {
                    board.getCell(i, j).setData(answerCell.getData());
                }
            }
        }
        cellsLiveData.postValue(board.getCells());
        isCompleted.postValue(true);
    }

    private boolean unusedInBox(int row, int col, int data) {
        if (data == Board.EMPTY) return true;
        int rowStart = row / 3 * 3, colStart = col / 3 * 3;
        for (int i = 0; i < SQRT_SIZE; i++) {
            for (int j = 0; j < SQRT_SIZE; j++) {
                if (row != rowStart + i && col != colStart + j && board.getCell(rowStart + i, colStart + j).getData() == data)
                    return false;
            }
        }
        return true;
    }

    private boolean unusedInCol(int row, int col, int data) {
        if (data == Board.EMPTY) return true;
        for (int i = 0; i < SIZE; i++) {
            if (i != row && board.getCell(i, col).getData() == data) return false;
        }
        return true;
    }

    private boolean unusedInRow(int row, int col, int data) {
        if (data == Board.EMPTY) return true;
        for (int i = 0; i < SIZE; i++) {
            if (i != col && board.getCell(row, i).getData() == data) return false;
        }
        return true;
    }

    private boolean isLegal(int row, int col, int data) {
        return unusedInBox(row, col, data) && unusedInRow(row, col, data) && unusedInCol(row, col, data);
    }

    // Set illegal cells to violated and check if the game is completed
    private boolean isSudokuCompleted() {
        int data;
        Cell cell;
        boolean isCompleted = true;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cell = board.getCell(i, j);
                data = cell.getData();
                if (!cell.isStartingCell()) {
                    if (data == Board.EMPTY) {
                        isCompleted = false;
                        cell.setViolated(false);
                    } else if (!isLegal(i, j, data)) {
                        cell.setViolated(true);
                        isCompleted = false;
                    } else {
                        cell.setViolated(false);
                    }
                }
            }
        }
        return isCompleted;
    }
}
