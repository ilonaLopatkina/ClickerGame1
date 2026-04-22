package com.example.clickerproject;

import android.content.Context;
import android.content.SharedPreferences;

public class GameDataManager {

    private static final String PREFS_NAME = "ClickerGameData";
    private static final String KEY_CLICKS = "clicks";
    private static final String KEY_CLICK_VALUE = "clickValue";
    private static final String KEY_IS_BLOCKED = "isBlocked";
    private static final String KEY_ACHIEVEMENT_500 = "achievement500Shown";
    private static final String KEY_ACHIEVEMENT_2500 = "achievement2500Shown";
    private static final String KEY_ACHIEVEMENT_3500 = "achievement3500Shown";
    private static final String KEY_LAST_ACTIVITY = "lastActivity";
    private static final String KEY_CURRENT_ACTIVITY = "currentActivity"; // ← НОВОЕ ПОЛЕ!

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public GameDataManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveGameState(int clicks, int clickValue, boolean isBlocked,
                              boolean achievement500, boolean achievement2500,
                              boolean achievement3500, String lastActivity, String currentActivity) { // ← ДОБАВИЛ currentActivity
        editor.putInt(KEY_CLICKS, clicks);
        editor.putInt(KEY_CLICK_VALUE, clickValue);
        editor.putBoolean(KEY_IS_BLOCKED, isBlocked);
        editor.putBoolean(KEY_ACHIEVEMENT_500, achievement500);
        editor.putBoolean(KEY_ACHIEVEMENT_2500, achievement2500);
        editor.putBoolean(KEY_ACHIEVEMENT_3500, achievement3500);
        editor.putString(KEY_LAST_ACTIVITY, lastActivity);
        editor.putString(KEY_CURRENT_ACTIVITY, currentActivity); // ← СОХРАНЯЕМ ТЕКУЩУЮ АКТИВНОСТЬ
        editor.apply();
    }

    public void resetGame() {
        editor.clear();
        editor.apply();
    }

    public int getClicks() {
        return prefs.getInt(KEY_CLICKS, 0);
    }

    public int getClickValue() {
        return prefs.getInt(KEY_CLICK_VALUE, 1);
    }

    public boolean isBlocked() {
        return prefs.getBoolean(KEY_IS_BLOCKED, false);
    }

    public boolean isAchievement500Shown() {
        return prefs.getBoolean(KEY_ACHIEVEMENT_500, false);
    }

    public boolean isAchievement2500Shown() {
        return prefs.getBoolean(KEY_ACHIEVEMENT_2500, false);
    }

    public boolean isAchievement3500Shown() {
        return prefs.getBoolean(KEY_ACHIEVEMENT_3500, false);
    }

    public String getLastActivity() {
        return prefs.getString(KEY_LAST_ACTIVITY, "PastureActivity0");
    }

    // ← НОВЫЙ МЕТОД!
    public String getCurrentActivity() {
        return prefs.getString(KEY_CURRENT_ACTIVITY, "SplashActivity");
    }
}