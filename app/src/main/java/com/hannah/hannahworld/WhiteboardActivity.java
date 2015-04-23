package com.hannah.hannahworld;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Color;
import android.os.Bundle;


public class WhiteboardActivity extends Activity {


        DrawView drawView;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            drawView = new DrawView(this);
            drawView.setBackgroundColor(Color.WHITE);
            setContentView(drawView);
            drawView.requestFocus();

        }
    }