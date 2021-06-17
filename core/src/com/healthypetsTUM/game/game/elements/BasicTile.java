package com.healthypetsTUM.game.game.elements;

import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.game.physics.EntityType;
import com.healthypetsTUM.game.game.physics.PhysicsEntity;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.ZPositions;

public class BasicTile extends Tile {
    public static final int TILE_WIDTH = Values.TILE_WIDTH_NORMAL;
    public static final int TILE_HEIGHT = Values.TILE_HEIGHT_NORMAL;

    public BasicTile(AssetsManager manager, float x, float y) {
        super(manager.getSprite(Values.TILE_SPRITE), ZPositions.Z_TILE, y+TILE_HEIGHT, y+TILE_HEIGHT);
        sprite.init(x, y, TILE_WIDTH, TILE_HEIGHT);
    }

    @Override
    protected void attachmentSet() {
        attachment.attachToEntity(this);
        attachment.updateAttachment(sprite.getX(), sprite.getWidth(), sprite.getTop());
    }

    @Override
    public void updatePhysics(float dt, float gravityY) {
        // does nothing
    }

    @Override
    public boolean onCollision(PhysicsEntity other) {
        return true;
        // does nothing
    }

    @Override
    public boolean canCollide() {
        return true;
    }

    @Override
    public EntityType getType() {
        return EntityType.TILE;
    }
}
