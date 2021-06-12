package com.jumping.game.util;


public class UserData {
    private int coins;
    private int maxLvl; //das letzte freigeschaltete level

    private String language;

    private int musicVolume;
    private int soundVolume;
    private boolean playMusic;
    private boolean playSound;

    private boolean privacyConsent;
    private boolean showTutorial;

    /**
     * Json
     */
    public UserData() {}

    public UserData(int coins, int maxLvl) {
        this.coins = coins;
        this.maxLvl = maxLvl;

        language = "English";
        musicVolume = 100;
        soundVolume = 100;
        playMusic = true;
        playSound = true;

        showTutorial = true;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public void subCoins(int coins) {
        this.coins -= coins;
    }

    public void increaseMaxLvl(int curLvl) {
        ++curLvl;
        ++this.maxLvl;

        if(this.maxLvl > curLvl) {
            this.maxLvl = curLvl;
        }
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setMusicVolume(int musicVolume) {
        this.musicVolume = musicVolume;
    }

    public void setSoundVolume(int soundVolume) {
        this.soundVolume = soundVolume;
    }

    public void setPlayMusic(boolean playMusic) {
        this.playMusic = playMusic;
    }

    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }

    public void setShowTutorial(boolean showTutorial) {
        this.showTutorial = showTutorial;
    }

    public void setPrivacyConsent(boolean privacyConsent) {
        this.privacyConsent = privacyConsent;
    }

    public String getLanguage() {
        return language;
    }

    public boolean playMusic() {
        return playMusic;
    }

    public boolean playSound() {
        return playSound;
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public int getSoundVolume() {
        return soundVolume;
    }

    public int getCoins() {
        return coins;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public static int minutesToMillis(int minutes) {
        return minutes*60*1000;
    }

    public static int hoursToMillis(int hours) {
        return hours*60*60*1000;
    }

    public boolean hasPrivacyConsent() {
        return privacyConsent;
    }

    public boolean showTutorial() {
        return showTutorial;
    }
}