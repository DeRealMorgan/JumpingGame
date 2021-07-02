package com.healthypetsTUM.game.util.ui;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.store.UserData;

public class UIBar {
    public Table table;

    private Label lvlLabel, coinLabel, mathLabel;

    public UIBar(AssetsManager assetsManager) {
        buildUI(assetsManager);
    }

    private void buildUI(AssetsManager assetsManager) {
        table = new Table();
        table.setFillParent(true);
        table.setVisible(true);
        table.setTouchable(Touchable.disabled);
        table.top();

        Table contentTable = new Table();
        table.add(contentTable).growX().top().center();

        lvlLabel = new Label(Values.LVL + "0", assetsManager.labelStyleSmall());
        coinLabel = new Label("0", assetsManager.labelStyleSmall());
        mathLabel = new Label("0", assetsManager.labelStyleSmall());

        Image lvlImg = new Image(assetsManager.getDrawable(Values.LVL_ICON));
        Image coinImg = new Image(assetsManager.getDrawable(Values.COIN));
        Image mathImg = new Image(assetsManager.getDrawable(Values.MATH_ICON));

        Table lvlTable = new Table();
        Table coinTable = new Table();
        Table mathTable = new Table();

        lvlTable.add(lvlImg).padRight(Values.PADDING_SMALL);
        lvlTable.add(lvlLabel).center();

        coinTable.add(coinImg).padRight(Values.PADDING_SMALL);
        coinTable.add(coinLabel).center();

        mathTable.add(mathImg).padRight(Values.PADDING_SMALL);
        mathTable.add(mathLabel).center();

        contentTable.add(lvlTable).expandX().pad(Values.PADDING_SMALL);
        contentTable.add(coinTable).expandX().pad(Values.PADDING_SMALL);
        contentTable.add(mathTable).expandX().pad(Values.PADDING_SMALL);
        contentTable.background(assetsManager.get9Drawable(Values.UI_BAR_BACK));
    }

   public void setLvl(int lvl) {
        lvlLabel.setText(Values.LVL + lvl);
   }

   public void setCoins(int coins) {
       coinLabel.setText(coins);
   }

   public void setMath(int math, int lvl) {
        mathLabel.setText(math + "/" + UserData.getMathNextLevel(math, lvl));
   }
}
