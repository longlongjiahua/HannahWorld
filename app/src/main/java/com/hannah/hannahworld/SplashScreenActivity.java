package com.hannah.hannahworld;
import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;




public class SplashScreenActivity extends Activity {

    public static final long TIME = 100;
    private static final String TAG = "SplashScreenActivity";
    private ImageView mImageView;

    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "SPLASHSCREEN");
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash_animination);
        //setContentView(R.layout.splash_screen);

        extraTimer.start();
        Log.i(TAG, "SPLASHSCREEN3");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "destroy::");

    }

    Thread extraTimer = new Thread() {

        public void run() {

            try {
                int lTimer1 = 0;
                while (lTimer1 < 8000) {
                    sleep(100);
                    lTimer1 = lTimer1 + 100;
                }
                startActivity(new Intent(SplashScreenActivity.this, MainMathActivity.class));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                finish();
            }
        }
    };


}
