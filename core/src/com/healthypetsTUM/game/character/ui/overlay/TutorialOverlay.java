package com.healthypetsTUM.game.character.ui.overlay;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.store.DataUtils;
import com.healthypetsTUM.game.util.ui.Overlay;
import com.healthypetsTUM.game.util.ui.ToggleButton;

public class TutorialOverlay extends Overlay {
    private final TextButton okButton;
    private final ToggleButton agreeButton;

    public TutorialOverlay(AssetsManager assetsManager, HealthSignInOverlay healthOverlay) {
        super(assetsManager, Values.TUTORIAL_HEADER);

        Label policyLabel = new Label(Values.TUTORIAL_BODY, assetsManager.labelStyleSmall());
        policyLabel.setWrap(true);
        policyLabel.setAlignment(Align.center);
        ScrollPane policyPane = new ScrollPane(policyLabel);
        policyPane.setScrollingDisabled(true, false);

        TextButton.TextButtonStyle okBtnStyle = new TextButton.TextButtonStyle();
        okBtnStyle.up = assetsManager.get9Drawable(Values.BTN_UP);
        okBtnStyle.down = okBtnStyle.up;
        okBtnStyle.font = assetsManager.labelStyle().font;

        okButton = new TextButton(Values.OK, okBtnStyle);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                DataUtils.getUserData().setPrivacyConsent(true);
                DataUtils.storeUserData();
                closeInstantly();
                healthOverlay.showInstantly();
            }
        });
        okButton.setTransform(true);

        agreeButton = new ToggleButton(assetsManager.getDrawable(Values.CHECKBOX_UNCHECKED),
                assetsManager.getDrawable(Values.CHECKBOX_CHECKED));
        agreeButton.setToggled(true);
        agreeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                agreeButtonClicked();
            }
        });

        Label agreeLabel = new Label(Values.AGREE, assetsManager.labelStyleSmall());
        agreeLabel.setAlignment(Align.left);

        contentTable.add(policyPane).width(Values.BTN_SIZE*6f).height(Values.BTN_SIZE*3f)
                .padBottom(Values.SPACING).colspan(3).row();
        contentTable.add(agreeButton.getImage()).size(Values.BTN_SIZE*0.7f).padLeft(Values.SPACING*2).left();
        contentTable.add(agreeLabel).padLeft(Values.PADDING).growX().row();
        contentTable.add(okButton).height(Values.BTN_SIZE).padTop(Values.SPACING)
                .padLeft(Values.SPACING_SMALL).padRight(Values.SPACING_SMALL).colspan(3).growX();

        //------------

        useClose(false);
        agreeButtonClicked();
        super.scaleOverlay();

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
}