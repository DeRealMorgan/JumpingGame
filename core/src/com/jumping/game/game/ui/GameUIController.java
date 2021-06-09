package com.jumping.game.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public interface GameUIController {
    GameOverUI getGameOverUI();
    GameOverlayUI getGameOverlayUI();
    PauseUI getPauseUI();
}
