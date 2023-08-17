package com.example.sudokumaster.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.sudokumaster.R;
import com.example.sudokumaster.database.DataBaseHelper;
import com.example.sudokumaster.sudoku.Board;
import com.example.sudokumaster.sudoku.Cell;
import com.example.sudokumaster.sudoku.CharacterSet;
import com.example.sudokumaster.sudoku.Difficulty;
import com.example.sudokumaster.view.custom.SudokuBoardView;
import com.example.sudokumaster.viewmodel.SudokuViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

// TODO: Read character setting from db
// TODO: create shopping activity
public class SudokuActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private ImageView ivReturn, ivSettings, ivClose;
    private LinearLayout llEraser, llHint, llAnswer;
    private final ArrayList<Button> numberButtons = new ArrayList<>();
    private Button btnNextLevel, btnChangeDifficulty;
    private TextView tvDifficulty, tvTime;
    private Difficulty difficulty;
    private RelativeLayout relativeLayout;
    private int level;
    private Timer timer;
    private TimerTask timerTask;
    private double time = 0.0;
    private SudokuViewModel sudokuViewModel;
    private SudokuBoardView sudokuBoardView;
    private DataBaseHelper dataBaseHelper;
    private HashMap<String, Integer> data;
    private CharacterSet characterSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        getInitData();
        initView();
        getDifficulty();
        setTimer();
        ivReturn.setOnClickListener(this);
        ivSettings.setOnClickListener(this);
        llEraser.setOnClickListener(this);
        llHint.setOnClickListener(this);
        llAnswer.setOnClickListener(this);
        characterSet = (CharacterSet) getIntent().getSerializableExtra("characterSet");
        for (int i = 0; i < numberButtons.size(); i++) {
            int data = i + 1;
            Button btn = numberButtons.get(i);
            btn.setText(String.valueOf(characterSet.getText(i + 1)));
            btn.setOnClickListener(v -> sudokuViewModel.getSudokuGame().handleInput(data));
        }
        sudokuBoardView.setOnTouchListener(this);
        sudokuBoardView.setCharacterSet(characterSet);
        sudokuViewModel = ViewModelProviders.of(this).get(SudokuViewModel.class);
        sudokuViewModel.createSudokuGame(difficulty);
        sudokuBoardView.setCells(sudokuViewModel.getSudokuGame().getCells());
        sudokuViewModel.getSudokuGame().getSelectedCellLiveData().observe(this, cellData -> {
            updateSelectedCellData(cellData);
        });
        sudokuViewModel.getSudokuGame().getCellsLiveData().observe(this, cells -> {
            updateCells(cells);
        });
        sudokuViewModel.getSudokuGame().getIsCompleted().observe(this, isCompleted -> {
            gameIsCompleted(isCompleted);
        });
    }

    private void createCompletionPopupWindow() {
        int width, height;
        boolean focusable = true;
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View completionPopUpView = layoutInflater.inflate(R.layout.popup_level_completion, null);
        btnNextLevel = completionPopUpView.findViewById(R.id.btnNextLevel);
        btnChangeDifficulty = completionPopUpView.findViewById(R.id.btnChangeDifficulty);
        btnNextLevel.setOnClickListener(v -> {
            Intent intent = getIntent();
            intent.putExtra("difficulty", difficulty);
            startActivity(intent);
            finish();
        });
        btnChangeDifficulty.setOnClickListener(v -> {
            Intent intent = new Intent(SudokuActivity.this, DifficultySelectionActivity.class);
            startActivity(intent);
        });
        width = ViewGroup.LayoutParams.MATCH_PARENT;
        height = ViewGroup.LayoutParams.WRAP_CONTENT;
        PopupWindow completionPopupWindow = new PopupWindow(completionPopUpView, width, height, focusable);
        relativeLayout.post(() -> completionPopupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 0, 0));
        timer.cancel();
    }

    private void createSettingsPopupWindow() {
        int width, height;
        boolean focusable = true;
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View settingsPopUpView = layoutInflater.inflate(R.layout.popup_settings, null);
        ivClose = settingsPopUpView.findViewById(R.id.ivClose);
        width = ViewGroup.LayoutParams.MATCH_PARENT;
        height = ViewGroup.LayoutParams.WRAP_CONTENT;
        PopupWindow settingsPopupWindow = new PopupWindow(settingsPopUpView, width, height, focusable);
        ivClose.setOnClickListener(v -> {
            settingsPopupWindow.dismiss();
        });
        relativeLayout.post(() -> settingsPopupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 0, 0));
    }

    private void gameIsCompleted(boolean isCompleted) {
        sudokuBoardView.gameIsCompleted(isCompleted);
        if (isCompleted) {
            if (difficulty != Difficulty.CUSTOM)
                dataBaseHelper.updateData(difficulty.toString(), level);
            createCompletionPopupWindow();
        }
    }

    private void updateCells(Cell[][] cells) {
        sudokuBoardView.updateCellsData(cells);
    }

    private void updateSelectedCellData(Pair<Integer, Integer> cellData) {
        sudokuBoardView.updateSelectedCellData(cellData.first, cellData.second);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ivReturn) {
            Intent intent = new Intent(SudokuActivity.this, DifficultySelectionActivity.class);
            startActivity(intent);
        } else if (id == R.id.ivSettings) {
            createSettingsPopupWindow();
        } else if (id == R.id.llEraser) {
            sudokuViewModel.getSudokuGame().handleInput(Board.EMPTY);
        } else if (id == R.id.llHint) {
            sudokuViewModel.getSudokuGame().showHint();
        } else if (id == R.id.llAnswer) {
            sudokuViewModel.getSudokuGame().showAnswer();
        }
    }

    // Update sudoku board UI
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int newRow, newCol, id = v.getId();
        boolean sameCell;
        Pair<Integer, Integer> newCell;
        if (id == R.id.sbv) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                newCell = sudokuBoardView.updateCell(event.getX(), event.getY());
                newRow = newCell.first;
                newCol = newCell.second;
                sameCell = newRow == sudokuBoardView.getSelectedRow() && newCol == sudokuBoardView.getSelectedCol();
                if (sameCell || sudokuBoardView.getCell(newRow, newCol).isStartingCell()) {
                    newRow = -1;
                    newCol = -1;
                }
                sudokuViewModel.getSudokuGame().updateSelectedCellData(newRow, newCol);
            }
            return true;
        }
        return false;
    }

    public void getInitData() {
        dataBaseHelper = new DataBaseHelper(this);
        data = dataBaseHelper.getData();
    }

    // Initialise views
    private void initView() {
        relativeLayout = findViewById(R.id.layoutSudoku);
        ivReturn = findViewById(R.id.ivReturn);
        ivSettings = findViewById(R.id.ivSettings);
        tvDifficulty = findViewById(R.id.tvDifficulty);
        tvTime = findViewById(R.id.tvTime);
        llEraser = findViewById(R.id.llEraser);
        llHint = findViewById(R.id.llHint);
        llAnswer = findViewById(R.id.llAnswer);
        numberButtons.add(findViewById(R.id.btnOne));
        numberButtons.add(findViewById(R.id.btnTwo));
        numberButtons.add(findViewById(R.id.btnThree));
        numberButtons.add(findViewById(R.id.btnFour));
        numberButtons.add(findViewById(R.id.btnFive));
        numberButtons.add(findViewById(R.id.btnSix));
        numberButtons.add(findViewById(R.id.btnSeven));
        numberButtons.add(findViewById(R.id.btnEight));
        numberButtons.add(findViewById(R.id.btnNine));
        sudokuBoardView = (SudokuBoardView) findViewById(R.id.sbv);
    }

    // Get difficulty from previous selection
    private void getDifficulty() {
        difficulty = (Difficulty) getIntent().getSerializableExtra("difficulty");
        level = difficulty != Difficulty.CUSTOM ? data.get(difficulty.toString()) + 1 : 0;
        if (difficulty == Difficulty.EASY) {
            tvDifficulty.setText("Beginner/Lv" + level);
        } else if (difficulty == Difficulty.MEDIUM) {
            tvDifficulty.setText("Advanced/Lv" + level);
        } else if (difficulty == Difficulty.HARD) {
            tvDifficulty.setText("Expert/Lv" + level);
        } else {
            tvDifficulty.setText("Custom Game");
        }
    }

    // Set up a timer
    private void setTimer() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                // Update time and set text
                runOnUiThread(() -> {
                    time++;
                    tvTime.setText(getTimerText());
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    // Calculate and format time
    private String getTimerText() {
        int rounded, seconds, minutes;
        rounded = (int) Math.round(time);
        seconds = rounded % 60;
        minutes = rounded / 60;
        return String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
    }
}