package com.jumping.game.game.elements;

import com.jumping.game.assets.AssetsManager;
import com.jumping.game.game.math.MathController;
import com.jumping.game.game.physics.EntityType;
import com.jumping.game.game.physics.PhysicsEngineImpl;
import com.jumping.game.game.physics.PhysicsEntity;
import com.jumping.game.util.Values;
import com.jumping.game.util.ZPositions;

public class MathAttachment extends TileAttachment {
    private final MathController controller;

    public MathAttachment(AssetsManager manager, MathController controller) {
        super(manager.getSprite(Values.MATH_SPRITE), ZPositions.Z_MATH_ATTACHMENT);
        sprite.setSize(Values.MATH_ATTACHMENT_WIDTH, Values.MATH_ATTACHMENT_HEIGHT);

        this.controller = controller;
    }

    @Override
    public void updatePhysics(float dt, float gravityY) {

    }

    @Override
    public void onCollision(PhysicsEntity other) {
        if(other.getY() > attachedTile.getTop() && PhysicsEngineImpl.isColliding(other, attachedTile))
            controller.showMathExercise();
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

    @Override
    public boolean canCollide() {
        return true;
    }

    @Override
    public EntityType getType() {
        return EntityType.MATH_EXERCISE;
    }
}
