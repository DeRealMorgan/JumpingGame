package com.healthypetsTUM.game.util.ui;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.healthypetsTUM.game.util.Sounds;

/**
 * ToggleButton allows to have a button in two states -> set and unset
 */
public class ToggleButton {
    private Image image;
    private Drawable toggledDrawable, releasedDrawable;
    private boolean toggled;

    public ToggleButton(Drawable released, Drawable toggled) {
        this.toggledDrawable = toggled;
        this.releasedDrawable = released;

        image = new Image();
        image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                ToggleButton.this.clicked();
            }
        });

        setToggled(false);
    }

    public void addListener(EventListener eventListener) {
        image.addListener(eventListener);
    }

    public void clicked() {
        toggled = !toggled;

        Drawable drawable = toggled ? toggledDrawable : releasedDrawable;

        image.setDrawable(drawable);
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;

        Drawable drawable = toggled ? toggledDrawable : releasedDrawable;

        image.setDrawable(drawable);
    }

    public void setVisible(boolean visible) {
        if(!visible) image.setTouchable(Touchable.disabled);
        image.setVisible(visible);
    }

    public void setY(float y) {
        image.setY(y);
    }

    public void setX(float x) {
        image.setX(x);
    }

    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    public void setTouchable(Touchable touchable) {
        image.setTouchable(touchable);
    }

    public boolean isToggled() {
        return toggled;
    }

    public Image getImage() {
        return image;
    }
}
