package com.hannah.hannahworld;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.hannah.hannahworld.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakeNumberActivity extends Activity {
    private static final String TAG = "MakeNumberActivity";

    private TextView myCard;
    private static final String TextView_TAG = "The Android Logo";
    public GridView numberGridView;
    public GridView formulaGridView;
    public GridView operatorGridView;
    public ArrayList<String> mFormulaList = new ArrayList<String>(Arrays.asList("1", "2", "3", "4"));
    public ArrayList<String> mNumberList = new ArrayList<String>(Arrays.asList("1", "2", "3", "4"));
    public ArrayList<String> mOperatorList = new ArrayList<String>(Arrays.asList("+", "-", "*","/"));
    public TextViewAdapter numberAdapter;
    public TextViewAdapter formulaAdapter;
    public TextViewAdapter operatorAdapter;
    private DragDropHelp number2Formula;
    private DragDropHelp formula2Number;
    private DragDropHelp operator2Formula;
    private DragDropHelp formula2Operator;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_makenumberactivity);
        numberGridView = (GridView) findViewById(R.id.gv_numbers);
        formulaGridView = (GridView) findViewById(R.id.grid_view_formula);
        operatorGridView = (GridView) findViewById(R.id.gv_operators);
        init();
        number2Formula = new DragDropHelp(numberGridView, formulaGridView, this, mNumberList, mFormulaList, numberAdapter,formulaAdapter, new DragDropIt() {
            @Override
            public void handleSourceData(TextViewAdapter sourceAdapter, int clickPos, ArrayList<String> listData){
                deleteSource(sourceAdapter,clickPos,listData);
            }
            @Override
            public void handleTargetData(GridView mGridView,float x,String str,TextViewAdapter targetAdapter, ArrayList<String> listData) {
                insertIntoTarget(mGridView, x, str, targetAdapter, listData);
            }
        });
        formula2Number = new DragDropHelp(formulaGridView,numberGridView, this, mFormulaList,mNumberList, formulaAdapter, numberAdapter, new DragDropIt() {
            @Override
            public void handleSourceData(TextViewAdapter sourceAdapter, int clickPos, ArrayList<String> listData){
                deleteSource(sourceAdapter,clickPos,listData);
            }
            @Override
            public void handleTargetData(GridView mGridView,float x,String str,TextViewAdapter targetAdapter, ArrayList<String> listData) {
                insertIntoTarget(mGridView, x, str, targetAdapter, listData);
            }
        });
        formula2Operator = new DragDropHelp(formulaGridView,operatorGridView, this, mFormulaList,mOperatorList, formulaAdapter,operatorAdapter, new DragDropIt() {
            @Override
            public void handleSourceData(TextViewAdapter sourceAdapter, int clickPos, ArrayList<String> listData){
                deleteSource(sourceAdapter,clickPos,listData);
            }
            @Override
            public void handleTargetData(GridView mGridView,float x,String str,TextViewAdapter targetAdapter, ArrayList<String> listData) {
            }
        });
        operator2Formula = new DragDropHelp(operatorGridView,formulaGridView, this,mOperatorList, mFormulaList,operatorAdapter, formulaAdapter, new DragDropIt() {
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
        final String SOURCELIST_TAG = "listSource";
        final String TARGETLIST_TAG = "listTarget";
        final String TARGETLAYOUT_TAG = "targetLayout";
        numberAdapter = new TextViewAdapter(this, mNumberList);
        formulaAdapter = new TextViewAdapter(this, mFormulaList);
        operatorAdapter = new TextViewAdapter(this, mOperatorList);
        numberGridView.setAdapter(numberAdapter);
        operatorGridView.setAdapter(operatorAdapter);
        formulaGridView.setAdapter(formulaAdapter);
    }
    private void deleteSource(TextViewAdapter sourceAdapter, int clickPos, ArrayList<String> listData){
        listData.remove(clickPos);
        sourceAdapter.notifyDataSetChanged();
    }
    private void insertIntoTarget(GridView mGridView,float x,String str,TextViewAdapter targetAdapter, ArrayList<String> listData) {
        int insertPos = Utils.getInsertPosition(mGridView, x);
        Log.i("handleTargetData", "POS:"+insertPos);
        listData.add(insertPos, str);
        targetAdapter.notifyDataSetChanged();
    }
}