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
import android.view.MotionEvent;
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
import com.hannah.hannahworld.util.Utils;
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
    protected GridView sourceGridView;
    private  LinearLayout targetView;
    private GridView targetGridView;
    public MyDragEventListener myDragEventListener = new MyDragEventListener();
    protected ArrayList<String> sourceGridViewData;
    protected ArrayList<String> targetViewData;
    protected TextViewAdapter droppedAdapter;
    protected TextViewAdapter sourceAdapter;
    private MakeNumberActivity activity;
    protected DragDropIt mDragDropIt;
    private static final String TAG = "DragDropHelp";
    private int clickPos;
    private final static String operators = ")(+-*/";
    private float downX, downY, upX, upY;
    private View clickedView;
    //private Action mTouchSwipeListen = Action.None;

    public DragDropHelp(GridView sourceView,GridView targetGridView, LinearLayout targetView, Activity activity,
                        ArrayList<String> sourceGridViewData, ArrayList<String> targetViewData,
                        TextViewAdapter sourceAdapter, TextViewAdapter droppedAdapter, DragDropIt mDragDropIt) {
        this.targetView = targetView;
        this.targetGridView = targetGridView;
        this.sourceGridView = sourceView;
        this.activity = (MakeNumberActivity) activity;
        this.sourceGridViewData = sourceGridViewData;
        this.targetViewData = targetViewData;
        this.mDragDropIt = mDragDropIt;
        this.sourceAdapter = sourceAdapter;
        this.droppedAdapter = droppedAdapter;
        sourceGridView.setOnTouchListener(sourceGridViewItemLongClickListener);

    }

    public View.OnTouchListener sourceGridViewItemLongClickListener
            = new View.OnTouchListener() {


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    downY = event.getY();
                    int position = Utils.getTouchPosition(sourceGridView, downX, downY, sourceGridViewData);
                    //return false; // allow other events like Click to be processed
                    String str="";
                    if(position>sourceGridViewData.size() || position<0 )
                        return true;
                   clickedView = sourceGridView.getChildAt(position - sourceGridView.getFirstVisiblePosition());
                    clickedView.setBackgroundColor(Color.GREEN);
                    ClipData.Item item = new ClipData.Item("" + sourceGridViewData.get(position));  //TODO : catch exception
                    clickPos = position;
                    String[] clipDescription = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                    ClipData dragData = new ClipData((CharSequence) v.getTag(),
                            clipDescription,
                            item);
                    DragShadowBuilder myShadow = new MyDragShadowBuilder(sourceAdapter.getView(position, null, null));
                    if (sourceGridView.getId() == R.id.grid_view_formula)
                        if (operators.contains(sourceGridViewData.get(position))) {
                            targetView = activity.lvOperator;
                            targetViewData = activity.mOperatorList;
                            droppedAdapter = activity.formulaAdapter;
                        } else {
                            targetView = activity.lvNumber;
                            targetViewData = activity.mNumberList;
                            droppedAdapter = activity.numberAdapter;
                        }
                    //Selected item is passed as item in dragData
                    sourceGridView.setOnDragListener(myDragEventListener);
                    targetView.setOnDragListener(myDragEventListener);
                    Log.i(TAG, sourceGridViewData.get(position) + "fromsource");
                    v.startDrag(dragData, //ClipData
                            myShadow,  //View.DragShadowBuilder
                            sourceGridViewData.get(position),  //Object myLocalState
                            0);    //flags
                    //return true will be prevent click event to be continue. It will be perform only OnItemLongClickListener.
                    return true;
                }

            }
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
                   if(clickedView!=null)
                       clickedView.setBackgroundColor(Color.GREEN);
                    ClipData.Item item = event.getClipData().getItemAt(0);
                    //If apply only if drop on buttonTarget
                    float x = event.getX();
                    if (v == targetView) {
                        String droppedItem = item.getText().toString();
                        Log.i(TAG, droppedItem + "xxx");
                        mDragDropIt.handleSourceData(sourceAdapter, clickPos, sourceGridViewData);
                        mDragDropIt.handleTargetData(targetGridView, x, droppedItem, droppedAdapter, targetViewData);
                        return true;
                    }
                    return false;
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