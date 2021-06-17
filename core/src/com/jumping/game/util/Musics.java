package com.jumping.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.jumping.game.util.store.DataUtils;
import com.jumping.game.util.store.UserData;

public class Musics implements Disposable { //siehe Sounds
    private static final FileHandle musicHandle = Gdx.files.internal("music/");

    private Music currSong;
    private boolean play;
    private float volume;

    private int currentSong;

    public Musics() {
        UserData data = DataUtils.getUserData();

        this.play = data.playMusic();
        this.volume = data.getMusicVolume();

        loadSong();
    }

    private void loadSong() {
        if(currSong != null) {
            dispose();
        }

        switch (currentSong) { //todo song mit Nr currentSong laden
            default:
                currSong = Gdx.audio.newMusic(musicHandle.child("Track1.mp3"));
                break;
        }
        currSong.setVolume(volume);
        if(!play) currSong.setVolume(0);

        ++currentSong;
    }

    public void mute(boolean mute) {
        play = !mute;

        if(currSong != null) {
            if(!play) currSong.setVolume(0);
            else currSong.setVolume(volume);
        }
    }

    public void changeVolume(int vol) {
        volume = vol*0.01f;

        if(currSong != null) {
            if(play) currSong.setVolume(volume);
            else currSong.setVolume(0);
        }
    }

    public void start() {
        currSong.play();

        currSong.setOnCompletionListener((music) -> {
            loadSong();
            start();
        });
    }

    public void stop() {
        currSong.stop();
        currSong.setPosition(0);
    }


    @Override
    public void dispose() {
        currSong.dispose();
        currSong = null;
    }
}
