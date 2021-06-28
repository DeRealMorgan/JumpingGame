package com.healthypetsTUM.game.character.ui.overlay;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.character.ui.ClothPiece;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.interfaces.ShopListener;
import com.healthypetsTUM.game.util.store.DataUtils;
import com.healthypetsTUM.game.util.store.UserData;
import com.healthypetsTUM.game.util.ui.Overlay;

import java.util.List;

public class ShopOverlay extends Overlay {
    private Table itemsTable;

    private Table[] wholeTables;
    private Label[] itemLabels;
    private Image[] coins, items, itemStates;

    private ScrollPane scrollPane;

    private ShopListener shopListener;

    private long nextClick;

    private Drawable boughtBack, equipedBack;

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
        contentTable.add(scrollPane).width(Values.BTN_SIZE*6f).height(Values.BTN_SIZE*8f)
                .padBottom(Values.SPACING_SMALL).row();
    }

    private void createItems(AssetsManager assetsManager) {
        itemLabels = new Label[Values.ITEM_COUNT];
        coins = new Image[Values.ITEM_COUNT];
        items = new Image[Values.ITEM_COUNT];
        itemStates = new Image[Values.ITEM_COUNT];

        wholeTables = new Table[Values.ITEM_COUNT];

        boughtBack = assetsManager.get9Drawable(Values.BOUGHT_BACK);
        equipedBack = assetsManager.get9Drawable(Values.EQUIPED_BACK);

        for(int i = 0; i < itemLabels.length; ++i) {
            coins[i] = new Image(assetsManager.getDrawable(Values.COIN));
            coins[i].setScaling(Scaling.fillX);

            itemLabels[i] = new Label(Integer.toString(getCost(i)), assetsManager.labelStyleSmall());
            itemLabels[i].setAlignment(Align.center);

            items[i] = new Image(assetsManager.getDrawable(i+Values.SHOP_ITEM));
            items[i].setScaling(Scaling.fillX);

            itemStates[i] = new Image(assetsManager.get9Drawable(Values.DISABLED_IMG));
            itemStates[i].setTouchable(Touchable.enabled);

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
                    purchase(finalI, true);
                }
            });
        }

        for(int i = 0; i < wholeTables.length; ++i) {
            if(i > 0 && (i+1) % Values.SHOP_ROW_AMOUNT == 0) {
                itemsTable.stack(wholeTables[i], itemStates[i]).space(Values.PADDING_SMALL).growX().row();
                continue;
            }
            itemsTable.stack(wholeTables[i], itemStates[i]).space(Values.PADDING_SMALL).growX();
        }

        scaleOverlay();

        UserData data = DataUtils.getUserData();

        List<Integer> unlocked = data.getUnlockedItems();
        for(int i : unlocked) enableItem(i);

        List<Integer> bought = data.getBoughtItems();
        for(int i : bought) purchase(i, false);

        List<Integer> equiped = data.getEquipedItems();
        for(int i : equiped) wholeTables[i].background(equipedBack);

    }

    private void purchase(int item, boolean buy) {
        if(nextClick <= TimeUtils.millis() && DataUtils.getUserData().getCoins() >= getCost(item)) {
            if(buy)
                shopListener.buy(item, getCost(item));

            wholeTables[item].getListeners().clear();
            wholeTables[item].background(boughtBack);
            wholeTables[item].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Sounds.click();
                    shopListener.equipItem(item);
                    unequipOther(item);
                    close();
                    wholeTables[item].background(equipedBack);
                }
            });

            coins[item].remove();
            itemLabels[item].remove();

            nextClick = TimeUtils.millis()+200; //alle X-ms klicken erlauben
        }
    }

    public void itemCollected(int item) {
        enableItem(item);
    }

    private void unequipOther(int item) {
        List<Integer> bought = DataUtils.getUserData().getBoughtItems();
        if(ClothPiece.bodyPart(item) == ClothPiece.HEAD) {
            for (int i = 0; i <= 6; ++i) {
                if(item == i) continue;

                if(bought.contains(i))
                    wholeTables[i].background(boughtBack);

            }
        } else {
            for (int i = 7; i <= Values.ITEM_COUNT; ++i) {
                if(item == i) continue;

                if(bought.contains(i))
                    wholeTables[i].background(boughtBack);
            }
        }
    }

    private void enableItem(int i) {
        itemStates[i].setTouchable(Touchable.disabled);
        itemStates[i].setDrawable(null);
    }

    private int getCost(int item) {
        return 100+100*item;
    }

}