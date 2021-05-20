package com.jumping.game.game.physics;

import com.jumping.game.util.Values;

import java.util.Collection;

public interface PhysicsEngine {
    void addEntity(PhysicsEntity entity);
    void addEntities(Collection<PhysicsEntity> entityCollection);

    void removeEntity(PhysicsEntity entity);
    void removeEntities(Collection<PhysicsEntity> entityCollection);

    void handlePlayerCollisions(PhysicsEntity player);

    default float getGravityY() {
        return Values.GRAVITY;
    }
}
