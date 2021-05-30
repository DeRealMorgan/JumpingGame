package com.jumping.game.util;

public interface UIManager {
    void update(float dt);
    void drawUI();
    void resize(int w, int h);
    void dispose();
}
