package com.healthypetsTUM.game.character.ui.overlay;

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
import com.healthypetsTUM.game.util.store.UserData;
import com.healthypetsTUM.game.util.ui.Overlay;

import java.util.List;

public class WorldsOverlay extends Overlay {
    private final Table itemsTable;

    private Table[] wholeTables;
    private Label[] itemLabels, levelLabels;
    private Image[] coins;
    private Image[] levels;
    private Image[] itemStates;

    private ShopListener shopListener;

    private long nextClick;

    private Drawable boughtBack, equipedBack;

    public WorldsOverlay(AssetsManager assetsManager, ShopListener shopListener) {
        super(assetsManager, Values.WORLDS_HEADER);

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

        ScrollPane scrollPane = new ScrollPane(itemsTable);
        scrollPane.setScrollingDisabled(true, false);
        contentTable.add(scrollPane).width(Values.BTN_SIZE*6f).height(Values.BTN_SIZE*8f)
                .padBottom(Values.SPACING_SMALL).row();
    }

    private void createItems(AssetsManager assetsManager) {
        itemLabels = new Label[Values.WORLD_COUNT];
        levelLabels = new Label[Values.WORLD_COUNT];
        coins = new Image[Values.WORLD_COUNT];
        Image[] items = new Image[Values.WORLD_COUNT];
        levels = new Image[Values.WORLD_COUNT];
        itemStates = new Image[Values.WORLD_COUNT];

        wholeTables = new Table[Values.WORLD_COUNT];

        boughtBack = assetsManager.get9Drawable(Values.BOUGHT_BACK);
        equipedBack = assetsManager.get9Drawable(Values.EQUIPED_BACK);

        for(int i = 0; i < itemLabels.length; ++i) {
            coins[i] = new Image(assetsManager.getDrawable(Values.COIN));

            itemLabels[i] = new Label(Integer.toString(getCost(i)), assetsManager.labelStyleSmall());
            itemLabels[i].setAlignment(Align.center);

            levelLabels[i] = new Label(Integer.toString(getLevel(i)), assetsManager.labelStyleSmall());
            levelLabels[i].setAlignment(Align.center);

            items[i] = new Image(assetsManager.getBackground(i+Values.BACKGROUND));
            items[i].setScaling(Scaling.fillY);

            levels[i] = new Image(assetsManager.getDrawable(Values.LVL_ICON));

            itemStates[i] = new Image(assetsManager.get9Drawable(Values.DISABLED_IMG));
            itemStates[i].setTouchable(Touchable.enabled);

            wholeTables[i] = new Table();
            wholeTables[i].background(assetsManager.get9Drawable(Values.SHOP_ITEM_BACK));
            wholeTables[i].add(items[i]).height(Values.BTN_SIZE*4).pad(Values.PADDING_SMALL).colspan(2).row();
            wholeTables[i].add(coins[i]).center().padLeft(Values.SPACING_SMALL)
                    .padRight(Values.SPACING_SMALL).padBottom(Values.SPACING_SMALL);
            wholeTables[i].add(itemLabels[i]).padBottom(Values.SPACING_SMALL).row();
            wholeTables[i].add(levels[i]).center().padLeft(Values.SPACING_SMALL)
                    .padRight(Values.SPACING_SMALL).padBottom(Values.SPACING_SMALL);
            wholeTables[i].add(levelLabels[i]).padBottom(Values.SPACING_SMALL);

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
            if(i > 0 && (i+1) % Values.WORLD_ROW_AMOUNT == 0) {
                itemsTable.stack(wholeTables[i], itemStates[i]).space(Values.PADDING_SMALL).growX().row();
                continue;
            }
            itemsTable.stack(wholeTables[i], itemStates[i]).space(Values.PADDING_SMALL).growX();
        }

        scaleOverlay();

        UserData userData = DataUtils.getUserData();
        for(int i = 0; i < Values.WORLD_COUNT; ++i)
            if(getLevel(i) <= userData.getLvl()) enableItem(i);

        List<Integer> bought = userData.getBoughtWorlds();
        for(int i : bought) purchase(i, false);

        if(userData.getEquipedWorld() >= 0)
            wholeTables[userData.getEquipedWorld()].background(equipedBack);
    }

    private void purchase(int item, boolean buy) {
        if(nextClick <= TimeUtils.millis() && DataUtils.getUserData().getCoins() >= getCost(item)
                && DataUtils.getUserData().getLvl() >= getLevel(item)) {
            if(buy) shopListener.buyWorld(item, getCost(item));

            wholeTables[item].getListeners().clear();
            wholeTables[item].background(boughtBack);
            wholeTables[item].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Sounds.click();
                    int equiped = DataUtils.getUserData().getEquipedWorld();
                    if(equiped != -1)
                        wholeTables[equiped].background(boughtBack);

                    shopListener.equipWorld(item);
                    wholeTables[item].background(equipedBack);
                    close();
                }
            });

            coins[item].remove();
            itemLabels[item].remove();

            levels[item].remove();
            levelLabels[item].remove();

            nextClick = TimeUtils.millis()+500; //alle X-ms klicken erlauben
        }
    }

    public void levelChanged(int lvl) {
        for(int i = 0; i < itemLabels.length; ++i)
            if(getLevel(i) <= lvl) enableItem(i);
    }

    private void enableItem(int i) {
        itemStates[i].setTouchable(Touchable.disabled);
        itemStates[i].setDrawable(null);
    }

    private int getCost(int item) {
        return 1000+1000*item;
    }

    private int getLevel(int item) {
        return item+1;
    }
}
