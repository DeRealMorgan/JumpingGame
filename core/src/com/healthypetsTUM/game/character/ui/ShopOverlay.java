package com.healthypetsTUM.game.character.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.interfaces.ShopListener;
import com.healthypetsTUM.game.util.store.DataUtils;
import com.healthypetsTUM.game.util.ui.Overlay;

public class ShopOverlay extends Overlay {
    private Table itemsTable;

    private Table[] wholeTables;
    private Label[] itemLabels;
    private Image[] coins, items;

    private ScrollPane scrollPane;

    private ShopListener shopListener;

    private long nextClick;

    private Drawable boughtBack;

    public ShopOverlay(AssetsManager assetsManager, ShopListener shopListener) {
        super(assetsManager, Values.SHOP_HEADER);

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
        contentTable.add(scrollPane).width(Values.BTN_SIZE*7f).height(Values.BTN_SIZE*10f)
                .padBottom(Values.SPACING_SMALL).row();
    }

    private void createItems(AssetsManager assetsManager) {
        itemLabels = new Label[Values.ITEM_COUNT];
        coins = new Image[Values.ITEM_COUNT];
        items = new Image[Values.ITEM_COUNT];

        wholeTables = new Table[Values.ITEM_COUNT];

        boughtBack = assetsManager.get9Drawable(Values.BOUGHT_BACK);

        for(int i = 0; i < itemLabels.length; ++i) {
            coins[i] = new Image(assetsManager.getDrawable(Values.COIN));

            itemLabels[i] = new Label("1000", assetsManager.labelStyleSmall());
            itemLabels[i].setAlignment(Align.center);

            items[i] = new Image(assetsManager.getDrawable(i+Values.SHOP_ITEM));
            items[i].setScaling(Scaling.fillX);

            wholeTables[i] = new Table();
            wholeTables[i].background(assetsManager.get9Drawable(Values.SHOP_ITEM_BACK));
            wholeTables[i].add(items[i]).height(Values.BTN_SIZE*2).pad(Values.PADDING_SMALL).colspan(2).row();
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
            if(i > 0 && (i+1) % Values.SHOP_ROW_AMOUNT == 0) {
                itemsTable.add(wholeTables[i]).space(Values.PADDING_SMALL).growX().row();
                continue;
            }
            itemsTable.add(wholeTables[i]).space(Values.PADDING_SMALL).growX();
        }


        scaleOverlay();
    }

    private void purchase(int item) {
        if(nextClick <= TimeUtils.millis() && DataUtils.getUserData().getCoins() >= getCost(item)) {
            shopListener.buy(item, getCost(item));

            wholeTables[item].getListeners().clear();
            wholeTables[item].background(boughtBack);
            wholeTables[item].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Sounds.click();
                    shopListener.equip(item);
                }
            });

            coins[item].remove();
            itemLabels[item].remove();

            nextClick = TimeUtils.millis()+500; //alle X-ms klicken erlauben
        }
    }


    private int getCost(int item) {
        return 0;
    }

}