package com.healthypetsTUM.game.game.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;

public interface GameUIController {
    GameOverUI getGameOverUI();
    GameOverlayUI getGameOverlayUI();
    PauseUI getPauseUI();

    Stage getStage();
}
