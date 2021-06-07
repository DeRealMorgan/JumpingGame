package com.jumping.game;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.*;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.jumping.game.util.Values;

import java.time.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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

	public int getStepCountToday() {
		ZonedDateTime startTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault());
		ZonedDateTime endTime = LocalDateTime.now().atZone(ZoneId.systemDefault());

		return getSteps(startTime, endTime);
	}

	public int getStepCountYesterday() {
		ZonedDateTime startTime = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(1);
		ZonedDateTime endTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).with(LocalTime.MAX);

		return getSteps(startTime, endTime);
	}

	private int getSteps(ZonedDateTime startTime, ZonedDateTime endTime) {

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
		Fitness.getHistoryClient(this, GoogleSignIn.getAccountForExtension(this, fitnessOptions))
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

		return totalSteps.get();
	}
}
