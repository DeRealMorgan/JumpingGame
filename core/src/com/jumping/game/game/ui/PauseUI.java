package com.jumping.game.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.jumping.game.assets.AssetsManager;
import com.jumping.game.util.ScreenName;
import com.jumping.game.util.Values;
import com.jumping.game.util.interfaces.ScreenManager;

public class PauseUI {
    private ScreenManager screenManager;
    private Table contentTable, screenTable;
    private Label pauseLabel, scoreLabel, mathLabel;
    private TextButton continueBtn, backBtn;

    public PauseUI(AssetsManager assetsManager, ScreenManager screenManager) {
        buildUI(assetsManager);
        this.screenManager = screenManager;
    }

    private void buildUI(AssetsManager assetsManager) {
        contentTable = new Table();

        screenTable = new Table();
        screenTable.setFillParent(true);
        screenTable.setVisible(false);
        screenTable.setTouchable(Touchable.disabled);
        screenTable.center();
        screenTable.background(assetsManager.getDrawable(Values.OVERLAY_BACKGROUND));
        screenTable.add(contentTable).center();

        pauseLabel = new Label(Values.PAUSE, assetsManager.labelStyleHuge());
        pauseLabel.setAlignment(Align.center);

        scoreLabel = new Label("", assetsManager.labelStyleBig());
        scoreLabel.setAlignment(Align.left);

        mathLabel = new Label("", assetsManager.labelStyleBig());
        mathLabel.setAlignment(Align.left);

        backBtn = new TextButton(Values.BACK, assetsManager.textBtnStyle());
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.setScreen(ScreenName.CHARACTER_SCREEN);
            }
        });
        continueBtn = new TextButton(Values.REPLAY, assetsManager.textBtnStyle());
        contentTable.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.setScreen(ScreenName.MINIGAME_SCREEN);
            }
        });

        contentTable.add(pauseLabel).padBottom(Values.PADDING_BIG).row();
        contentTable.add(scoreLabel).padBottom(Values.PADDING).row();
        contentTable.add(mathLabel).padBottom(Values.PADDING_BIG).row();
        contentTable.add(continueBtn).padBottom(Values.PADDING).row();
        contentTable.add(backBtn).row();

    }

    public void addToStage(Stage stage) {
        stage.addActor(screenTable);
    }

    public void setScore(int score) {
        scoreLabel.setText(Values.SCORE + score);
    }

    public void setMathScore(int score) {
        mathLabel.setText(Values.MATH_SCORE + score);
    }

    public void show() {
        screenTable.setTouchable(Touchable.childrenOnly);
        screenTable.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(.5f)));
    }

    private void hide() {
        screenTable.setTouchable(Touchable.disabled);
        screenTable.addAction(Actions.sequence(Actions.visible(false), Actions.fadeOut(.5f)));
    }
}