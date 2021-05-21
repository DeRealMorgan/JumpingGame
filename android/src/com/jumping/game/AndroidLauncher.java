package com.jumping.game;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.jumping.game.util.Values;

public class AndroidLauncher extends AndroidApplication {
	private Main main;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		boolean essentialPermsGranted = requestPermissions();

		main = new Main(essentialPermsGranted);
		initialize(main, config);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
			main.permissionGranted(requestCode);
	}

	/**
	 * Returns true if all essential permissions are granted
	 */
	private boolean requestPermissions() {
		boolean essentialGranted = true;
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
				!= PackageManager.PERMISSION_GRANTED) {
			if(Build.VERSION.SDK_INT > 29) {
				ActivityCompat.requestPermissions(this,
						new String[] {Manifest.permission.ACTIVITY_RECOGNITION},
						Values.REQUEST_ACTIVITY_RECOG_CODE);
			}
			essentialGranted = false;
		}

		return essentialGranted;
	}
}
