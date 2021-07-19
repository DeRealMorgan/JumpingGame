package com.healthypetsTUM.game;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.*;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.healthypetsTUM.game.util.Values;
import com.healthypetsTUM.game.util.store.UserData;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class NotificationWorker extends Worker {
    private UserData d;
    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        Json json = new Json();

        SharedPreferences preferences = super.getApplicationContext().getSharedPreferences(Values.SHARED_PREF, Context.MODE_PRIVATE);

        System.out.println(preferences.contains(Values.USER_DATA));
        d = json.fromJson(UserData.class, Base64Coder.decodeString(preferences.getString(Values.USER_DATA, "")));

        if(d.isRunning()) {
            startNewWorker(super.getApplicationContext());
            return Result.success();
        }

        AndroidLauncher.getStepCountToday(super.getApplicationContext(), null, count -> {
            int lastData = d.getLastStepCount();
            long lastStamp = d.getLastStepStamp();

            Instant i = Instant.ofEpochSecond(lastStamp);
            ZonedDateTime time = ZonedDateTime.ofInstant(i, ZoneId.systemDefault());

            ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
            if(time.getYear() != today.getYear() || time.getDayOfYear() < today.getDayOfYear()) // after midnight
                collectItems(0, count);
            else if(time.getYear() == today.getYear() && time.getDayOfYear() == today.getDayOfYear())
                collectItems(lastData, count);

        });

        return Result.success();
    }

    private void collectItems(int lastCollectedData, int currentData) {
        if(currentData < Values.TREAT_START) {
            startNewWorker(super.getApplicationContext());
            return;
        }

        if(lastCollectedData < Values.TREAT_START) {
            treat(currentData);
            startNewWorker(super.getApplicationContext());
            return;
        }

        startNewWorker(super.getApplicationContext());
    }

    public static void startNewWorker(Context c) {
        OneTimeWorkRequest.Builder workBuilder = new OneTimeWorkRequest.Builder(NotificationWorker.class);
        workBuilder.setInitialDelay(Values.TREAT_CHECK_TIME_DELTA, TimeUnit.SECONDS);

        WorkManager instanceWorkManager = WorkManager.getInstance(c);
        instanceWorkManager.enqueueUniqueWork(Values.TREATS_WORK, ExistingWorkPolicy.REPLACE, workBuilder.build());
    }

    private void treat(int stepCount) {
        notifyUser();

        Json json = new Json();

        SharedPreferences preferences = super.getApplicationContext().getSharedPreferences(Values.SHARED_PREF, Context.MODE_PRIVATE);

        d.treatFound();
        d.setLastStepCount(stepCount);
        d.setLastStepStamp(System.currentTimeMillis());

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Values.USER_DATA, Base64Coder.encodeString(json.toJson(d)));
        editor.commit();
    }

    private void notifyUser() {
        Intent intent = new Intent(getApplicationContext(), AndroidLauncher.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                Values.NOTIFY_CHANNEL_ID)
                .setSmallIcon(17301504)
                .setContentTitle(Values.TREAT_NOTIFICATION_TITLE)
                .setContentText(Values.TREAT_NOTIFICATION_TEXT)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(new Random().nextInt(), builder.build());
    }
}
