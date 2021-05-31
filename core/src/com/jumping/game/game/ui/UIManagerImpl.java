package com.jumping.game.game.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jumping.game.util.interfaces.UIManager;

public class UIManagerImpl implements UIManager, GameUIController {
    private final Stage stage;

    public UIManagerImpl(Viewport viewport, SpriteBatch batch) {
        this.stage = new Stage(viewport, batch);
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
