package com.jumping.game.util.interfaces;

public interface RenderPipeline {
    void update(float dt);
    void resize(int width, int height);
    void dispose();
    void render(float dt);
}
