package com.hannah.hannahworld;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.HashMap;

public class KeepDataFragment extends Fragment {
        // data object we want to retain
        private HashMap<Integer, ArrayList<String>> wkWords = new HashMap<Integer, ArrayList<String>>();
        public int week;
  @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // retain this fragment
            setRetainInstance(true);
        }

        public void setWkWords(HashMap<Integer, ArrayList<String>> wkWords) {
            this.wkWords = wkWords;
        }

        public HashMap<Integer, ArrayList<String>> getWkWords() {

            return wkWords;
        }

    }
