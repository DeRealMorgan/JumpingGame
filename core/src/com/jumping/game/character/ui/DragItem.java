package com.jumping.game.character.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.jumping.game.assets.AssetsManager;

public abstract class DragItem implements DragListener {
    protected Vector2 cacheVec2 = new Vector2();
    protected Rectangle cacheRect = new Rectangle();

    protected boolean moving, isPresent;
    protected Image dragItemImg;

    public DragItem(AssetsManager assetsManager, String name) {
        dragItemImg = new Image(assetsManager.getDrawable(name));
        dragItemImg.pack();
        dragItemImg.setOrigin(Align.center);

        assetsManager.addInputProcessor(this);
        hide();
    }

    public void show() {
        dragItemImg.setVisible(true);
        isPresent = true;
    }

    public void hide() {
        dragItemImg.setVisible(false);
        isPresent = false;
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
        dragItemImg.getActions().clear();
        dragItemImg.addAction(Actions.moveToAligned(x, y, Align.center, 0.01f));
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
    }
}
