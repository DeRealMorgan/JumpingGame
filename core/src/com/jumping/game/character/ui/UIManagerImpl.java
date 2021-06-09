package com.jumping.game.character.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jumping.game.assets.AssetsManager;
import com.jumping.game.util.ScreenName;
import com.jumping.game.util.interfaces.ScreenManager;
import com.jumping.game.util.interfaces.UIManager;
import com.jumping.game.util.Values;

public class UIManagerImpl implements UIManager {
    private final ScreenManager screenManager;

    private final Stage stage;

    private Table contentTable, backgroundTable, uiTableLeft, uiTableRight, uiTableBottom, energyTable;
    private Button shopBtn, achievementsBtn, worldsBtn, leaderboardBtn, showerBtn, foodBtn, petBtn, minigameBtn;

    private Character character;

    private HandPetting hand;
    private Shower shower;
    private FoodItem food;

    private final static float BOTTOM_PADDING = 40, TOP_PADDING = 40, SIDE_PADDING = 40;

    public UIManagerImpl(Viewport viewport, SpriteBatch batch, AssetsManager assetsManager,
                         ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.stage = new Stage(viewport, batch);

        createUI(assetsManager);
    }

    private void createUI(AssetsManager assetsManager) {
        assetsManager.addInputProcessor(stage);
        backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        stage.addActor(backgroundTable);

        character = new Character(assetsManager, stage);

        contentTable = new Table();
        contentTable.setFillParent(true);
        stage.addActor(contentTable);

        uiTableLeft = new Table();
        uiTableLeft.setFillParent(true);
        uiTableLeft.top().left();
        uiTableLeft.padTop(TOP_PADDING).padLeft(SIDE_PADDING);
        stage.addActor(uiTableLeft);

        uiTableRight = new Table();
        uiTableRight.setFillParent(true);
        uiTableRight.top().right();
        uiTableRight.padTop(TOP_PADDING).padRight(SIDE_PADDING);
        stage.addActor(uiTableRight);

        uiTableBottom = new Table();
        uiTableBottom.setFillParent(true);
        uiTableBottom.bottom();
        uiTableBottom.padBottom(BOTTOM_PADDING);
        stage.addActor(uiTableBottom);

        energyTable = new Table();
        energyTable.setFillParent(true);
        energyTable.top();
        stage.addActor(energyTable);

        Button.ButtonStyle shopBtnStyle = new Button.ButtonStyle();
        shopBtnStyle.down = assetsManager.getDrawable(Values.SHOP_BTN);
        shopBtnStyle.up = shopBtnStyle.down;
        shopBtn = new Button(shopBtnStyle);

        Button.ButtonStyle worldsBtnStyle = new Button.ButtonStyle();
        worldsBtnStyle.down = assetsManager.getDrawable(Values.WORLDS_BTN);
        worldsBtnStyle.up = worldsBtnStyle.down;
        worldsBtn = new Button(worldsBtnStyle);

        uiTableLeft.add(shopBtn).size(Values.BTN_SIZE).row();
        uiTableLeft.add(worldsBtn).size(Values.BTN_SIZE).row();

        Button.ButtonStyle achievementsBtnStyle = new Button.ButtonStyle();
        achievementsBtnStyle.down = assetsManager.getDrawable(Values.ACHIEVEMENTS_BTN);
        achievementsBtnStyle.up = achievementsBtnStyle.down;
        achievementsBtn = new Button(achievementsBtnStyle);

        Button.ButtonStyle leaderboardBtnStyle = new Button.ButtonStyle();
        leaderboardBtnStyle.down = assetsManager.getDrawable(Values.LEADERBOARD_BTN);
        leaderboardBtnStyle.up = leaderboardBtnStyle.down;
        leaderboardBtn = new Button(leaderboardBtnStyle);

        uiTableRight.add(achievementsBtn).size(Values.BTN_SIZE).row();
        uiTableRight.add(leaderboardBtn).size(Values.BTN_SIZE).row();

        Button.ButtonStyle showerBtnStyle = new Button.ButtonStyle();
        showerBtnStyle.down = assetsManager.getDrawable(Values.SHOWER_BTN);
        showerBtnStyle.up = showerBtnStyle.down;
        showerBtn = new Button(showerBtnStyle);
        showerBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setUiVisible(false);
                shower.show();
            }
        });

        Button.ButtonStyle foodBtnStyle = new Button.ButtonStyle();
        foodBtnStyle.down = assetsManager.getDrawable(Values.FOOD_BTN);
        foodBtnStyle.up = foodBtnStyle.down;
        foodBtn = new Button(foodBtnStyle);
        foodBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setUiVisible(false);
                food.show();
            }
        });

        Button.ButtonStyle petBtnStyle = new Button.ButtonStyle();
        petBtnStyle.down = assetsManager.getDrawable(Values.PET_BTN);
        petBtnStyle.up = petBtnStyle.down;
        petBtn = new Button(petBtnStyle);
        petBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setUiVisible(false);
                hand.show();
            }
        });

        Button.ButtonStyle minigameBtnStyle = new Button.ButtonStyle();
        minigameBtnStyle.down = assetsManager.getDrawable(Values.MINIGAME_BTN);
        minigameBtnStyle.up = minigameBtnStyle.down;
        minigameBtn = new Button(minigameBtnStyle);
        minigameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setUiVisible(false);
                showMinigame();
            }
        });

        uiTableBottom.add(showerBtn).size(Values.BTN_SIZE).growX();
        uiTableBottom.add(foodBtn).size(Values.BTN_SIZE).growX();
        uiTableBottom.add(petBtn).size(Values.BTN_SIZE).growX();
        uiTableBottom.add(minigameBtn).size(Values.BTN_SIZE).growX();

        //stage.setDebugAll(true);

        hand = new HandPetting(assetsManager, this::pettingDone);
        hand.addAll(contentTable);

        shower = new Shower(assetsManager, this::showerDone);
        shower.addAll(contentTable);

        food = new FoodItem(assetsManager, this::foodDone);
        food.addAll(contentTable);

        contentTable.pack();
        hand.position();
        shower.position();
        food.position();
    }

    private void showerDone() {
        setUiVisible(true);
    }

    private void pettingDone() {
        setUiVisible(true);
    }

    private void foodDone() {
        setUiVisible(true);
    }

    private void showMinigame() {
        screenManager.setScreen(ScreenName.MINIGAME_SCREEN);
    }

    @Override
    public void update(float dt) {
        stage.act(dt);

        if(hand.isPresent() && character.isOverlappingHead(hand.getBounds()))
            hand.showHearts(character.getHeadBounds());

        if(shower.isPresent() && character.isOverlappingBody(shower.getBounds()))
            shower.showEffect(character.getBodyBounds());

        if(food.isPresent() && character.isOverlappingHead(food.getBounds()))
            food.showCrumbs(character.getHeadBounds());
    }

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
    }

    public void setUiVisible(boolean show) {
        if(uiTableBottom.isVisible() != show) {
            if(show) {
                uiTableBottom.setTouchable(Touchable.childrenOnly);
                uiTableLeft.setTouchable(Touchable.childrenOnly);
                uiTableRight.setTouchable(Touchable.childrenOnly);

                uiTableBottom.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(1)));
                uiTableLeft.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(1)));
                uiTableRight.addAction(Actions.sequence(Actions.visible(true), Actions.fadeIn(1)));
            } else {
                uiTableBottom.setTouchable(Touchable.disabled);
                uiTableLeft.setTouchable(Touchable.disabled);
                uiTableRight.setTouchable(Touchable.disabled);

                uiTableBottom.addAction(Actions.sequence(Actions.fadeOut(1), Actions.visible(false)));
                uiTableLeft.addAction(Actions.sequence(Actions.fadeOut(1), Actions.visible(false)));
                uiTableRight.addAction(Actions.sequence(Actions.fadeOut(1), Actions.visible(false)));
            }
        }
    }

    public void show() {
        //TODO
    }

    @Override
    public void drawUI() {
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
