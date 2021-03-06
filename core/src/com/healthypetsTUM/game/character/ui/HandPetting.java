package com.healthypetsTUM.game.character.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.character.Duration;
import com.healthypetsTUM.game.character.ui.listener.HandPettingListener;
import com.healthypetsTUM.game.util.MathUtils;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;

import java.util.ArrayList;
import java.util.List;

public class HandPetting extends DragItem {
    private final List<Image> heartsList;
    private int currentIndex;
    private long lastHeartEffect;

    private Duration duration;

    private HandPettingListener listener;

    private int soundID = -1;

    public HandPetting(AssetsManager assetsManager, HandPettingListener listener) {
        super(assetsManager, Values.HAND);

        this.listener = listener;

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

        duration = new Duration(Values.PET_DURATION);
    }

    @Override
    protected void touchedDown() {
        duration.start();

        if(soundID == -1) soundID = Sounds.pet();
    }

    @Override
    protected void touchMoved() {
        if(duration.addTimeStampDiff()) {
            listener.pettingDone();

            Sounds.stop(soundID);
            soundID = -1;

            hide();
            moving = false;
        } else {
            updateProgressbar(duration.getPercentDone());
        }
    }

    public void showHearts(Rectangle headBounds) {
        if(lastHeartEffect + Values.HEART_COOLDOWN < System.currentTimeMillis()) {
            Image img = heartsList.get(currentIndex);
            img.setPosition(headBounds.x + MathUtils.getRandomX(0, (int)headBounds.width), headBounds.y + MathUtils.getRandomX(0, (int)headBounds.height) - 200, Align.center);
            img.setScale(.7f);
            img.addAction(Actions.sequence(Actions.visible(true), Actions.parallel(Actions.fadeIn(1), Actions.scaleTo(1, 1, 2)), Actions.fadeOut(2), Actions.visible(false)));

            currentIndex = (currentIndex+1)%heartsList.size();
            lastHeartEffect = System.currentTimeMillis();
        }
    }

    public void reset() {
        currentIndex = 0;
        duration.reset();

        Sounds.stop(soundID);
        soundID = -1;
    }

    @Override
    public void addAll(Table t) {
        for(Image i : heartsList) t.addActor(i);
        super.addAll(t);
    }
}
