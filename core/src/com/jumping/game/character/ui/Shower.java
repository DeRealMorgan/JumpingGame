package com.jumping.game.character.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.jumping.game.assets.AssetsManager;
import com.jumping.game.character.Duration;
import com.jumping.game.util.MathUtils;
import com.jumping.game.util.Values;

import java.util.ArrayList;
import java.util.List;

public class Shower extends DragItem {
    private Drawable soapDrawable, showerDrawable;
    private List<Image> waterdropList, soapBubbleList;
    private int currentWaterIndex, currentBubbleIndex;
    private long lastWaterEffect, lastBubbleEffect;

    private Duration duration;

    private boolean useShower;

    public Shower(AssetsManager assetsManager) {
        super(assetsManager, Values.SOAP);

        soapDrawable = assetsManager.getDrawable(Values.SOAP);
        showerDrawable = assetsManager.getDrawable(Values.SHOWER);

        waterdropList = new ArrayList<>();
        for(int i = 0; i < 20; ++i) {
            Image waterdrop = new Image(assetsManager.getDrawable(Values.WATER_DROPS));
            waterdrop.pack();
            waterdrop.setOrigin(Align.center);
            waterdrop.setVisible(false);
            waterdrop.getColor().a = 0;
            waterdropList.add(waterdrop);
        }

        soapBubbleList = new ArrayList<>();
        for(int i = 0; i < 30; ++i) {
            Image soapBubble = new Image(assetsManager.getDrawable(Values.SOAP_BUBBLE));
            soapBubble.pack();
            soapBubble.setOrigin(Align.center);
            soapBubble.setVisible(false);
            soapBubble.getColor().a = 0;
            soapBubbleList.add(soapBubble);
        }

        duration = new Duration(Values.SOAP_DURATION);
    }

    public void showEffect(Rectangle bodyBounds) {
        if(!moving) return;
        if(useShower) {
            if(lastWaterEffect + 1000000 < System.currentTimeMillis()) { // effect long gone
                lastWaterEffect = System.currentTimeMillis() + Values.WATERDROP_COOLDOWN;
                return;
            }

            if(lastWaterEffect + Values.WATERDROP_COOLDOWN < System.currentTimeMillis()) {
                Image img = waterdropList.get(currentWaterIndex);
                int inset = (int)(bodyBounds.getWidth()/5);
                img.setPosition(bodyBounds.x + MathUtils.getRandomX(inset, (int)bodyBounds.width-inset), bodyBounds.y + MathUtils.getRandomX(inset, (int)bodyBounds.height-inset), Align.center);
                img.setScale(.7f);
                img.addAction(Actions.sequence(Actions.visible(true), Actions.parallel(Actions.fadeIn(1), Actions.scaleTo(1, 1, 2))));

                currentWaterIndex = (currentWaterIndex+1)%waterdropList.size();
                lastWaterEffect = System.currentTimeMillis();
            }
        } else {
            if(lastBubbleEffect + 100000 < System.currentTimeMillis()) { // effect long gone
                lastBubbleEffect = System.currentTimeMillis() + Values.SOAP_COOLDOWN;
                return;
            }

            if(lastBubbleEffect + Values.SOAP_COOLDOWN < System.currentTimeMillis()) {
                Image img = soapBubbleList.get(currentBubbleIndex);
                int inset = (int)(bodyBounds.getWidth()/5);
                img.setPosition(bodyBounds.x + MathUtils.getRandomX(inset, (int)bodyBounds.width-inset), bodyBounds.y + MathUtils.getRandomX(inset, (int)bodyBounds.height-inset), Align.center);
                img.setScale(.7f);
                img.addAction(Actions.sequence(Actions.visible(true), Actions.parallel(Actions.fadeIn(1), Actions.scaleTo(1, 1, 2))));

                currentBubbleIndex = (currentBubbleIndex+1)%soapBubbleList.size();
                lastBubbleEffect = System.currentTimeMillis();
            }
        }
    }

    @Override
    protected void touchedDown() {
        duration.start();
    }

    @Override
    protected void touchMoved() {
        if(duration.addTimeStampDiff()) {
            duration.reset(Values.SHOWER_DURATION);
            useShower();
            moving = false;
        }
    }

    @Override
    protected void touchedUp() {}

    public void useShower() {
        this.useShower = true;

        dragItemImg.setDrawable(showerDrawable);
    }

    public void useSoap() {
        this.useShower = false;

        dragItemImg.setDrawable(soapDrawable);
    }

    @Override
    public void addAll(Table t) {
        for(Image i : soapBubbleList) t.addActor(i);
        for(Image i : waterdropList) t.addActor(i);
        super.addAll(t);
    }
}
