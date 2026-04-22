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

public class PastureActivity2500 extends AppCompatActivity {

    private TextView woolCounter;
    private ImageView sheep;
    private Button improveButton;
    private int clicks;
    private boolean isBlocked;
    private int clickValue;
    private boolean achievement500Shown;
    private boolean achievement2500Shown;
    private boolean achievement3500Shown;

    private static final int NEED_CLICKS_RUSTY = 500;
    private static final int NEED_CLICKS_SHARP = 2500;
    private static final int NEED_CLICKS_EL = 3500;

    private static final int REQUEST_IMPROVEMENTS = 1;
    private static final int REQUEST_ACHIEVEMENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasture_2500);

        //кнопка назад
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Блокируем назад
            }
        });

        // Инициализация музыки (без остановки) - ДОБАВЛЕНО КОММЕНТАРИЙ КАК В PastureActivity0
        MusicPlayer.getInstance(this);

        Intent intent = getIntent();
        clicks = intent.getIntExtra("clicks", 0);
        clickValue = intent.getIntExtra("currentClickValue", 1);
        isBlocked = intent.getBooleanExtra("isBlocked", false);
        achievement500Shown = intent.getBooleanExtra("achievement500Shown", false);
        achievement2500Shown = intent.getBooleanExtra("achievement2500Shown", false);
        achievement3500Shown = intent.getBooleanExtra("achievement3500Shown", false);

        setupViews();
        setupClickListeners();
        updateCounter();
        updateImproveButtonState();

        // Сохраняем начальное состояние
        saveGameState("PastureActivity2500");
    }

    private void setupViews() {
        woolCounter = findViewById(R.id.textView5);
        sheep = findViewById(R.id.imageView7);
        improveButton = findViewById(R.id.button17);

        // ДОБАВЛЕНО: сразу обновляем счетчик и кнопку как в PastureActivity0
        updateCounter();
        updateImproveButtonState();
    }

    private void setupClickListeners() {
        // Клик по овце - добавляем анимацию
        setupButtonAnimation(sheep);
        sheep.setOnClickListener(v -> {
            if (!isBlocked) {
                clicks += clickValue;
                updateCounter();
                checkAchievements();
                checkForBlocking();
                // АВТОСОХРАНЕНИЕ ПРИ КАЖДОМ КЛИКЕ
                saveGameState("PastureActivity2500");

                // Переход на Pasture5000 при достижении >=5000 кликов
                if (clicks >= 5000) {
                    switchToPasture5000();
                }
            }
        });

        // Кнопка улучшений - ДОБАВЛЕН КОММЕНТАРИЙ КАК В PastureActivity0
        setupButtonAnimation(improveButton);
        improveButton.setOnClickListener(v -> {
            // ВСЕГДА ОТКРЫВАЕМ УЛУЧШЕНИЯ БЕЗ ПРОВЕРКИ isBlocked
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
            intent.putExtra("lastActivity", "PastureActivity2500");
            intent.putExtra("callingActivity", "PastureActivity2500");
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

    private void checkAchievements() {
        if (clicks >= 500 && !achievement500Shown && clickValue == 1) {
            showAchievement(500);
            achievement500Shown = true;
            saveGameState("PastureActivity2500");
        } else if (clicks >= 2500 && !achievement2500Shown && clickValue == 3) {
            showAchievement(2500);
            achievement2500Shown = true;
            saveGameState("PastureActivity2500");
        } else if (clicks >= 3500 && !achievement3500Shown && clickValue == 10) {
            showAchievement(3500);
            achievement3500Shown = true;
            saveGameState("PastureActivity2500");
        }
    }

    private void checkForBlocking() {
        if ((clickValue == 1 && clicks >= NEED_CLICKS_RUSTY) ||
                (clickValue == 3 && clicks >= NEED_CLICKS_SHARP) ||
                (clickValue == 10 && clicks >= NEED_CLICKS_EL)) {
            blockGame();
        } else {
            unblockGame();
        }
    }

    private void showAchievement(int achievementLevel) {
        Intent intent;
        switch (achievementLevel) {
            case 500:
                intent = new Intent(this, AchievementActivity500.class);
                break;
            case 2500:
                intent = new Intent(this, AchievementActivity2500.class);
                break;
            case 3500:
                intent = new Intent(this, AchievementActivity3500.class);
                break;
            default:
                return;
        }
        intent.putExtra("clicks", clicks);
        intent.putExtra("currentClickValue", clickValue);
        startActivityForResult(intent, REQUEST_ACHIEVEMENT);
        overridePendingTransition(0, 0);
    }

    private void blockGame() {
        isBlocked = true;
        updateImproveButtonState();
        saveGameState("PastureActivity2500");
    }

    private void unblockGame() {
        isBlocked = false;
        updateImproveButtonState();
        saveGameState("PastureActivity2500");
    }

    private void updateImproveButtonState() {
        // КНОПКА ВСЕГДА АКТИВНА, НО МЕНЯЕМ ВНЕШНИЙ ВИД ПРИ БЛОКИРОВКЕ
        improveButton.setVisibility(View.VISIBLE);
        improveButton.setEnabled(true); // ← ВСЕГДА МОЖНО НАЖАТЬ

        if (isBlocked) {
            // ИГРА ЗАБЛОКИРОВАНА - кнопка обычная
            improveButton.setAlpha(1.0f);
            improveButton.setBackgroundResource(R.drawable.rounded_button);
        } else {
            // ИГРА РАЗБЛОКИРОВАНА - кнопка полупрозрачная, но ВСЕ РАВНО НАЖИМАЕМАЯ
            improveButton.setAlpha(1.0f);
            improveButton.setBackgroundResource(R.drawable.rounded_button);
        }
    }

    private void updateCounter() {
        woolCounter.setText(" " + clicks);
    }

    // ДОБАВЛЕНО: методы жизненного цикла как в PastureActivity0
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

            // УПРОЩЕННАЯ ЛОГИКА КАК В PastureActivity0
            if ("rusty_scissors".equals(improvementType)) {
                clickValue = 3;
                clicks = clicksAfterPurchase;
                isBlocked = false;
                updateImproveButtonState();
                updateCounter();
                saveGameState("PastureActivity2500");

            } else if ("sharp_scissors".equals(improvementType)) {
                clickValue = 10;
                clicks = clicksAfterPurchase;
                isBlocked = false;
                updateImproveButtonState();
                updateCounter();
                saveGameState("PastureActivity2500");

            } else if ("el_machine".equals(improvementType)) {
                clickValue = 50;
                clicks = clicksAfterPurchase;
                isBlocked = false;
                updateImproveButtonState();
                updateCounter();
                saveGameState("PastureActivity2500");

                // Проверяем переход после покупки улучшения
                if (clicks >= 5000) {
                    switchToPasture5000();
                }
            }
        }
    }

    private void switchToPasture5000() {
        saveGameState("PastureActivity5000");

        Intent intent = new Intent(this, PastureActivity5000.class);
        intent.putExtra("clicks", clicks);
        intent.putExtra("currentClickValue", clickValue);
        intent.putExtra("isBlocked", isBlocked);
        intent.putExtra("achievement500Shown", achievement500Shown);
        intent.putExtra("achievement2500Shown", achievement2500Shown);
        intent.putExtra("achievement3500Shown", achievement3500Shown);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }

    private void saveGameState(String lastActivityName) {
        GameDataManager manager = new GameDataManager(this);
        manager.saveGameState(clicks, clickValue, isBlocked,
                achievement500Shown, achievement2500Shown, achievement3500Shown,
                lastActivityName, "PastureActivity2500");
    }
}