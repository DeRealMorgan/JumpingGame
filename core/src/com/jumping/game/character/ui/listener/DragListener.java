package com.jumping.game.character.ui.listener;

import com.badlogic.gdx.InputProcessor;

public interface DragListener extends InputProcessor {
    @Override
    default boolean keyDown(int keycode) {
        return false;
    }

    @Override
    default boolean keyUp(int keycode) {
        return false;
    }

    @Override
    default boolean keyTyped(char character) {
        return false;
    }

    @Override
    boolean touchDown(int screenX, int screenY, int pointer, int button);

    @Override
    boolean touchUp(int screenX, int screenY, int pointer, int button);

    @Override
    boolean touchDragged(int screenX, int screenY, int pointer);

    @Override
    default boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    default boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
