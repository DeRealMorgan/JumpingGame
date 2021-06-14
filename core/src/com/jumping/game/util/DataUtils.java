package com.jumping.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public final class DataUtils {
    public static final FileHandle langFileHandleInt = Gdx.files.internal("lan/");

    public static FileHandle userDataFileHandleLoc;

    private static UserData userData;
    private static Json json = new Json();

    public static void init() {
        userDataFileHandleLoc = Gdx.files.local("HealthPets/usr.data");
    }

    public static void firstStart() {
        UserData userData = new UserData(-1);
        DataUtils.userDataFileHandleLoc.writeString(Base64Coder.encodeString(json.toJson(userData)), false);
    }

    public static UserData getUserData() {
        if(userData == null)
            userData = json.fromJson(UserData.class, Base64Coder.decodeString(userDataFileHandleLoc.readString()));

        return userData;
    }

    public static String getDataPath() {
        return userDataFileHandleLoc.path();
    }

    public static Thread storeUserData() {
        String data = Base64Coder.encodeString(json.toJson(userData));

        Thread t = new Thread(() -> userDataFileHandleLoc.writeString(data, false));
        t.start();

        return t;
    }

    public static FileHandle getLangDataFileHandle(String lang) {
        return langFileHandleInt.child(lang+".data");
    }
}