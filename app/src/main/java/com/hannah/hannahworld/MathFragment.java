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

    public  class MathFragment extends Fragment {
    TextView mTv1, mTv2, mTv3, mTv5;
    private MathActivity activity;
    private int pageNo;
    public static final String ARG_OBJECT = "object";

    public static Fragment instance(final int str) {
        final MathFragment fragment = new MathFragment();

        final Bundle args = new Bundle();
        args.putInt("PAGE", str);
        fragment.setArguments(args);

        return fragment;
    }
    private int t1 = 0, t3, t5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.pageNo = getArguments().getInt("PAGE");
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onAttach(final Activity activity) {
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
        if (activity.operation == 0) {
            mTv2.setText("x");
        } else if (activity.operation == 1) {
            mTv2.setText("\u00F7"); //unicode of division sign
        }
        if (activity.mNums[pageNo][0] == null) {
            giveNewQuestion();
            mTv1.setText("" + t1);
            mTv3.setText("" + t3);
            activity.mNums[pageNo][0] = t1;
            activity.mNums[pageNo][1] = t3;
        }
        else {
            t1 = activity.mNums[pageNo][0];
            t3 = activity.mNums[pageNo][1];
            mTv1.setText("" + t1);
            mTv3.setText("" + t3);
        }

        if (activity.mNums[pageNo][2] != null) {
            mTv5.setText("" + activity.mNums[pageNo][2]);
            t5 = activity.mNums[pageNo][2];
            if ((t5 == t1 * t3 && activity.operation == 0) || (t5 == t1 / t3 && t1 % t3 == 0 && activity.operation == 1)) {
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

    /**
     * generate new question, and make sure 1)
     * not too simple: contain 0 or 1;
     * 2) duplicate questions
     */

    private void giveNewQuestion(){
       while(true) {
           giveQuestionNumbers();
           if(alreadyO1()) {
               continue;
           }
           String qStr1 = ""+t1+" " + t3;
           String qStr2 = "" + t3 + " " + t1;

           if(activity.questionsGiven.containsKey(qStr1) || activity.questionsGiven.containsKey(qStr2)){
               continue;
           }
           activity.questionsGiven.put(qStr1,1);
           return;

       }
    }
    private boolean alreadyO1(){
        if(t1==1 || t3==1){
            if(activity.questionsGiven.containsKey(""+1)){
                return true;
            }
            else {
                activity.questionsGiven.put(""+1,1);
            }
        }
        if(t1==0 || t3==0){
            if(activity.questionsGiven.containsKey(""+0)){
                return true;
            }
            else {
                activity.questionsGiven.put(""+0, 1);
            }
        }
        return false;
    }

     private void giveQuestionNumbers() {
        t3 = randInt(1, 9);
        if (activity.operation == 0) {
            t1 = randInt(1, 9);
        } else if (activity.operation == 1) {
            t1 = t3 * randInt(1, 9);
        }
    }

}
