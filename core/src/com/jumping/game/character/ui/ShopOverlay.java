package com.jumping.game.character.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.jumping.game.assets.AssetsManager;
import com.jumping.game.util.Overlay;
import com.jumping.game.util.Values;
import com.jumping.game.util.interfaces.ShopListener;

public class ShopOverlay extends Overlay {
    private Table itemsTable;

    private Stage stage;
    private float coinUIX, coinUIY;

    private Table[] itemTable;
    private Label[] itemLabels;
    private TextButton[] costsButton;
    private Image[] coins, items;

    private ScrollPane scrollPane;
    private ShopListener shopListener;

    private long nextClick;

    public ShopOverlay(AssetsManager assetsManager, ShopListener shopListener) {
        super(assetsManager, Values.SHOP_HEADER);

        this.shopListener = shopListener;

        //------------

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                close();
            }
        });

        itemsTable = new Table();
        createItems(assetsManager);

        scrollPane = new ScrollPane(itemsTable);
        scrollPane.setScrollingDisabled(true, false);
        contentTable.add(scrollPane).width(Values.BTN_SIZE*5.5f).height(Values.BTN_SIZE*6f)
                .padBottom(Values.SPACING_SMALL).row();

        itemsTable.pack();
        resize(-1, -1);
    }

    private void createItems(AssetsManager assetsManager) {
        itemTable = new Table[Values.ITEM_COUNT];
        itemLabels = new Label[Values.ITEM_COUNT];
        coins = new Image[Values.ITEM_COUNT];

        Table[] wholeTables = new Table[Values.ITEM_COUNT];

        for(int i = 0; i < itemLabels.length; ++i) {
            coins[i] = new Image(assetsManager.getDrawable(Values.COIN));

            itemLabels[i] = new Label("    ", assetsManager.labelStyle());
            itemLabels[i].setAlignment(Align.center);

            items[i] = new Image(assetsManager.getDrawable(Values.SHOP_ITEM+i));

            wholeTables[i] = new Table();
            wholeTables[i].background(assetsManager.get9Drawable(Values.SHOP_ITEM_BACK));
            itemTable[i] = new Table();
            itemTable[i].pad(Values.SPACING_SMALL);
            itemTable[i].add(items[i]).grow();

            wholeTables[i].add(itemTable[i]).row();
            wholeTables[i].add(coins[i]).padRight(Values.SPACING_SMALL);
            wholeTables[i].add(itemLabels[i]).padBottom(Values.SPACING_SMALL);

            wholeTables[i].setTouchable(Touchable.enabled);
            int finalI = i;
            wholeTables[i].addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    purchase(finalI);
                }
            });
        }

        for(int i = 0; i < itemTable.length; ++i) {
            if(i > 0 && i % Values.SHOP_ROW_AMOUNT == 0) {
                itemsTable.add(wholeTables[i]).expandX().row();
                continue;
            }
            itemsTable.add(wholeTables[i]).expandX();
        }
    }

    private void purchase(int i) {
        if(nextClick <= TimeUtils.millis()) {
            // TODO
            nextClick = TimeUtils.millis()+500; //alle X-ms klicken erlauben
        }
    }

    public void resize(int width, int height) {
        super.scaleOverlay();
    }

    public void setShopListener(ShopListener shopListener) {
        this.shopListener = shopListener;
    }

    public void setNewScreen(Stage stage, Table coinUITable) {
        this.stage = stage;

        coinUIX = coinUITable.getX()+coinUITable.getWidth()/8;
        coinUIY = coinUITable.getY()+coinUITable.getHeight()/2;
    }
}