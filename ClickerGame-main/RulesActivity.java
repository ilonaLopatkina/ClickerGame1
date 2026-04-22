package com.example.clickerproject;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class RulesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        //кнопка назад
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Блокируем назад
            }
        });

        setupCloseButton();

        // Блокируем системную кнопку "назад"
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Ничего не делаем — кнопка назад полностью заблокирована
                // Можно добавить тост, если нужно:
                // Toast.makeText(PastureActivity0.this, "Назад заблокирован", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCloseButton() {
        View closeButton = findViewById(R.id.button5);
        if (closeButton != null) {
            // Добавляем анимацию нажатия
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
}