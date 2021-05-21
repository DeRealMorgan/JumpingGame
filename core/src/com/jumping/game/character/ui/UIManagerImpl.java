package com.jumping.game.character.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jumping.game.assets.AssetsManager;
import com.jumping.game.util.UIManager;

public class UIManagerImpl implements UIManager {
    private final Stage stage;

    private Table contentTable, backgroundTable;
    private ImageButton shopBtn, achievementsBtn, showerBtn, foodBtn, petBtn, minigameBtn;

    public UIManagerImpl(Viewport viewport, SpriteBatch batch, AssetsManager assetsManager) {
        this.stage = new Stage(viewport, batch);

        createUI(assetsManager);
    }

    private void createUI(AssetsManager assetsManager) {

    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void drawUI() {
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
