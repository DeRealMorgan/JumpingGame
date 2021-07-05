package com.healthypetsTUM.game.character.ui.overlay;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.game.math.MathImpl;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.interfaces.VoidRunnableInt;
import com.healthypetsTUM.game.util.ui.Overlay;

public class TreatsOverlay extends Overlay {
    private final Label foundLabel;
    private final TextButton okButton;

    private MathImpl math;

    private int mathToDo = 2, item;

    private final VoidRunnableInt onSuccess;

    public TreatsOverlay(AssetsManager assetsManager, int item, VoidRunnableInt onSuccess) {
        super(assetsManager, Values.TREATS_HEADER);

        this.item = item;
        this.onSuccess = onSuccess;

        Image treatImage = new Image(assetsManager.getDrawable(item + Values.SHOP_ITEM));
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
                math.showMathExercise(null);
                math.showSoftKeyboard();
                closeInstantly();
            }
        });
        okButton.setTransform(true);

        Table paneTable = new Table();
        paneTable.add(treatImage).height(Values.BTN_SIZE*2).center().row();
        paneTable.add(foundLabel).growX();
        ScrollPane scrollPane = new ScrollPane(paneTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.pack();

        contentTable.add(scrollPane).width(Values.BTN_SIZE*6f).height(Values.BTN_SIZE*5f)
                .padBottom(Values.SPACING).colspan(3).row();
        contentTable.add(okButton).height(Values.BTN_SIZE).padTop(Values.SPACING)
                .padLeft(Values.SPACING_SMALL).padRight(Values.SPACING_SMALL).colspan(3).growX();

        math = new MathImpl(assetsManager, this::mathCorrect, this::mathWrong, this::onMathShow, false, false);
        //------------

        useClose(false);
        super.scaleOverlay();

        okButton.setOrigin(Align.center);
    }

    public void update(float dt) {
        math.update(dt);
    }

    private void mathCorrect(int i) {
        if(mathToDo != 0) {
            math.showMathExercise(null);
            --mathToDo;
        } else {
            foundLabel.setText(Values.TREAT_COLLECTED);
            showInstantly();

            changeOK();

            changeHead(true);
            onSuccess.run(item);

            math.closeSoftKeyboard();
        }
    }

    private void mathWrong() {
        math.closeSoftKeyboard();
        foundLabel.setText(Values.TREAT_LOST);
        showInstantly();

        changeHead(false);

        okButton.getListeners().clear();
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                closeInstantly();
            }
        });
    }

    private void changeHead(boolean success) {
        if(success)
            headerLabel.setText(Values.TREAT_SUCCESS);
        else
            headerLabel.setText(Values.TREAT_FAIL);
    }

    private void changeOK() {
        okButton.getListeners().clear();
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                close();
            }
        });
    }

    private void onMathShow() {}

    public MathImpl getMath() {
        return math;
    }
}