package com.jumping.game.game.physics;

public interface PhysicsEntity {
    /**
     * Gravity is premultiplied by dt
     */
    void updatePhysics(float dt, float gravityY);

    void onCollision(PhysicsEntity other);

    float getX();
    float getY();

    float getWidth();
    float getHeight();

    default float getTop() {
        return getY() + getHeight();
    }

    default float getRight() {
        return getX() + getWidth();
    }

    boolean canCollide();

    /**
     * Collision type used
     */
    EntityType getType();
}
