package com.healthypetsTUM.game.util.interfaces;

public interface StoreProvider {
    void store(String name, String val);
    String load(String name);

}
