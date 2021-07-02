package com.healthypetsTUM.game.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.interfaces.ScreenManager;
import com.healthypetsTUM.game.util.interfaces.UIManager;
import com.healthypetsTUM.game.util.store.DataUtils;
import com.healthypetsTUM.game.util.store.UserData;
import com.healthypetsTUM.game.util.ui.UIBar;

public class UIManagerImpl implements UIManager, GameUIController {
    private final Stage stage, stageBack;

    private final GameOverUI gameOverUI;
    private final GameOverlayUI gameOverlayUI;
    private final PauseUI pauseUI;

    private final UIBar uiBar;

    public UIManagerImpl(Viewport viewport, SpriteBatch batch, AssetsManager assetsManager,
                         ScreenManager screenManager, Runnable onPause, Runnable onResume, Runnable onBackPressed) {
        stage = new Stage(viewport, batch);
        stageBack = new Stage(viewport, batch);

        assetsManager.addInputProcessor(stage);

        gameOverlayUI = new GameOverlayUI(assetsManager, onPause);
        gameOverlayUI.addToStage(stage);

        gameOverUI = new GameOverUI(assetsManager, screenManager);
        gameOverUI.addToStage(stage);

        pauseUI = new PauseUI(assetsManager, screenManager, onResume, onBackPressed);
        pauseUI.addToStage(stage);

        uiBar = new UIBar(assetsManager);
        stage.addActor(uiBar.table);

        Image background = new Image(assetsManager.getBackground(DataUtils.getUserData().getEquipedWorld()
                + Values.BACKGROUND));
        Table backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.center();
        backgroundTable.add(background).grow();
        stageBack.addActor(backgroundTable);

        updateUIBar();
    }

    @Override
    public void update(float dt) {
        stageBack.act(dt);
        stage.act(dt);
    }

    public void updateScore(int coins) {
        gameOverlayUI.updateCoins(coins);
        gameOverUI.updateCoins(coins);
        pauseUI.updateCoins(coins);
    }

    public void updateMathScore(int coins, int math) {
        uiBar.setMath(math, DataUtils.getUserData().getLvl());
        gameOverlayUI.updateCoins(coins);
        gameOverUI.updateCoins(coins);
        pauseUI.updateCoins(coins);
    }

    private void updateUIBar() {
        UserData data = DataUtils.getUserData();
        uiBar.setCoins(data.getCoins());
        uiBar.setMath(data.getMath(), data.getLvl());
        uiBar.setLvl(data.getLvl());
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
    public void drawUIBottom() {
        stageBack.draw();
    }

    @Override
    public void drawUITop() {
        stage.draw();
    }

    @Override
    public void resize(int w, int h) {
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
