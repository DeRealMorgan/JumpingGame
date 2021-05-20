package com.jumping.game;

import com.badlogic.gdx.Gdx;
import com.jumping.game.game.GameScreen;
import com.jumping.game.game.assets.AssetsManagerImpl;
import com.jumping.game.renderer.RenderPipelineImpl;
import com.jumping.game.util.Game;
import com.jumping.game.util.GameState;

public class Main extends Game {
	private RenderPipelineImpl renderPipeline;
	private GameState gameState;
	private AssetsManagerImpl assetsManager;

	@Override
	public void create() {
		this.gameState = GameState.ACTIVE;

		this.renderPipeline = new RenderPipelineImpl();
		this.assetsManager = new AssetsManagerImpl();

		// todo start with loading screen!
		this.assetsManager.loadAll();

		GameScreen gameScreen = new GameScreen(renderPipeline, assetsManager);
		setScreen(gameScreen);
	}

	@Override
	public void render() {
		float dt = Gdx.graphics.getDeltaTime();

		if (gameState != GameState.PAUSED) {
			super.update(dt);
			renderPipeline.render(dt);
		}
	}

	@Override
	public void resize(int width, int height) {
		renderPipeline.resize(width, height);
	}

	@Override
	public void pause() {
		gameState = GameState.PAUSED;
		super.pauseScreen();
	}

	@Override
	public void resume() {
		gameState = GameState.ACTIVE;
		super.resumeScreen();
	}

	@Override
	public void dispose() {
		renderPipeline.dispose();
		super.disposeScreen();
	}
}
