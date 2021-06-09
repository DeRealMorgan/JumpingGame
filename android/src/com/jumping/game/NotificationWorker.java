package com.jumping.game;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.*;
import com.jumping.game.util.StoreProviderImpl;
import com.jumping.game.util.Values;
import com.jumping.game.util.interfaces.GoogleFit;
import com.jumping.game.util.interfaces.StoreProvider;

import java.time.*;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class NotificationWorker extends Worker {
    private GoogleFit googleFit;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams,
                              GoogleFit googleFit) {
        super(context, workerParams);
        this.googleFit = googleFit;
    }

    @NonNull
    @Override
    public Result doWork() {
        int count = googleFit.getStepCountToday();
        StoreProvider provider = new StoreProviderImpl();
        Optional<Integer> lastData = provider.getInt(Values.STEP_COUNT);
        Optional<Long> lastStamp = provider.getLong(Values.STEP_COUNT_TIME);

        Instant i = Instant.ofEpochSecond(lastStamp.get());
        ZonedDateTime time = ZonedDateTime.ofInstant(i, ZoneId.systemDefault());

        ZonedDateTime today = ZonedDateTime.now(ZoneId.systemDefault());
        if(time.getYear() != today.getYear() || time.getDayOfYear() < today.getDayOfYear()) // after midnight
            collectItems(0, count);
        else if(time.getYear() == today.getYear() && time.getDayOfYear() == today.getDayOfYear())
            collectItems(lastData.get(), count);

        return Result.success();
    }

    private void collectItems(int lastCollectedData, int currentData) {
        if(currentData < Values.TREAT_START) {
            startNewWorker();
            return;
        }


        int currentInterval = (currentData-Values.TREAT_START)/Values.TREAT_INTERVAL + 1;

        if(lastCollectedData < Values.TREAT_START) {
            treat(currentInterval);
            startNewWorker();
            return;
        }

        int lastInterval = (lastCollectedData-Values.TREAT_START)/Values.TREAT_INTERVAL + 1;

        treat(currentInterval-lastInterval);
    }

    private void startNewWorker() {
        OneTimeWorkRequest.Builder workBuilder = new OneTimeWorkRequest.Builder(NotificationWorker.class);
        workBuilder.setInitialDelay(Duration.between(LocalDateTime.now(), LocalDateTime.now().plusMinutes(Values.TREAT_CHECK_TIME_DELTA)));

        WorkManager instanceWorkManager = WorkManager.getInstance(getApplicationContext());
        instanceWorkManager.enqueueUniqueWork(Values.TREATS_WORK, ExistingWorkPolicy.REPLACE, workBuilder.build());
    }

    private void treat(int amount) {
        notifyUser();
    }

    private void notifyUser() {
        Intent intent = new Intent(getApplicationContext(), AndroidLauncher.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                Values.NOTIFY_CHANNEL_ID)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_light) // todo
                .setContentTitle(Values.TREAT_NOTIFICATION_TITLE)
                .setContentText(Values.TREAT_NOTIFICATION_TEXT)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ThreadLocalRandom.current().nextInt(), builder.build());
    }
}
