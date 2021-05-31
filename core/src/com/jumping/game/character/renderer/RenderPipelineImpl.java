package com.jumping.game.character.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jumping.game.util.interfaces.UIManager;
import com.jumping.game.util.Values;

public class RenderPipelineImpl implements RenderPipeline {
    private final SpriteBatch batch;

    private UIManager uiManager;

    private final Viewport uiViewport;
    private final OrthographicCamera uiCam;

    public RenderPipelineImpl() {
        this.batch = new SpriteBatch();

        this.uiCam = new OrthographicCamera();
        this.uiViewport = new ExtendViewport(Values.CHARACTER_WORLD_WIDTH, Values.CHARACTER_WORLD_HEIGHT, this.uiCam);
        this.uiCam.position.set(uiViewport.getWorldWidth()/2, uiViewport.getWorldHeight()/2, 0);
    }

    @Override
    public void update(float dt) {
        uiCam.update();
    }

    public void render(float dt) {
        Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        uiViewport.apply();
        batch.setProjectionMatrix(uiCam.combined);
        uiManager.drawUI();
    }

    public void resize(int width, int height) {
        uiViewport.update(width, height);
    }

    @Override
    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    @Override
    public Viewport getUIViewport() {
        return uiViewport;
    }

    @Override
    public SpriteBatch getBatch() {
        return batch;
    }

    public void dispose() {
        batch.dispose();
    }
}
