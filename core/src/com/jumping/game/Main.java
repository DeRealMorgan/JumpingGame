package com.jumping.game;

import com.badlogic.gdx.Gdx;
import com.jumping.game.assets.AssetsManagerImpl;
import com.jumping.game.character.CharacterScreen;
import com.jumping.game.game.GameScreen;
import com.jumping.game.util.Game;
import com.jumping.game.util.GameState;
import com.jumping.game.util.ScreenName;
import com.jumping.game.util.interfaces.RenderPipeline;
import com.jumping.game.util.interfaces.ScreenManager;

public class Main extends Game implements ScreenManager {
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

		setScreen(ScreenName.CHARACTER_SCREEN);
	}

	public void permissionGranted(int code) {

	}

	@Override
	public void setScreen(ScreenName name) {
		switch (name) {
			case MINIGAME_SCREEN:
				GameScreen gameScreen = new GameScreen(assetsManager);
				setScreen(gameScreen);
				this.renderPipeline = gameScreen.getRenderPipeline();
				break;
			case CHARACTER_SCREEN:
				CharacterScreen characterScreen = new CharacterScreen(assetsManager, this);
				setScreen(characterScreen);
				this.renderPipeline = characterScreen.getRenderPipeline();
				break;
		}

		resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
