package com.healthypetsTUM.game.util;

import com.healthypetsTUM.game.util.interfaces.RenderPipeline;
import com.healthypetsTUM.game.util.interfaces.Screen;
import com.healthypetsTUM.game.util.ui.EmptyRenderPipelineImpl;

public class EmptyScreen implements Screen {
    private EmptyRenderPipelineImpl renderPipeline;

    public EmptyScreen() {}

    @Override
    public void show() {}

    @Override
    public void update(float dt) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

    @Override
    public RenderPipeline getRenderPipeline() {
        return renderPipeline;
    }
}
