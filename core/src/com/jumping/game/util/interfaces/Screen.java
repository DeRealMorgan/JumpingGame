package com.jumping.game.util.interfaces;

public interface Screen extends com.badlogic.gdx.Screen {
    @Override // is called if this screen is being set as the main screen
    void show();

    @Override
    @Deprecated // use render pipeline instead
    default void render(float delta) {}

    void update(float dt);

    @Override
    @Deprecated // do not use, resize is handled in render pipeline
    default void resize(int width, int height) {}

    @Override
    void pause();

    @Override
    void resume();

    @Override // is called if another screen is being set - old screen still exists
    void hide();

    @Override
    void dispose();
}
