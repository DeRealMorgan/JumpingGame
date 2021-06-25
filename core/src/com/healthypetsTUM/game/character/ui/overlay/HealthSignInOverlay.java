package com.healthypetsTUM.game.character.ui.overlay;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
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

public class HealthSignInOverlay extends Overlay {
    private ScrollPane policyPane;
    private Label policyLabel;
    private TextButton okButton;

    private Runnable onSignIn;

    public HealthSignInOverlay(AssetsManager assetsManager, Runnable onSignIn) {
        super(assetsManager, Values.HEALTH_HEADER);

        this.onSignIn = onSignIn;

        policyLabel = new Label(Values.HEALTH_BODY, assetsManager.labelStyleSmall());
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
                Sounds.click();
                DataUtils.getUserData().setHealthConsent(true);
                DataUtils.storeUserData();
                onSignIn.run();
                close();
            }
        });
        okButton.setTransform(true);

        contentTable.add(policyPane).width(Values.BTN_SIZE*6f).height(Values.BTN_SIZE*4f)
                .padBottom(Values.SPACING).colspan(3).row();
        contentTable.add(okButton).height(Values.BTN_SIZE).padTop(Values.SPACING)
                .padLeft(Values.SPACING_SMALL).padRight(Values.SPACING_SMALL).colspan(3).growX();

        //------------

        useClose(false);
        super.scaleOverlay();

        okButton.setOrigin(Align.center);
    }
}