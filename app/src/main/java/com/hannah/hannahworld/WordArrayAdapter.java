package com.hannah.hannahworld;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class WordArrayAdapter extends ArrayAdapter<String> {

        private ArrayList<String> words;
        private LayoutInflater mInflater;
    private final Context context;

        public WordArrayAdapter(Context context, ArrayList<String> words) {
            super(context, R.layout.one_word, words);
            this.words = words;
            this.context = context;
        }
       @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null){
                LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.one_day_words, parent, false);
        }
            final ViewHolder holder;
                holder = new ViewHolder();
                holder.tvId = (TextView) convertView.findViewById(R.id.sid);
                holder.etSpace = (EditText) convertView.findViewById(R.id.editText12);
                if (words != null) {
                    holder.tvId.setText("" + position);
                    if (words.get(position) != null) {
                        holder.etSpace.setText(words.get(position));
                        //holder.etSpace.setFocusable(false);
                        holder.etSpace.setEnabled(false);
                        //holder.etSpace.setCursorVisible(false);
                        //holder.etSpace.setKeyListener(null);
                        // holder.etSpace.setBackgroundColor(Color.TRANSPARENT);
                    }
                } else {
                    holder.etSpace.setTag(position);
                   // holder.tvId.setText(list.get(position).toString());
                }
                convertView.setTag(holder);

            /*
            if (words == null) {
                int tag_position = (Integer) holder.etSpace.getTag();
                holder.etSpace.setId(tag_position);

                holder.etSpace.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before,
                                              int count) {
                        final int position2 = holder.etSpace.getId();
                        final EditText etSpace = (EditText) holder.etSpace;
                        if (etSpace.getText().toString().length() > 0) {
                            list.set(position2, Integer.parseInt(etSpace.getText().toString()));
                        } else {
                            //Toast.makeText(getApplicationContext(), "Please enter some value", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }

                });
            }
            */
            return convertView;
        }

    private class ViewHolder {
        EditText etSpace;
        TextView tvWord;
        TextView tvId;
    }
}
