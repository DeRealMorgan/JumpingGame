package com.jumping.game.character.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jumping.game.util.UIManager;

public interface RenderPipeline extends com.jumping.game.util.interfaces.RenderPipeline {
    void setUiManager(UIManager uiManager);

    Viewport getUIViewport();
    SpriteBatch getBatch();
}
