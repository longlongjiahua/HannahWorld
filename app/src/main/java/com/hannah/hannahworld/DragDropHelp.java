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

public class DragDropHelp {

    LinearLayout targetLayout;
    GridView listSource, listTarget;
    MyDragEventListener myDragEventListener = new MyDragEventListener();
    ArrayList<String> listData;
    Activity activity;
    List<String> droppedList;
    ArrayAdapter<String> droppedAdapter;
    public DragDropHelp(GridView targetView, GridView sourceView, Activity activity, ArrayList<String> listData) {
        this.listTarget = targetView;
        this.listSource = sourceView;
        this.activity = activity;
        this.listData = listData;
    }

    public void init() {
        final String SOURCELIST_TAG = "listSource";
        final String TARGETLIST_TAG = "listTarget";
        final String TARGETLAYOUT_TAG = "targetLayout";

        listSource.setTag(SOURCELIST_TAG);
        listTarget.setTag(TARGETLIST_TAG);
        targetLayout.setTag(TARGETLAYOUT_TAG);
        listSource.setAdapter(new TextViewAdapter(activity, listData));
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
            ClipData.Item item = new ClipData.Item(listData.get(position));

            String[] clipDescription = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData dragData = new ClipData((CharSequence) v.getTag(),
                    clipDescription,
                    item);
            DragShadowBuilder myShadow = new MyDragShadowBuilder(v);

            v.startDrag(dragData, //ClipData
                    myShadow,  //View.DragShadowBuilder
                    month[position],  //Object myLocalState
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
                case DragEvent.ACTION_DRAG_LOCATION:
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

    public class TextViewAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<String> texts;

        public TextViewAdapter(Context c, ArrayList<String> texts) {
           this.context = c;
            this.texts = texts;
        }
        public int getCount() {
            return texts.size();
        }
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textview;
            if (convertView == null) {
                textview = new Button(context);
                textview.setLayoutParams(new GridView.LayoutParams(75, 75));
                textview.setPadding(1, 1, 1, 1);
                textview.setFocusable(false);
                textview.setClickable(false);
            } else {
                textview = (Button) convertView;
            }
            textview.setText(texts.get(position));
            textview.setTextColor(Color.GREEN);
            textview.setId(position);
            return textview;
        }
    }
}
