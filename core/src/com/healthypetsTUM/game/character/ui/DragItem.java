package com.healthypetsTUM.game.character.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.character.ui.listener.DragListener;
import com.healthypetsTUM.game.util.Values;

public abstract class DragItem implements DragListener {
    protected Vector2 cacheVec2 = new Vector2();
    protected Rectangle cacheRect = new Rectangle();

    protected boolean moving, isPresent;
    protected Image dragItemImg;

    private Table progressbarTable;
    protected ProgressBar bar;

    protected Drawable progressbarGreen, progressbarEmpty;

    public DragItem(AssetsManager assetsManager, String name) {
        dragItemImg = new Image(assetsManager.getDrawable(name));
        dragItemImg.pack();
        dragItemImg.setOrigin(Align.center);

        assetsManager.addInputProcessor(this);
        hideInstantly();

        progressbarGreen = assetsManager.get9Drawable(Values.PROGRESSBAR_FRONT_GREEN);
        progressbarEmpty = assetsManager.getDrawable(Values.PROGRESSBAR_FRONT_EMPTY);

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = assetsManager.get9Drawable(Values.PROGRESSBAR_BACK);
        progressBarStyle.background.setMinHeight(50);
        progressBarStyle.background.setMinWidth(0);
        progressBarStyle.knobBefore = progressbarEmpty;

        bar = new ProgressBar(0, 100, 1, false, progressBarStyle);

        progressbarTable = new Table();
        progressbarTable.setFillParent(true);
        progressbarTable.add(bar).growX().pad(Values.PADDING*2).bottom().padBottom(Values.PADDING*6);
        progressbarTable.bottom();
        progressbarTable.setTouchable(Touchable.disabled);
        progressbarTable.setVisible(false);
    }

    public void show() {
        dragItemImg.getColor().a = 0;
        dragItemImg.setVisible(true);
        dragItemImg.addAction(Actions.fadeIn(.2f));
        isPresent = true;

        progressbarTable.setVisible(true);
    }

    public void hide() {
        dragItemImg.addAction(Actions.sequence(Actions.fadeOut(.3f), Actions.visible(false)));
        isPresent = false;
    }

    public void hideInstantly() {
        dragItemImg.setVisible(false);
        isPresent = false;
    }

    public void position() {
        dragItemImg.setPosition(dragItemImg.getStage().getWidth()/2,
                dragItemImg.getStage().getHeight()/4, Align.center);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(!isPresent) return false;

        dragItemImg.getStage().screenToStageCoordinates(cacheVec2.set(screenX, screenY));

        if(cacheRect.set(dragItemImg.getX(), dragItemImg.getY(), dragItemImg.getWidth(), dragItemImg.getHeight())
                .contains(cacheVec2)) {
            moving = true;
            moveTo(cacheVec2.x, cacheVec2.y);
        }

        touchedDown();

        return true;
    }

    protected void touchedDown() {}
    protected void touchMoved() {}
    protected void touchedUp() {}

    public Rectangle getBounds() {
        return cacheRect.set(dragItemImg.getX(), dragItemImg.getY(), dragItemImg.getWidth(), dragItemImg.getHeight());
    }

    private void moveTo(float x, float y) {
        float boundedX = Math.min(dragItemImg.getStage().getWidth()-dragItemImg.getWidth()/2,
                Math.max(x, dragItemImg.getWidth()/2));
        float boundedY = Math.min(dragItemImg.getStage().getHeight()-dragItemImg.getHeight()/2,
                Math.max(y, dragItemImg.getHeight()/2));
        dragItemImg.getActions().clear();
        dragItemImg.addAction(Actions.moveToAligned(boundedX, boundedY, Align.center, 0.01f));
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(!isPresent) return false;

        moving = false;
        touchedUp();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(!isPresent) return false;

        if(moving) {
            dragItemImg.getStage().screenToStageCoordinates(cacheVec2.set(screenX, screenY));
            moveTo(cacheVec2.x, cacheVec2.y);

            touchMoved();
            return true;
        }
        return false;
    }

    public Image getDragItemImg() {
        return dragItemImg;
    }

    /**
     * Returns true if the item is use
     */
    public boolean isPresent() {
        return isPresent;
    }

    public void addAll(Table t) {
        t.addActor(dragItemImg);
        t.addActor(progressbarTable);
    }
}
