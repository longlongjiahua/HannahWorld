package com.hannah.hannahworld;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import android.os.CountDownTimer;

/**
 * Created by xuyong1 on 06/05/15.
 */

    public abstract class AbstractMathFragment extends Fragment {
    protected int randInt(int min, int max) {
        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

}
