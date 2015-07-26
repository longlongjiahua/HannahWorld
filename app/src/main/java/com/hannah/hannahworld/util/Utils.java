package com.hannah.hannahworld.util;

import android.util.Log;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuyong1 on 14/07/15.
 */
public class Utils {
    public static int getInsertPosition(GridView gridView, float insert, List<String> list) {

        if (list == null || list.size() == 0)
            return 0;

        int firstPosition = gridView.getFirstVisiblePosition();
        int lastPosition = gridView.getLastVisiblePosition();
        float leftMost = gridView.getChildAt(firstPosition).getLeft();
        float rightMost = gridView.getChildAt(lastPosition).getRight();
        if (insert < leftMost) {
            return 0;
        }
        if (insert > rightMost) {
            return list.size();
        }
        int pos = 0;
        for (int i = 1; i <=lastPosition; i++) {
            View last = gridView.getChildAt(i-1 - firstPosition);
            View curr = gridView.getChildAt(i - firstPosition);
            float left = last.getLeft();
            float right = curr.getRight();
            if (left < insert && right > insert) {
                Log.i("getInsertPosition", "dropplace" + i + ":" + left + " " + right);
                return i;
            }
            //Log.i(TAG, "place" + i + ":" + left + " " + right);
            //Toast.makeText(getApplication(), "place" + left + right, Toast.LENGTH_LONG);
        }
        return pos;
    }
    public static int getTouchPosition(GridView gridView, float touchX, float touchY, List<String> list) {
        int firstPosition = gridView.getFirstVisiblePosition();
        int lastPosition = gridView.getLastVisiblePosition();
        float leftMost = gridView.getChildAt(firstPosition).getLeft();
        float rightMost = gridView.getChildAt(lastPosition).getRight();
        float top = gridView.getChildAt(firstPosition).getTop();
        float bottom  = gridView.getChildAt(firstPosition).getBottom();
        Log.i("getTouchPosition", "place:" + touchX + ":" +touchY +":" +top + " " + bottom+":" + leftMost);
        if(touchY >bottom || touchY <top)
            return -1;
        if(touchX <leftMost || touchX >rightMost)
            return -1;
        int pos = -1;
        for (int i = 0; i <= lastPosition; i++) {
            View curr = gridView.getChildAt(i - firstPosition);
            float left = curr.getLeft();
            float right = curr.getRight();
            if (touchX >left && touchX <right){
                return i;
            }
            //Log.i(TAG, "place" + i + ":" + left + " " + right);
            //Toast.makeText(getApplication(), "place" + left + right, Toast.LENGTH_LONG);
        }
        return pos;
    }

}
