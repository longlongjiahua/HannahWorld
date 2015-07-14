package com.hannah.hannahworld;

import android.widget.GridView;

import java.util.ArrayList;

public  interface DragDropIt {
    void handleSourceData(TextViewAdapter sourceAdapter, int clickPos, ArrayList<String> listData);
    void handleTargetData(GridView mGridView,float x,String str,TextViewAdapter targetAdapter,ArrayList<String> listData);
}
