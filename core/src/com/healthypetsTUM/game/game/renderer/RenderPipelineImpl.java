package com.healthypetsTUM.game.game.renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.ZSprite;
import com.healthypetsTUM.game.util.interfaces.UIManager;

import java.util.*;

public class RenderPipelineImpl implements RenderPipeline {
    private final SpriteBatch batch;

    private final List<ZSprite> spriteList;
    private UIManager uiManager;

    private final Viewport gameViewport, uiViewport;
    private final OrthographicCamera gameCam, uiCam;

    private final Vector3 nextCameraPos;
    private final Interpolation camInterpolation;
    private float interpolAlpha = 1;

    public RenderPipelineImpl() {
        this.batch = new SpriteBatch();
        this.spriteList = new ArrayList<>();

        this.gameCam = new OrthographicCamera();
        //this.gameViewport = new ExtendViewport(Values.MIN_WORLD_WIDTH, Values.MIN_WORLD_HEIGHT, // todo implement
        //        Values.MAX_WORLD_WIDTH, Values.MAX_WORLD_HEIGHT, gameCam);
        this.gameViewport = new ExtendViewport(Values.MIN_WORLD_WIDTH, Values.MIN_WORLD_HEIGHT, this.gameCam);
        this.gameCam.position.set(gameViewport.getWorldWidth()/2, gameViewport.getWorldHeight()/2, 0);

        this.uiCam = new OrthographicCamera();
        this.uiViewport = new ExtendViewport(Values.CHARACTER_WORLD_WIDTH, Values.CHARACTER_WORLD_HEIGHT, this.uiCam);
        this.uiCam.position.set(uiViewport.getWorldWidth()/2, uiViewport.getWorldHeight()/2, 0);

        this.nextCameraPos = new Vector3(gameCam.position.x, gameCam.position.y, 0);
        this.camInterpolation = Interpolation.linear;

        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void update(float dt) {
        if(interpolAlpha < 1) {
            interpolAlpha += Values.CAM_INTERPOLATION_STEP * dt;
            gameCam.position.interpolate(nextCameraPos, interpolAlpha, camInterpolation);
        }

        gameCam.update();
        uiCam.update();

        List<ZSprite> removeList = new ArrayList<>();
        for(ZSprite sprite : spriteList) {
            if(sprite.isRemove()) removeList.add(sprite);
        }
        spriteList.removeAll(removeList);
    }

    public void render(float dt) {
        Gdx.gl.glClearColor(0.75f, 0.75f, 0.75f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        uiViewport.apply();
        batch.setProjectionMatrix(uiCam.combined);
        uiManager.drawUIBottom();

        gameViewport.apply();
        batch.setProjectionMatrix(gameCam.combined);
        batch.begin();
        for(ZSprite s : spriteList) s.draw(batch);
        batch.end();

        uiViewport.apply();
        batch.setProjectionMatrix(uiCam.combined);
        uiManager.drawUITop();
    }

    public void resize(int width, int height) {
        gameViewport.update(width, height, true);
        uiViewport.update(width, height);
    }

    @Override
    public void submitSprites(Collection<ZSprite> sprites) {
        spriteList.addAll(sprites);
        sort();
    }

    @Override
    public void submitSprite(ZSprite sprite) {
        spriteList.add(sprite);
        sort();
    }

    private void sort() {
        Collections.sort(spriteList, (z1, z2) -> {
            return Integer.compare(z1.getZ(), z2.getZ());
        });
    }

    @Override
    public void removeSprites(Collection<ZSprite> sprites) {
        spriteList.removeAll(sprites);
    }

    @Override
    public void removeSprite(ZSprite sprite) {
        spriteList.remove(sprite);
    }

    @Override
    public void setToTile(float tileY) {
        gameCam.position.y = gameViewport.getWorldHeight()/2 + tileY - Values.SPACE_BELOW_TILE_Y;
    }

    @Override
    public void moveToTile(float tileY) { //todo smooth move
        nextCameraPos.set(gameCam.position.x, gameViewport.getWorldHeight()/2 + tileY - Values.SPACE_BELOW_TILE_Y, 0);
        interpolAlpha = 0;
    }

    @Override
    public void follow(float y) {
        // todo smooth follow
    }

    @Override
    public float getGameCamTopY() {
        return gameCam.position.y + gameCam.viewportHeight/2;
    }

    @Override
    public float getGameCamBottomY() {
        return gameCam.position.y - gameViewport.getWorldHeight()/2;
    }

    @Override
    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    @Override
    public Viewport getUIViewport() {
        return uiViewport;
    }

    @Override
    public SpriteBatch getBatch() {
        return batch;
    }

    public void dispose() {
        batch.dispose();
    }
}
