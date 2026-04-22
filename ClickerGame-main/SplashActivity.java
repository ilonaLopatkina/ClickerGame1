package com.example.clickerproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Блокировка кнопки "назад"
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {}
        });

        GameDataManager manager = new GameDataManager(this);
        int savedClicks = manager.getClicks();

        if (savedClicks > 0) {
            // игрок уже играл
            switchToPasture(manager.getLastActivity(), manager);
            return;
        }

        //Музыка
        MusicPlayer.getInstance(this);

        // Кнопка "Играть"
        Button playButton = findViewById(R.id.button);
        setupButtonAnimation(playButton);
        playButton.setOnClickListener(v -> switchToPasture("PastureActivity0", new GameDataManager(this)));

        // Кнопка "Настройки"
        ImageButton settingsButton = findViewById(R.id.button4);
        setupButtonAnimation(settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        });

        // Кнопка "Правила"
        ImageButton rulesButton = findViewById(R.id.button3);
        setupButtonAnimation(rulesButton);
        rulesButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RulesActivity.class);
            startActivity(intent);
            overridePendingTransition(0,0);
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

    private void switchToPasture(String lastActivity, GameDataManager manager) {
        Class<?> activityClass;

        // ИСПОЛЬЗУЕМ ТЕПЕРЬ CURRENT_ACTIVITY вместо LAST_ACTIVITY
        String activityToLaunch = manager.getCurrentActivity();

        switch (activityToLaunch) {
            case "PastureActivity0":
                activityClass = PastureActivity0.class;
                break;
            case "PastureActivity2500":
                activityClass = PastureActivity2500.class;
                break;
            case "PastureActivity5000":
                activityClass = PastureActivity5000.class;
                break;
            case "AchievementActivity500":
                activityClass = AchievementActivity500.class;
                break;
            case "AchievementActivity2500":
                activityClass = AchievementActivity2500.class;
                break;
            case "AchievementActivity3500":
                activityClass = AchievementActivity3500.class;
                break;
            case "ImprovementsActivity":
                activityClass = ImprovementsActivity.class;
                break;
            default:
                activityClass = PastureActivity0.class;
        }

        Intent intent = new Intent(this, activityClass);
        intent.putExtra("clicks", manager.getClicks());
        intent.putExtra("currentClickValue", manager.getClickValue());
        intent.putExtra("isBlocked", manager.isBlocked());
        intent.putExtra("achievement500Shown", manager.isAchievement500Shown());
        intent.putExtra("achievement2500Shown", manager.isAchievement2500Shown());
        intent.putExtra("achievement3500Shown", manager.isAchievement3500Shown());
        startActivity(intent);
        finish();
        overridePendingTransition(0,0);
    }
}