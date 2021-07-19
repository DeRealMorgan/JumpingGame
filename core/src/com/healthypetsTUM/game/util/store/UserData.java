package com.healthypetsTUM.game.util.store;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.healthypetsTUM.game.character.ui.ClothPiece;
import com.healthypetsTUM.game.util.Values;

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
    private long lastStepStamp, lastPlayStamp;

    private int petAmount, showerAmount, foodAmount, playAmount;

    private Application.ApplicationType type;

    /**
     * Json
     */
    public UserData() {}

    public UserData(int ignore) {
        type = Gdx.app.getType();

        coins = 100;
        lvl = 1;
        math = 0;

        musicVolume = 50;
        soundVolume = 50;
        playMusic = true;
        playSound = true;

        showTutorial = true;

        boughtItems = new ArrayList<>();
        boughtWorlds = new ArrayList<>();
        boughtWorlds.add(0);
        equipedItems = new ArrayList<>();
        unlockedItems = new ArrayList<>();
        unlockedItems.add(0);

        equipedWorld = -1;

        if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            coins = 10000;
            lvl = 10;
            math = 245;
            unlockedItems.add(0);
            unlockedItems.add(1);
            unlockedItems.add(2);
            unlockedItems.add(3);
            lastStepCount = 5500;
        }
    }

    public void newDay() {
        petAmount = Math.max(0, petAmount-1);
        playAmount = Math.max(0, playAmount-1);
        showerAmount = Math.max(0, showerAmount-1);
        foodAmount = Math.max(0, foodAmount-1);
    }

    public void incPetAmount() {
        petAmount = Math.min(petAmount+1, Values.MAX_PET_AMOUNT);
    }

    public void incPlayAmount() {
        playAmount = Math.min(playAmount+1, Values.MAX_PLAY_AMOUNT);
    }

    public void incFoodAmount() {
        foodAmount = Math.min(foodAmount+1, Values.MAX_FOOD_AMOUNT);
    }

    public void incShowerAmount() {
        showerAmount = Math.min(showerAmount+1, Values.MAX_SHOWER_AMOUNT);
    }

    public void setLastPlayStamp(long lastPlayStamp) {
        this.lastPlayStamp = lastPlayStamp;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public void setTotalSteps(int totalSteps) {
        this.totalSteps = totalSteps;
    }

    public void addBoughtItem(int item) {
        boughtItems.add(item);
    }

    public void addBoughtWorld(int world) {
        boughtWorlds.add(world);
    }

    public void unlockItem(int item) {
        unlockedItems.add(item);
    }

    public void equipWorld(int world) {
        equipedWorld = world;
    }

    public void equipItem(int item) {
        ClothPiece piece = ClothPiece.bodyPart(item);
        switch (piece) {
            case HEAD:
                for(int i = 0; i <= 6; ++i) {
                    int finalI = i;
                    equipedItems.removeIf((x) -> x == finalI);
                }
                break;
            case LEGS:
                for(int i = 7; i <= Values.ITEM_COUNT; ++i) {
                    int finalI = i;
                    equipedItems.removeIf((x) -> x == finalI);
                }
                break;
        }
        equipedItems.add(item);
    }

    public void unequipItem(int item) {
        equipedItems.remove((Integer) item);
    }

    public void setMath(int math) {
        this.math = math;
    }

    public void incMath() {
        this.math++;
        this.lvl = 1 + math/25;
    }

    public static int getMathNextLevel(int math, int lvl) {
        return (lvl)*25;
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

    public void setHealthConsent(boolean healthConsent) {
        this.healthConsent = healthConsent;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void setLastStepCount(int lastStepCount) {
        this.lastStepCount = lastStepCount;


        if(type == Application.ApplicationType.Desktop)
            this.lastStepCount = 5500;
    }

    public void treatFound() {
        this.treatFound = true;
    }

    public void setTreatFound(boolean treatFound) {
        this.treatFound = treatFound;
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
        if(type == Application.ApplicationType.Desktop) return lvl;
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

    public int getFoodAmount() {
        return foodAmount;
    }

    public int getPetAmount() {
        return petAmount;
    }

    public int getPlayAmount() {
        return playAmount;
    }

    public int getShowerAmount() {
        return showerAmount;
    }

    public long getLastPlayStamp() {
        return lastPlayStamp;
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