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
import com.jumping.game.util.Sounds;
import com.jumping.game.util.Values;
import com.jumping.game.util.interfaces.ScreenManager;

public class PauseUI {
    private ScreenManager screenManager;
    private Table contentTable, screenTable;
    private Label pauseLabel, scoreLabel, mathLabel;
    private TextButton continueBtn, backBtn;
    private Runnable onResume;

    public PauseUI(AssetsManager assetsManager, ScreenManager screenManager, Runnable onResume) {
        buildUI(assetsManager);
        this.screenManager = screenManager;
        this.onResume = onResume;
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

        pauseLabel = new Label(Values.PAUSE, assetsManager.labelStyleBig());
        pauseLabel.setAlignment(Align.center);

        scoreLabel = new Label("", assetsManager.labelStyle());
        scoreLabel.setAlignment(Align.left);

        mathLabel = new Label("", assetsManager.labelStyle());
        mathLabel.setAlignment(Align.left);

        backBtn = new TextButton(Values.BACK, assetsManager.textBtnStyle());
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                screenManager.setScreen(ScreenName.CHARACTER_SCREEN);
            }
        });
        continueBtn = new TextButton(Values.CONTINUE, assetsManager.textBtnStyle());
        continueBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                hide();
                onResume.run();
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

    public void updateScore(int score) {
        scoreLabel.setText(Values.SCORE + score);
    }

    public void updateMathScore(int score) {
        mathLabel.setText(Values.MATH_SCORE + score);
    }

    public void show() {
        screenTable.setTouchable(Touchable.childrenOnly);
        screenTable.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(.5f)));
    }

    private void hide() {
        screenTable.setTouchable(Touchable.disabled);
        screenTable.addAction(Actions.sequence(Actions.visible(false)));
    }
}