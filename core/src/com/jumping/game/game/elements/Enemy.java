package com.jumping.game.game.elements;

import com.jumping.game.game.physics.EntityType;
import com.jumping.game.game.physics.PhysicsEntity;
import com.jumping.game.util.ZSprite;

public abstract class Enemy implements PhysicsEntity {
    protected ZSprite sprite;
    protected EntityType type;

    public Enemy(ZSprite sprite, EntityType type) {
        this.sprite = sprite;
        this.type = type;
    }

}
