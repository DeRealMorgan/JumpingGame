package com.healthypetsTUM.game.game.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.healthypetsTUM.game.util.ZSprite;
import com.healthypetsTUM.game.util.interfaces.UIManager;

import java.util.Collection;

public interface RenderPipeline extends com.healthypetsTUM.game.util.interfaces.RenderPipeline {
    void submitSprites(Collection<ZSprite> sprites);
    void submitSprite(ZSprite sprite);

    void removeSprites(Collection<ZSprite> sprites);
    void removeSprite(ZSprite sprite);

    void moveToTile(float tileY);
    void setToTile(float tileY);
    void follow(float y);

    float getGameCamTopY();
    float getGameCamBottomY();

    void setUiManager(UIManager uiManager);

    Viewport getUIViewport();
    SpriteBatch getBatch();

}
