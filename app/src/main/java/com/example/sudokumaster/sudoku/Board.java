package com.example.sudokumaster.sudoku;

public class Board {
    public static final int EMPTY = 0;
    private final Cell[][] cells;
    private int size;

    public Board(int size) {
        this.size = size;
        cells = new Cell[size][size];
    }

    public Cell[][] getCells() {
        return cells;
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public void setCells() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(i, j, EMPTY, false);
            }
        }
        ;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setDemoCells() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell(i, j, (j % 3) + 1 + (i % 3) * 3, true);
            }
        }
    }

    public void setCellsEmpty() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j].setData(EMPTY);
            }
        }
    }

    public void setCellsStarting() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j].setStartingCell(true);
            }
        }
    }

    public void setCellsNotStarting() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j].setStartingCell(false);
            }
        }
    }

    public void setCellsNotViolated() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j].setViolated(false);
            }
        }
    }
}
