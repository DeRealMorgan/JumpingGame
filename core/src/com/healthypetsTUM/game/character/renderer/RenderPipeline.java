package com.healthypetsTUM.game.character.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.healthypetsTUM.game.util.interfaces.UIManager;

public interface RenderPipeline extends com.healthypetsTUM.game.util.interfaces.RenderPipeline {
    void setUiManager(UIManager uiManager);

    Viewport getUIViewport();
    SpriteBatch getBatch();
}
