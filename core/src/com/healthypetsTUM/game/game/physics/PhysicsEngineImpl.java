package com.healthypetsTUM.game.game.physics;

import com.badlogic.gdx.math.Rectangle;
import com.healthypetsTUM.game.util.Values;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PhysicsEngineImpl implements PhysicsEngine {
    private static Rectangle r1 = new Rectangle(), r2 = new Rectangle();
    private final List<PhysicsEntity> entityList;

    private boolean run = true;

    public PhysicsEngineImpl() {
        this.entityList = new ArrayList<>();
    }

    public void update(float dt) {
        if(!run) return;

        float gravity = Values.GRAVITY * dt;
        for(PhysicsEntity entity : entityList)
            entity.updatePhysics(dt, gravity);
    }

    /**
     * Handles all collisions with the entity. On collision the calling entity .onCollision() will be called first
     */
    @Override
    public void handlePlayerCollisions(PhysicsEntity player) {
        Rectangle r1 = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());

        Rectangle r2 = new Rectangle(0, 0, 0, 0);

        entityList.removeIf(PhysicsEntity::isRemove);

        for(PhysicsEntity e : entityList) {
            if(e == player) continue;

            if(e.canCollide() && r2.set(e.getX(), e.getY(), e.getWidth(), e.getHeight()).overlaps(r1))
                player.onCollision(e);
        }
    }

    public static boolean isColliding(PhysicsEntity e, PhysicsEntity e1) {
        r1.set(e.getX(), e.getY(), e.getWidth(), e.getHeight());
        return e.canCollide() && e1.canCollide() && r2.set(e1.getX(), e1.getY(), e1.getWidth(), e1.getHeight()).overlaps(r1);
    }

    @Override
    public void stop() {
        run = false;
    }

    @Override
    public void addEntity(PhysicsEntity entity) {
        entityList.add(entity);
    }

    @Override
    public void addEntities(Collection<PhysicsEntity> entityCollection) {
        entityList.addAll(entityCollection);
    }

    @Override
    public void removeEntity(PhysicsEntity entity) {
        entityList.remove(entity);
    }

    @Override
    public void removeEntities(Collection<PhysicsEntity> entityCollection) {
        entityList.removeAll(entityCollection);
    }
}
