package com.hannah.hannahworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import org.codehaus.jackson.map.HandlerInstantiator;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runOnUiThread(new  Runnable() {
            @Override
            public  void  run() {
                try {
                    Thread.sleep(1*1000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_alarm) {
            startActivity(new Intent(this, AlarmActivity.class));
            return true;
        }
        if (id == R.id.graph_animation) {
            startActivity(new Intent(this, GraphyAniminationActivity.class));
            return true;
        }
        if (id == R.id. white_board) {
            startActivity(new Intent(this, WhiteboardActivity.class));
            return true;
        }
        if (id == R.id.post_data) {
            startActivity(new Intent(this, PostDataActivity.class));
            return true;
        }
        if (id == R.id.testjson) {

            return true;
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
