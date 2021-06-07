package com.jumping.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jumping.game.Main;
import com.jumping.game.util.GoogleFitImplStub;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.resizable = false;
		config.width = 400;
		config.height = 700;
		config.x = 0;
		config.y = 0;
		new LwjglApplication(new Main(false, new GoogleFitImplStub()), config);
	}
}
