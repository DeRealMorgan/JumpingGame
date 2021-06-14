package com.jumping.game.assets;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public interface AssetsManager {
    TextureRegion getTextureRegion(String name);
    Sprite getSprite(String name);
    Drawable getDrawable(String name);
    Drawable get9Drawable(String name);
    SpriteDrawable getSpriteDrawable(String name);

    Label.LabelStyle labelStyleSmall();
    Label.LabelStyle labelStyle();
    Label.LabelStyle labelStyleBig();
    TextField.TextFieldStyle textFieldStyleBig();
    TextButton.TextButtonStyle textBtnStyle();

    SpriteDrawable getBackground(String name);

    void addInputProcessor(InputProcessor processor);
    void removeInputProcessor(InputProcessor processor);
    void clearInputProcessors();
}
