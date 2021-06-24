package com.healthypetsTUM.game.util.store;


import java.util.ArrayList;
import java.util.List;

public class UserData {
    private int coins;
    private int lvl; //das letzte freigeschaltete level
    private int math;

    private int totalSteps;

    private List<Integer> boughtItems, boughtWorlds, equipedItems, unlockedItems;

    private int equipedWorld;

    private int musicVolume;
    private int soundVolume;
    private boolean playMusic;
    private boolean playSound;

    private boolean privacyConsent, healthConsent;
    private boolean showTutorial;
    private boolean isRunning;

    private boolean treatFound;

    private int lastStepCount;
    private long lastStepStamp;

    /**
     * Json
     */
    public UserData() {}

    public UserData(int ignore) {
        coins = 500;
        lvl = 1;
        math = 0;

        musicVolume = 50;
        soundVolume = 50;
        playMusic = true;
        playSound = true;

        showTutorial = true;

        boughtItems = new ArrayList<>();
        boughtWorlds = new ArrayList<>();
        equipedItems = new ArrayList<>();
        unlockedItems = new ArrayList<>();
        unlockedItems.add(0);
        unlockedItems.add(3);

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

    public void unlockItem(int item) {
        unlockedItems.add(item);
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

    public void incMath() { this.math++;}

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

    public void setHealthConsent(boolean healthConsent) {
        this.healthConsent = healthConsent;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void setLastStepCount(int lastStepCount) {
        this.lastStepCount = lastStepCount;
    }

    public void treatFound() {
        this.treatFound = true;
    }

    public void setLastStepStamp(long lastStepStamp) {
        this.lastStepStamp = lastStepStamp;
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

    public int getLastStepCount() {
        return lastStepCount;
    }

    public long getLastStepStamp() {
        return lastStepStamp;
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

    public List<Integer> getUnlockedItems() {
        return unlockedItems;
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

    public boolean hasHealthConsent() {
        return healthConsent;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isTreatFound() {
        return treatFound;
    }

    public boolean showTutorial() {
        return showTutorial;
    }
}