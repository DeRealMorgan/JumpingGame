package com.jumping.game.character.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.jumping.game.assets.AssetsManager;
import com.jumping.game.util.MathUtils;
import com.jumping.game.util.Values;

import java.util.ArrayList;
import java.util.List;

public class Hand extends DragItem {
    private List<Image> heartsList;
    private int currentIndex;
    private long lastHeartEffect;

    public Hand(AssetsManager assetsManager) {
        super(assetsManager, Values.HAND);

        heartsList = new ArrayList<>();
        for(int i = 0; i < 10; ++i) {
            Image hearts = new Image(assetsManager.getDrawable(Values.HEARTS));
            hearts.pack();
            hearts.setOrigin(Align.center);
            hearts.setVisible(false);
            hearts.getColor().a = 0;
            heartsList.add(hearts);
        }

        moving = false;
    }

    public void showHearts(Rectangle headBounds) {
        if(lastHeartEffect + 100000 < System.currentTimeMillis()) { // effect long gone
            lastHeartEffect = System.currentTimeMillis() + Values.HEART_COOLDOWN;
            return;
        }

        if(lastHeartEffect + Values.HEART_COOLDOWN < System.currentTimeMillis()) {
            Image img = heartsList.get(currentIndex);
            img.setPosition(headBounds.x + MathUtils.getRandomX(0, (int)headBounds.width), headBounds.y + MathUtils.getRandomX(0, (int)headBounds.height), Align.center);
            img.setScale(.7f);
            img.addAction(Actions.sequence(Actions.visible(true), Actions.parallel(Actions.fadeIn(1), Actions.scaleTo(1, 1, 2)), Actions.fadeOut(2), Actions.visible(false)));

            currentIndex = (currentIndex+1)%heartsList.size();
            lastHeartEffect = System.currentTimeMillis();
        }
    }

    @Override
    public void addAll(Table t) {
        for(Image i : heartsList) t.addActor(i);
        super.addAll(t);
    }
}
