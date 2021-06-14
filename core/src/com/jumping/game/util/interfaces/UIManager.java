package com.jumping.game.util.interfaces;

public interface UIManager {
    void update(float dt);
    void drawUIBottom();
    void drawUITop();
    void resize(int w, int h);
    void dispose();
}
