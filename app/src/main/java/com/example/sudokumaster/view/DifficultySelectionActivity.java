package com.example.sudokumaster.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sudokumaster.R;
import com.example.sudokumaster.RecViewAdapter.CharacterSetRecViewAdapter;
import com.example.sudokumaster.database.DataBaseHelper;
import com.example.sudokumaster.sudoku.Difficulty;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.HashMap;

public class DifficultySelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnEasy, btnMedium, btnHard, btnCustom;
    private PieChart pieChart;
    private int easy, medium, hard;
    private DataBaseHelper dataBaseHelper;
    private ImageView ivClose;
    private HashMap<String, Integer> data;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty_selection);
        initView();
        dataBaseHelper = new DataBaseHelper(this);
        data = dataBaseHelper.getData();
        if (data.isEmpty()) {
            dataBaseHelper.createInitialProfile();
            data = dataBaseHelper.getData();
        }
        setPieChart();
        btnEasy.setOnClickListener(this);
        btnMedium.setOnClickListener(this);
        btnHard.setOnClickListener(this);
        btnCustom.setOnClickListener(this);
    }


    // Send the chosen difficulty to the next activity
    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent(DifficultySelectionActivity.this, SudokuActivity.class);
        if (id == R.id.btnEasy) {
            intent.putExtra("difficulty", Difficulty.EASY);
        } else if (id == R.id.btnMedium) {
            intent.putExtra("difficulty", Difficulty.MEDIUM);
        } else if (id == R.id.btnHard) {
            intent.putExtra("difficulty", Difficulty.HARD);
        } else if (id == R.id.btnCustom) {
            Toast.makeText(this, "This option is not ready yet!!!", Toast.LENGTH_SHORT).show();
            return;
        }
        createCharacterSelectionPopupWindow(intent);
    }

    private void createCharacterSelectionPopupWindow(Intent intent) {
        int width, height;
        boolean focusable = true;
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View charSelectionPopUpView = layoutInflater.inflate(R.layout.popup_char_set_selection, null);
        width = ViewGroup.LayoutParams.MATCH_PARENT;
        height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ivClose = charSelectionPopUpView.findViewById(R.id.ivClose);
        recyclerView = charSelectionPopUpView.findViewById(R.id.rvCharSetSelection);
        PopupWindow charSelectionPopupWindow = new PopupWindow(charSelectionPopUpView, width, height, focusable);
        CharacterSetRecViewAdapter adapter = new CharacterSetRecViewAdapter(
                charSelectionPopupWindow.getContentView().getContext(),
                intent);
        adapter.setCharacterSets();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(charSelectionPopupWindow.getContentView().getContext()));
        ivClose.setOnClickListener(v -> {
            charSelectionPopupWindow.dismiss();
        });
        relativeLayout.post(() -> charSelectionPopupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, 0, 0));
    }

    // Initialise Views
    private void initView() {
        relativeLayout = findViewById(R.id.layoutSelection);
        btnEasy = findViewById(R.id.btnEasy);
        btnMedium = findViewById(R.id.btnMedium);
        btnHard = findViewById(R.id.btnHard);
        btnCustom = findViewById(R.id.btnCustom);
        pieChart = findViewById(R.id.pieChart);
    }

    // Set the data and color of the pie chart
    private void setPieChart() {
        easy = data.get(DataBaseHelper.EASY);
        medium = data.get(DataBaseHelper.MEDIUM);
        hard = data.get(DataBaseHelper.HARD);
        if (easy == 0 && medium == 0 && hard == 0) {
            pieChart.addPieSlice(new PieModel(
                    "None",
                    0,
                    Color.GRAY
            ));
        } else {
            pieChart.addPieSlice(new PieModel(
                    "Easy",
                    easy,
                    Color.parseColor("#47B39C")));
            pieChart.addPieSlice(new PieModel(
                    "Medium",
                    medium,
                    Color.parseColor("#FFC154")));
            pieChart.addPieSlice(new PieModel(
                    "Hard",
                    hard,
                    Color.parseColor("#EC6B56")));
        }

        // Animate the pie chart
        pieChart.startAnimation();
    }
}