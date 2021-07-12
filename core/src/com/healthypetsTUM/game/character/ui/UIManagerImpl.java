package com.healthypetsTUM.game.character.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.character.ui.overlay.*;
import com.healthypetsTUM.game.game.math.MathImpl;
import com.healthypetsTUM.game.util.MathUtils;
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

    private Table backgroundTable;
    private Table uiTableLeft;
    private Table uiTableRight;
    private Table uiTableBottom;
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

    private MathImpl math;

    private final static float BOTTOM_PADDING = 40, TOP_PADDING = 40, SIDE_PADDING = 40;

    private Image[] pooImg;
    private Label hungryLabel;
    private Label boredLabel;
    private Table statusTable;

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

        backgroundTable.background(assetsManager.getBackground(DataUtils.getUserData().getEquipedWorld() +
                Values.BACKGROUND));
        stage.addActor(backgroundTable);

        pooImg = new Image[6];
        for(int i = 0; i < 3; ++i) {
            pooImg[i] = new Image(assetsManager.getDrawable(Values.POO_ICON));
            pooImg[i].setTouchable(Touchable.disabled);
            pooImg[i].setVisible(false);
            pooImg[i].setScaling(Scaling.fit);
            pooImg[i].setSize(350, 350);
            pooImg[i].setPosition(MathUtils.getRandomX(0, Values.CHARACTER_WORLD_WIDTH-200), 250);

            stage.addActor(pooImg[i]);
        }

        character = new Character(assetsManager, stage);

        for(int i = 3; i < pooImg.length; ++i) {
            pooImg[i] = new Image(assetsManager.getDrawable(Values.POO_ICON));
            pooImg[i].setTouchable(Touchable.disabled);
            pooImg[i].setVisible(false);
            pooImg[i].setScaling(Scaling.fit);
            pooImg[i].setSize(300, 300);
            pooImg[i].setPosition(MathUtils.getRandomX(0, Values.CHARACTER_WORLD_WIDTH-200), 250);

            stage.addActor(pooImg[i]);
        }

        hungryLabel = new Label("Bitte füttere mich...", assetsManager.labelStyleSmall());
        boredLabel = new Label("Bitte spiele mit mir...", assetsManager.labelStyleSmall());

        statusTable = new Table();
        statusTable.add(hungryLabel).center().padBottom(Values.PADDING_SMALL).row();
        statusTable.add(boredLabel).center().row();
        statusTable.background(assetsManager.getDrawable(Values.MENU_BACK));
        Table statTable = new Table();
        statTable.setFillParent(true);
        statTable.add(statusTable).center().expandY().padTop(200);
        stage.addActor(statTable);

        Table contentTable = new Table();
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
        Button shopBtn = new Button(shopBtnStyle);
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
        Button worldsBtn = new Button(worldsBtnStyle);
        worldsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                worldsOverlay.show();
            }
        });

        uiTableLeft.add(shopBtn).size(Values.BTN_SIZE).padBottom(20f).row();
        uiTableLeft.add(worldsBtn).size(Values.BTN_SIZE).row();

        Button.ButtonStyle mathBtnStyle = new Button.ButtonStyle();
        mathBtnStyle.down = assetsManager.getDrawable(Values.ACHIEVEMENTS_BTN);
        mathBtnStyle.up = mathBtnStyle.down;
        Button mathBtn = new Button(mathBtnStyle);
        mathBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                
                math.useNext(true);
                math.setOnCorrectMath(arg -> {
                    DataUtils.getUserData().incMath();
                    DataUtils.getUserData().addCoins(10);
                    DataUtils.storeUserData();
                    updateUIBar();
                    worldsOverlay.levelChanged(DataUtils.getUserData().getLvl());
                });
                math.showMathExercise(null);
            }
        });

        Button.ButtonStyle settingsBtnStyle = new Button.ButtonStyle();
        settingsBtnStyle.down = assetsManager.getDrawable(Values.SETTINGS_BTN);
        settingsBtnStyle.up = settingsBtnStyle.down;
        Button settingsBtn = new Button(settingsBtnStyle);
        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Sounds.click();
                settingsOverlay.show();
            }
        });

        uiTableRight.add(settingsBtn).size(Values.BTN_SIZE).padBottom(20f).row();
        uiTableRight.add(mathBtn).size(Values.BTN_SIZE).row();

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

        Table progressTable = new Table();
        progressLabel = new Label(DataUtils.getUserData().getLastStepCount() + Values.STEPS_PROGRESS1 + Values.MAX_STEPS + Values.STEPS_PROGRESS2, assetsManager.labelStyleSmall());
        progressBar = new ProgressBar(0, Values.MAX_STEPS, 1, false, progressBarStyle);

        Button.ButtonStyle showerBtnStyle = new Button.ButtonStyle();
        showerBtnStyle.down = assetsManager.getDrawable(Values.SHOWER_BTN);
        showerBtnStyle.up = showerBtnStyle.down;
        showerBtnStyle.disabled = assetsManager.getDrawable(Values.SHOWER_BTN_DISABLED);
        showerBtn = new Button(showerBtnStyle);
        showerBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(showerBtn.isDisabled()) return;
                Sounds.click();

                math.useNext(false);
                math.setOnCorrectMath(arg -> {
                    setUiVisible(false);
                    shower.show();

                    DataUtils.getUserData().incMath();
                    DataUtils.storeUserData();
                    updateUIBar();
                    worldsOverlay.levelChanged(DataUtils.getUserData().getLvl());
                });
                math.showMathExercise(null);
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
        foodBtnStyle.disabled = assetsManager.getDrawable(Values.FOOD_BTN_DISABLED);
        foodBtn = new Button(foodBtnStyle);
        foodBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(foodBtn.isDisabled()) return;
                Sounds.click();

                math.useNext(false);
                math.setOnCorrectMath(arg -> {
                    foodShopOverlay.show();

                    DataUtils.getUserData().incMath();
                    DataUtils.storeUserData();
                    updateUIBar();
                    worldsOverlay.levelChanged(DataUtils.getUserData().getLvl());
                });

                math.showMathExercise(null);
            }
        });

        Button.ButtonStyle petBtnStyle = new Button.ButtonStyle();
        petBtnStyle.down = assetsManager.getDrawable(Values.PET_BTN);
        petBtnStyle.up = petBtnStyle.down;
        petBtnStyle.disabled = assetsManager.getDrawable(Values.PET_BTN_DISABLED);
        petBtn = new Button(petBtnStyle);
        petBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(petBtn.isDisabled()) return;
                Sounds.click();

                math.useNext(false);
                math.setOnCorrectMath(arg -> {
                    setUiVisible(false);
                    hand.show();

                    DataUtils.getUserData().incMath();
                    DataUtils.storeUserData();
                    updateUIBar();
                    worldsOverlay.levelChanged(DataUtils.getUserData().getLvl());
                });
                math.showMathExercise(null);
            }
        });

        Button.ButtonStyle minigameBtnStyle = new Button.ButtonStyle();
        minigameBtnStyle.down = assetsManager.getDrawable(Values.MINIGAME_BTN);
        minigameBtnStyle.up = minigameBtnStyle.down;
        minigameBtnStyle.disabled = assetsManager.getDrawable(Values.MINIGAME_BTN_DISABLED);
        minigameBtn = new Button(minigameBtnStyle);
        minigameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(minigameBtn.isDisabled()) return;
                Sounds.click();
                setUiVisible(false);
                showMinigame();
            }
        });

        Table showerTable = new Table();
        Table foodTable = new Table();
        Table petTable = new Table();
        Table minigameTable = new Table();

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
            TutorialOverlay consentOverlay = new TutorialOverlay(assetsManager, healthOverlay);
            stage.addActor(consentOverlay.table);
            consentOverlay.showInstantly();
        }
        if(DataUtils.getUserData().isTreatFound()) {
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

        currentSteps(DataUtils.getUserData().getLastStepCount());
        setBars();

        math = new MathImpl(assetsManager, arg -> {}, () -> {}, () -> {});
        math.useNext(true);
        stage.addActor(math.table);
    }

    private void onTreatSuccess(int item) {
        shopOverlay.itemCollected(item);
        DataUtils.getUserData().unlockItem(item);
        DataUtils.storeUserData();
    }

    public void currentSteps(int steps) {
        boolean disableBtns = steps < 4000;

        showerBtn.setDisabled(disableBtns);
        petBtn.setDisabled(disableBtns);
        foodBtn.setDisabled(disableBtns);
        minigameBtn.setDisabled(disableBtns);

        progressBar.setValue(steps);
        if(progressBar.getPercent() < 0.01f) {
            progressBar.getStyle().knobBefore = progressbarEmpty;
        } else if(steps < Values.MAX_STEPS/2) {
            progressBar.getStyle().knobBefore = progressbarRed;
        } else {
            progressBar.getStyle().knobBefore = progressbarGreen;
        }

        progressLabel.setText(steps + Values.STEPS_PROGRESS1 + Values.MAX_STEPS + Values.STEPS_PROGRESS2);
    }

    private void setBars() {
        UserData data = DataUtils.getUserData();
        showerProgressbar.setValue(data.getShowerAmount());
        if(data.getShowerAmount() > 0) {
            showerProgressbar.getStyle().knobBefore = progressbarGreenSmall;
            setNotDirty();
        } else
            setDirty();

        foodProgressbar.setValue(data.getFoodAmount());
        if(data.getFoodAmount() > 0) {
            foodProgressbar.getStyle().knobBefore = progressbarGreenSmall;
            setNotHungry();
        } else
            setHungry();

        petProgressbar.setValue(data.getPetAmount());
        if(data.getPetAmount() > 0) {
            petProgressbar.getStyle().knobBefore = progressbarGreenSmall;
            character.setHappy();
        } else
            character.setSad();

        minigameProgressbar.setValue(data.getPlayAmount());
        if(data.getPlayAmount() > 0) {
            minigameProgressbar.getStyle().knobBefore = progressbarGreenSmall;
            setNotBored();
        } else
            setBored();
    }

    private void setNotDirty() {
        for(Image i : pooImg) i.setVisible(false);
    }

    private void setDirty() {
        for(Image i : pooImg) i.setVisible(true);
    }

    private void setNotHungry() {
        hungryLabel.setVisible(false);
        hungryLabel.remove();

        if(!boredLabel.isVisible())
            statusTable.setVisible(false);

    }

    private void setHungry() {
        hungryLabel.setVisible(true);
    }

    private void setBored() {
        boredLabel.setVisible(true);
    }

    private void setNotBored() {
        boredLabel.setVisible(false);
        boredLabel.remove();

        if(!hungryLabel.isVisible())
            statusTable.setVisible(false);
    }

    private void showerDone() {
        setUiVisible(true);

        DataUtils.getUserData().incShowerAmount();
        DataUtils.storeUserData();
        setBars();

        shower.reset();
    }

    private void pettingDone() {
        setUiVisible(true);

        DataUtils.getUserData().incPetAmount();
        DataUtils.storeUserData();
        setBars();

        hand.reset();
    }

    private void foodDone() {
        setUiVisible(true);

        DataUtils.getUserData().incFoodAmount();
        DataUtils.storeUserData();
        setBars();

        food.reset();
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

        math.update(dt);
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
        uiBar.setLvl(data.getLvl(), data.getMath());
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
    public void unequipItem(int item) {
        UserData data = DataUtils.getUserData();
        data.unequipItem(item);
        DataUtils.storeUserData();

        character.unequipCloth(item);
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
        if(item == -1) backgroundTable.background(new SpriteDrawable());

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
