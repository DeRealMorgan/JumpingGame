package com.healthypetsTUM.game.character.ui.overlay;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.interfaces.ShopListener;
import com.healthypetsTUM.game.util.store.DataUtils;
import com.healthypetsTUM.game.util.ui.Overlay;

public class FoodShopOverlay extends Overlay {
    private Table itemsTable;

    private Table[] wholeTables;
    private Label[] itemLabels;
    private Image[] coins, items;

    private ScrollPane scrollPane;

    private ShopListener shopListener;

    private long nextClick;

    public FoodShopOverlay(AssetsManager assetsManager, ShopListener shopListener) {
        super(assetsManager, Values.SHOP_FOOD_HEADER);

        this.shopListener = shopListener;

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                close();
            }
        });

        itemsTable = new Table();
        createItems(assetsManager);

        scrollPane = new ScrollPane(itemsTable);
        scrollPane.setScrollingDisabled(true, false);
        contentTable.add(scrollPane).width(Values.BTN_SIZE*6f).height(Values.BTN_SIZE*8f)
                .padBottom(Values.SPACING_SMALL).row();
    }

    private void createItems(AssetsManager assetsManager) {
        itemLabels = new Label[Values.FOOD_ITEM_COUNT];
        coins = new Image[Values.FOOD_ITEM_COUNT];
        items = new Image[Values.FOOD_ITEM_COUNT];

        wholeTables = new Table[Values.FOOD_ITEM_COUNT];

        for(int i = 0; i < itemLabels.length; ++i) {
            coins[i] = new Image(assetsManager.getDrawable(Values.COIN));
            coins[i].setScaling(Scaling.fillX);

            itemLabels[i] = new Label(Integer.toString(getCost(i)), assetsManager.labelStyleSmall());
            itemLabels[i].setAlignment(Align.center);

            items[i] = new Image(assetsManager.getDrawable(i+Values.FOOD_ITEM));
            items[i].setScaling(Scaling.fillX);

            wholeTables[i] = new Table();
            wholeTables[i].background(assetsManager.get9Drawable(Values.SHOP_ITEM_BACK));
            wholeTables[i].add(items[i]).height(Values.BTN_SIZE*1.5f).pad(Values.PADDING).colspan(2).row();
            wholeTables[i].add(coins[i]).center().padLeft(Values.SPACING_SMALL)
                    .padRight(Values.SPACING_SMALL).padBottom(Values.SPACING_SMALL);
            wholeTables[i].add(itemLabels[i]).padBottom(Values.SPACING_SMALL);

            wholeTables[i].setTouchable(Touchable.enabled);
            int finalI = i;
            wholeTables[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Sounds.click();
                    purchase(finalI);
                }
            });
        }

        for(int i = 0; i < wholeTables.length; ++i) {
            if(i > 0 && (i+1) % Values.FOOD_ROW_AMOUNT == 0) {
                itemsTable.add(wholeTables[i]).space(Values.PADDING_SMALL).growX().row();
                continue;
            }
            itemsTable.add(wholeTables[i]).space(Values.PADDING_SMALL).growX();
        }

        scaleOverlay();
    }

    private void purchase(int item) {
        if(nextClick <= TimeUtils.millis() && DataUtils.getUserData().getCoins() >= getCost(item)) {
            close();
            shopListener.buyFood(item, getCost(item));
            nextClick = TimeUtils.millis()+500; //alle X-ms klicken erlauben
        }
    }


    private int getCost(int item) {
        return 10+item*10;
    }

}