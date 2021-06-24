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
import com.healthypetsTUM.game.util.interfaces.StoreProvider;

import java.time.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AndroidLauncher extends AndroidApplication implements GoogleFit, StoreProvider {
	private Main main;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		boolean essentialPermsGranted = requestPermissions();

		initNotificationChannels();
		main = new Main(essentialPermsGranted, this, this);
		initialize(main, config);

		startNotificationWorker();
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
		editor.commit();
	}

	@Override
	public String load(String name) {
		SharedPreferences preferences = super.getApplicationContext().getSharedPreferences(Values.SHARED_PREF, Context.MODE_PRIVATE);
		return preferences.getString(name, "");
	}

	@Override
	public int getStepCountToday() {
		return getStepCountToday(getContext(), this);
	}

	@Override
	public int getStepCountYesterday() {
		return getStepCountYesterday(getContext(), this);
	}

	@Override
	public int getStepCountLast24() {
		return getStepCountLast24(getContext(), this);
	}

	public static int getStepCountToday(Context c, Activity a) {
		ZonedDateTime startTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(5); // remove minus days
		ZonedDateTime endTime = LocalDateTime.now().atZone(ZoneId.systemDefault());

		return getSteps(c, a, startTime, endTime);
	}

	public static int getStepCountYesterday(Context c, Activity a) {
		ZonedDateTime startTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(1);
		ZonedDateTime endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).with(LocalTime.MAX);

		return getSteps(c, a, startTime, endTime);
	}

	public static int getStepCountLast24(Context c, Activity a) {
		ZonedDateTime startTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).minusDays(1);
		ZonedDateTime endTime = LocalDateTime.now().atZone(ZoneId.systemDefault());

		return getSteps(c, a, startTime, endTime);
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

	private static int getSteps(Context c, Activity a, ZonedDateTime startTime, ZonedDateTime endTime) {
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

		AtomicInteger totalSteps = new AtomicInteger();

		GoogleSignInAccount signInAcc = GoogleSignIn.getAccountForExtension(c, fitnessOptions);

		if (!GoogleSignIn.hasPermissions(signInAcc, fitnessOptions)) {
			if(a == null) return -1;
			GoogleSignIn.requestPermissions(a, // your activity
					Values.GOOGLE_FIT_REQUEST_CODE, signInAcc, fitnessOptions);
			return -1;
		}

		Fitness.getHistoryClient(c, signInAcc)
				.readData(request)
				.addOnSuccessListener((response) -> totalSteps.set(response.getBuckets().stream()
						.map(Bucket::getDataSets)
						.flatMap(List::stream)
						.collect(Collectors.toList())
						.stream()

						.map(DataSet::getDataPoints)
						.flatMap(List::stream)
						.collect(Collectors.toList())
						.stream().mapToInt((d) -> d.getValue(Field.FIELD_STEPS).asInt()).sum())
				);

		System.out.println("current steps: " + totalSteps.get());
		return totalSteps.get();
	}

	private void initNotificationChannels() {
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
