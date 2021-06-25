package com.healthypetsTUM.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.healthypetsTUM.game.util.store.DataUtils;
import com.healthypetsTUM.game.util.store.UserData;

public class Sounds {
    private static final FileHandle soundHandle = Gdx.files.internal("sounds/");

    private static Sound[] sounds;
    private static boolean play;
    private static float volume;

    public static void init() {
        sounds = new Sound[5];
        sounds[0] = Gdx.audio.newSound(soundHandle.child("clickSound.wav"));
        sounds[1] = Gdx.audio.newSound(soundHandle.child("coinSound.wav"));
        sounds[2] = Gdx.audio.newSound(soundHandle.child("jumpSound.wav"));
        sounds[3] = Gdx.audio.newSound(soundHandle.child("correctSound.wav"));
        sounds[4] = Gdx.audio.newSound(soundHandle.child("wrongSound.wav"));

        UserData data = DataUtils.getUserData();
        play = data.playSound();
        changeVolume(data.getSoundVolume());
    }

    public static void click() {
        if(!play) return;
        sounds[0].play(volume);
    }

    public static void coins() {
        if(!play) return;
        sounds[1].play(volume);
    }

    public static void jump() {
        if(!play) return;
        sounds[2].play(volume);
    }

    public static void correct() {
        if(!play) return;
        sounds[3].play(volume);
    }

    public static void wrong() {
        if(!play) return;
        sounds[4].play(volume);
    }

    public static void mute(boolean mute) {
        play = !mute;
    }

    public static void changeVolume(int vol) {
        volume = vol*0.01f;
    }

    public static void dispose() {
        for (Sound s : sounds) s.dispose();
    }
}
