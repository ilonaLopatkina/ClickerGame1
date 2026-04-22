package com.example.clickerproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class GameSettingsActivity extends AppCompatActivity {

    private int clicks;
    private int clickValue;
    private boolean isBlocked;
    private boolean achievement500Shown;
    private boolean achievement2500Shown;
    private boolean achievement3500Shown;
    private String lastActivity;

    private TextView pointsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_settings);

        // Блокируем кнопку назад
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {}
        });

        // Получаем данные игры
        clicks = getIntent().getIntExtra("points", 0);
        clickValue = getIntent().getIntExtra("currentClickValue", 1);
        isBlocked = getIntent().getBooleanExtra("isBlocked", false);
        achievement500Shown = getIntent().getBooleanExtra("achievement500Shown", false);
        achievement2500Shown = getIntent().getBooleanExtra("achievement2500Shown", false);
        achievement3500Shown = getIntent().getBooleanExtra("achievement3500Shown", false);
        lastActivity = getIntent().getStringExtra("lastActivity");

        pointsText = findViewById(R.id.textView5);
        pointsText.setText(" " + clicks);

        setupAllButtons();
    }

    private void setupAllButtons() {
        // Закрыть
        View closeButton = findViewById(R.id.button5);
        if (closeButton != null) {
            setupButtonAnimation(closeButton);
            closeButton.setOnClickListener(v -> {
                finish();
                overridePendingTransition(0, 0);
            });
        }

        // Звук
        Button soundButton = findViewById(R.id.button6);
        setupButtonAnimation(soundButton);
        MusicPlayer music = MusicPlayer.getInstance(this);
        updateButtonColor(soundButton, music.isPlaying());
        soundButton.setOnClickListener(v -> {
            music.toggle();
            updateButtonColor(soundButton, music.isPlaying());
        });

        // Сохранить
        Button saveButton = findViewById(R.id.button8);
        setupButtonAnimation(saveButton);
        saveButton.setOnClickListener(v -> saveGame());

        // Начать сначала
        Button resetButton = findViewById(R.id.button9);
        setupButtonAnimation(resetButton);
        resetButton.setOnClickListener(v -> resetGame());

        // Выйти из игры с сохранением
        Button exitButton = findViewById(R.id.button10);
        setupButtonAnimation(exitButton);
        exitButton.setOnClickListener(v -> exitGameWithSave1());
    }

    // Метод для анимации нажатия кнопок
    private void setupButtonAnimation(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // При нажатии - немного уменьшаем и делаем полупрозрачной
                        v.animate()
                                .scaleX(0.9f)
                                .scaleY(0.9f)
                                .alpha(0.7f)
                                .setDuration(100)
                                .start();
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // При отпускании - возвращаем к исходному состоянию
                        v.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .alpha(1f)
                                .setDuration(100)
                                .start();
                        break;
                }
                return false; // false чтобы onClick тоже работал
            }
        });
    }

    private void updateButtonColor(Button button, boolean isPlaying) {
        if (isPlaying) {
            button.setBackgroundResource(R.drawable.rounded2_button);
        } else {
            button.setBackgroundResource(R.drawable.gray_rounded2_button);
        }
    }

    private void saveGame() {
        GameDataManager manager = new GameDataManager(this);

        // Получаем callingActivity из интента (активность откуда открыли настройки)
        String callingActivity = getIntent().getStringExtra("callingActivity");
        if (callingActivity == null) {
            callingActivity = "SplashActivity"; // fallback
        }

        manager.saveGameState(clicks, clickValue, isBlocked,
                achievement500Shown, achievement2500Shown, achievement3500Shown,
                lastActivity, callingActivity); // ← ДОБАВЬ callingActivity

        Toast.makeText(this, "Игра сохранена", Toast.LENGTH_SHORT).show();
    }

    private void exitGameWithSave1() {
        GameDataManager manager = new GameDataManager(this);

        // СОХРАНЯЕМ ТЕКУЩУЮ АКТИВНОСТЬ (откуда открыли настройки)
        String callingActivity = getIntent().getStringExtra("callingActivity");
        if (callingActivity == null) {
            callingActivity = "SplashActivity"; // fallback
        }

        manager.saveGameState(clicks, clickValue, isBlocked,
                achievement500Shown, achievement2500Shown, achievement3500Shown,
                lastActivity, callingActivity); // ← ПЕРЕДАЕМ currentActivity

        Toast.makeText(this, "Игра сохранена", Toast.LENGTH_SHORT).show();

        new android.os.Handler().postDelayed(() -> {
            MusicPlayer.getInstance(this).stop();
            finishAffinity();
            System.exit(0);
        }, 1500);
    }

    private void resetGame() {
        GameDataManager manager = new GameDataManager(this);
        manager.resetGame();
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }
}