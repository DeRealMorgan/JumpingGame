package com.jumping.game.util.interfaces;

import java.util.Optional;

public interface StoreProvider {
    Optional<Boolean> getBool(String key);
    Optional<Integer> getInt(String key);
    Optional<Long> getLong(String key);
}
