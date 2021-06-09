package com.jumping.game.util.interfaces;

import com.badlogic.gdx.scenes.scene2d.Stage;

public interface UIManager {
    void update(float dt);
    void drawUI();
    void resize(int w, int h);
    void dispose();
}
