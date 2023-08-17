package com.example.sudokumaster.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sudokumaster.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private TextView tvStart;
    private final ArrayList<ImageView> clouds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        move();

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DifficultySelectionActivity.class);
                startActivity(intent);
            }
        });
    }

    // Initialise views
    private void initView() {
        tvStart = findViewById(R.id.tvStart);
        clouds.add(findViewById(R.id.cloud1));
        clouds.add(findViewById(R.id.cloud2));
        clouds.add(findViewById(R.id.cloud3));
    }

    // Set up animation for the clouds
    private void move() {
        int[] animationDuration = {8000, 15000, 13000};
        for (int i = 0; i < clouds.size(); i++) {
            TranslateAnimation animation =
                    new TranslateAnimation(
                            Animation.RELATIVE_TO_PARENT,
                            0.0f,
                            Animation.RELATIVE_TO_PARENT,
                            1.5f,
                            Animation.RELATIVE_TO_SELF,
                            0.0f,
                            Animation.RELATIVE_TO_SELF,
                            0.0f);
            animation.setDuration(animationDuration[i]);
            animation.setRepeatCount(Animation.INFINITE);
            clouds.get(i).startAnimation(animation);
        }
    }
}