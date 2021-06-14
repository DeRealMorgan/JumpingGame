package com.jumping.game.util;


import java.util.ArrayList;
import java.util.List;

public class UserData {
    private int coins;
    private int lvl; //das letzte freigeschaltete level
    private int math;

    private int totalSteps;

    private List<Integer> boughtItems, boughtWorlds, equipedItems;

    private int equipedWorld;

    private int musicVolume;
    private int soundVolume;
    private boolean playMusic;
    private boolean playSound;

    private boolean privacyConsent;
    private boolean showTutorial;
    private boolean isRunning;

    /**
     * Json
     */
    public UserData() {}

    public UserData(int ignore) {
        coins = 500;
        lvl = 1;
        math = 0;

        musicVolume = 100;
        soundVolume = 100;
        playMusic = true;
        playSound = true;

        showTutorial = true;

        boughtItems = new ArrayList<>();
        boughtWorlds = new ArrayList<>();
        equipedItems = new ArrayList<>();

        equipedWorld = 0;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public void addItem(int item) {
        boughtItems.add(item);
    }

    public void addWorld(int world) {
        boughtWorlds.add(world);
    }

    public void equipWorld(int world) {
        equipedWorld = world;
    }

    public void equipItem(int item) {
        // TODO: remove other equiped item of same type
        equipedItems.add(item);
    }

    public void setMath(int math) {
        this.math = math;
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

    public void setRunning(boolean running) {
        isRunning = running;
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

    public int getLvl() {
        return lvl;
    }

    public int getMath() {
        return math;
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public List<Integer> getBoughtItems() {
        return boughtItems;
    }

    public List<Integer> getBoughtWorlds() {
        return boughtWorlds;
    }

    public int getEquipedWorld() {
        return equipedWorld;
    }

    public List<Integer> getEquipedItems() {
        return equipedItems;
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

    public boolean isRunning() {
        return isRunning;
    }

    public boolean showTutorial() {
        return showTutorial;
    }
}