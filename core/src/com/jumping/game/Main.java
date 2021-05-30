package com.jumping.game;

import com.badlogic.gdx.Gdx;
import com.jumping.game.assets.AssetsManagerImpl;
import com.jumping.game.character.CharacterScreen;
import com.jumping.game.util.Game;
import com.jumping.game.util.GameState;
import com.jumping.game.util.interfaces.RenderPipeline;

public class Main extends Game {
	private RenderPipeline renderPipeline;
	private GameState gameState;
	private AssetsManagerImpl assetsManager;

	private boolean essentialPermissionsGranted;

	public Main(boolean essentialPermissionsGranted) {
		this.essentialPermissionsGranted = essentialPermissionsGranted;
	}

	@Override
	public void create() {
		this.gameState = GameState.ACTIVE;
		this.assetsManager = new AssetsManagerImpl();

		// todo start with loading screen!
		this.assetsManager.loadAll();


		/*GameScreen gameScreen = new GameScreen(assetsManager);
		setScreen(gameScreen);
		this.renderPipeline = gameScreen.getRenderPipeline();*/

		CharacterScreen characterScreen = new CharacterScreen(assetsManager);
		setScreen(characterScreen);
		this.renderPipeline = characterScreen.getRenderPipeline();
	}

	public void permissionGranted(int code) {

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
		super.resize(width, height);
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
