package com.jumping.game.game.player;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.jumping.game.game.assets.AssetsManager;
import com.jumping.game.game.physics.EntityType;
import com.jumping.game.game.physics.PhysicsEntity;
import com.jumping.game.game.ui.GameUIController;
import com.jumping.game.util.MathUtils;
import com.jumping.game.util.Values;
import com.jumping.game.util.ZPositions;
import com.jumping.game.util.ZSprite;

public class Player implements PhysicsEntity {
    private GameUIController gameUIController;

    private ZSprite sprite;
    private final Vector2 velocityVec;
    private PlayerListener playerListener; // todo maybe change to list

    public Player(AssetsManager manager, float x, float y, GameUIController gameUIController) {
        this.gameUIController = gameUIController;

        Sprite sprite = manager.getSprite(Values.PLAYER_SPRITE);
        sprite.setSize(Values.PLAYER_WIDTH, Values.PLAYER_HEIGHT);
        this.sprite = new ZSprite(ZPositions.Z_PLAYER, sprite);
        this.sprite.init(x, y, Values.PLAYER_WIDTH, Values.PLAYER_HEIGHT);

        this.velocityVec = new Vector2();

        this.playerListener = new PlayerListenerStub();
    }

    @Override
    public void updatePhysics(float dt, float gravityY) {
        velocityVec.y += gravityY;
        sprite.addToPosition(velocityVec.x * dt, velocityVec.y * dt);

        float playerWidth2 = sprite.getWidth()/2;
        float midX = sprite.getX() + playerWidth2;
        if(midX > Values.WORLD_WIDTH)
            sprite.setX(-playerWidth2);
        else if(midX < 0)
            sprite.setX(Values.WORLD_WIDTH-playerWidth2);

    }

    public void updateVelocityX(float velocityX) {
        if(Math.abs(velocityX) < Values.VELOCITY_TRESHOLD_X) {
            this.velocityVec.x = 0;
            return;
        }

        this.velocityVec.x = -(velocityX * Values.ACCELATE_X);
    }

    @Override
    public void onCollision(PhysicsEntity other) {
        EntityType otherType = other.getType();

        switch (otherType) {
            case PLAYER:
                break;
            case TILE:
                if(velocityVec.y > 0)
                    return;
                handleTileCollision(other);
                break;
            case MATH_EXERCISE:
                other.onCollision(this);
                break;
                // todo add other tiles (like breakable_tile) ...
            case NONE:
                break;
        }
    }

    private void handleTileCollision(PhysicsEntity tile) {
        float tileTop = tile.getTop();
        if(MathUtils.isInDelta(getY(), tileTop, Values.PLAYER_COLLISION_DELTA)) {
            sprite.setY(tileTop);
            jump();

            tile.onCollision(this); // todo
            playerListener.jumpedToNextPlatform(tile);
        }

    }

    public void start() {
        jump();
    }

    private void jump() {
        velocityVec.y = Values.PLAYER_JUMP_VELOCITY_Y;
    }

    // todo add tolerance to also collide with tiles if very close
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

    @Override
    public boolean canCollide() {
        return true;
    }

    @Override
    public EntityType getType() {
        return EntityType.NONE;
    }

    public ZSprite getSprite() {
        return sprite;
    }

    public void listenToPlayer(PlayerListener listener) {
        this.playerListener = listener;
    }
}
