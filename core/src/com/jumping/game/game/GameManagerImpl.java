package com.jumping.game.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.jumping.game.assets.AssetsManager;
import com.jumping.game.character.Duration;
import com.jumping.game.game.elements.BasicTile;
import com.jumping.game.game.elements.MathAttachment;
import com.jumping.game.game.elements.Tile;
import com.jumping.game.game.elements.TileAttachment;
import com.jumping.game.game.math.MathControllerImpl;
import com.jumping.game.game.physics.PhysicsEngineImpl;
import com.jumping.game.game.physics.PhysicsEntity;
import com.jumping.game.game.player.Player;
import com.jumping.game.game.renderer.RenderPipeline;
import com.jumping.game.game.ui.UIManagerImpl;
import com.jumping.game.util.MathUtils;
import com.jumping.game.util.Values;
import com.jumping.game.util.ZSprite;
import com.jumping.game.util.interfaces.ScreenManager;

import java.util.ArrayList;
import java.util.List;

public class GameManagerImpl implements GameManager {
    private final AssetsManager assetsManager;

    private final RenderPipeline renderPipeline;
    private final PhysicsEngineImpl physicsEngine;

    private final UIManagerImpl uiManager;
    private final MathControllerImpl mathController;

    private final int width = Values.WORLD_WIDTH;
    private final int height = Values.WORLD_HEIGHT;

    private final Player player;
    private boolean playerJumped;
    private int correctMathCount, score;

    private final List<Tile> tileList;
    private final List<ZSprite> tileSpriteList;
    private final List<PhysicsEntity> tileEntityList;

    private Tile recentlyJumpedTile, topTile;

    private float tileDistY = 50;
    private float tileSpawnRate = 2.75f;

    private boolean pause, stop;

    private Duration slowUpdate;

    public GameManagerImpl(RenderPipeline renderPipeline, AssetsManager assetsManager, ScreenManager screenManager) {
        this.renderPipeline = renderPipeline;
        this.assetsManager = assetsManager;

        this.physicsEngine = new PhysicsEngineImpl();

        this.uiManager = new UIManagerImpl(this.renderPipeline.getUIViewport(), this.renderPipeline.getBatch(),
                assetsManager, screenManager);
        this.renderPipeline.setUiManager(uiManager);

        this.mathController = new MathControllerImpl(this, uiManager, assetsManager, this::mathCorrect);

        this.tileList = new ArrayList<>();
        this.tileSpriteList = new ArrayList<>();
        this.tileEntityList = new ArrayList<>();

        this.recentlyJumpedTile = new BasicTile(assetsManager, width/2, 0);
        this.topTile = recentlyJumpedTile;

        this.tileList.add(recentlyJumpedTile);
        this.player = new Player(assetsManager, width/2, recentlyJumpedTile.getTop(), uiManager);
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
        uiManager.getGameOverUI().show();
        physicsEngine.stop();
    }

    public void mathCorrect() {
        ++correctMathCount;
        uiManager.getGameOverUI().setMathScore(correctMathCount);
    }

    @Override
    public void scoreChanged(int score) {
        this.score = Math.max(this.score, score);
        uiManager.getGameOverUI().setScore(this.score);
    }

    public void update(float dt) {
        uiManager.update(dt);
        if(pause || stop) return;
        if(!slowUpdate.addTimeStampDiff()) {
            float percentDone = slowUpdate.getPercentDone();
            dt *= percentDone;
        }

        if(Gdx.app.getType() == Application.ApplicationType.Android)
            player.updateVelocityX(Gdx.input.getAccelerometerX());
        else if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
            if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
                player.updateVelocityX(Values.DESKTOP_VELOCITY_X);
            else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                player.updateVelocityX(-Values.DESKTOP_VELOCITY_X);
            else
                player.updateVelocityX(0);
        }

        physicsEngine.update(dt);
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
        sort();
        float bottomY = renderPipeline.getGameCamBottomY();
        for(int i = tileList.size()-1; i >= 0; --i) {
            if(tileList.get(i).getMaxYPos() < bottomY) {
                tileList.remove(i); // todo maybe reuse?
                break; // all tiles below screen are removed
            }
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
        float tileDistY = Values.TILE_DISTANCE_Y*0.75f; // todo abhängig von sprunghöhe

        while(lastTile.getTop() + tileDistY <= toY) { // todo here complicated world creation logic
            Tile t = new BasicTile(assetsManager, MathUtils.getRandomWorldX(BasicTile.TILE_WIDTH), lastTile.getTop() + tileDistY);
            tiles.add(t);
            lastTile = t;

            if(MathUtils.getTrue(mathAttachmentProbability)) { // TODO REMOVE TRUE
                MathAttachment attachment = new MathAttachment(assetsManager, mathController);
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

    /**
     * Sorting list by tile max y. Largest y first.
     */
    private void sort() {
        tileList.sort((t1, t2) -> {
            float dif = t1.getMaxYPos() - t2.getMaxYPos();
            if(dif < 0) return -1;
            if(dif > 0) return 1;
            return 0;
        });
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
