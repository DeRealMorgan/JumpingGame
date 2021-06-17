package com.healthypetsTUM.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.healthypetsTUM.game.Main;
import com.healthypetsTUM.game.util.GoogleFitImplStub;
import com.healthypetsTUM.game.util.StoreProviderStub;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.resizable = false;
		config.width = 400;
		config.height = 700;
		config.x = 0;
		config.y = 0;
		new LwjglApplication(new Main(false, new GoogleFitImplStub(),
				new StoreProviderStub()), config);
	}
}
