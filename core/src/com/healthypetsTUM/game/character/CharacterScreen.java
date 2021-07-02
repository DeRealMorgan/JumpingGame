package com.healthypetsTUM.game.character;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.character.renderer.RenderPipeline;
import com.healthypetsTUM.game.character.renderer.RenderPipelineImpl;
import com.healthypetsTUM.game.character.ui.UIManagerImpl;
import com.healthypetsTUM.game.util.interfaces.GoogleFit;
import com.healthypetsTUM.game.util.interfaces.Screen;
import com.healthypetsTUM.game.util.interfaces.ScreenManager;

public class CharacterScreen implements Screen {
    private final RenderPipelineImpl renderPipeline;
    private final UIManagerImpl uiManager;

    public CharacterScreen(AssetsManager assetsManager, ScreenManager screenManager, GoogleFit googleFit) {
        this.renderPipeline = new RenderPipelineImpl();

        this.uiManager = new UIManagerImpl(renderPipeline.getUIViewport(), renderPipeline.getBatch(), assetsManager,
                screenManager, googleFit::signIn);
        this.renderPipeline.setUiManager(this.uiManager);
    }

    public void currentSteps(int steps) {
        if(Gdx.app.getType() == Application.ApplicationType.Desktop)
            uiManager.currentSteps(5500);
        else
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

