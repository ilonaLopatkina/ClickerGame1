package com.example.clickerproject;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicPlayer {
    private static MusicPlayer instance;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = true;

    private MusicPlayer(Context context) {
        // Создаем только если mediaPlayer еще не создан
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context.getApplicationContext(), R.raw.background_music);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    public static MusicPlayer getInstance(Context context) {
        if (instance == null) {
            instance = new MusicPlayer(context);
        }
        return instance;
    }

    public void toggle() {
        if (isPlaying) {
            pause();
        } else {
            resume();
        }
    }

    public void pause() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    public void resume() {
        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer.start();
            isPlaying = true;
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            instance = null;
        }
    }
}