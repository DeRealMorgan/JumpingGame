package com.healthypetsTUM.game.util.ui;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.util.Values;

public abstract class Overlay {
    public Table table;
    protected Table contentTable;
    private Table closeTable, wrapperTable;

    protected Table overlayBackTable;

    protected Stack headerStack;

    protected Label headerLabel;
    private Table headerTable;

    protected Button closeButton;

    private boolean visible;

    public Overlay(AssetsManager manager, String headerString) {
        table = new Table();
        table.setFillParent(true);
        table.setVisible(false);
        table.setTouchable(Touchable.disabled);

        overlayBackTable = new Table();
        overlayBackTable.setFillParent(true);
        overlayBackTable.center();
        Image overlayBackImage = new Image(manager.getDrawable(Values.BLANK_IMG));
        overlayBackImage.setColor(Values.OVERLAY_BACK_COLOR);
        overlayBackTable.add(overlayBackImage).grow();
        table.addActor(overlayBackTable);

        wrapperTable = new Table();
        wrapperTable.background(manager.getDrawable(Values.MENU_BACK));
        table.add(wrapperTable).growX().padRight(Values.EDGE_DISTANCE*2).padLeft(Values.EDGE_DISTANCE*2).center();

        contentTable = new Table();
        headerLabel = new Label(headerString, manager.labelStyle());
        headerLabel.setAlignment(Align.center);
        headerTable = new Table();

        closeTable = new Table();
        Button.ButtonStyle style = new Button.ButtonStyle();
        style.up = manager.getDrawable(Values.CLOSE_BTN);
        style.down = style.up;
        closeButton = new Button(style);
        closeTable.add(closeButton).expandX().growX().size(Values.BTN_SIZE).padRight(Values.SPACING_SMALL).right();

        headerStack = new Stack();
        headerStack.add(headerLabel);
        headerStack.add(closeTable);
        headerTable.add(headerStack).padTop(Values.SPACING*.5f).padBottom(Values.SPACING*.5f).growX();
        headerTable.background(manager.getDrawable(Values.WINDOW_BANNER));
        wrapperTable.add(headerTable).growX().padTop(Values.SPACING*1.2f).spaceBottom(Values.SPACING).row();
        wrapperTable.add(contentTable).growX().padLeft(Values.SPACING).padRight(Values.SPACING).padBottom(Values.SPACING*1.2f);
    }

    protected void scaleOverlay() {
        wrapperTable.pack();
        closeTable.setPosition(wrapperTable.getRight()-closeTable.getWidth()-Values.SPACING*.5f,
                headerTable.getTop()-closeTable.getHeight()-Values.SPACING*.5f);
    }

    public void useClose(boolean use) {
        closeButton.setVisible(use);

        if(use)
            closeButton.setTouchable(Touchable.enabled);
        else
            closeButton.setTouchable(Touchable.disabled);
    }

    public void show() {
        if(visible) return;
        OverlayUtils.show(table);
        visible = true;
    }

    public void showInstantly() {
        OverlayUtils.showInstantly(table);
        visible = true;
    }


    public void close() {
        if(!visible) return;
        OverlayUtils.close(table);
        visible = false;
    }

    public void closeInstantly() {
        OverlayUtils.closeInstantly(table);
        visible = false;
    }
}
