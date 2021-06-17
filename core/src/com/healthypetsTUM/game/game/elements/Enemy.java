package com.healthypetsTUM.game.game.elements;

import com.healthypetsTUM.game.game.physics.EntityType;
import com.healthypetsTUM.game.game.physics.PhysicsEntity;
import com.healthypetsTUM.game.util.ZSprite;

public abstract class Enemy implements PhysicsEntity {
    protected ZSprite sprite;
    protected EntityType type;

    public Enemy(ZSprite sprite, EntityType type) {
        this.sprite = sprite;
        this.type = type;
    }

}
