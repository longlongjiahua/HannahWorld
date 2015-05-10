package com.hannah.hannahworld;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class BroadcastTimeCountService extends Service {
    public static final String BROADCAST_ACTION = "displayUI";
    public static final String TIMELEFT = "time left";
    private int mStartID;
    private static final String FORMAT = "%02d:%02d:%02d";
    private Intent intent;

    int minutes;
    long mMillSecond;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mStartID = startId;
        Log.i("COUNTTIME::","xxxxxxx");
        minutes = intent.getIntExtra(MathActivity.INTENT_EXTRA_MINUTES, 0);
        mMillSecond = minutes * 60 * 1000;
        if (minutes == 0) mMillSecond = 5 * 60 * 1000;
        // Don't automatically restart this Service if it is killed
        Log.i("COUNTTIME::","yyy"+minutes);

        new CountDownTimer(mMillSecond, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                Log.i("COUNTTIME::",""+ millisUntilFinished);

                broadcastToUI(millisUntilFinished);
            }

            public void onFinish() {
                // stop Service if it was started with this ID
                // Otherwise let other start commands proceed
                stopSelf(mStartID);
            }
        }.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

    }

    // Can't bind to this Service
    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }
    private void broadcastToUI(long millisUntilFinished) {
        String text = "";
        text += "" + String.format(FORMAT,
                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                ));


        intent.putExtra(TIMELEFT, text);
        sendBroadcast(intent);
    }
}
