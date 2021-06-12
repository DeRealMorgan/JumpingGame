package com.jumping.game.util;


import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class SquareButton extends Stack {
    private Button btn;

    public SquareButton(Drawable icon, Drawable background) {
        Image backImg = new Image(background);

        Button.ButtonStyle bs = new Button.ButtonStyle();
        bs.up = icon;
        bs.down = bs.up;
        bs.pressedOffsetX = 5f;
        bs.pressedOffsetY = 5f;
        btn = new Button(bs);
        add(backImg);
        add(btn);
    }

    @Override
    public boolean addListener(EventListener listener) {
        return btn.addListener(listener);
    }

    @Override
    public void setTouchable(Touchable touchable) {
        super.setTouchable(touchable);
    }

    @Override
    public void setVisible(boolean visible) {
        if(visible) {
            super.setVisible(true);
            super.setTouchable(Touchable.enabled);
            return;
        }

        super.setVisible(false);
        super.setTouchable(Touchable.disabled);
    }
}