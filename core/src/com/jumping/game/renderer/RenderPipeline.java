package com.jumping.game.renderer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jumping.game.game.ui.UIManager;
import com.jumping.game.util.ZSprite;

import java.util.Collection;

public interface RenderPipeline {
    void submitSprites(Collection<ZSprite> sprites);
    void submitSprite(ZSprite sprite);

    void removeSprites(Collection<ZSprite> sprites);
    void removeSprite(ZSprite sprite);

    void moveToTile(float tileY);
    void setToTile(float tileY);
    void follow(float y);

    float getGameCamTopY();
    float getGameCamBottomY();

    void update(float dt);

    void setUiManager(UIManager uiManager);

    Viewport getUIViewport();
    SpriteBatch getBatch();
}
