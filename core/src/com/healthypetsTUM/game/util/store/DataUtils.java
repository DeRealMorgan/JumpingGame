package com.healthypetsTUM.game.util.store;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.interfaces.StoreProvider;

public final class DataUtils {
    public static final FileHandle langFileHandleInt = Gdx.files.internal("lan/");

    public static FileHandle userDataFileHandleLoc;

    private static UserData userData;
    private static final Json json = new Json();
    private static StoreProvider storeProvider;

    public static void init(StoreProvider storeProvider) {
        DataUtils.storeProvider = storeProvider;

        userDataFileHandleLoc = Gdx.files.local("HealthPets/usr.data");
    }

    public static void firstStart() {
        userData = new UserData(-1);
        storeUserData();
    }

    public static UserData getUserData() {
        if(userData != null) return userData;

        if(Gdx.app.getType() != Application.ApplicationType.Android)
            userData = json.fromJson(UserData.class, Base64Coder.decodeString(userDataFileHandleLoc.readString()));
        else {
            userData = json.fromJson(UserData.class, Base64Coder.decodeString(storeProvider.load(Values.USER_DATA)));
        }

        return userData;
    }

    public static String getDataPath() {
        return userDataFileHandleLoc.path();
    }

    public static void storeUserData() {
        String data = Base64Coder.encodeString(json.toJson(userData));

        storeProvider.store(Values.USER_DATA, data);
    }

    public static FileHandle getLangDataFileHandle(String lang) {
        return langFileHandleInt.child(lang+".data");
    }
}