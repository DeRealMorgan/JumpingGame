package com.healthypetsTUM.game;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.*;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.interfaces.GoogleFit;
import com.healthypetsTUM.game.util.interfaces.StatsProvider;
import com.healthypetsTUM.game.util.interfaces.StepListener;
import com.healthypetsTUM.game.util.interfaces.StoreProvider;
import com.healthypetsTUM.game.util.store.DataUtils;
import com.healthypetsTUM.game.util.store.UserData;

import java.time.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AndroidLauncher extends AndroidApplication implements GoogleFit, StoreProvider, StatsProvider {
	private Main main;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		boolean essentialPermsGranted = requestPermissions();

		initNotificationChannels();
		main = new Main(essentialPermsGranted, this, this, this);
		initialize(main, config);

		startNotificationWorker();
	}

	@Override
	public void setStats() {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
	}

	@Override
	public boolean isApiTooLow() {
		return Build.VERSION.SDK_INT < 26;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
			main.permissionGranted(requestCode);
	}

	private void startNotificationWorker() {
		NotificationWorker.startNewWorker(super.getApplicationContext());
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

	@Override
	public void store(String name, String val) {
		SharedPreferences preferences = super.getApplicationContext().getSharedPreferences(Values.SHARED_PREF, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(name, val);
		if(!editor.commit()) System.out.println("COULD NOT STORE VALUE");
	}

	@Override
	public String load(String name) {
		SharedPreferences preferences = super.getApplicationContext().getSharedPreferences(Values.SHARED_PREF, Context.MODE_PRIVATE);
		return preferences.getString(name, "");
	}

	@Override
	public void getStepCountToday(StepListener listener) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			getStepCountToday(getContext(), this, listener);
		} else {
			listener.stepsReturned(4000);
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	public void getStepCountYesterday(StepListener listener) {
		getStepCountYesterday(getContext(), this, listener);
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	@Override
	public void getStepCountLast24(StepListener listener) {
		getStepCountLast24(getContext(), this, listener);
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	public static void getStepCountToday(Context c, Activity a, StepListener listener) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			ZonedDateTime startTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
			ZonedDateTime endTime = LocalDateTime.now().atZone(ZoneId.systemDefault());

			getSteps(c, a, startTime, endTime, listener);
		} else {
			listener.stepsReturned(4000);
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	public static void getStepCountYesterday(Context c, Activity a, StepListener listener) {
		ZonedDateTime startTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(1);
		ZonedDateTime endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).with(LocalTime.MAX);

		getSteps(c, a, startTime, endTime, listener);
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	public static void getStepCountLast24(Context c, Activity a, StepListener listener) {
		ZonedDateTime startTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).minusDays(1);
		ZonedDateTime endTime = LocalDateTime.now().atZone(ZoneId.systemDefault());

		getSteps(c, a, startTime, endTime, listener);
	}

	@Override
	public void signIn() {
		FitnessOptions fitnessOptions = FitnessOptions.builder()
				.addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
				.addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
				.build();

		GoogleSignInAccount signInAcc = GoogleSignIn.getAccountForExtension(this.getApplicationContext(), fitnessOptions);

		GoogleSignIn.requestPermissions(this, // your activity
				Values.GOOGLE_FIT_REQUEST_CODE, signInAcc, fitnessOptions);
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	private static void getSteps(Context c, Activity a, ZonedDateTime startTime, ZonedDateTime endTime,
								 StepListener listener) {
		// TODO https://developers.google.com/fit/android/disconnect
		DataSource datasource = new DataSource.Builder()
				.setAppPackageName("com.google.android.gms")
				.setDataType(DataType.TYPE_STEP_COUNT_DELTA)
				.setType(DataSource.TYPE_DERIVED)
				.setStreamName("estimated_steps")
				.build();

		DataReadRequest request = new DataReadRequest.Builder()
				.aggregate(datasource)
				.bucketByTime(1, TimeUnit.DAYS)
				.setTimeRange(startTime.toEpochSecond(), endTime.toEpochSecond(), TimeUnit.SECONDS)
				.build();

		FitnessOptions fitnessOptions = FitnessOptions.builder()
				.addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
				.addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
				.build();

		GoogleSignInAccount signInAcc = GoogleSignIn.getAccountForExtension(c, fitnessOptions);

		if (!GoogleSignIn.hasPermissions(signInAcc, fitnessOptions)) {
			if(a == null) {
				listener.stepsReturned(-1);
				return;
			}
			GoogleSignIn.requestPermissions(a, // your activity
					Values.GOOGLE_FIT_REQUEST_CODE, signInAcc, fitnessOptions);

			listener.stepsReturned(-1);
			return;
		}

		Fitness.getHistoryClient(c, signInAcc)
				.readData(request)
				.addOnSuccessListener((response) -> {
					int steps = response.getBuckets().stream()
						.map(Bucket::getDataSets)
						.flatMap(List::stream)
						.collect(Collectors.toList())
						.stream()
						.map(DataSet::getDataPoints)
						.flatMap(List::stream)
						.collect(Collectors.toList())
						.stream().mapToInt((d) -> d.getValue(Field.FIELD_STEPS).asInt()).sum();

					listener.stepsReturned(steps);
				});
	}

	private void initNotificationChannels() {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager notificationManager =
					(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel notificationChannel = new NotificationChannel(Values.NOTIFY_CHANNEL_ID,
					Values.NOTIFY_CHANNEL_NAME, importance);
			notificationChannel.enableLights(false);
			notificationChannel.enableVibration(false);
			notificationManager.createNotificationChannel(notificationChannel);
		}
	}
}
