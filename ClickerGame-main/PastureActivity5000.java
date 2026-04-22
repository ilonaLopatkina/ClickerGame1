package com.example.clickerproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class PastureActivity5000 extends AppCompatActivity {

    private TextView woolCounter;
    private ImageView sheep;
    private Button improveButton;
    private int clicks;
    private boolean isBlocked = true;
    private int clickValue;
    private boolean achievement500Shown;
    private boolean achievement2500Shown;
    private boolean achievement3500Shown;

    private static final int REQUEST_IMPROVEMENTS = 1;
    private static final int REQUEST_ACHIEVEMENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasture_5000);

        //кнопка назад
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Блокируем назад
            }
        });

        MusicPlayer.getInstance(this);

        Intent intent = getIntent();
        clicks = intent.getIntExtra("clicks", 0);
        clickValue = intent.getIntExtra("currentClickValue", 1);
        isBlocked = true; // Принудительно блокируем
        achievement500Shown = intent.getBooleanExtra("achievement500Shown", false);
        achievement2500Shown = intent.getBooleanExtra("achievement2500Shown", false);
        achievement3500Shown = intent.getBooleanExtra("achievement3500Shown", false);

        setupViews();
        setupClickListeners();
        updateImproveButtonState();
        updateCounter();

        // Сохраняем начальное состояние
        saveGameState("PastureActivity5000");
    }

    private void setupViews() {
        woolCounter = findViewById(R.id.textView5);
        sheep = findViewById(R.id.imageView3);
        improveButton = findViewById(R.id.button17);

        updateCounter();
        updateImproveButtonState();
    }

    private void setupClickListeners() {
        // 🚫 ПОЛНАЯ БЛОКИРОВКА КЛИКОВ НА ЭТОМ ПАСТБИЩЕ
        sheep.setOnClickListener(v -> {
            // Ничего не делаем — клик заблокирован
        });

        // Кнопка улучшений
        setupButtonAnimation(improveButton);
        improveButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ImprovementsActivity.class);
            intent.putExtra("clicks", clicks);
            intent.putExtra("currentClickValue", clickValue);
            startActivityForResult(intent, REQUEST_IMPROVEMENTS);
            overridePendingTransition(0, 0);
        });

        // Кнопка настроек
        View settingsButton = findViewById(R.id.button15);
        setupButtonAnimation(settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameSettingsActivity.class);
            intent.putExtra("points", clicks);
            intent.putExtra("currentClickValue", clickValue);
            intent.putExtra("isBlocked", isBlocked);
            intent.putExtra("achievement500Shown", achievement500Shown);
            intent.putExtra("achievement2500Shown", achievement2500Shown);
            intent.putExtra("achievement3500Shown", achievement3500Shown);
            intent.putExtra("lastActivity", "PastureActivity5000"); // ← ИСПРАВЬ!
            intent.putExtra("callingActivity", "PastureActivity5000"); // ← ИСПРАВЬ!
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        // Кнопка правил
        ImageButton rulesButton = findViewById(R.id.button16);
        setupButtonAnimation(rulesButton);
        rulesButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RulesActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
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

    private void updateImproveButtonState() {
        // КНОПКА ВСЕГДА АКТИВНА НА ВСЕХ ПАСТБИЩАХ
        improveButton.setVisibility(View.VISIBLE);
        improveButton.setEnabled(true);
        improveButton.setAlpha(1.0f);
    }

    private void updateCounter() {
        woolCounter.setText(" " + clicks);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCounter();
        updateImproveButtonState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == REQUEST_IMPROVEMENTS || requestCode == REQUEST_ACHIEVEMENT)
                && resultCode == RESULT_OK && data != null) {

            String improvementType = data.getStringExtra("improvement_type");
            int clicksAfterPurchase = data.getIntExtra("clicks_after_purchase", clicks);

            // ЛОГИРУЕМ ДО ИЗМЕНЕНИЙ
            android.util.Log.d("Pasture5000", "BEFORE - improvement: " + improvementType +
                    ", clicks: " + clicks + " -> " + clicksAfterPurchase +
                    ", current clickValue: " + clickValue);

            // ПРИМЕНЯЕМ УЛУЧШЕНИЕ ТОЖЕ!
            if ("rusty_scissors".equals(improvementType)) {
                clickValue = 3;
                android.util.Log.d("Pasture5000", "Applied rusty_scissors, new clickValue: 3");
            } else if ("sharp_scissors".equals(improvementType)) {
                clickValue = 10;
                android.util.Log.d("Pasture5000", "Applied sharp_scissors, new clickValue: 10");
            } else if ("el_machine".equals(improvementType)) {
                clickValue = 50;
                android.util.Log.d("Pasture5000", "Applied el_machine, new clickValue: 50");
            }

            clicks = clicksAfterPurchase;
            isBlocked = true; // ← снова блокируем

            // ЛОГИРУЕМ ПОСЛЕ ИЗМЕНЕНИЙ
            android.util.Log.d("Pasture5000", "AFTER - clicks: " + clicks + ", clickValue: " + clickValue);

            updateImproveButtonState();
            updateCounter();
            saveGameState("PastureActivity5000");
        }
    }

    private void saveGameState(String lastActivityName) {
        GameDataManager manager = new GameDataManager(this);
        manager.saveGameState(clicks, clickValue, isBlocked,
                achievement500Shown, achievement2500Shown, achievement3500Shown,
                lastActivityName, "PastureActivity5000"); // ← ДОБАВЬ ВТОРОЙ ПАРАМЕТР
    }
}