package com.svitlasystem.schedule;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.svitlasystem.networking.ScheduledService;

public class PollReceiver extends WakefulBroadcastReceiver {

    private static final int PERIOD = 1 * 60 * 1000; // 3 minutes
    private static final int INITIAL_DELAY = 0; // 0 seconds

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null) {
            startWakefulService(context, new Intent(context, ScheduledService.class));
        } else {
            scheduleWork(context);
        }
    }

    public static void scheduleWork(Context context) {
        AlarmManager mgr =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, PollReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + INITIAL_DELAY,
                PERIOD, pi);
    }
}
