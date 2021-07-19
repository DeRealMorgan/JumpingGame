package com.healthypetsTUM.game.util.interfaces;

public interface StatsProvider {
    void setStats();
    default boolean isApiTooLow() {
        return false;
    }
}
