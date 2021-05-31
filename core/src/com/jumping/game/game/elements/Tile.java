package com.jumping.game.game.elements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jumping.game.game.physics.EntityType;
import com.jumping.game.game.physics.PhysicsEntity;
import com.jumping.game.util.ZSprite;

public abstract class Tile implements PhysicsEntity {
    protected ZSprite sprite;
    protected EntityType type;
    protected float minY, maxY; // y position the tile can be in the world (e.g. moving up/down), use top of tile
    protected TileAttachment attachment;
    protected boolean isRemove;

    public Tile(Sprite sprite, int z, float minY, float maxY) {
        this.sprite = new ZSprite(z, sprite);
        this.type = EntityType.TILE;

        this.minY = minY;
        this.maxY = maxY;
    }

    public void setAttachment(TileAttachment attachment) {
        this.attachment = attachment;
        attachmentSet();
    }

    protected abstract void attachmentSet();

    public TileAttachment getAttachment() {
        return attachment;
    }

    public boolean hasAttachment() {
        return attachment != null;
    }

    @Override
    public boolean isRemove() {
        return isRemove;
    }

    public ZSprite getSprite() {
        return sprite;
    }

    public float getMinYPos() {
        return minY;
    }

    public float getMaxYPos() {
        return maxY;
    }

    public boolean isEntity(PhysicsEntity entity) {
        return this == entity;
    }

    @Override
    public float getX() {
        return sprite.getX();
    }

    @Override
    public float getY() {
        return sprite.getY();
    }

    @Override
    public float getWidth() {
        return sprite.getWidth();
    }

    @Override
    public float getHeight() {
        return sprite.getHeight();
    }

}
