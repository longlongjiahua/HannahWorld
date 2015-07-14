package com.hannah.hannahworld.util;

import android.util.Log;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by xuyong1 on 14/07/15.
 */
public class Utils {
    public static int getInsertPosition(GridView gridView,float insert) {

        int firstPosition = gridView.getFirstVisiblePosition();
        int lastPosition = gridView.getLastVisiblePosition();
        float leftMost = gridView.getChildAt(firstPosition).getLeft();
        float rightMost = gridView.getChildAt(lastPosition).getRight();
        if(insert< leftMost){
            return 0;
        }
        else if(insert>rightMost){
            return lastPosition;
        }
        else {
            int pos =0;
            for (int i = 1; i < lastPosition; i++) {
                View curr = gridView.getChildAt(i - firstPosition);
                float left = curr.getLeft();
                float right = curr.getRight();
                if (left < insert && right > insert) {
                    //Log.i(TAG, "dropplace" + i + ":" + left + " " + right);
                    return i;
                }
                //Log.i(TAG, "place" + i + ":" + left + " " + right);
                //Toast.makeText(getApplication(), "place" + left + right, Toast.LENGTH_LONG);
            }
            return pos;
        }
    }
}
