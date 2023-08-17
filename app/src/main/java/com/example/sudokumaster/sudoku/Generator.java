package com.example.sudokumaster.sudoku;

public class Generator {

    private static Generator instance = null;
    private static final int SIZE = 9;
    private static final int SQRT_SIZE = 3;
    private final Board board;

    private Generator() {
        board = new Board(SIZE);
        board.setCells();
        board.setCellsStarting();
    }

    public static synchronized Generator getInstance() {
        if (instance == null) return new Generator();
        return instance;
    }

    public Board generateSudoku(Difficulty difficulty) {
        fillDiagonalMatrix();
        fillRemaining(0, SQRT_SIZE);
        return board;
    }

    private void fillDiagonalMatrix() {
        for (int i = 0; i < SIZE; i += SQRT_SIZE) {
            fillMatrix(i, i);
        }
    }

    private void fillMatrix(int row, int col) {
        int data;
        for (int i = 0; i < SQRT_SIZE; i++) {
            for (int j = 0; j < SQRT_SIZE; j++) {
                do {
                    data = (int) Math.floor(Math.random() * SIZE + 1);
                } while (!unusedInBox(row, col, data));
                board.getCell(row + i, col + j).setData(data);
            }
        }
    }

    private boolean fillRemaining(int i, int j) {
        if (j >= SIZE && i < SIZE - 1) {
            i = i + 1;
            j = 0;
        }
        if (i >= SIZE && j >= SIZE) return true;

        if (i < SQRT_SIZE) {
            if (j < SQRT_SIZE) j = SQRT_SIZE;
        } else if (i < SIZE - SQRT_SIZE) {
            if (j == (i / SQRT_SIZE) * SQRT_SIZE) j = j + SQRT_SIZE;
        } else {
            if (j == SIZE - SQRT_SIZE) {
                i = i + 1;
                j = 0;
                if (i >= SIZE) return true;
            }
        }

        for (int data = 1; data <= SIZE; data++) {
            if (isLegal(i, j, data)) {
                board.getCell(i, j).setData(data);
                if (fillRemaining(i, j + 1)) return true;
                board.getCell(i, j).setData(0);
            }
        }
        return false;
    }

    private boolean unusedInBox(int row, int col, int data) {
        for (int i = 0; i < SQRT_SIZE; i++) {
            for (int j = 0; j < SQRT_SIZE; j++) {
                if (board.getCell(row + i, col + j).getData() == data) return false;
            }
        }
        return true;
    }

    private boolean unusedInCol(int col, int data) {
        for (int i = 0; i < SIZE; i++) {
            if (board.getCell(i, col).getData() == data) return false;
        }
        return true;
    }

    private boolean unusedInRow(int row, int data) {
        for (int i = 0; i < SIZE; i++) {
            if (board.getCell(row, i).getData() == data) return false;
        }
        return true;
    }

    private boolean isLegal(int row, int col, int data) {
        return unusedInBox(row / 3 * 3, col / 3 * 3, data) && unusedInRow(row, data) && unusedInCol(col, data);
    }

    // Set a certain number of cells to empty based on the chosen difficulty
    public Board removeCells(Difficulty difficulty) {
        int targetIndex, targetRow, targetCol, count = difficulty.getBetweenMinMax();
        Cell targetCell;
        while (count != 0) {
            targetIndex = (int) Math.floor(Math.random() * SIZE * SIZE);
            targetRow = targetIndex / 9;
            targetCol = targetIndex % 9;
            targetCell = board.getCell(targetRow, targetCol);
            if (targetCell.getData() != Board.EMPTY) {
                targetCell.setData(Board.EMPTY);
                targetCell.setStartingCell(false);
                count--;
            }
        }
        return board;
    }
}
