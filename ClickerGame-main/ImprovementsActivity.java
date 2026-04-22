package com.example.clickerproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class ImprovementsActivity extends AppCompatActivity {

    private int achievementLevel;
    private int clicks;
    private int currentClickValue;
    private String purchasedImprovementType = null;
    private boolean resultAlreadySet = false; // Флаг чтобы не дублировать результат

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improvements);

        //кнопка назад
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Блокируем назад
            }
        });

        clicks = getIntent().getIntExtra("clicks", 0);
        currentClickValue = getIntent().getIntExtra("currentClickValue", 1);

        // В onCreate ImprovementsActivity
        int currentClicks = getIntent().getIntExtra("clicks", 0);
        int currentClickValue = getIntent().getIntExtra("currentClickValue", 1);

        android.util.Log.d("Improvements", "Received - clicks: " + currentClicks + ", clickValue: " + currentClickValue);

        setupCloseButton();
        displayClicksCounter();
        setupImprovementButtons();
    }

    private void setupCloseButton() {
        View closeButton = findViewById(R.id.button5);
        if (closeButton != null) {
            setupButtonAnimation(closeButton);
            closeButton.setOnClickListener(v -> {
                // Если результат еще не установлен (для острых ножниц он устанавливается сразу)
                if (!resultAlreadySet) {
                    // Если было куплено улучшение, возвращаем результат
                    if (purchasedImprovementType != null) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("improvement_type", purchasedImprovementType);
                        resultIntent.putExtra("clicks_after_purchase", clicks);
                        setResult(RESULT_OK, resultIntent);
                    } else {
                        // Закрываем без покупки
                        setResult(RESULT_CANCELED);
                    }
                }
                finish();
                overridePendingTransition(0, 0);
            });
        }

        // КНОПКА НАСТРОЕК - ДЕЛАЕМ НЕАКТИВНОЙ
        View settingsButton = findViewById(R.id.button4);
        if (settingsButton != null) {
            settingsButton.setEnabled(false); // ← НЕЛЬЗЯ НАЖАТЬ
            settingsButton.setAlpha(0.5f);    // ← ПОЛУПРОЗРАЧНАЯ
        }
    }

    private void displayClicksCounter() {
        TextView existingTextView = findViewById(R.id.textView5);
        if (existingTextView != null) {
            existingTextView.setText("" + clicks);
        }
    }

    private void setupImprovementButtons() {
        // ПОКАЗЫВАЕМ ВСЕ УЛУЧШЕНИЯ СРАЗУ
        setupImprovementButton(R.id.button12, "rusty_scissors", 500, 1);
        setupImprovementButton(R.id.button13, "sharp_scissors", 2500, 3);
        setupImprovementButton(R.id.button14, "el_machine", 3500, 10);
    }

    private void setupImprovementButton(int buttonId, String improvementType, int cost, int requiredClickValue) {
        Button button = findViewById(buttonId);

        // ПРОВЕРЯЕМ МОЖНО ЛИ КУПИТЬ
        boolean canBuy = (currentClickValue == requiredClickValue && clicks >= cost);

        if (canBuy) {
            // МОЖНО КУПИТЬ
            button.setBackgroundResource(R.drawable.rounded4_button_green);
            button.setEnabled(true);
            button.setAlpha(1.0f);
            setupButtonAnimation(button);
            button.setOnClickListener(v -> {
                purchaseImprovement(improvementType, cost);
                deactivateAllButtons();
            });
        } else {
            // НЕЛЬЗЯ КУПИТЬ - ЛЮБАЯ ПРИЧИНА
            button.setEnabled(false);
            button.setAlpha(0.5f);
            button.setBackgroundResource(R.drawable.gray_rounded2_button);
            button.setOnClickListener(null); // ← ГЛАВНОЕ: убираем обработчик
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

    private void activateButton(int buttonId, String improvementType, int cost) {
        Button button = findViewById(buttonId);

        // Проверяем, хватает ли кликов для покупки
        if (clicks >= cost) {
            button.setBackgroundResource(R.drawable.rounded4_button_green);
            button.setEnabled(true);
            button.setAlpha(1.0f);
            setupButtonAnimation(button);

            button.setOnClickListener(v -> {
                // Покупаем улучшение
                purchaseImprovement(improvementType, cost);
                // Делаем все кнопки неактивными после покупки
                deactivateAllButtons();
            });
        } else {
            // Не хватает кликов - кнопка неактивна
            button.setEnabled(false);
            button.setAlpha(0.5f);
        }
    }

    private void purchaseImprovement(String improvementType, int cost) {
        // Сохраняем тип купленного улучшения
        purchasedImprovementType = improvementType;

        // Вычитаем стоимость из кликов
        clicks -= cost;

        // Обновляем счетчик кликов
        displayClicksCounter();

        // ОБНОВЛЯЕМ currentClickValue в зависимости от купленного улучшения
        if ("rusty_scissors".equals(improvementType)) {
            currentClickValue = 3;
        } else if ("sharp_scissors".equals(improvementType)) {
            currentClickValue = 10;
        } else if ("el_machine".equals(improvementType)) {
            currentClickValue = 50;
        }

        // СОХРАНЯЕМ РЕЗУЛЬТАТЫ ПОСЛЕ ПОКУПКИ С ОБНОВЛЕННЫМИ ДАННЫМИ
        saveGameState();

        // Для острых ножниц сразу устанавливаем результат и помечаем флаг
        if ("sharp_scissors".equals(improvementType)) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("improvement_type", purchasedImprovementType);
            resultIntent.putExtra("clicks_after_purchase", clicks);
            setResult(RESULT_OK, resultIntent);
            resultAlreadySet = true; // Помечаем что результат уже установлен
        }
        // Для других улучшений результат установится при нажатии на крестик
    }

    // МЕТОД ДЛЯ ПРОДВИНУТОГО СОХРАНЕНИЯ
    private void saveGameState() {
        GameDataManager manager = new GameDataManager(this);

        // Определяем какая активность будет следующей после покупки
        String nextActivity = "PastureActivity0"; // по умолчанию

        if ("sharp_scissors".equals(purchasedImprovementType)) {
            nextActivity = "PastureActivity2500";
        } else if ("el_machine".equals(purchasedImprovementType)) {
            nextActivity = "PastureActivity5000";
        }

        // isBlocked = false после покупки улучшения (игра разблокируется)
        boolean isBlockedAfterPurchase = false;

        manager.saveGameState(clicks, currentClickValue, isBlockedAfterPurchase,
                false, false, false, // достижения пока false
                nextActivity, "ImprovementsActivity");

        android.util.Log.d("Improvements", "Продвинутое сохранение после покупки: " + purchasedImprovementType +
                ", clicks: " + clicks + ", clickValue: " + currentClickValue +
                ", nextActivity: " + nextActivity);
    }

    private void deactivateButton(int buttonId) {
        Button button = findViewById(buttonId);
        button.setEnabled(false);
        button.setAlpha(0.5f);
    }

    private void deactivateAllButtons() {
        // Делаем все кнопки улучшений неактивными
        deactivateButton(R.id.button12);
        deactivateButton(R.id.button13);
        deactivateButton(R.id.button14);
    }
}