package com.healthypetsTUM.game.util;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ZSprite {
    private int z;
    private final Sprite sprite;
    private boolean remove;

    public ZSprite(int z, Sprite s) {
        this.z = z;
        this.sprite = s;

        setOrigin();
    }

    public void init(float x, float y, float width, float height) {
        sprite.setSize(width, height);
        setOrigin();
        setPosition(x, y);
    }

    public void setY(float y) {
        sprite.setY(y);
    }

    public void setX(float x) {
        sprite.setX(x);
    }

    public void addToPosition(float x, float y) {
        float posX = sprite.getX();
        float posY = sprite.getY();

        sprite.setPosition(posX + x, posY + y); // todo eventuell this.setposition
    }

    public void setPosition(float x, float y) {
        sprite.setOriginBasedPosition(x, y);
    }

    public void setSize(float width, float height) {
        sprite.setSize(width, height);
        setOrigin();
    }

    private void setOrigin() {
        sprite.setOrigin(getX() + getWidth()/2, getY());
    }

    public void remove() {
        remove = true;
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public int getZ() {
        return z;
    }

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getTop() {
        return getY()+getHeight();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public boolean isRemove() {
        return remove;
    }
}
