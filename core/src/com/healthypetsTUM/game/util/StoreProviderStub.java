package com.healthypetsTUM.game.util;

import com.healthypetsTUM.game.util.interfaces.StoreProvider;

public class StoreProviderStub implements StoreProvider {
    @Override
    public void store(String name, String val) {
    }

    @Override
    public String load(String name) {
        return "";
    }
}
