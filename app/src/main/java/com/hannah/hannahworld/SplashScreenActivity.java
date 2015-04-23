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

	public static final long TIME = 1000;
	private static final String TAG = "SplashScreenActivity";
	  private ImageView mImageView;

	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG,"SPLASHSCREEN");
		super.onCreate(savedInstanceState);
		 //setContentView(R.layout.activity_splash_animination);
		//setContentView(R.layout.splash_screen);
		startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
	}
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        mImageView = (ImageView) findViewById(R.id.icon);

        if (hasFocus) {
            //fadeIn.run();
        }
    }
    Runnable fadeIn = new Runnable() {
        public void run() {
            mImageView.animate().setDuration(1*1000)
                    .setInterpolator(new LinearInterpolator()).alpha(1.0f);
            //.withEndAction(rotate);
        }
    };


}
