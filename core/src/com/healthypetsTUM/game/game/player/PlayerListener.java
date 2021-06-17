package com.healthypetsTUM.game.game.player;

import com.healthypetsTUM.game.game.physics.PhysicsEntity;

public interface PlayerListener {
    void jumpedToNextPlatform(PhysicsEntity platform);
    void scoreChanged(int score);
    void gameOver();
}
