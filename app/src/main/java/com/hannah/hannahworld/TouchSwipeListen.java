package com.hannah.hannahworld;
import android.app.Activity;
import android.app.UiAutomation;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

import java.util.List;

public class TouchSwipeListen implements View.OnTouchListener {
    private static final String TAG= "TouchSwipeListen";
    public static enum Action {
        LR, // Left to Right
        RL, // Right to Left
        TB, // Top to bottom
        BT, // Bottom to Top
        None // when no action was detected
    }

    private static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
    private Action mTouchSwipeListen = Action.None;
    private Activity activity;
    private OnSwipeDecteted callback;
    public boolean swipeDetected() {
        return mTouchSwipeListen != Action.None;
    }
    public TouchSwipeListen(
            Activity activity,OnSwipeDecteted callback ) {
        this.activity = activity;
        this.callback = callback;
    }


    public Action getAction() {
        return mTouchSwipeListen;
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                mTouchSwipeListen = Action.None;
                return false; // allow other events like Click to be processed
            }
            case MotionEvent.ACTION_MOVE: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    if (deltaX < 0) {
                       Log.i(TAG, "Swipe Left to Right");
                        mTouchSwipeListen = Action.LR;

                        return true;
                    }
                    if (deltaX > 0) {
                       Log.i(TAG, "Swipe Right to Left");
                        mTouchSwipeListen = Action.RL;
                        return true;
                    }
                } else
                    if (Math.abs(deltaY) > MIN_DISTANCE) {
                                callback.onSwipeDecteted(deltaY, downX, downY);
                        // top or down
                        if (deltaY < 0) {
                           Log.i(TAG, "Swipe Top to Bottom");
                            mTouchSwipeListen = Action.TB;
                            return false;
                        }
                        if (deltaY > 0) {
                           Log.i(TAG, "Swipe Bottom to Top");
                            mTouchSwipeListen = Action.BT;

                            return false;
                        }
                    }
                return true;
            }
        }
        return false;
    }
}