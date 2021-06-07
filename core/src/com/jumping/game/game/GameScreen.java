package com.jumping.game.game;

import com.jumping.game.assets.AssetsManager;
import com.jumping.game.game.renderer.RenderPipeline;
import com.jumping.game.game.renderer.RenderPipelineImpl;
import com.jumping.game.util.interfaces.Screen;
import com.jumping.game.util.interfaces.ScreenManager;

public class GameScreen implements Screen {
    private final RenderPipelineImpl renderPipeline;
    private final GameManagerImpl gameManager;
    private final AssetsManager assetsManager;

    public GameScreen(AssetsManager assetsManager, ScreenManager screenManager) {
        this.renderPipeline = new RenderPipelineImpl();
        this.assetsManager = assetsManager;

        this.gameManager = new GameManagerImpl(renderPipeline, assetsManager, screenManager);
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

    @Override
    public RenderPipeline getRenderPipeline() {
        return renderPipeline;
    }
}
