package com.jumping.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.jumping.game.util.interfaces.StoreProvider;

import java.util.Optional;

public class StoreProviderImpl implements StoreProvider {
    @Override
    public Optional<Boolean> getBool(String key) {
        Preferences preferences = Gdx.app.getPreferences(Values.PREFERENCES);
        if(preferences.contains(key))
            return Optional.of(preferences.getBoolean(key));
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getInt(String key) {
        Preferences preferences = Gdx.app.getPreferences(Values.PREFERENCES);
        if(preferences.contains(key))
            return Optional.of(preferences.getInteger(key));
        return Optional.empty();
    }

    @Override
    public Optional<Long> getLong(String key) {
        Preferences preferences = Gdx.app.getPreferences(Values.PREFERENCES);
        if(preferences.contains(key))
            return Optional.of(preferences.getLong(key));
        return Optional.empty();
    }
}
