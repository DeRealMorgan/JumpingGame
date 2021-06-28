package com.healthypetsTUM.game.character.ui.overlay;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.ui.Overlay;

public class TreatsOverlay extends Overlay {
    private Image treatImage;
    private Label foundLabel;
    private TextButton okButton;

    private ScrollPane scrollPane;
    private Table paneTable;

    public TreatsOverlay(AssetsManager assetsManager, int item) {
        super(assetsManager, Values.TREATS_HEADER);

        treatImage = new Image(assetsManager.getDrawable(item+Values.SHOP_ITEM));
        treatImage.setScaling(Scaling.fillY);

        foundLabel = new Label(Values.TREATS_BODY, assetsManager.labelStyleSmall());
        foundLabel.setWrap(true);
        foundLabel.setAlignment(Align.center);

        TextButton.TextButtonStyle okBtnStyle = new TextButton.TextButtonStyle();
        okBtnStyle.up = assetsManager.get9Drawable(Values.BTN_UP);
        okBtnStyle.down = okBtnStyle.up;
        okBtnStyle.font = assetsManager.labelStyle().font;

        okButton = new TextButton(Values.OK, okBtnStyle);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                // TODO
            }
        });
        okButton.setTransform(true);

        paneTable = new Table();
        paneTable.add(treatImage).height(Values.BTN_SIZE*2).center().row();
        paneTable.add(foundLabel).growX();
        scrollPane = new ScrollPane(paneTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.pack();

        contentTable.add(scrollPane).width(Values.BTN_SIZE*6f).height(Values.BTN_SIZE*5f)
                .padBottom(Values.SPACING).colspan(3).row();
        contentTable.add(okButton).height(Values.BTN_SIZE).padTop(Values.SPACING)
                .padLeft(Values.SPACING_SMALL).padRight(Values.SPACING_SMALL).colspan(3).growX();

        //------------

        useClose(false);
        super.scaleOverlay();

        okButton.setOrigin(Align.center);
    }
}