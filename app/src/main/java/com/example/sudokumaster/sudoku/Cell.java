package com.example.sudokumaster.sudoku;

public class Cell {

    private int row;
    private int col;
    private Integer data;
    private boolean isStartingCell;
    private boolean isViolated = false;

    public Cell(int row, int col, int data, boolean isStartingCell) {
        this.row = row;
        this.col = col;
        this.data = data;
        this.isStartingCell = isStartingCell;
    }

    public boolean isStartingCell() {
        return isStartingCell;
    }

    public void setStartingCell(boolean startingCell) {
        isStartingCell = startingCell;
    }

    public boolean isViolated() {
        return isViolated;
    }

    public void setViolated(boolean violated) {
        isViolated = violated;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Integer getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public void copyCell(Cell cell) {
        data = cell.data;
        isStartingCell = cell.isStartingCell;
        isViolated = cell.isViolated;
    }
}
