package com.healthypetsTUM.game.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.interfaces.ScreenManager;

public class PauseUI {
    private final ScreenManager screenManager;
    private Table screenTable;
    private final Runnable onResume, onBack;

    private Label coinsLabel;

    public PauseUI(AssetsManager assetsManager, ScreenManager screenManager, Runnable onResume, Runnable onBack) {
        buildUI(assetsManager);
        this.screenManager = screenManager;
        this.onResume = onResume;
        this.onBack = onBack;
    }

    private void buildUI(AssetsManager assetsManager) {
        Table contentTable = new Table();
        contentTable.background(assetsManager.get9Drawable(Values.MENU_BACK));
        contentTable.padBottom(Values.PADDING).padTop(Values.PADDING);

        screenTable = new Table();
        screenTable.setFillParent(true);
        screenTable.setVisible(false);
        screenTable.setTouchable(Touchable.disabled);
        screenTable.center();
        screenTable.background(assetsManager.getDrawable(Values.OVERLAY_BACKGROUND));
        screenTable.add(contentTable).center();

        Table pauseTable = new Table();
        Label pauseLabel = new Label(Values.PAUSE, assetsManager.labelStyleBig());
        pauseLabel.setAlignment(Align.center);
        pauseTable.add(pauseLabel).padTop(Values.SPACING*.5f).padBottom(Values.SPACING*.5f).growX();
        pauseTable.background(assetsManager.getDrawable(Values.WINDOW_BANNER));


        coinsLabel = new Label("0", assetsManager.labelStyle());
        coinsLabel.setAlignment(Align.left);

        Image coinImg = new Image(assetsManager.getDrawable(Values.COIN));

        Table coinTable = new Table();
        coinTable.add(coinsLabel).padRight(Values.PADDING);
        coinTable.add(coinImg).left();

        TextButton backBtn = new TextButton(Values.BACK, assetsManager.textBtnStyle());
        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                onBack.run();
            }
        });
        TextButton continueBtn = new TextButton(Values.CONTINUE, assetsManager.textBtnStyle());
        continueBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                hide();
                onResume.run();
            }
        });

        contentTable.add(pauseTable).spaceBottom(Values.PADDING_BIG).width(Values.BTN_SIZE*6).growX().row();
        contentTable.add(coinTable).padBottom(Values.PADDING).row();
        contentTable.add(continueBtn).padBottom(Values.PADDING).padLeft(Values.PADDING).padRight(Values.PADDING)
                .growX().row();
        contentTable.add(backBtn).padLeft(Values.PADDING).padRight(Values.PADDING).growX().row();

    }

    public void addToStage(Stage stage) {
        stage.addActor(screenTable);
    }

    public void updateCoins(int coins) {
        coinsLabel.setText(Integer.toString(coins));
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