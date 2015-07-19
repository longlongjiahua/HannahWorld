package com.hannah.hannahworld;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import com.hannah.hannahworld.makenumberalgorithm.QuesionAndAnswerUtils;
import com.hannah.hannahworld.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakeNumberActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "MakeNumberActivity";

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
    private Button btAnswer;
    private Button btNextQuestion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_makenumberactivity);

        btAnswer = (Button) findViewById(R.id.bt_answer);
        btNextQuestion = (Button) findViewById(R.id.bt_submit);
        btAnswer.setOnClickListener(this);
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
            case R.id.bt_answer:
                String answer = QuesionAndAnswerUtils.giveAnswer(questionNumbers);
                AlertDialog alertDialog = new AlertDialog.Builder(this).create(); //Read Update
                alertDialog.setTitle("Answer");
                alertDialog.setMessage(answer);
                alertDialog.setButton("Continue..", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //  add functions
                    }
                });
                alertDialog.show();
                //Toast.makeText(this, answer, Toast.LENGTH_LONG ).show();
                break;
            case R.id.bt_submit:
                if(btNextQuestion.getText().toString().equals("Submit")) {
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
                    mFormulaList.clear();
                    mNumberList.clear();
                    for(String str: QuesionAndAnswerUtils.provide24GameQuestion())
                            mNumberList.add(str);
                    btNextQuestion .setText("Submit");
                    numberAdapter.notifyDataSetChanged();
                    formulaAdapter.notifyDataSetChanged();
                }
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
}
