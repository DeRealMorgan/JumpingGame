package com.healthypetsTUM.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.healthypetsTUM.game.util.store.DataUtils;
import com.healthypetsTUM.game.util.store.UserData;

import java.util.HashMap;
import java.util.Map;

public class Sounds {
    private static final FileHandle soundHandle = Gdx.files.internal("sounds/");

    private static Sound[] sounds;
    private static boolean play;
    private static float volume;

    private static Map<Integer, Long> playingSounds;

    public static void init() {
        playingSounds = new HashMap<>();

        sounds = new Sound[9];
        sounds[0] = Gdx.audio.newSound(soundHandle.child("clickSound.wav"));
        sounds[1] = Gdx.audio.newSound(soundHandle.child("coinSound.wav"));
        sounds[2] = Gdx.audio.newSound(soundHandle.child("jumpSound.wav"));
        sounds[3] = Gdx.audio.newSound(soundHandle.child("correctSound.wav"));
        sounds[4] = Gdx.audio.newSound(soundHandle.child("wrongSound.wav"));
        sounds[5] = Gdx.audio.newSound(soundHandle.child("buySound.wav"));
        sounds[6] = Gdx.audio.newSound(soundHandle.child("foodSound.wav"));
        sounds[7] = Gdx.audio.newSound(soundHandle.child("petSound.wav"));
        sounds[8] = Gdx.audio.newSound(soundHandle.child("showerSound.wav"));

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

    public static void buy() {
        if(!play) return;
        sounds[5].play(volume);
    }

    public static int eat() {
        if(!play) return -1;

        if(playingSounds.containsKey(6)) return -1;
        playingSounds.put(6, sounds[6].loop(volume));

        return 6;
    }

    public static int pet() {
        if(!play) return -1;

        if(playingSounds.containsKey(7)) return -1;
        playingSounds.put(7, sounds[7].loop(volume));

        return 7;
    }

    public static int shower() {
        if(!play) return -1;

        if(playingSounds.containsKey(8)) return -1;
        playingSounds.put(8, sounds[8].loop(volume));

        return 8;
    }

    public static void stop(int index) {
        System.out.println("index");
        if(index == -1) return;
        System.out.println(index);
        if(playingSounds.containsKey(index)) {
            sounds[index].stop(playingSounds.get(index));
            System.out.println("true");
        }

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
