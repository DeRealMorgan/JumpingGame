package com.healthypetsTUM.game.character;

import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.character.renderer.RenderPipeline;
import com.healthypetsTUM.game.character.renderer.RenderPipelineImpl;
import com.healthypetsTUM.game.character.ui.UIManagerImpl;
import com.healthypetsTUM.game.util.interfaces.Screen;
import com.healthypetsTUM.game.util.interfaces.ScreenManager;

public class CharacterScreen implements Screen {
    private final RenderPipelineImpl renderPipeline;
    private final UIManagerImpl uiManager;
    private final AssetsManager assetsManager;

    public CharacterScreen(AssetsManager assetsManager, ScreenManager screenManager) {
        this.renderPipeline = new RenderPipelineImpl();
        this.assetsManager = assetsManager;

        this.uiManager = new UIManagerImpl(renderPipeline.getUIViewport(), renderPipeline.getBatch(), assetsManager,
                screenManager);
        this.renderPipeline.setUiManager(this.uiManager);
    }

    public void currentSteps(int steps) {
        uiManager.currentSteps(steps);
    }

    @Override
    public void show() {
        uiManager.show();
        // todo add all sprites to the pipeline
    }

    @Override
    public void render(float delta) {}

    @Override
    public void update(float dt) {
        uiManager.update(dt);
        renderPipeline.update(dt);
    }

    @Override
    public void resize(int width, int height) {
        uiManager.resize(width, height);
    }

    @Override
    public void pause() {
        //TODO
    }

    @Override
    public void resume() {
        //TODO
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {

    }

    @Override
    public RenderPipeline getRenderPipeline() {
        return renderPipeline;
    }
}

