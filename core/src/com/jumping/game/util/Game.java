package com.jumping.game.util;

import com.badlogic.gdx.ApplicationListener;
import com.jumping.game.util.interfaces.Screen;

public abstract class Game implements ApplicationListener {
    private Screen screen;

    public Game() {
        this.screen = new EmptyScreen();
    }

    @Override
    public abstract void dispose();

    protected void disposeScreen() {
        screen.dispose();
    }

    @Override
    public abstract void pause();

    protected void pauseScreen() {
        screen.pause();
    }

    @Override
    public abstract void resume();

    protected void resumeScreen() {
        screen.resume();
    }

    @Override
    public abstract void render();

    protected void update(float dt) {
        screen.update(dt);
    }

    @Override
    public void resize(int width, int height) {
        screen.resize(width, height);
    }

    protected void setScreen (Screen screen) {
        if (this.screen != null) this.screen.hide();

        this.screen = screen;
        this.screen.show();
    }
}

