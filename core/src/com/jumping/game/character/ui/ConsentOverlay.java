package com.jumping.game.character.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.jumping.game.assets.AssetsManager;
import com.jumping.game.util.store.DataUtils;
import com.jumping.game.util.ui.Overlay;
import com.jumping.game.util.ui.ToggleButton;
import com.jumping.game.util.Values;

public class ConsentOverlay extends Overlay {
    private ScrollPane policyPane;
    private Label policyLabel;
    private TextButton okButton;
    private ToggleButton agreeButton;
    private Label agreeLabel;

    public ConsentOverlay(AssetsManager assetsManager) {
        super(assetsManager, Values.CONSENT_HEADER);

        policyLabel = new Label(Values.CONSENT_BODY, assetsManager.labelStyleSmall());
        policyLabel.setWrap(true);
        policyLabel.setAlignment(Align.center);
        policyPane = new ScrollPane(policyLabel);
        policyPane.setScrollingDisabled(true, false);

        TextButton.TextButtonStyle okBtnStyle = new TextButton.TextButtonStyle();
        okBtnStyle.up = assetsManager.get9Drawable(Values.BTN_UP);
        okBtnStyle.down = okBtnStyle.up;
        okBtnStyle.font = assetsManager.labelStyle().font;

        okButton = new TextButton(Values.OK, okBtnStyle);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                DataUtils.getUserData().setPrivacyConsent(true);
                DataUtils.storeUserData();
                close();
            }
        });
        okButton.setTransform(true);

        agreeButton = new ToggleButton(assetsManager.getDrawable(Values.CHECKBOX_UNCHECKED),
                assetsManager.getDrawable(Values.CHECKBOX_CHECKED));
        agreeButton.setToggled(true);
        agreeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                agreeButtonClicked();
            }
        });

        agreeLabel = new Label(Values.AGREE, assetsManager.labelStyleSmall());
        agreeLabel.setAlignment(Align.center);

        contentTable.add(policyPane).width(Values.BTN_SIZE*5.6f).height(Values.BTN_SIZE*4f)
                .padBottom(Values.SPACING).colspan(3).row();
        contentTable.add(agreeButton.getImage()).size(Values.BTN_SIZE*0.7f).padLeft(Values.SPACING*2).left();
        contentTable.add(agreeLabel).growX();
        contentTable.add().size(Values.BTN_SIZE*0.7f).padRight(Values.SPACING).row();
        contentTable.add(okButton).height(Values.BTN_SIZE).padTop(Values.SPACING)
                .padLeft(Values.SPACING_SMALL).padRight(Values.SPACING_SMALL).colspan(3).growX();

        //------------

        useClose(false);
        agreeButtonClicked();
        resize(-1, -1);

        okButton.setOrigin(Align.center);
    }

    private void agreeButtonClicked() {
        if(agreeButton.isToggled()) {
            okButton.setTouchable(Touchable.enabled);
            okButton.setDisabled(false);
        } else {
            okButton.setTouchable(Touchable.disabled);
            okButton.setDisabled(true);
        }
    }

    public void resize(int width, int height) {
        super.scaleOverlay();
    }
}