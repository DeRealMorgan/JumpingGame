package com.jumping.game.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jumping.game.assets.AssetsManager;
import com.jumping.game.util.interfaces.ScreenManager;
import com.jumping.game.util.interfaces.UIManager;

public class UIManagerImpl implements UIManager, GameUIController {
    private final Stage stage;

    private final GameOverUI gameOverUI;

    public UIManagerImpl(Viewport viewport, SpriteBatch batch, AssetsManager assetsManager, ScreenManager screenManager) {
        stage = new Stage(viewport, batch);
        assetsManager.addInputProcessor(stage);

        gameOverUI = new GameOverUI(assetsManager, screenManager);
        gameOverUI.addToStage(stage);
    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void showMathDialog(Table t) {
        stage.addActor(t);
        t.setVisible(true);
    }

    @Override
    public GameOverUI getGameOverUI() {
        return gameOverUI;
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
