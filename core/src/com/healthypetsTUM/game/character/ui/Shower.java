package com.healthypetsTUM.game.character.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.character.ui.listener.ShowerListener;
import com.healthypetsTUM.game.util.MathUtils;
import com.healthypetsTUM.game.util.Values;

import java.util.ArrayList;
import java.util.List;

public class Shower extends DragItem {
    private Drawable soapDrawable, showerDrawable;
    private List<Image> waterdropList, soapBubbleList;
    private int currentWaterIndex, currentBubbleIndex;
    private long lastWaterEffect, lastBubbleEffect;

    private boolean useShower, done;

    private ShowerListener listener;

    public Shower(AssetsManager assetsManager, ShowerListener listener) {
        super(assetsManager, Values.SOAP);

        this.listener = listener;

        soapDrawable = assetsManager.getDrawable(Values.SOAP);
        showerDrawable = assetsManager.getDrawable(Values.SHOWER);

        waterdropList = new ArrayList<>();
        soapBubbleList = new ArrayList<>();
        for(int i = 0; i < 20; ++i) {
            Image waterdrop = new Image(assetsManager.getDrawable(Values.WATER_DROPS));
            waterdrop.pack();
            waterdrop.setOrigin(Align.center);
            waterdrop.setVisible(false);
            waterdrop.getColor().a = 0;
            waterdropList.add(waterdrop);

            Image soapBubble = new Image(assetsManager.getDrawable(Values.SOAP_BUBBLE));
            soapBubble.pack();
            soapBubble.setOrigin(Align.center);
            soapBubble.setVisible(false);
            soapBubble.getColor().a = 0;
            soapBubbleList.add(soapBubble);
        }
    }

    public void showEffect(Rectangle bodyBounds) {
        if(!moving || done) return;
        if(useShower) {
            if(lastWaterEffect + Values.SOAPWATER_COOLDOWN < System.currentTimeMillis()) {
                Image img = waterdropList.get(currentWaterIndex);
                int inset = (int)(bodyBounds.getWidth()/5);
                img.setPosition(bodyBounds.x + MathUtils.getRandomX(inset, (int)bodyBounds.width-inset),
                        bodyBounds.y + MathUtils.getRandomX(inset, (int)bodyBounds.height-inset) - 200, Align.center);
                img.setScale(.7f);
                img.addAction(Actions.sequence(Actions.visible(true), Actions.parallel(Actions.fadeIn(1),
                       Actions.scaleTo(1, 1, 2)), Actions.delay(2), Actions.fadeOut(2)));

                currentBubbleIndex--;
                soapBubbleList.get(currentBubbleIndex).addAction(Actions.fadeOut(2));

                currentWaterIndex++;
                if(currentWaterIndex == waterdropList.size()) {
                    moving = false;
                    done = true;
                    hide();
                    listener.showerDone();
                }
                lastWaterEffect = System.currentTimeMillis();
            }
        } else {
            if(lastBubbleEffect + 100000 < System.currentTimeMillis()) { // effect long gone
                lastBubbleEffect = System.currentTimeMillis() + Values.SOAPWATER_COOLDOWN;
                return;
            }

            if(lastBubbleEffect + Values.SOAPWATER_COOLDOWN < System.currentTimeMillis()) {
                Image img = soapBubbleList.get(currentBubbleIndex);
                int inset = (int)(bodyBounds.getWidth()/5);
                img.setPosition(bodyBounds.x + MathUtils.getRandomX(inset, (int)bodyBounds.width-inset),
                        bodyBounds.y + MathUtils.getRandomX(inset, (int)bodyBounds.height-inset) - 200, Align.center);
                img.setScale(.7f);
                img.addAction(Actions.sequence(Actions.visible(true), Actions.parallel(Actions.fadeIn(1),
                        Actions.scaleTo(1, 1, 2))));

                currentBubbleIndex++;
                lastBubbleEffect = System.currentTimeMillis();
                if(currentBubbleIndex == soapBubbleList.size()) {
                    moving = false;
                    useShower();
                }
            }
        }
    }

    public void useShower() {
        this.useShower = true;

        dragItemImg.setDrawable(showerDrawable);
        position();
    }

    public void useSoap() {
        this.useShower = false;

        dragItemImg.setDrawable(soapDrawable);
        position();
    }

    public void reset() {
        useSoap();

        done = false;
        currentBubbleIndex = 0;
        currentWaterIndex = 0;
    }

    @Override
    public void addAll(Table t) {
        for(Image i : soapBubbleList) t.addActor(i);
        for(Image i : waterdropList) t.addActor(i);
        super.addAll(t);
    }
}
