package com.jumping.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public final class DataUtils {
    public static final FileHandle lvlFileHandleInt = Gdx.files.internal("levels/");
    public static final FileHandle langFileHandleInt = Gdx.files.internal("lan/");

    public static FileHandle lvlFileHandleLoc;
    public static FileHandle userDataFileHandleLoc;

    private static UserData userData;
    private static Json json = new Json();

    public static void init() {
        lvlFileHandleLoc = Gdx.files.local("HexWormsGame/levels/");
        userDataFileHandleLoc = Gdx.files.local("HexWormsGame/usr.data");
    }

    public static UserData getUserData() {
        if(userData == null)
            userData = json.fromJson(UserData.class, Base64Coder.decodeString(userDataFileHandleLoc.readString()));

        return userData;
    }

    public static void storeUserData() {
        String data = Base64Coder.encodeString(json.toJson(userData));

        new Thread(() -> userDataFileHandleLoc.writeString(data, false)).start();
    }

    public static FileHandle getLangDataFileHandle(String lang) {
        return langFileHandleInt.child(lang+".data");
    }

    public static FileHandle getLvlDataFileHandleLoc(int lvl) {
        return lvlFileHandleLoc.child("levels_"+((lvl/100)*100)+"/levels_" + ((lvl/100)*100) +".data");
    }

    public static FileHandle getLvlDataFileHandleInt(int lvl) {
        return lvlFileHandleInt.child("levels_"+((lvl/100)*100)+"/levels_" + ((lvl/100)*100) +".data");
    }
}