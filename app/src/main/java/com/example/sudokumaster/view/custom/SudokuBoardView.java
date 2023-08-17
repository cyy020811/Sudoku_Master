package com.example.sudokumaster.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.sudokumaster.R;
import com.example.sudokumaster.sudoku.Board;
import com.example.sudokumaster.sudoku.Cell;
import com.example.sudokumaster.sudoku.CharacterSet;

public class SudokuBoardView extends View {

    private final int SQRT_SIZE = 3;
    private final int SIZE = 9;
    private final float TEXT_SIZE = 40;
    private float cellSizePixels = 0.0f;
    private final Paint thickLine;
    private final Paint thinLine;
    private final Paint selectedCell;
    private final Paint conflictingCell;
    private final Paint relatedCell;
    private final Paint startingCell;
    private final Paint boardBG;
    private final Paint textPaint;
    private final Paint conflictingTextPaint;
    private final Paint completedCell;
    private boolean isCompleted = false;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Cell[][] cells = null;
    private int blue;
    private int lightBlue;
    private int red;
    private int yellow;
    private int darkYellow;
    private int green;
    private CharacterSet characterSet;

    public SudokuBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setColors(context);
        thickLine = new Paint();
        thinLine = new Paint();
        selectedCell = new Paint();
        conflictingCell = new Paint();
        relatedCell = new Paint();
        startingCell = new Paint();
        completedCell = new Paint();
        boardBG = new Paint();
        textPaint = new Paint();
        conflictingTextPaint = new Paint();
    }

    private void setColors(Context context) {
        blue = ContextCompat.getColor(context, R.color.blue);
        lightBlue = ContextCompat.getColor(context, R.color.lightBlue);
        red = ContextCompat.getColor(context, R.color.red);
        yellow = ContextCompat.getColor(context, R.color.cell);
        darkYellow = ContextCompat.getColor(context, R.color.darkYellow);
        green = ContextCompat.getColor(context, R.color.green);
    }

    private void setPaints() {
        setSelectedCell();
        setConflictingCell();
        setRelatedCell();
        setStartingCell();
        setCompletedCell();
        setThickLine();
        setThinLine();
        setBoardBG();
        setTextPaint();
        setConflictingTextPaint();
    }

    private void setCompletedCell() {
        completedCell.setColor(green);
        completedCell.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void setConflictingTextPaint() {
        conflictingTextPaint.setColor(Color.WHITE);
        conflictingTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        conflictingTextPaint.setTextSize(TEXT_SIZE);
    }

    private void setStartingCell() {
        startingCell.setColor(darkYellow);
        startingCell.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void setTextPaint() {
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(TEXT_SIZE);
    }

    private void setBoardBG() {
        boardBG.setColor(yellow);
        boardBG.setStyle(Paint.Style.FILL);
    }

    private void setSelectedCell() {
        selectedCell.setColor(blue);
        selectedCell.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void setConflictingCell() {
        conflictingCell.setColor(red);
        conflictingCell.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void setRelatedCell() {
        relatedCell.setColor(lightBlue);
        relatedCell.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void setThinLine() {
        thinLine.setColor(Color.BLACK);
        thinLine.setStyle(Paint.Style.STROKE);
        thinLine.setStrokeWidth(3);
    }

    private void setThickLine() {
        thickLine.setColor(Color.BLACK);
        thickLine.setStyle(Paint.Style.STROKE);
        thickLine.setStrokeWidth(5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizePixels = Math.min(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sizePixels, sizePixels);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        cellSizePixels = ((float) getWidth() / SIZE);
        setPaints();
        canvas.drawRoundRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), 15f, 15f, boardBG);
        drawLines(canvas);
        fillCells(canvas);
        drawText(canvas);
    }

    // Draw the grid lines
    private void drawLines(Canvas canvas) {
        Paint lineToUse;
        canvas.drawRoundRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), 15f, 15f, thickLine);
        for (int i = 1; i < SIZE; i++) {
            lineToUse = i % 3 == 0 ? thickLine : thinLine;
            canvas.drawLine(
                    i * cellSizePixels,
                    0f,
                    i * cellSizePixels,
                    (float) getHeight(),
                    lineToUse
            );

            canvas.drawLine(
                    0f,
                    i * cellSizePixels,
                    (float) getWidth(),
                    i * cellSizePixels,
                    lineToUse
            );
        }
    }

    // Draw numbers on the UI
    private void drawText(Canvas canvas) {
        String value;
        Cell cell;
        Rect textBound = new Rect();
        Paint paintToUse;
        float textWidth, textHeight;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cell = cells[i][j];
                if (cell.getData() != Board.EMPTY) {
                    value = String.valueOf(characterSet.getText(cell.getData()));
                    textPaint.getTextBounds(value, 0, value.length(), textBound);
                    textWidth = textPaint.measureText(value);
                    textHeight = textBound.height();
                    paintToUse = cells[i][j].isViolated() || isCompleted ? conflictingTextPaint : textPaint;
                    canvas.drawText(value,
                            (cell.getCol() * cellSizePixels) + cellSizePixels / 2 - textWidth / 2,
                            (cell.getRow() * cellSizePixels) + cellSizePixels / 2 + textHeight / 2,
                            paintToUse);
                }
            }
        }
    }

    // Highlight selected and related cells
    private void fillCells(Canvas canvas) {
        boolean sameRow, sameCol, sameGrid, unselected = false;
        if (selectedRow == -1 || selectedCol == -1) unselected = true;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sameRow = i == selectedRow;
                sameCol = j == selectedCol;
                sameGrid = i / SQRT_SIZE == selectedRow / SQRT_SIZE && j / SQRT_SIZE == selectedCol / SQRT_SIZE;
                if (isCompleted) {
                    fillCell(canvas, i, j, completedCell);
                } else if (cells[i][j].isViolated()) {
                    fillCell(canvas, i, j, conflictingCell);
                } else if (sameRow && sameCol && !unselected) {
                    fillCell(canvas, i, j, selectedCell);
                } else if (sameRow || sameCol || sameGrid && !unselected) {
                    fillCell(canvas, i, j, relatedCell);
                } else if (cells[i][j].isStartingCell()) {
                    fillCell(canvas, i, j, startingCell);
                }
            }
        }
        drawLines(canvas);
    }

    // Highlight a cell with a paint
    private void fillCell(Canvas canvas, int row, int col, Paint paint) {
        canvas.drawRect(
                col * cellSizePixels + 1,
                row * cellSizePixels + 1,
                (col + 1) * cellSizePixels - 1,
                (row + 1) * cellSizePixels - 1,
                paint
        );
    }

    // Calculate possible row and col then return
    public Pair<Integer, Integer> updateCell(float x, float y) {
        int possibleSelectedRow, possibleSelectedCol;
        possibleSelectedRow = (int) (y / cellSizePixels);
        possibleSelectedCol = (int) (x / cellSizePixels);
        return new Pair(possibleSelectedRow, possibleSelectedCol);
    }

    // Update UI based on the selected row and col
    public void updateSelectedCellData(int row, int col) {
        selectedRow = row;
        selectedCol = col;
        invalidate();
    }

    // Update UI based on the input in a cell
    public void updateCellsData(Cell[][] cells) {
        this.cells = cells;
        invalidate();
    }

    // Update UI based on the completed status of the game
    public void gameIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
        invalidate();
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public int getSelectedCol() {
        return selectedCol;
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public void setCells(Cell[][] cells) {
        this.cells = cells;
    }

    public void setCharacterSet(CharacterSet characterSet) {this.characterSet = characterSet;}
}
