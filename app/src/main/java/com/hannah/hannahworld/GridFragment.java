package com.hannah.hannahworld;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public final class GridFragment extends Fragment {
    MainActivity activity;
    GridView gridView;
    List<String> weeks = new ArrayList<String>();
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
    }
        @Override
        public View onCreateView ( final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState)
        {
            weeks.clear();
            for(int i=1; i<13; i++){
                weeks.add("Week" + i);
            }
            View view= inflater.inflate(R.layout.gridfragment, container, false);
            gridView = (GridView) view.findViewById(R.id.main_grid);
            gridView.setAdapter(new WordAdapter(activity, weeks));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    TextView tv = (TextView) v.findViewById(R.id.aword);
                    int week = 0;
                    try {
                        week = Integer.parseInt(tv.getText().toString());
                    } catch(NumberFormatException nfe) {
                        System.out.println("Could not parse " + nfe);
                    }
                    Intent i= new Intent(v.getContext(), OneDayActivity.class);
                    i.putExtra("tagcontent", week);
                    //log.info("distanceToSend::" + im.distance);
                    v.getContext().startActivity(i);
                    //Toast.makeText(getActivity(),tv.getText(), Toast.LENGTH_SHORT).show();
                }
            });
            return view;

        }

        @Override
        public void onViewCreated ( final View view, final Bundle savedInstanceState)
        {
            super.onViewCreated(view, savedInstanceState);
        }
    }




