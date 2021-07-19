package com.healthypetsTUM.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.healthypetsTUM.game.Main;
import com.healthypetsTUM.game.util.GoogleFitImplStub;
import com.healthypetsTUM.game.util.StoreProviderStub;
import com.healthypetsTUM.game.util.interfaces.StatsProvider;
import com.healthypetsTUM.game.util.store.DataUtils;
import com.healthypetsTUM.game.util.store.UserData;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.resizable = false;
		config.width = 400;
		config.height = 700;
		config.x = 0;
		config.y = 0;
		new LwjglApplication(new Main(false, new GoogleFitImplStub(),
				new StoreProviderStub(), new StatsProvider() {
			@Override
			public void setStats() {
				UserData data = DataUtils.getUserData();
				LocalDate last = new Date(data.getLastPlayStamp()).toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDate();
				LocalDate now = new Date(System.currentTimeMillis()).toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDate();
				if (last.getYear() != now.getYear() || last.getDayOfYear() != now.getDayOfYear()) // new day
					data.newDay();

				data.setLastPlayStamp(System.currentTimeMillis());
				DataUtils.storeUserData();
			}
		}), config);
	}
}
