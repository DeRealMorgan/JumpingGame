package com.jumping.game.assets;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public interface AssetsManager {
    Sprite getSprite(String name);
    Drawable getDrawable(String name);
    Drawable get9Drawable(String name);

    Label.LabelStyle labelStyleBig();
    TextField.TextFieldStyle textFieldStyleBig();

    void addInputProcessor(InputProcessor processor);
    void removeInputProcessor(InputProcessor processor);
}
