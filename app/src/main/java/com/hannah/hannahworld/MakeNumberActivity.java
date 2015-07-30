package com.hannah.hannahworld;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;

import com.hannah.hannahworld.makenumberalgorithm.QuesionAndAnswerUtils;
import com.hannah.hannahworld.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakeNumberActivity extends Activity implements View.OnClickListener {
    private TextView myCard;
    private static final String TextView_TAG = "The Android Logo";
    public LinearLayout lvNumber;
    public LinearLayout lvFormula;
    public LinearLayout lvOperator;
    public GridView numberGridView;
    public GridView formulaGridView;
    public GridView operatorGridView;
    public ArrayList<String> mFormulaList = new ArrayList<String>();
    private  ArrayList<String> questionNumbers;
    public ArrayList<String> mNumberList = new ArrayList<String>(Arrays.asList("1", "2", "3", "4"));
    public ArrayList<String> mOperatorList = new ArrayList<String>(Arrays.asList("+", "-", "*","/",")","("));
    public TextViewAdapter numberAdapter;
    public TextViewAdapter formulaAdapter;
    public TextViewAdapter operatorAdapter;
    private DragDropHelp number2Formula;
    private DragDropHelp formula2NumberOrOperator;
    private DragDropHelp operator2Formula;
    private Button btNextQuestion;
    private TextView tvScore;
    private TextView tvTimeCountDown;
    private boolean unRegistered = false;
    private static final String TAG = "MakeNumberActivity";
    private boolean mServiceBound =false;
    private BroadcastTimeCountService mService;
    private boolean mBound = false;
    private TextView tvCheckAnswer;
    private Intent broadcastIntent;
    private String mCountTime="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setHomeButtonEnabled(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        broadcastIntent = new Intent(this, BroadcastTimeCountService.class);
        setContentView(R.layout.activity_makenumberactivity);
        tvCheckAnswer = (TextView) findViewById(R.id.tv_judge_answer);
        tvScore = (TextView) findViewById(R.id.tv_your_score);
        tvTimeCountDown = (TextView) findViewById(R.id.tv_time_countdown);
        btNextQuestion = (Button) findViewById(R.id.bt_submit);
        btNextQuestion.setOnClickListener(this);
        questionNumbers = (ArrayList<String>) QuesionAndAnswerUtils.provide24GameQuestion();
        mNumberList = new ArrayList<String> (questionNumbers);
        mFormulaList = new ArrayList<String>(Arrays.asList(" ", " ", " ", " "," "));
        init();
        formulaGridView.setOnTouchListener(new TouchSwipeListen(this, new OnSwipeDecteted() {
            @Override
            public void onSwipeDecteted(float distance, float downPosX, float downPosY) {
                int pos = Utils.getTouchPosition(formulaGridView, downPosX, downPosY, mFormulaList);
                Log.i(TAG, "pos:" +pos);
                if(pos<0)
                    return;
                String str = mFormulaList.get(pos);
                char mChar =str.charAt(0);
                deleteSource(formulaAdapter, pos, mFormulaList);
                if(distance>0.0 && mChar>='0' && mChar<='9'){
                     mNumberList.add(str);
                     numberAdapter.notifyDataSetChanged();
                }
            }
        }));
        number2Formula = new DragDropHelp(numberGridView, formulaGridView,lvFormula, this, mNumberList, mFormulaList, numberAdapter,formulaAdapter, new DragDropIt() {
            @Override
            public void handleSourceData(TextViewAdapter sourceAdapter, int clickPos, ArrayList<String> listData){
                deleteSource(sourceAdapter,clickPos,listData);
            }
            @Override
            public void handleTargetData(GridView mGridView,float x,String str,TextViewAdapter targetAdapter, ArrayList<String> listData) {
                insertIntoTarget(mGridView, x, str, targetAdapter, listData);
            }
        });
         operator2Formula = new DragDropHelp(operatorGridView,formulaGridView,lvFormula, this, mOperatorList, mFormulaList,operatorAdapter, formulaAdapter, new DragDropIt() {
            @Override
            public void handleSourceData(TextViewAdapter sourceAdapter, int clickPos, ArrayList<String> listData){

            }
            @Override
            public void handleTargetData(GridView mGridView,float x,String str,TextViewAdapter targetAdapter, ArrayList<String> listData) {
                insertIntoTarget(mGridView, x, str, targetAdapter, listData);
            }
        });
     }
    /*
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                DialogHelp.page(getFragmentManager(), R.string.quit_game);
                if(operatorGridView.isEnabled()){
                    DialogHelp.page(getFragmentManager(), R.string.quit_game);
                }
                break;
        }

        return true;
    }
    */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(numberGridView.isEnabled()) {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.quit_game)
                        .setMessage(R.string.quit_game)
                        .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Stop the activity
                                MakeNumberActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.button_no, null)
                        .show();
            }
            else {
                MakeNumberActivity.this.finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
    
    public void onResume() {
        super.onResume();
        Log.i(TAG, "ONRESUME");
        // startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastTimeCountService.BROADCAST_ACTION));
    }
    
    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "ONPAUSE");
        stopService(broadcastIntent);
        if (mBound)
            unbindService(mConnection);
        if (!unRegistered) {
            unregisterReceiver(broadcastReceiver);
            //stopService(broadcastIntent);
            unRegistered = true;
        }
    }
    @Override
    public void onStop(){
        super.onStop();
        Log.i(TAG, "ONSTOP");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "ONDESTROY");
    }

    private void init() {
        lvNumber = (LinearLayout) findViewById(R.id.lv_number);
        lvFormula = (LinearLayout) findViewById(R.id.lv_formula);
        lvOperator= (LinearLayout) findViewById(R.id.lv_operator);
        numberGridView = (GridView) findViewById(R.id.gv_numbers);
        formulaGridView = (GridView) findViewById(R.id.grid_view_formula);
        operatorGridView = (GridView) findViewById(R.id.gv_operators);
        numberAdapter = new TextViewAdapter(this, mNumberList);
        formulaAdapter = new TextViewAdapter(this, mFormulaList);
        operatorAdapter = new TextViewAdapter(this, mOperatorList);
        numberGridView.setAdapter(numberAdapter);
        operatorGridView.setAdapter(operatorAdapter);
        formulaGridView.setAdapter(formulaAdapter);
    }

    public void onClick(View v) {
        switch (v.getId()) {
              case R.id.bt_submit:
                  if(btNextQuestion.getText().toString().equals("Start")) {
                       final Intent mServiceIntent = new Intent(MakeNumberActivity.this, BroadcastTimeCountService.class);
                      mServiceIntent.putExtra(MathActivity.INTENT_EXTRA_MINUTES, Constants.MAKENUMBERTIME);
                      MakeNumberActivity.this.bindService(mServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
                      //MathActivity.this.startServce(mServiceIntent);
                      btNextQuestion.setText("Submit");

                  }
                else if(btNextQuestion.getText().toString().equals("Submit")) {
                      numberGridView.setVisibility(View.GONE);
                      tvCheckAnswer.setVisibility(View.VISIBLE);
                    if (judgeAnswer()) {
                        tvCheckAnswer.setText("Correct!");
                    } else {
                        tvCheckAnswer.setText("Incorrect!");
                        String correctString = QuesionAndAnswerUtils.giveAnswer(questionNumbers);
                        formulaGridView.setNumColumns(correctString.length());
                        mFormulaList.clear();
                       for(String str:Arrays.asList(correctString.split("(?!^)"))){
                           mFormulaList.add(str);
                       }
                    }
                      formulaAdapter.notifyDataSetChanged();
                    btNextQuestion.setText("Next");
                }
                else {
                      if (btNextQuestion.getText().toString().equals("Next")) {
                          tvCheckAnswer.setVisibility(View.GONE);
                          numberGridView.setVisibility(View.VISIBLE);
                          mFormulaList.clear();
                          mNumberList.clear();
                          questionNumbers = (ArrayList<String>) QuesionAndAnswerUtils.provide24GameQuestion();
                          for (String str : questionNumbers)
                              mNumberList.add(str);
                          for(int i=0; i<4; i++)
                              mFormulaList.add(" ");
                          btNextQuestion.setText("Submit");
                          numberAdapter.notifyDataSetChanged();
                          formulaAdapter.notifyDataSetChanged();
                      }
                  }

                  break;
        }
    }

    public boolean judgeAnswer() {
        String formulaString = "";
        int length = mFormulaList.size();
        for (int i = 0; i < length; i++) {
            formulaString += mFormulaList.get(i);
        }
        Log.i(TAG, formulaString);
        return QuesionAndAnswerUtils.isCorrectAnswer(formulaString);
    }

    private void deleteSource(TextViewAdapter sourceAdapter, int clickPos, ArrayList<String> listData){
        listData.remove(clickPos);
        sourceAdapter.notifyDataSetChanged();
    }
    private void insertIntoTarget(GridView mGridView,float x,String str,TextViewAdapter targetAdapter, List<String> listData) {
        int insertPos = Utils.getInsertPosition(mGridView, x, listData);
        Log.i("handleTargetData", "POS:"+insertPos);
        if(listData.size()>insertPos && listData.get(insertPos).equals(" ")){
            listData.set(insertPos, str);
        }
        else {
            listData.add(insertPos, str);
        }
        if(listData.size()>7 && listData.contains(" ")){
            while(listData.remove(" ")) {}
           }
        formulaGridView.setNumColumns(listData.size()+1);
        targetAdapter.notifyDataSetChanged();
    }


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BroadcastTimeCountService.MathBinder binder = (BroadcastTimeCountService.MathBinder) service;
            mService = binder.getService();
            Log.i(TAG, "beginBroadcast");
            mService.beginBroadcast(Constants.MAKENUMBERTIME);
            mBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mService = null;

            mBound = false;
        }
    };

    void doUnbindService() {
        if (mBound) {
            // Detach our existing connection.
            unbindService(mConnection);
            mBound = false;
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            mCountTime = intent.getStringExtra(BroadcastTimeCountService.TIMELEFT);
            Log.i("BroadcastReceiver:::", mCountTime);
            if(mCountTime.equals("00:00")){
                formulaGridView.setEnabled(false);
                numberGridView.setEnabled(false);
                operatorGridView.setEnabled(false);
            }
            //mathFragments[currentPageNo].tvTimeCountDown.setText(time);
            tvTimeCountDown.setText(mCountTime);
        }
    };

}
