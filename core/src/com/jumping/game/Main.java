package com.jumping.game;

import com.badlogic.gdx.Gdx;
import com.jumping.game.assets.AssetsManagerImpl;
import com.jumping.game.character.CharacterScreen;
import com.jumping.game.game.GameScreen;
import com.jumping.game.util.*;
import com.jumping.game.util.interfaces.GoogleFit;
import com.jumping.game.util.interfaces.RenderPipeline;
import com.jumping.game.util.interfaces.ScreenManager;
import com.jumping.game.util.interfaces.StoreProvider;
import com.jumping.game.util.store.DataUtils;
import com.jumping.game.util.store.UserData;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main extends Game implements ScreenManager {
	private RenderPipeline renderPipeline;
	private GameState gameState;
	private AssetsManagerImpl assetsManager;

	private boolean essentialPermissionsGranted;
	private GoogleFit googleFit;
	private StoreProvider storeProvider;

	private ScreenName currentScreen;

	private ReentrantLock lock = new ReentrantLock();
	private Condition waitCondition = lock.newCondition();

	private Thread stepRefreshThread;
	private long nextStepRefresh;

	private Musics musics;

	public Main(boolean essentialPermissionsGranted, GoogleFit googleFit, StoreProvider storeProvider) {
		this.essentialPermissionsGranted = essentialPermissionsGranted;
		this.googleFit = googleFit;
		this.storeProvider = storeProvider;
	}

	@Override
	public void create() {
		DataUtils.init(storeProvider);
		DataUtils.firstStart();
		DataUtils.getUserData();
		DataUtils.storeUserData();

		this.gameState = GameState.ACTIVE;
		this.assetsManager = new AssetsManagerImpl();

		// todo start with loading screen!
		this.assetsManager.loadAll();

		setScreen(ScreenName.CHARACTER_SCREEN);

		startGoogleFitRefresh();
	}

	public void permissionGranted(int code) {

	}

	private void startGoogleFitRefresh() {
		stepRefreshThread = new Thread(this::runGoogleFitRefresh);
		stepRefreshThread.start();

	}

	private void runGoogleFitRefresh() {
		while(gameState == GameState.ACTIVE) {
			if (nextStepRefresh <= System.currentTimeMillis()) {
				int steps = googleFit.getStepCountToday();
				UserData data = DataUtils.getUserData();
				data.setLastStepCount(steps);
				data.setLastStepStamp(System.currentTimeMillis());
				if (currentScreen == ScreenName.CHARACTER_SCREEN) {
					((CharacterScreen) getScreen()).currentSteps(steps);
				}

				nextStepRefresh = System.currentTimeMillis() + Values.REFRESH_STEPS_INTERVAL;
			} else {
				try {
					lock.lockInterruptibly();
					waitCondition.await(nextStepRefresh - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					return;
				} finally {
					if (lock.isHeldByCurrentThread()) lock.unlock();
				}
			}
		}
	}

	@Override
	public void setScreen(ScreenName name) {
		currentScreen = name;
		switch (name) {
			case MINIGAME_SCREEN:
				GameScreen gameScreen = new GameScreen(assetsManager, this);
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

		stepRefreshThread.interrupt();
		System.out.println("lol1");
	}

	@Override
	public void resume() {
		System.out.println("Lol");
		gameState = GameState.ACTIVE;
		super.resumeScreen();

		startGoogleFitRefresh();
	}

	@Override
	public void dispose() {
		DataUtils.getUserData().setRunning(false);
		try {
			DataUtils.storeUserData().join();
		} catch (InterruptedException ignored) {}

		renderPipeline.dispose();
		super.disposeScreen();
	}
}
