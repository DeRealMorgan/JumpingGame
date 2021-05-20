package com.jumping.game.game;

import com.jumping.game.game.player.PlayerListener;

public interface GameManager extends PlayerListener {
    void pauseUpdate();
    void resumeUpdate();
}
