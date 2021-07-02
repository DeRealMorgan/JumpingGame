package com.healthypetsTUM.game.game.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;

public class GameOverlayUI {
    private Table screenTable;
    private Button pauseBtn;
    private Label coinsLabel;

    private Runnable onPause;

    public GameOverlayUI(AssetsManager assetsManager, Runnable onPause) {
        buildUI(assetsManager);
        this.onPause = onPause;
    }

    private void buildUI(AssetsManager assetsManager) {
        Table contentTable = new Table();

        screenTable = new Table();
        screenTable.setFillParent(true);
        screenTable.setVisible(true);
        screenTable.setTouchable(Touchable.childrenOnly);
        screenTable.top().left().padTop(Values.TOP_PADDING_UI_GAME);
        screenTable.add(contentTable).growX().fillX().expandX().top().left();

        coinsLabel = new Label("0", assetsManager.labelStyle());
        coinsLabel.setAlignment(Align.left);

        Image coinImg = new Image(assetsManager.getDrawable(Values.COIN));

        Table coinTable = new Table();
        coinTable.add(coinImg).padRight(Values.PADDING);
        coinTable.add(coinsLabel).left();

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

        Table uiTable = new Table();
        uiTable.add(coinTable).left().expandX().row();
        contentTable.add(uiTable).left().padBottom(Values.PADDING_SMALL).padTop(Values.PADDING_BIG);
        contentTable.add(pauseBtn).right().expandX().size(Values.BTN_SIZE).padTop(Values.PADDING_BIG).row();
        contentTable.padLeft(Values.PADDING).padRight(Values.PADDING);

    }

    public void enable() {
        pauseBtn.setDisabled(false);
    }

    public void disable() {
        pauseBtn.setDisabled(true);
    }

    public void updateCoins(int coins) {
        coinsLabel.setText(Integer.toString(coins));
    }

    public void addToStage(Stage stage) {
        stage.addActor(screenTable);
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

