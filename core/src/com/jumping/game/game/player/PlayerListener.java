package com.jumping.game.game.player;

import com.jumping.game.game.physics.PhysicsEntity;

public interface PlayerListener {
    void jumpedToNextPlatform(PhysicsEntity platform);
    void scoreChanged(int score);
    void gameOver();
}
