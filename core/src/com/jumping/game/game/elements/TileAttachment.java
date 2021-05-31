package com.jumping.game.game.elements;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jumping.game.game.physics.EntityType;
import com.jumping.game.game.physics.PhysicsEntity;
import com.jumping.game.util.ZSprite;

public abstract class TileAttachment implements PhysicsEntity {
    protected ZSprite sprite;
    protected EntityType type;
    protected Tile attachedTile;

    public TileAttachment(Sprite sprite, int z) {
        this.sprite = new ZSprite(z, sprite);
        this.type = EntityType.TILE;
    }

    public void remove() {
        sprite.remove();
    }

    public boolean isRemove() {
        return sprite.isRemove();
    }

    public void updateAttachment(float tileX, float tileWidth, float tileTop) {
        this.sprite.setPosition(tileX + tileWidth/2, tileTop);
    }

    public void attachToEntity(Tile t) {
        this.attachedTile = t;
    }

    public Tile getAttachedTile() {
        return attachedTile;
    }

    public ZSprite getSprite() {
        return sprite;
    }
}
