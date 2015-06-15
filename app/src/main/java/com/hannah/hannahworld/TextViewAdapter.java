package com.hannah.hannahworld;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

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