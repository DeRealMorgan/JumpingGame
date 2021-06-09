package com.jumping.game.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jumping.game.assets.AssetsManager;
import com.jumping.game.util.interfaces.ScreenManager;
import com.jumping.game.util.interfaces.UIManager;

public class UIManagerImpl implements UIManager, GameUIController {
    private final Stage stage;

    private final GameOverUI gameOverUI;
    private final GameOverlayUI gameOverlayUI;
    private final PauseUI pauseUI;

    public UIManagerImpl(Viewport viewport, SpriteBatch batch, AssetsManager assetsManager,
                         ScreenManager screenManager, Runnable onPause, Runnable onResume) {
        stage = new Stage(viewport, batch);
        assetsManager.addInputProcessor(stage);

        gameOverUI = new GameOverUI(assetsManager, screenManager);
        gameOverUI.addToStage(stage);

        gameOverlayUI = new GameOverlayUI(assetsManager, onPause);
        gameOverlayUI.addToStage(stage);

        pauseUI = new PauseUI(assetsManager, screenManager, onResume);
        pauseUI.addToStage(stage);
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    public void updateScore(int score) {
        gameOverlayUI.updateScore(score);
        gameOverUI.updateScore(score);
        pauseUI.updateScore(score);
    }

    public void updateMathScore(int score) {
        gameOverlayUI.updateMathScore(score);
        gameOverUI.updateMathScore(score);
        pauseUI.updateMathScore(score);
    }

    @Override
    public GameOverUI getGameOverUI() {
        return gameOverUI;
    }

    @Override
    public GameOverlayUI getGameOverlayUI() {
        return gameOverlayUI;
    }

    @Override
    public PauseUI getPauseUI() {
        return pauseUI;
    }

    @Override
    public void drawUI() {
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
