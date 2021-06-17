package com.healthypetsTUM.game.util.ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.healthypetsTUM.game.util.Values;

public final class OverlayUtils {
    public static void show(Table table) {
        table.getColor().a = 0f;

        AlphaAction fadeInAction = new AlphaAction();
        fadeInAction.setAlpha(1f);
        fadeInAction.setDuration(Values.FADE_OVERLAY_DUR);

        table.setTouchable(Touchable.enabled);
        table.addAction(fadeInAction);
        table.setVisible(true);
    }

    public static void close(Table table) {
        table.getColor().a = 1f;

        AlphaAction fadeOutAction = new AlphaAction();
        fadeOutAction.setAlpha(0f);
        fadeOutAction.setDuration(Values.FADE_OVERLAY_DUR);

        table.addAction(fadeOutAction);
        table.setTouchable(Touchable.disabled);
    }

    public static void showInstantly(Table table) {
        table.getColor().a = 1f;
        table.setTouchable(Touchable.enabled);
        table.setVisible(true);
    }

    public static void closeInstantly(Table table) {
        table.getColor().a = 0f;
        table.setTouchable(Touchable.disabled);
    }

}