package com.healthypetsTUM.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.healthypetsTUM.game.assets.AssetsManagerImpl;
import com.healthypetsTUM.game.character.CharacterScreen;
import com.healthypetsTUM.game.game.GameScreen;
import com.healthypetsTUM.game.util.Game;
import com.healthypetsTUM.game.util.GameState;
import com.healthypetsTUM.game.util.ScreenName;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.interfaces.*;
import com.healthypetsTUM.game.util.store.DataUtils;
import com.healthypetsTUM.game.util.store.UserData;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main extends Game implements ScreenManager {
	private RenderPipeline renderPipeline;
	private GameState gameState;
	private AssetsManagerImpl assetsManager;

	private final GoogleFit googleFit;
	private final StoreProvider storeProvider;
	private final StatsProvider statsProvider;

	private ScreenName currentScreen;

	private final ReentrantLock lock = new ReentrantLock();
	private final Condition waitCondition = lock.newCondition();

	private Thread stepRefreshThread;
	private long nextStepRefresh;

	public Main(boolean essentialPermissionsGranted, GoogleFit googleFit, StoreProvider storeProvider,
				StatsProvider statsProvider) {
		this.googleFit = googleFit;
		this.storeProvider = storeProvider;
		this.statsProvider = statsProvider;
	}

	@Override
	public void create() {
		DataUtils.init(storeProvider);

		if(storeProvider.load(Values.FIRST_START).equals(""))  {
			storeProvider.store(Values.FIRST_START, Values.FIRST_START);
			DataUtils.firstStart();
		}

		this.gameState = GameState.ACTIVE;
		this.assetsManager = new AssetsManagerImpl();

		// todo start with loading screen!
		this.assetsManager.loadAll();

		setStats();

		setScreen(ScreenName.CHARACTER_SCREEN);

		startGoogleFitRefresh();
	}

	private void setStats() {
		statsProvider.setStats();
	}

	public void permissionGranted(int code) {
		if(code == Values.GOOGLE_FIT_REQUEST_CODE) {
			googleFit.getStepCountToday(steps -> {
				if(steps != -1) {
					UserData data = DataUtils.getUserData();
					if(Gdx.app.getType() == Application.ApplicationType.Desktop) {
						data.setLastStepCount(5500);
					} else {
						data.setLastStepCount(steps);
					}
					data.setLastStepStamp(System.currentTimeMillis());
					DataUtils.storeUserData();
					if (currentScreen == ScreenName.CHARACTER_SCREEN) {
						((CharacterScreen) getScreen()).currentSteps(steps);
					}
				}
			});
		}
	}

	private void startGoogleFitRefresh() {
		stepRefreshThread = new Thread(this::runGoogleFitRefresh);
		stepRefreshThread.start();

	}

	private void runGoogleFitRefresh() {
		while(gameState == GameState.ACTIVE) {
			if (nextStepRefresh <= System.currentTimeMillis()) {
				if(statsProvider.isApiTooLow()) {
					UserData data = DataUtils.getUserData();
					data.setLastStepCount(5500);
					data.setLastStepStamp(System.currentTimeMillis());
					DataUtils.storeUserData();
					if (currentScreen == ScreenName.CHARACTER_SCREEN) {
						((CharacterScreen) getScreen()).currentSteps(5500);
					}
				} else {
					googleFit.getStepCountToday(steps -> {
						if (steps != -1) {
							UserData data = DataUtils.getUserData();
							if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
								data.setLastStepCount(5500);
							} else {
								data.setLastStepCount(steps);
							}
							data.setLastStepStamp(System.currentTimeMillis());
							DataUtils.storeUserData();
							if (currentScreen == ScreenName.CHARACTER_SCREEN) {
								((CharacterScreen) getScreen()).currentSteps(steps);
							}
						}
					});
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
		assetsManager.clearInputProcessors();
		currentScreen = name;
		switch (name) {
			case MINIGAME_SCREEN:
				GameScreen gameScreen = new GameScreen(assetsManager, this);
				setScreen(gameScreen);
				this.renderPipeline = gameScreen.getRenderPipeline();
				break;
			case CHARACTER_SCREEN:
				CharacterScreen characterScreen = new CharacterScreen(assetsManager, this, googleFit);
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
	}

	@Override
	public void resume() {
		gameState = GameState.ACTIVE;
		super.resumeScreen();

		startGoogleFitRefresh();
	}

	@Override
	public void dispose() {
		UserData userData = DataUtils.getUserData();
		userData.setRunning(false);
		userData.setLastPlayStamp(System.currentTimeMillis());
		DataUtils.storeUserData();

		renderPipeline.dispose();
		super.disposeScreen();
	}
}
