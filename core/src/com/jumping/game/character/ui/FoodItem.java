package com.jumping.game.character.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.jumping.game.assets.AssetsManager;
import com.jumping.game.character.Duration;
import com.jumping.game.character.ui.listener.FoodItemListener;
import com.jumping.game.util.MathUtils;
import com.jumping.game.util.Values;

import java.util.ArrayList;
import java.util.List;

public class FoodItem extends DragItem {
    private List<Image> crumbsList;
    private int currentIndex;
    private long lastFoodEffect;

    private Duration duration;

    private FoodItemListener listener;

    public FoodItem(AssetsManager assetsManager, String foodName, FoodItemListener listener) {
        super(assetsManager, foodName);

        this.listener = listener;

        crumbsList = new ArrayList<>();
        for(int i = 0; i < 20; ++i) {
            Image crumb = new Image(assetsManager.getDrawable(Values.CRUMBS));
            crumb.pack();
            crumb.setOrigin(Align.center);
            crumb.setVisible(false);
            crumb.getColor().a = 0;
            crumbsList.add(crumb);
        }

        moving = false;

        duration = new Duration(Values.FOOD_DURATION);
    }

    @Override
    protected void touchedDown() {
        duration.start();
    }

    @Override
    protected void touchMoved() {
        if(duration.addTimeStampDiff()) {
            listener.foodEaten();
            hide();
            moving = false;
        }
    }

    public void showHearts(Rectangle headBounds) {
        if(lastFoodEffect + 100000 < System.currentTimeMillis()) { // effect long gone
            lastFoodEffect = System.currentTimeMillis() + Values.CRUMB_COOLDOWN;
            return;
        }

        if(lastFoodEffect + Values.CRUMB_COOLDOWN < System.currentTimeMillis()) {
            Image img = crumbsList.get(currentIndex);
            img.setPosition(headBounds.x + headBounds.width/2, headBounds.y + headBounds.height/2, Align.center);
            img.addAction(Actions.sequence(Actions.visible(true), Actions.parallel(Actions.sequence(Actions.delay(1), Actions.fadeOut(1)), Actions.moveBy(MathUtils.getRandomX(-300, 300), -800)), Actions.visible(false)));

            currentIndex = (currentIndex+1)%crumbsList.size();
            lastFoodEffect = System.currentTimeMillis();
        }
    }

    @Override
    public void addAll(Table t) {
        for(Image i : crumbsList) t.addActor(i);
        super.addAll(t);
    }
}
