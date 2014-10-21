package com.hannah.hannahworld;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by xuyong1 on 10/19/14.
 */
public class OneDayWordAdapter extends BaseAdapter {
    private List<String> wordList;

    private final Context context;
    private final LayoutInflater inflater;
    private String selectedAddress = null;

    public OneDayWordAdapter(final Context context,   final List<String> wordList)
    {
        final Resources res = context.getResources();
        this.wordList= wordList;

        this.context = context;
        inflater = LayoutInflater.from(context);
    }



    public void setSelectedAddress(final String selectedAddress)
    {
        this.selectedAddress = selectedAddress;

        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return wordList.size();
    }

    @Override
    public Object getItem(final int position)
    {
        return wordList.get(position);
    }

    @Override
    public long getItemId(final int position)
    {
        //return keys.get(position).hashCode();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View oneRow;
        if (convertView == null) {
            oneRow = new View(context);
            oneRow = inflater.inflate(R.layout.one_word, null);
            // set value into textview
            TextView textView = (TextView) oneRow
                    .findViewById(R.id.aword);
            textView.setText(wordList.get(position));

        } else {
            oneRow = (View) convertView;
        }

        return oneRow;
    }
 }
