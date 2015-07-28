package com.hannah.hannahworld;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class BroadcastTimeCountService extends Service {
    private final static String TAG = "BroadcastTimeCountSer";
    public static final String BROADCAST_ACTION = "displayUI";
    public static final String TIMELEFT = "time left";
    private int mStartID;
    private static final String FORMAT = "%02d:%02d";
    private Intent intent;
    private final IBinder mBinder = new MathBinder();
    int minutes;
    long mMillSecond;
    private MCountDownTimer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);
    }


    @Override
    public void onDestroy() {
        Log.i(TAG, "destroy");
        timer.cancel();
    }

    // Can't bind to this Service

    private void broadcastToUI(long millisUntilFinished) {
        String text = "";
        text += "" + String.format(FORMAT,
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                ));

        intent.putExtra(TIMELEFT, text);
        sendBroadcast(intent);
    }


    @Override
    public IBinder onBind(Intent intent) {
        int time = intent.getExtras().getInt(MathActivity.INTENT_EXTRA_MINUTES);
        //beginBroadcast(time);
        Log.i(TAG, "time" + time);
        return mBinder;


    }

    public class MathBinder extends Binder {
        BroadcastTimeCountService getService() {
            // Return this instance of LocalService so clients can call public methods
            return BroadcastTimeCountService.this;
        }
    }


    public void beginBroadcast(int minutes) {
        //minutes = intent.getIntExtra(MathActivity.INTENT_EXTRA_MINUTES, 0);
        mMillSecond = minutes * 60 * 1000;
        timer = new MCountDownTimer(mMillSecond);
        timer.start();
     }
    class MCountDownTimer  extends  CountDownTimer{
        MCountDownTimer(long mMillSecond) {
            super(mMillSecond, 1000);
        }

        public void onTick(long millisUntilFinished) {
            Log.i("COUNTTIME::", "" + millisUntilFinished);
            broadcastToUI(millisUntilFinished);
        }

        public void onFinish() {
            // stop Service if it was started with this ID
            // Otherwise let other start commands proceed
            Log.i("COUNTTIME::", "yyyfinished");
            broadcastToUI(0L);
            //stopSelf(mStartID);
        }
    };
 }
