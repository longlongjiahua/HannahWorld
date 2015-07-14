package com.hannah.hannahworld;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.GridView;
import android.widget.TextView;

import com.hannah.hannahworld.TextViewAdapter;
/*
requirement analysis
four way drop: number-><-formula
               operator-><-formula
 the fifth way drop: switch strings within formula

if drop to the formula line, insert position dependent on the drop site
if drop to number line, always add the number to the end(append)
if drop to operator line, the operator keep same;
the common part is to drag and drop

using an interface DragDropIt as callback;
 */
// this is a base class

public class DragDropHelp {

    protected LinearLayout targetLayout;
    protected GridView listSource, listTarget;
    protected MyDragEventListener myDragEventListener = new MyDragEventListener();
    protected ArrayList<String> listSourceData;
    protected ArrayList<String> listTargetData;
    protected Activity activity;
    protected TextViewAdapter droppedAdapter;
    protected TextViewAdapter sourceAdapter;
    protected DragDropIt mDragDropIt;
    private static final String TAG = "DragDropHelp";
    private int clickPos;

    public DragDropHelp(GridView sourceView, GridView targetView, Activity activity,
                        ArrayList<String> listSourceData, ArrayList<String> listTargetData, DragDropIt mDragDropIt) {
        this.listTarget = targetView;
        this.listSource = sourceView;
        this.activity = activity;
        this.listSourceData = listSourceData;
        this.listTargetData = listTargetData;
        this.mDragDropIt = mDragDropIt;
        init();
    }

    public void init() {
        final String SOURCELIST_TAG = "listSource";
        final String TARGETLIST_TAG = "listTarget";
        final String TARGETLAYOUT_TAG = "targetLayout";

        listSource.setTag(SOURCELIST_TAG);
        listTarget.setTag(TARGETLIST_TAG);
        sourceAdapter = new TextViewAdapter(activity, listSourceData);
        listSource.setAdapter(sourceAdapter);
        listSource.setOnItemLongClickListener(listSourceItemLongClickListener);

        droppedAdapter = new TextViewAdapter(activity, listTargetData);
        listTarget.setAdapter(droppedAdapter);
        listSource.setOnDragListener(myDragEventListener);
        listTarget.setOnDragListener(myDragEventListener);

    }

    OnItemLongClickListener listSourceItemLongClickListener
            = new OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> l, View v,
                                       int position, long id) {

            //Selected item is passed as item in dragData
            Log.i(TAG, listSourceData.get(position) + "fromsource");
            clickPos = position;
            ClipData.Item item = new ClipData.Item("" + listSourceData.get(position));

            String[] clipDescription = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData dragData = new ClipData((CharSequence) v.getTag(),
                    clipDescription,
                    item);
            DragShadowBuilder myShadow = new MyDragShadowBuilder(v);

            v.startDrag(dragData, //ClipData
                    myShadow,  //View.DragShadowBuilder
                    listSourceData.get(position),  //Object myLocalState
                    0);    //flags

            return true;
        }
    };

    private static class MyDragShadowBuilder extends View.DragShadowBuilder {
        private static Drawable shadow;

        public MyDragShadowBuilder(View v) {
            super(v);
            shadow = new ColorDrawable(Color.LTGRAY);
        }

        @Override
        public void onProvideShadowMetrics(Point size, Point touch) {
            int width = getView().getWidth();
            int height = getView().getHeight();

            shadow.setBounds(0, 0, width, height);
            size.set(width, height);
            touch.set(width / 2, height / 2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            shadow.draw(canvas);
        }

    }

    protected class MyDragEventListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            final int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:

                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:  //
                    //commentMsg += v.getTag() + " : ACTION_DRAG_LOCATION - " + event.getX() + " : " + event.getY() + "\n";
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    // Gets the item containing the dragged data
                    Log.i(TAG, "dropxxx");
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    //If apply only if drop on buttonTarget
                    float x = event.getX();
                    if (v == listTarget) {

                        String droppedItem = item.getText().toString();
                        Log.i(TAG, droppedItem + "xxx");
                        mDragDropIt.handleSourceData(sourceAdapter, clickPos, listSourceData);
                        mDragDropIt.handleTargetData(listTarget, x, droppedItem,droppedAdapter, listTargetData);
                        //listTargetData.add(droppedItem);
                        //droppedAdapter.notifyDataSetChanged();
                        return true;
                    } else {
                        return false;
                    }
                case DragEvent.ACTION_DRAG_ENDED:
                    if (event.getResult()) {
                    } else {
                    }
                    return true;
                default: //unknown case
                    return false;
            }
        }
    }
}
