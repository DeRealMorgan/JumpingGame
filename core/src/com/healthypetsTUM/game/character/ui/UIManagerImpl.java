package com.healthypetsTUM.game.character.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.character.ui.overlay.*;
import com.healthypetsTUM.game.util.ScreenName;
import com.healthypetsTUM.game.util.Sounds;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.interfaces.ScreenManager;
import com.healthypetsTUM.game.util.interfaces.ShopListener;
import com.healthypetsTUM.game.util.interfaces.UIManager;
import com.healthypetsTUM.game.util.store.DataUtils;
import com.healthypetsTUM.game.util.store.UserData;
import com.healthypetsTUM.game.util.ui.UIBar;

public class UIManagerImpl implements UIManager, ShopListener {
    private final ScreenManager screenManager;
    private final AssetsManager assetsManager;
    private final Stage stage;

    private Table contentTable, backgroundTable, uiTableLeft, uiTableRight, uiTableBottom, progressTable;
    private Table showerTable, foodTable, petTable, minigameTable;
    private Button shopBtn, achievementsBtn, worldsBtn, settingsBtn;
    private Button showerBtn, foodBtn, petBtn, minigameBtn;

    private Character character;
    private Label progressLabel;
    private ProgressBar progressBar, showerProgressbar, foodProgressbar, petProgressbar, minigameProgressbar;
    private Drawable progressbarRed, progressbarGreen;

    private ShopOverlay shopOverlay;
    private WorldsOverlay worldsOverlay;
    private SettingsOverlay settingsOverlay;
    private FoodShopOverlay foodShopOverlay;
    private UIBar uiBar;

    private int stepsToday;

    private HandPetting hand;
    private Shower shower;
    private FoodItem food;

    private final static float BOTTOM_PADDING = 40, TOP_PADDING = 40, SIDE_PADDING = 40;

    public UIManagerImpl(Viewport viewport, SpriteBatch batch, AssetsManager assetsManager,
                         ScreenManager screenManager, Runnable onHealthSignIn) {
        this.screenManager = screenManager;
        this.assetsManager = assetsManager;
        this.stage = new Stage(viewport, batch);

        createUI(assetsManager, onHealthSignIn);

    }

    private void createUI(AssetsManager assetsManager, Runnable onHealthSignIn) {
        assetsManager.addInputProcessor(stage);
        backgroundTable = new Table();
        backgroundTable.setFillParent(true);

        backgroundTable.background(assetsManager.getBackground(DataUtils.getUserData().getEquipedWorld() + Values.BACKGROUND));
        stage.addActor(backgroundTable);

        character = new Character(assetsManager, stage);

        contentTable = new Table();
        contentTable.setFillParent(true);
        stage.addActor(contentTable);

        uiTableLeft = new Table();
        uiTableLeft.setFillParent(true);
        uiTableLeft.top().left();
        uiTableLeft.padTop(TOP_PADDING*3+10).padLeft(SIDE_PADDING);
        stage.addActor(uiTableLeft);

        uiTableRight = new Table();
        uiTableRight.setFillParent(true);
        uiTableRight.top().right();
        uiTableRight.padTop(TOP_PADDING*3+10).padRight(SIDE_PADDING);
        stage.addActor(uiTableRight);

        uiTableBottom = new Table();
        uiTableBottom.setFillParent(true);
        uiTableBottom.bottom();
        uiTableBottom.padBottom(BOTTOM_PADDING);
        stage.addActor(uiTableBottom);

        Button.ButtonStyle shopBtnStyle = new Button.ButtonStyle();
        shopBtnStyle.down = assetsManager.getDrawable(Values.SHOP_BTN);
        shopBtnStyle.up = shopBtnStyle.down;
        shopBtn = new Button(shopBtnStyle);
        shopBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                shopOverlay.show();
            }
        });

        Button.ButtonStyle worldsBtnStyle = new Button.ButtonStyle();
        worldsBtnStyle.down = assetsManager.getDrawable(Values.WORLDS_BTN);
        worldsBtnStyle.up = worldsBtnStyle.down;
        worldsBtn = new Button(worldsBtnStyle);
        worldsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                worldsOverlay.show();
            }
        });

        uiTableLeft.add(shopBtn).size(Values.BTN_SIZE).padBottom(20f).row();
        uiTableLeft.add(worldsBtn).size(Values.BTN_SIZE).row();

        Button.ButtonStyle achievementsBtnStyle = new Button.ButtonStyle();
        achievementsBtnStyle.down = assetsManager.getDrawable(Values.ACHIEVEMENTS_BTN);
        achievementsBtnStyle.up = achievementsBtnStyle.down;
        achievementsBtn = new Button(achievementsBtnStyle);

        Button.ButtonStyle settingsBtnStyle = new Button.ButtonStyle();
        settingsBtnStyle.down = assetsManager.getDrawable(Values.SETTINGS_BTN);
        settingsBtnStyle.up = settingsBtnStyle.down;
        settingsBtn = new Button(settingsBtnStyle);
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                settingsOverlay.show();
            }
        });

        uiTableRight.add(settingsBtn).size(Values.BTN_SIZE).padBottom(20f).row();
        uiTableRight.add(achievementsBtn).size(Values.BTN_SIZE).row();

        int minH = 50;
        progressbarRed = assetsManager.get9Drawable(Values.PROGRESSBAR_FRONT_RED);
        progressbarGreen = assetsManager.get9Drawable(Values.PROGRESSBAR_FRONT_GREEN);

        progressbarRed.setMinHeight(minH);
        progressbarGreen.setMinHeight(minH);
        progressbarRed.setMinWidth(0);
        progressbarGreen.setMinWidth(0);

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = assetsManager.get9Drawable(Values.PROGRESSBAR_BACK);
        progressBarStyle.background.setMinHeight(minH);
        progressBarStyle.knob = progressbarRed;

        ProgressBar.ProgressBarStyle progressbarStyleSmall = new ProgressBar.ProgressBarStyle();
        progressbarStyleSmall.background = assetsManager.get9Drawable(Values.PROGRESSBAR_BACK);
        progressbarStyleSmall.background.setMinHeight(15);
        progressbarStyleSmall.knob = assetsManager.get9Drawable(Values.PROGRESSBAR_FRONT);
        progressbarStyleSmall.knob.setMinWidth(0);

        showerProgressbar = new ProgressBar(0, 100, 1, false, progressbarStyleSmall);
        foodProgressbar = new ProgressBar(0, 100, 1, false, progressbarStyleSmall);
        petProgressbar = new ProgressBar(0, 100, 1, false, progressbarStyleSmall);
        minigameProgressbar = new ProgressBar(0, 100, 1, false, progressbarStyleSmall);

        progressTable = new Table();
        progressLabel = new Label(stepsToday + Values.STEPS_PROGRESS1 + Values.MAX_STEPS + Values.STEPS_PROGRESS2, assetsManager.labelStyleSmall());
        progressBar = new ProgressBar(0, 100, 100, false, progressBarStyle);

        Button.ButtonStyle showerBtnStyle = new Button.ButtonStyle();
        showerBtnStyle.down = assetsManager.getDrawable(Values.SHOWER_BTN);
        showerBtnStyle.up = showerBtnStyle.down;
        showerBtn = new Button(showerBtnStyle);
        showerBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                setUiVisible(false);
                shower.show();
            }
        });

        Table progressBarTable = new Table();
        progressBarTable.add(progressBar).fill().grow().padLeft(50).padRight(50);

        Table progressLabelTabel = new Table();
        progressLabelTabel.add(progressLabel);

        progressTable.stack(progressBarTable, progressLabelTabel).growX();

        Button.ButtonStyle foodBtnStyle = new Button.ButtonStyle();
        foodBtnStyle.down = assetsManager.getDrawable(Values.FOOD_BTN);
        foodBtnStyle.up = foodBtnStyle.down;
        foodBtn = new Button(foodBtnStyle);
        foodBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                foodShopOverlay.show();
            }
        });

        Button.ButtonStyle petBtnStyle = new Button.ButtonStyle();
        petBtnStyle.down = assetsManager.getDrawable(Values.PET_BTN);
        petBtnStyle.up = petBtnStyle.down;
        petBtn = new Button(petBtnStyle);
        petBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
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
                Sounds.click();
                setUiVisible(false);
                showMinigame();
            }
        });

        showerTable = new Table();
        foodTable = new Table();
        petTable = new Table();
        minigameTable = new Table();

        showerTable.add(showerBtn).size(Values.BTN_SIZE).row();
        showerTable.add(showerProgressbar).width(Values.BTN_SIZE).height(Values.BTN_SIZE/7f).padTop(5f);
        foodTable.add(foodBtn).size(Values.BTN_SIZE).row();
        foodTable.add(foodProgressbar).width(Values.BTN_SIZE).height(Values.BTN_SIZE/7f).padTop(5f);
        petTable.add(petBtn).size(Values.BTN_SIZE).row();
        petTable.add(petProgressbar).width(Values.BTN_SIZE).height(Values.BTN_SIZE/7f).padTop(5f);
        minigameTable.add(minigameBtn).size(Values.BTN_SIZE).row();
        minigameTable.add(minigameProgressbar).width(Values.BTN_SIZE).height(Values.BTN_SIZE/7f).padTop(5f);

        uiTableBottom.add(progressTable).growX().colspan(4).padBottom(30).row();
        uiTableBottom.add(showerTable).growX();
        uiTableBottom.add(foodTable).growX();
        uiTableBottom.add(petTable).growX();
        uiTableBottom.add(minigameTable).growX();

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

        shopOverlay = new ShopOverlay(assetsManager, this);
        stage.addActor(shopOverlay.table);

        worldsOverlay = new WorldsOverlay(assetsManager, this);
        stage.addActor(worldsOverlay.table);

        settingsOverlay = new SettingsOverlay(assetsManager);
        stage.addActor(settingsOverlay.table);

        foodShopOverlay = new FoodShopOverlay(assetsManager, this);
        stage.addActor(foodShopOverlay.table);

        HealthSignInOverlay healthOverlay = new HealthSignInOverlay(assetsManager, onHealthSignIn);
        stage.addActor(healthOverlay.table);
        healthOverlay.closeInstantly();

        ConsentOverlay consentOverlay = new ConsentOverlay(assetsManager, healthOverlay);
        stage.addActor(consentOverlay.table);

        if(!DataUtils.getUserData().hasPrivacyConsent())
            consentOverlay.showInstantly();

        uiBar = new UIBar(assetsManager);
        stage.addActor(uiBar.table);
        updateUIBar();
    }

    public void currentSteps(int steps) {
        progressBar.setValue(steps);
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

    private void updateUIBar() {
        UserData data = DataUtils.getUserData();
        uiBar.setCoins(data.getCoins());
        uiBar.setMath(data.getMath());
        uiBar.setLvl(data.getLvl());
    }

    @Override
    public void buy(int item, int cost) {
        UserData data = DataUtils.getUserData();
        data.equipItem(item);
        data.subCoins(cost);
        DataUtils.storeUserData();

        updateUIBar();

        character.equipCloth(item, assetsManager);
        // TODO
    }

    @Override
    public void buyWorld(int item, int cost) {
        UserData data = DataUtils.getUserData();
        data.equipWorld(item);
        data.subCoins(cost);
        DataUtils.storeUserData();

        updateUIBar();

        backgroundTable.background(assetsManager.getBackground(item + Values.BACKGROUND));
        // TODO
    }

    @Override
    public void buyFood(int item, int cost) {
        UserData data = DataUtils.getUserData();
        data.subCoins(cost);
        DataUtils.storeUserData();

        setUiVisible(false);
        food.show(assetsManager.getDrawable(item+Values.FOOD_ITEM));

        updateUIBar();

        // TODO
    }

    @Override
    public void equip(int item) {
        //TODO
    }

    @Override
    public void equipWorld(int item) {
        //TODO
    }

    public void show() {
        //TODO
    }

    @Override
    public void drawUIBottom() {
        stage.draw();
    }

    @Override
    public void drawUITop() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
