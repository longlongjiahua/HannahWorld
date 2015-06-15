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
 */

public class DragDropHelp {

    LinearLayout targetLayout;
    GridView listSource, listTarget;
    MyDragEventListener myDragEventListener = new MyDragEventListener();
    ArrayList<String> listSourceData;
    Activity activity;
    List<String> droppedList;
    ArrayAdapter<String> droppedAdapter;
    public DragDropHelp(GridView targetView, GridView sourceView, Activity activity, ArrayList<String> listSourceData) {
        this.listTarget = targetView;
        this.listSource = sourceView;
        this.activity = activity;
        this.listSourceData = listSourceData;
    }

    public void init() {
        final String SOURCELIST_TAG = "listSource";
        final String TARGETLIST_TAG = "listTarget";
        final String TARGETLAYOUT_TAG = "targetLayout";

        listSource.setTag(SOURCELIST_TAG);
        listTarget.setTag(TARGETLIST_TAG);
        targetLayout.setTag(TARGETLAYOUT_TAG);
        listSource.setAdapter(new TextViewAdapter(activity, listSourceData));
        listSource.setOnItemLongClickListener(listSourceItemLongClickListener);


        droppedList = new ArrayList<String>();
        droppedAdapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_list_item_1, droppedList);
        listTarget.setAdapter(droppedAdapter);

        listSource.setOnDragListener(myDragEventListener);
        targetLayout.setOnDragListener(myDragEventListener);

    }

    OnItemLongClickListener listSourceItemLongClickListener
            = new OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> l, View v,
                                       int position, long id) {

            //Selected item is passed as item in dragData
            ClipData.Item item = new ClipData.Item(listSourceData.get(position));

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
                    //All involved view accept ACTION_DRAG_STARTED for MIMETYPE_TEXT_PLAIN
                    if (event.getClipDescription()
                            .hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        return true; //Accept
                    } else {
                        return false; //reject
                    }
                case DragEvent.ACTION_DRAG_ENTERED:
                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:  //
                    //commentMsg += v.getTag() + " : ACTION_DRAG_LOCATION - " + event.getX() + " : " + event.getY() + "\n";
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return true;
                case DragEvent.ACTION_DROP:
                    // Gets the item containing the dragged data
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    //If apply only if drop on buttonTarget
                    if (v == targetLayout) {
                        String droppedItem = item.getText().toString();
                        //Here a callback
                        droppedList.add(droppedItem);
                        droppedAdapter.notifyDataSetChanged();
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
