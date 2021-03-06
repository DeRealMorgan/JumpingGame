package com.healthypetsTUM.game.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.healthypetsTUM.game.assets.AssetsManager;
import com.healthypetsTUM.game.character.Duration;
import com.healthypetsTUM.game.game.elements.BasicTile;
import com.healthypetsTUM.game.game.elements.MathAttachment;
import com.healthypetsTUM.game.game.elements.Tile;
import com.healthypetsTUM.game.game.elements.TileAttachment;
import com.healthypetsTUM.game.game.math.MathImpl;
import com.healthypetsTUM.game.game.physics.PhysicsEngineImpl;
import com.healthypetsTUM.game.game.physics.PhysicsEntity;
import com.healthypetsTUM.game.game.player.Player;
import com.healthypetsTUM.game.game.renderer.RenderPipeline;
import com.healthypetsTUM.game.game.ui.PauseUI;
import com.healthypetsTUM.game.game.ui.UIManagerImpl;
import com.healthypetsTUM.game.util.MathUtils;
import com.healthypetsTUM.game.util.ScreenName;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.ZSprite;
import com.healthypetsTUM.game.util.interfaces.ScreenManager;
import com.healthypetsTUM.game.util.store.DataUtils;
import com.healthypetsTUM.game.util.store.UserData;

import java.util.ArrayList;
import java.util.List;

public class GameManagerImpl implements GameManager {
    private final AssetsManager assetsManager;
    private final ScreenManager screenManager;

    private final RenderPipeline renderPipeline;
    private final PhysicsEngineImpl physicsEngine;

    private final UIManagerImpl uiManager;
    private final MathImpl mathImpl;

    private final Player player;
    private boolean playerJumped;
    private int correctMathCount, score;

    private final List<Tile> tileList;
    private final List<ZSprite> tileSpriteList;
    private final List<PhysicsEntity> tileEntityList;

    private Tile recentlyJumpedTile, topTile;

    private final float tileDistY = 50;
    private final float tileSpawnRate = 2.75f;
    private float difficultyScale = 1f, difficultyScaleInv = 1f;

    private boolean pause, stop;

    private Duration slowUpdate;

    public GameManagerImpl(RenderPipeline renderPipeline, AssetsManager assetsManager,
                           ScreenManager screenManager) {
        assetsManager.clearInputProcessors();
        this.renderPipeline = renderPipeline;
        this.assetsManager = assetsManager;
        this.screenManager = screenManager;

        this.physicsEngine = new PhysicsEngineImpl();

        this.uiManager = new UIManagerImpl(this.renderPipeline.getUIViewport(), this.renderPipeline.getBatch(),
                assetsManager, screenManager, this::onUIPause, this::onUIResume, this::onBackPressed);
        this.renderPipeline.setUiManager(uiManager);

        this.mathImpl = new MathImpl(assetsManager, this::mathCorrect, this::mathWrong, this::onMathShow);
        uiManager.getStage().addActor(mathImpl.table);
        this.mathImpl.closeInstantly();

        this.tileList = new ArrayList<>();
        this.tileSpriteList = new ArrayList<>();
        this.tileEntityList = new ArrayList<>();

        int width = Values.WORLD_WIDTH;
        this.recentlyJumpedTile = new BasicTile(assetsManager, width /2, 0);
        this.topTile = recentlyJumpedTile;

        this.tileList.add(recentlyJumpedTile);
        this.player = new Player(assetsManager, width /2, recentlyJumpedTile.getTop(), uiManager);
        this.player.listenToPlayer(this);
        this.playerJumped = true;
        updateWorld();

        this.slowUpdate = new Duration(Values.SLOW_UPDATE);

        start(); // todo change to wait for click on screen or countdown or so
    }

    /**
     * Starts the game
     */
    private void start() {
        physicsEngine.addEntity(player);
        player.start();
    }

    private void onUIPause() {
        pauseUpdate();
        PauseUI pauseUI = uiManager.getPauseUI();
        pauseUI.show();
    }

    private void onBackPressed() {
        screenManager.setScreen(ScreenName.CHARACTER_SCREEN);

        int earnedCoins = getEarnedCoins();
        UserData data = DataUtils.getUserData();
        data.incPlayAmount();
        data.addCoins(earnedCoins);
        DataUtils.storeUserData();
    }

    private void onUIResume() {
        resumeUpdate();
    }

    @Override
    public void disablePause() {
        uiManager.getGameOverlayUI().disable();
    }

    @Override
    public void enablePause() {
        uiManager.getGameOverlayUI().enable();
    }

    @Override
    public void pauseUpdate() {
        pause = true;
    }

    @Override
    public void resumeUpdate() {
        pause = false;
    }

    @Override
    public void resumeUpdateSlow() {
        resumeUpdate();

        slowUpdate.reset();
        slowUpdate.start();
    }

    @Override
    public void gameOver() {
        stop = true;
        player.gameOver();

        int earnedCoins = getEarnedCoins();
        UserData data = DataUtils.getUserData();
        data.incPlayAmount();
        data.addCoins(earnedCoins);
        DataUtils.storeUserData();

        uiManager.getGameOverUI().updateCoins(earnedCoins);
        uiManager.getGameOverUI().show();
        physicsEngine.stop();
    }

    private int getEarnedCoins() {
        return (score+correctMathCount)/100;
    }

    public void mathCorrect(int timeLeftS) {
        correctMathCount += 1000 + timeLeftS*10;
        UserData data = DataUtils.getUserData();
        data.incMath();
        DataUtils.storeUserData();
        resumeUpdateSlow();
        enablePause();
        uiManager.updateMathScore(getEarnedCoins(), correctMathCount);
    }

    public void mathWrong() {
        enablePause();
        resumeUpdate();
        gameOver();
    }

    public void onMathShow() {
        pauseUpdate();
        disablePause();
    }

    @Override
    public void scoreChanged(int score) {
        this.score = Math.max(this.score, score);
        uiManager.updateScore(getEarnedCoins());
    }

    public void update(float dt) {
        mathImpl.update(dt);
        uiManager.update(dt);
        if(pause || stop) return;
        if(!slowUpdate.addTimeStampDiff()) {
            float percentDone = slowUpdate.getPercentDone();
            dt *= percentDone;
        }

        if(Gdx.app.getType() == Application.ApplicationType.Android)
            player.updateVelocityX(Gdx.input.getAccelerometerX()*difficultyScaleInv);
        else if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
                player.updateVelocityX(Values.DESKTOP_VELOCITY_X);
            else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                player.updateVelocityX(-Values.DESKTOP_VELOCITY_X);
            else
                player.updateVelocityX(0);
        }

        physicsEngine.update(dt*difficultyScale);
        physicsEngine.handlePlayerCollisions(player);
        if(player.getTop() < renderPipeline.getGameCamBottomY()) gameOver();

        if(playerJumped) {
            playerJumped = false;
            clearWorld();
            updateWorld();
            submitWorld();
        }
    }

    private void clearWorld() {
        renderPipeline.removeSprites(tileSpriteList);
        physicsEngine.removeEntities(tileEntityList);
    }

    private void submitWorld() {
        renderPipeline.submitSprites(tileSpriteList);
        physicsEngine.addEntities(tileEntityList);
    }

    private void updateWorld() {
        float bottomY = renderPipeline.getGameCamBottomY();
        for(int i = tileList.size()-1; i >= 0; --i) {
            if(tileList.get(i).getMaxYPos() < bottomY) //todo bug - lowest platform not removed
                tileList.remove(i);
        }

        generatePlatforms();
        collectTiles();
        generateEnemies();
    }

    @Override
    public void jumpedToNextPlatform(PhysicsEntity platform) {
        setRecentlyJumpedTile(platform);
        renderPipeline.moveToTile(platform.getY());
        playerJumped = true;
    }

    private void setRecentlyJumpedTile(PhysicsEntity platform) {
        for(Tile t : tileList) {
            if(t.isEntity(platform)) {
                recentlyJumpedTile = t;
                return;
            }
        }
    }

    /**
     * Generate platforms between topY and y higher,
     * so platforms are ready if player jumps up
     */
    private void generatePlatforms() { // todo
        if(topTile.getTop() - player.getY() >= Values.WORLD_HEIGHT*2) return; // enough platforms created

        final float toY = topTile.getTop() + Values.WORLD_HEIGHT*2;
        final float mathAttachmentProbability = 0.2f;

        // general idea:
        //  1. create a jumpable route so there will not be a dead end
        //  2. fill the left space with tiles and enemies

        List<Tile> tiles = new ArrayList<>();
        Tile lastTile = topTile;
        float tileDistY = Values.TILE_DISTANCE_Y*0.75f + Values.TILE_DISTANCE_Y*Math.min(0.15f, ((float)score)/5_000);
        difficultyScale = Math.max(1f, Math.min(1.2f, 1f + ((float) score)/20_000));
        difficultyScaleInv = 2f - difficultyScale;

        mathImpl.setMathTime(Values.MATH_TIME);

        while(lastTile.getTop() + tileDistY <= toY) { // todo here complicated world creation logic
            Tile t = new BasicTile(assetsManager, MathUtils.getRandomWorldX(BasicTile.TILE_WIDTH),
                    lastTile.getTop() + tileDistY);
            tiles.add(t);
            lastTile = t;

            if(MathUtils.getTrue(mathAttachmentProbability)) {
                MathAttachment attachment = new MathAttachment(assetsManager, mathImpl);
                t.setAttachment(attachment);
            }
        }

        tileList.addAll(tiles);
        topTile = lastTile;
    }

    private void generateEnemies() {

    }

    private void collectTiles() {
        tileSpriteList.clear();
        tileEntityList.clear();

        for(Tile t : tileList) {
            tileSpriteList.add(t.getSprite());
            tileEntityList.add(t);
            if(t.hasAttachment()) {
                TileAttachment attachment = t.getAttachment();
                tileSpriteList.add(attachment.getSprite());
                tileEntityList.add(attachment);
            }
        }
    }

    public void show() {
        renderPipeline.submitSprite(player.getSprite());
        renderPipeline.submitSprites(tileSpriteList);

        physicsEngine.addEntities(tileEntityList); // todo maybe not here?

        renderPipeline.setToTile(recentlyJumpedTile.getY());
    }

    public void hide() {
        renderPipeline.removeSprite(player.getSprite());
        renderPipeline.removeSprites(tileSpriteList);

        physicsEngine.removeEntities(tileEntityList); // todo maybe not?
    }
}
