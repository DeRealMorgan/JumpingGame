package com.jumping.game.game;

import com.jumping.game.assets.AssetsManager;
import com.jumping.game.game.renderer.RenderPipeline;
import com.jumping.game.util.interfaces.Screen;

public class GameScreen implements Screen {
    private final RenderPipeline renderPipeline;
    private final GameManagerImpl gameManager;
    private final AssetsManager assetsManager;

    public GameScreen(RenderPipeline renderPipeline, AssetsManager assetsManager) {
        this.renderPipeline = renderPipeline;
        this.assetsManager = assetsManager;

        this.gameManager = new GameManagerImpl(renderPipeline, assetsManager);
    }

    @Override
    public void show() {
        gameManager.show();
        // todo add all sprites to the pipeline
    }

    @Override
    public void update(float dt) {
        gameManager.update(dt);
        renderPipeline.update(dt);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        gameManager.hide();
    }

    @Override
    public void dispose() {

    }
}
