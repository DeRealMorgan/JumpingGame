package com.healthypetsTUM.game.game.physics;

public interface PhysicsEntity {
    /**
     * Gravity is premultiplied by dt
     */
    void updatePhysics(float dt, float gravityY);

    boolean onCollision(PhysicsEntity other);

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

    boolean isRemove();

    /**
     * Collision type used
     */
    EntityType getType();
}
