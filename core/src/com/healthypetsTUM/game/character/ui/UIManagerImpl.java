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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private Drawable progressbarRed, progressbarGreen, progressbarGreenSmall, progressbarEmpty;

    private ShopOverlay shopOverlay;
    private WorldsOverlay worldsOverlay;
    private SettingsOverlay settingsOverlay;
    private FoodShopOverlay foodShopOverlay;
    private TreatsOverlay treatsOverlay;
    private UIBar uiBar;

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
        progressbarGreenSmall = assetsManager.get9Drawable(Values.PROGRESSBAR_FRONT_GREEN);
        progressbarEmpty = assetsManager.getDrawable(Values.PROGRESSBAR_FRONT_EMPTY);

        progressbarRed.setMinHeight(minH);
        progressbarGreen.setMinHeight(minH);
        progressbarRed.setMinWidth(0);
        progressbarGreen.setMinWidth(0);

        progressbarGreenSmall.setMinWidth(0);
        progressbarGreenSmall.setMinHeight(30);

        progressbarEmpty.setMinHeight(0);
        progressbarEmpty.setMinWidth(0);

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = assetsManager.get9Drawable(Values.PROGRESSBAR_BACK);
        progressBarStyle.background.setMinHeight(minH);
        progressBarStyle.background.setMinWidth(0);
        progressBarStyle.knobBefore = progressbarEmpty;

        ProgressBar.ProgressBarStyle progressbarStyleSmall1 = new ProgressBar.ProgressBarStyle();
        progressbarStyleSmall1.background = assetsManager.get9Drawable(Values.PROGRESSBAR_BACK);
        progressbarStyleSmall1.background.setMinHeight(30);
        progressbarStyleSmall1.knobBefore = progressbarEmpty;
        progressbarStyleSmall1.knobBefore.setMinHeight(30);
        progressbarStyleSmall1.knobBefore.setMinWidth(0);

        ProgressBar.ProgressBarStyle progressbarStyleSmall2 = new ProgressBar.ProgressBarStyle();
        progressbarStyleSmall2.background = assetsManager.get9Drawable(Values.PROGRESSBAR_BACK);
        progressbarStyleSmall2.background.setMinHeight(30);
        progressbarStyleSmall2.knobBefore = progressbarEmpty;
        progressbarStyleSmall2.knobBefore.setMinHeight(30);
        progressbarStyleSmall2.knobBefore.setMinWidth(0);

        ProgressBar.ProgressBarStyle progressbarStyleSmall3 = new ProgressBar.ProgressBarStyle();
        progressbarStyleSmall3.background = assetsManager.get9Drawable(Values.PROGRESSBAR_BACK);
        progressbarStyleSmall3.background.setMinHeight(30);
        progressbarStyleSmall3.knobBefore = progressbarEmpty;
        progressbarStyleSmall3.knobBefore.setMinHeight(30);
        progressbarStyleSmall3.knobBefore.setMinWidth(0);

        ProgressBar.ProgressBarStyle progressbarStyleSmall4 = new ProgressBar.ProgressBarStyle();
        progressbarStyleSmall4.background = assetsManager.get9Drawable(Values.PROGRESSBAR_BACK);
        progressbarStyleSmall4.background.setMinHeight(30);
        progressbarStyleSmall4.knobBefore = progressbarEmpty;
        progressbarStyleSmall4.knobBefore.setMinHeight(30);
        progressbarStyleSmall4.knobBefore.setMinWidth(0);

        showerProgressbar = new ProgressBar(0, Values.MAX_SHOWER_AMOUNT, 1, false, progressbarStyleSmall1);
        foodProgressbar = new ProgressBar(0, Values.MAX_FOOD_AMOUNT, 1, false, progressbarStyleSmall2);
        petProgressbar = new ProgressBar(0, Values.MAX_PET_AMOUNT, 1, false, progressbarStyleSmall3);
        minigameProgressbar = new ProgressBar(0, Values.MAX_PLAY_AMOUNT, 1, false, progressbarStyleSmall4);

        progressTable = new Table();
        progressLabel = new Label(DataUtils.getUserData().getLastStepCount() + Values.STEPS_PROGRESS1 + Values.MAX_STEPS + Values.STEPS_PROGRESS2, assetsManager.labelStyleSmall());
        progressBar = new ProgressBar(0, 10000, 1, false, progressBarStyle);
        currentSteps(DataUtils.getUserData().getLastStepCount());
        setBars();

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

        if(!DataUtils.getUserData().hasPrivacyConsent()) {
            ConsentOverlay consentOverlay = new ConsentOverlay(assetsManager, healthOverlay);
            stage.addActor(consentOverlay.table);
            consentOverlay.showInstantly();
        } // else
        if(DataUtils.getUserData().isTreatFound() || true) {
            List<Integer> unlockedItems = DataUtils.getUserData().getUnlockedItems();

            List<Integer> list = new ArrayList<>();
            for(int i = 0; i < Values.ITEM_COUNT; ++i) {
                if(unlockedItems.contains(i)) continue;
                list.add(i);
            }

            int item = list.get(new Random().nextInt(list.size()));

            DataUtils.getUserData().setTreatFound(false);
            DataUtils.storeUserData();

            treatsOverlay = new TreatsOverlay(assetsManager, item, this::onTreatSuccess);
            stage.addActor(treatsOverlay.table);
            stage.addActor(treatsOverlay.getMath().table);
            treatsOverlay.showInstantly();
        }

        uiBar = new UIBar(assetsManager);
        stage.addActor(uiBar.table);
        updateUIBar();
    }

    private void onTreatSuccess(int item) {
        shopOverlay.itemCollected(item);
        DataUtils.getUserData().unlockItem(item);
        DataUtils.storeUserData();
    }

    public void currentSteps(int steps) {
        progressBar.setValue(steps);
        if(progressBar.getPercent() < 0.01f)
            progressBar.getStyle().knobBefore = progressbarEmpty;
        else if(steps < Values.MAX_STEPS/2)
            progressBar.getStyle().knobBefore = progressbarRed;
        else
            progressBar.getStyle().knobBefore = progressbarGreen;

        progressLabel.setText(steps + Values.STEPS_PROGRESS1 + Values.MAX_STEPS + Values.STEPS_PROGRESS2);
    }

    private void setBars() {
        UserData data = DataUtils.getUserData();
        showerProgressbar.setValue(data.getShowerAmount());
        if(data.getShowerAmount() > 0)
            showerProgressbar.getStyle().knobBefore = progressbarGreenSmall;

        foodProgressbar.setValue(data.getFoodAmount());
        if(data.getFoodAmount() > 0)
            foodProgressbar.getStyle().knobBefore = progressbarGreenSmall;

        petProgressbar.setValue(data.getPetAmount());
        if(data.getPetAmount() > 0)
            petProgressbar.getStyle().knobBefore = progressbarGreenSmall;

        minigameProgressbar.setValue(data.getPlayAmount());
        if(data.getPlayAmount() > 0)
            minigameProgressbar.getStyle().knobBefore = progressbarGreenSmall;
    }

    private void showerDone() {
        setUiVisible(true);

        DataUtils.getUserData().incShowerAmount();
        DataUtils.storeUserData();
        setBars();
    }

    private void pettingDone() {
        setUiVisible(true);

        DataUtils.getUserData().incPetAmount();
        DataUtils.storeUserData();
        setBars();
    }

    private void foodDone() {
        setUiVisible(true);

        DataUtils.getUserData().incFoodAmount();
        DataUtils.storeUserData();
        setBars();
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

        if(treatsOverlay != null)
            treatsOverlay.update(dt);
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
        data.subCoins(cost);
        data.addBoughtItem(item);
        DataUtils.storeUserData();

        updateUIBar();
    }

    @Override
    public void equipItem(int item) {
        UserData data = DataUtils.getUserData();
        data.equipItem(item);
        DataUtils.storeUserData();

        character.equipCloth(item, assetsManager);
    }

    @Override
    public void buyWorld(int item, int cost) {
        UserData data = DataUtils.getUserData();
        data.subCoins(cost);
        data.addBoughtWorld(item);
        DataUtils.storeUserData();

        updateUIBar();
    }

    @Override
    public void equipWorld(int item) {
        UserData data = DataUtils.getUserData();
        data.equipWorld(item);
        DataUtils.storeUserData();
        backgroundTable.background(assetsManager.getBackground(item + Values.BACKGROUND));
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
