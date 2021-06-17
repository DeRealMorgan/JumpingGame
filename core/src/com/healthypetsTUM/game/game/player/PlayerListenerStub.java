package com.healthypetsTUM.game.game.player;

import com.healthypetsTUM.game.game.physics.PhysicsEntity;

public class PlayerListenerStub implements PlayerListener {
    @Override
    public void jumpedToNextPlatform(PhysicsEntity entity) {}

    @Override
    public void scoreChanged(int score) {}

    @Override
    public void gameOver() {}
}
