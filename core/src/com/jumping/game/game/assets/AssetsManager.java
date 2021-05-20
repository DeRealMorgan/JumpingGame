package com.jumping.game.game.assets;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public interface AssetsManager {
    Sprite getSprite(String name);
    Drawable getDrawable(String name);
    Label.LabelStyle labelStyleBig();
}
