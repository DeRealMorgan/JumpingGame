package com.healthypetsTUM.game.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.util.ScreenName;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.interfaces.ScreenManager;

public class GameOverUI {
    private ScreenManager screenManager;
    private Table contentTable, screenTable;
    private Label gameOverLabel, scoreLabel, mathLabel;
    private TextButton replayBtn, backBtn;

    public GameOverUI(AssetsManager assetsManager, ScreenManager screenManager) {
        buildUI(assetsManager);
        this.screenManager = screenManager;
    }

    private void buildUI(AssetsManager assetsManager) {
        contentTable = new Table();
        contentTable.background(assetsManager.get9Drawable(Values.MENU_BACK));
        contentTable.padBottom(Values.PADDING_BIG*2).padTop(Values.PADDING);

        screenTable = new Table();
        screenTable.setFillParent(true);
        screenTable.setVisible(false);
        screenTable.setTouchable(Touchable.disabled);
        screenTable.center();
        screenTable.background(assetsManager.getDrawable(Values.OVERLAY_BACKGROUND));
        screenTable.add(contentTable).center();

        Table gameOverTable = new Table();
        gameOverLabel = new Label(Values.GAME_OVER, assetsManager.labelStyle());
        gameOverLabel.setAlignment(Align.center);
        gameOverTable.add(gameOverLabel).padTop(Values.SPACING*.5f).padBottom(Values.SPACING*.5f).growX();
        gameOverTable.background(assetsManager.getDrawable(Values.WINDOW_BANNER));

        scoreLabel = new Label("", assetsManager.labelStyle());
        scoreLabel.setAlignment(Align.left);

        mathLabel = new Label(Values.MATH_SCORE + 0, assetsManager.labelStyle());
        mathLabel.setAlignment(Align.left);

        backBtn = new TextButton(Values.BACK, assetsManager.textBtnStyle());
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                hide();
                screenManager.setScreen(ScreenName.CHARACTER_SCREEN);
            }
        });
        replayBtn = new TextButton(Values.REPLAY, assetsManager.textBtnStyle());
        replayBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                hide();
                screenManager.setScreen(ScreenName.MINIGAME_SCREEN);
            }
        });

        contentTable.add(gameOverTable).spaceBottom(Values.PADDING_BIG).width(Values.BTN_SIZE*6).growX().row();
        contentTable.add(scoreLabel).padBottom(Values.PADDING).row();
        contentTable.add(mathLabel).padBottom(Values.PADDING_BIG).row();
        contentTable.add(replayBtn).padBottom(Values.PADDING).padLeft(Values.PADDING)
                .padRight(Values.PADDING).growX().row();
        contentTable.add(backBtn).padLeft(Values.PADDING).padRight(Values.PADDING).growX().row();

    }

    public void addToStage(Stage stage) {
        stage.addActor(screenTable);
    }

    public void updateScore(int score) {
        scoreLabel.setText(Values.SCORE + score);
    }

    public void updateMathScore(int score) {
        mathLabel.setText(Values.MATH_SCORE + score);
    }

    public void show() {
        screenTable.setTouchable(Touchable.enabled);
        screenTable.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(.5f)));
    }

    private void hide() {
        screenTable.setTouchable(Touchable.disabled);
        screenTable.addAction(Actions.sequence(Actions.visible(false), Actions.fadeOut(.5f)));
    }
}
