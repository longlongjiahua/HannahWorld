package com.hannah.hannahworld;

import android.view.View;
import android.widget.GridView;
import android.util.Log;
import java.util.ArrayList;

public  class DeleteSourceElementImpl implements DeleteSourceElement {
    static String TAG = "DeleteSourceElementImpl";
    @Override
    public void deleteElement(int pos, ArrayList<String> listData, TextViewAdapter mAdapter){
        listData.remove(pos);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void appendElement(String str, ArrayList<String> listData, TextViewAdapter mAdapter){
        listData.add(str);
        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void insertElement(String str, int pos, ArrayList<String> listData, TextViewAdapter mAdapter){
        listData.add(pos, str);
        mAdapter.notifyDataSetChanged();
    }


}

