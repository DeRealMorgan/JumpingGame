package com.healthypetsTUM.game.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;

public class GameOverlayUI {
    private Table contentTable, screenTable;
    private Label scoreLabel, mathLabel;
    private Button pauseBtn;

    private Runnable onPause;

    public GameOverlayUI(AssetsManager assetsManager, Runnable onPause) {
        buildUI(assetsManager);
        this.onPause = onPause;
    }

    private void buildUI(AssetsManager assetsManager) {
        contentTable = new Table();

        screenTable = new Table();
        screenTable.setFillParent(true);
        screenTable.setVisible(true);
        screenTable.setTouchable(Touchable.childrenOnly);
        screenTable.top().left().padTop(Values.TOP_PADDING_UI_GAME);
        screenTable.add(contentTable).growX().fillX().expandX().top().left();

        scoreLabel = new Label(Values.SCORE + "0", assetsManager.labelStyleSmall());
        scoreLabel.setAlignment(Align.left);

        mathLabel = new Label(Values.MATH_SCORE + "0", assetsManager.labelStyleSmall());
        mathLabel.setAlignment(Align.left);

        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = assetsManager.getDrawable(Values.PAUSE_BTN);
        style.down = style.up;

        pauseBtn = new Button(style);
        pauseBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(pauseBtn.isDisabled()) return;
                Sounds.click();
                onPause.run();
            }
        });

        contentTable.add(scoreLabel).left().expandX();
        contentTable.add(pauseBtn).right().expandX().size(Values.BTN_SIZE).padTop(Values.PADDING).row();
        contentTable.add(mathLabel).left().padBottom(Values.PADDING).row();
        contentTable.padLeft(Values.PADDING).padRight(Values.PADDING);

    }

    public void enable() {
        pauseBtn.setDisabled(false);
    }

    public void disable() {
        pauseBtn.setDisabled(true);
    }

    public void updateScore(int score) {
        scoreLabel.setText(Values.SCORE + score);
    }

    public void updateMathScore(int score) {
        mathLabel.setText(Values.MATH_SCORE + score);
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

