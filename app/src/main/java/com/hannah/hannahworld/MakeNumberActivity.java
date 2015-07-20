package com.hannah.hannahworld;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
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
    private int selectedTime;
    private int selectedTimePos=1;
    private Spinner spTimeResource;
    private TextView tvScore;
    private TextView tvTimeCountDown;
    private boolean unRegistered = false;
    private static final String TAG = "MakeNumberActivity";
    private boolean mServiceBound =false;
    private BroadcastTimeCountService mService;
    private boolean mBound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makenumberactivity);
        tvScore = (TextView) findViewById(R.id.tv_your_score);
        tvTimeCountDown = (TextView) findViewById(R.id.tv_time_countdown);
        spTimeResource = (Spinner) findViewById(R.id.sp_timesource);
        spTimeResource.setSelection(selectedTimePos);
        spTimeResource.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        btNextQuestion = (Button) findViewById(R.id.bt_submit);
        btNextQuestion.setOnClickListener(this);
        questionNumbers = (ArrayList<String>) QuesionAndAnswerUtils.provide24GameQuestion();
        mNumberList = new ArrayList<String> (questionNumbers);
        init();
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
        formula2NumberOrOperator = new DragDropHelp(formulaGridView,numberGridView,lvNumber, this, mFormulaList,mNumberList, formulaAdapter, numberAdapter, new DragDropIt() {
            @Override
            public void handleSourceData(TextViewAdapter sourceAdapter, int clickPos, ArrayList<String> listData){
                deleteSource(sourceAdapter,clickPos,listData);
            }
            @Override
            public void handleTargetData(GridView mGridView,float x,String str,TextViewAdapter targetAdapter, ArrayList<String> listData) {
                if(listData==null || listData.size()<4 ||! listData.get(0).equals("+")) {
                    insertIntoTarget(mGridView, x, str, targetAdapter, listData);
                }
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
                      final Intent mServiceIntent = new Intent(MakeNumberActivity .this, BroadcastTimeCountService.class);
                      mServiceIntent.putExtra(MathActivity.INTENT_EXTRA_MINUTES, selectedTime);
                      Log.i("Time", ""+selectedTime);
                      MakeNumberActivity.this.bindService(mServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
                      //MathActivity.this.startServce(mServiceIntent);
                      btNextQuestion.setText("Submit");

                  }
                else if(btNextQuestion.getText().toString().equals("Submit")) {
                    if (judgeAnswer()) {

                    } else {
                        String correctString = QuesionAndAnswerUtils.giveAnswer(questionNumbers);
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
                          mFormulaList.clear();
                          mNumberList.clear();
                          questionNumbers = (ArrayList<String>) QuesionAndAnswerUtils.provide24GameQuestion();
                          for (String str : questionNumbers)
                              mNumberList.add(str);
                          btNextQuestion.setText("Submit");
                          numberAdapter.notifyDataSetChanged();
                          formulaAdapter.notifyDataSetChanged();
                      }
                  }

                  break;
        }
    }
    public boolean judgeAnswer(){
           String formulaString="";
            int length = mFormulaList.size();
            for(int i=0; i<length; i++){
                formulaString+=mFormulaList.get(i);
            }
            return QuesionAndAnswerUtils.isCorrectAnswer(formulaString);

    }

    private void deleteSource(TextViewAdapter sourceAdapter, int clickPos, ArrayList<String> listData){
        listData.remove(clickPos);
        sourceAdapter.notifyDataSetChanged();
    }
    private void insertIntoTarget(GridView mGridView,float x,String str,TextViewAdapter targetAdapter, List<String> listData) {
        int insertPos = Utils.getInsertPosition(mGridView, x, listData);
        Log.i("handleTargetData", "POS:"+insertPos);
        listData.add(insertPos, str);
        targetAdapter.notifyDataSetChanged();
    }

    private class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        private int[] times = {1,2,3,4};

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            // parent.getItemAtPosition(pos).toString();
            selectedTime = times[pos];
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }


    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BroadcastTimeCountService.MathBinder binder = (BroadcastTimeCountService.MathBinder) service;
            mService = binder.getService();
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

            String time = intent.getStringExtra(BroadcastTimeCountService.TIMELEFT);
            Log.i("BroadcastReceiver:::", time);
            if(time.equals("00:00")){
                //gridView.setEnabled(false);
            }
            //mathFragments[currentPageNo].tvTimeCountDown.setText(time);
            tvTimeCountDown.setText(time);
        }
    };

}
