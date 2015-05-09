package com.hannah.hannahworld;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

/**
 * Created by xuyong1 on 06/05/15.
 */

    public  class MathFragment extends Fragment {
    TextView mTv1, mTv2,mTv3,mTv5;
    private MathActivity activity;
    private int pageNo;
    public TextView tvScore;

        public static final String ARG_OBJECT = "object";
        public static Fragment instance(final int str)
        {
            final MathFragment fragment = new MathFragment();

            final Bundle args = new Bundle();
            args.putInt("PAGE", str);
            fragment.setArguments(args);

            return fragment;
        }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.pageNo =  getArguments().getInt("PAGE");
        super.onCreate(savedInstanceState);
      }

    @Override
    public void onAttach(final Activity activity){
        super.onAttach(activity);
        this.activity = (MathActivity) activity;
    }
    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_math_format, container, false);
            Bundle args = getArguments();
            mTv1 = ((TextView) rootView.findViewById(R.id.text1));
            mTv2 = ((TextView) rootView.findViewById(R.id.text2));
            mTv3 = ((TextView) rootView.findViewById(R.id.text3));
            mTv5 = ((TextView) rootView.findViewById(R.id.text5));
        tvScore = (TextView)rootView.findViewById(R.id.tv_your_score);
            int t1=0, t3, t5;
        if(activity.operation==0) {
            mTv2.setText("x");
        }
        else if(activity.operation ==1){
            mTv2.setText("\u00F7"); //unicode of division sign
        }
            if(activity.mNums[pageNo][0]==null) {
                t3 = randInt(1, 9);
                if (activity.operation == 0) {
                    t1 = randInt(1, 9);
                } else if (activity.operation == 1) {
                    t1 = t3 * randInt(1, 9);
                }
                activity.mNums[pageNo][0] = t1;
                activity.mNums[pageNo][1] = t3;
            }
            else{
                t1= activity.mNums[pageNo][0];
                t3= activity.mNums[pageNo][1];
             }
            mTv1.setText(""+t1);
            mTv3.setText(""+t3);
            if(activity.mNums[pageNo][2]!=null){
                mTv5.setText(""+activity.mNums[pageNo][2]);
                t5= activity.mNums[pageNo][2];
                if((t5==t1*t3 && activity.operation==0) ||(t5 == t1/t3 && t1%t3==0 && activity.operation==1) ){
                    mTv5.setBackgroundResource(R.drawable.rectangle_back2);
                }
            }
            return rootView;
        }

    public int randInt(int min, int max) {
        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
