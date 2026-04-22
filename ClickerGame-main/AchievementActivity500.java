package com.example.clickerproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class AchievementActivity500 extends AppCompatActivity {

    private static final int COST_RUSTY_SCISSORS = 500;
    private static final int REQUEST_IMPROVEMENTS = 1;
    private int clicks = 0; // Добавляем переменную для хранения очков

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements_500);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Блокируем назад
            }
        });

        // Получаем очки из Intent
        clicks = getIntent().getIntExtra("clicks", 0);

        setupCloseButton();
        displayClicksCounter();
        setupImprovementButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMPROVEMENTS && resultCode == RESULT_OK && data != null) {
            // Передаем результат обратно в главную активность
            setResult(RESULT_OK, data);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    private void setupCloseButton() {
        View closeButton = findViewById(R.id.button5);
        if (closeButton != null) {
            setupButtonAnimation(closeButton);
            closeButton.setOnClickListener(v -> {
                finish();
                overridePendingTransition(0, 0);
            });
        }
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

    private void displayClicksCounter() {
        TextView existingTextView = findViewById(R.id.textView5);
        if (existingTextView != null) {
            existingTextView.setText("" + clicks);
        }
    }

    private void setupImprovementButton() {
        int currentClickValue = getIntent().getIntExtra("currentClickValue", 1);

        Button improveButton = findViewById(R.id.button8);

        if (currentClickValue == 1 && clicks >= COST_RUSTY_SCISSORS) {
            improveButton.setBackgroundResource(R.drawable.rounded_button);
            // Добавляем анимацию для активной кнопки
            setupButtonAnimation(improveButton);
            improveButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, ImprovementsActivity.class);
                intent.putExtra("clicks", clicks);
                intent.putExtra("currentClickValue", currentClickValue);
                intent.putExtra("achievement_level", 500);
                startActivityForResult(intent, REQUEST_IMPROVEMENTS);
                overridePendingTransition(0, 0);
            });
        } else {
            improveButton.setEnabled(false);
            improveButton.setAlpha(0.5f);
            // Не добавляем анимацию для неактивной кнопки
        }

        // КНОПКА НАСТРОЕК - ДЕЛАЕМ НЕАКТИВНОЙ
        ImageButton settingsButton = findViewById(R.id.button4);
        settingsButton.setEnabled(false); // ← НЕЛЬЗЯ НАЖАТЬ
        settingsButton.setAlpha(0.5f);    // ← ПОЛУПРОЗРАЧНАЯ
        // Не добавляем анимацию для заблокированной кнопки

        // КНОПКА ПРАВИЛ
        ImageButton rulesButton = findViewById(R.id.button3);
        // Добавляем анимацию для кнопки правил
        setupButtonAnimation(rulesButton);
        rulesButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RulesActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
    }
}