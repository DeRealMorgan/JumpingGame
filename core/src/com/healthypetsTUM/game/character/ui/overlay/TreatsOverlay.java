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

        this.treatImage = new Image(assetsManager.getDrawable(item+Values.SHOP_ITEM));
        this.treatImage.setScaling(Scaling.fillX);

        foundLabel = new Label(Values.CONSENT_BODY, assetsManager.labelStyleSmall());
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
        paneTable.add(treatImage).width(Values.BTN_SIZE*2).center().padBottom(Values.PADDING).row();
        paneTable.add(foundLabel).growX();
        scrollPane = new ScrollPane(paneTable);
        scrollPane.setScrollingDisabled(true, false);

        contentTable.add(scrollPane).width(Values.BTN_SIZE*6f).height(Values.BTN_SIZE*4f)
                .padBottom(Values.SPACING).colspan(3).row();
        contentTable.add().size(Values.BTN_SIZE*0.7f).padRight(Values.SPACING).row();
        contentTable.add(okButton).height(Values.BTN_SIZE).padTop(Values.SPACING)
                .padLeft(Values.SPACING_SMALL).padRight(Values.SPACING_SMALL).colspan(3).growX();

        //------------

        useClose(false);
        resize(-1, -1);

        okButton.setOrigin(Align.center);
    }

    public void resize(int width, int height) {
        super.scaleOverlay();
    }
}