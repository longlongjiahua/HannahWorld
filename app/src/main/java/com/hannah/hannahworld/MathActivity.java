/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hannah.hannahworld;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MathActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments representing
     * each object in a collection. We use a {@link android.support.v4.app.FragmentStatePagerAdapter}
     * derivative, which will destroy and re-create fragments as needed, saving and restoring their
     * state in the process. This is important to conserve memory and is a best practice when
     * allowing navigation between objects in a potentially large collection.
     */
    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;

    private static String keys[] = {"1", "2", "3", "*",
            "4", "5", "6", "-",
            "7", "8", "9", "+",
            "0", "start", "X", "/",
    };
    public int operation; //0: x, 1 : *, 2 +, 3: -;

    public class Numbers {
        public Integer first, second, third;
    }

    public static String NUMBERS = "numbers";
    public static final String INTENT_EXTRA_MINUTES = "extra_math_activity_minutes";
    public final static int NQS = 30;
    public Integer[][] mNums = new Integer[NQS][3];
    public MathFragment[] mathFragments = new MathFragment[NQS];

    public int currentPageNo = 0;
    private int rightN;
    private static final String FORMAT = "%02d:%02d:%02d";
    private Intent broadcastIntent;
    private ButtonAdapter  mBtAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will display the object collection.
     */
    ViewPager mViewPager;
    public TextView tvScore,tvTimeCountDown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastIntent = new Intent(this, BroadcastTimeCountService.class);
        operation = getIntent().getExtras().getInt(MainPageViewActivity.MATHOPERATION);
          //Toast.makeText(getBaseContext(), "Operation:" + operation, Toast.LENGTH_LONG).show();
        setContentView(R.layout.activity_math);
        tvScore = (TextView) findViewById(R.id.tv_your_score);
        tvTimeCountDown = (TextView) findViewById(R.id.tv_time_countdown);
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        mBtAdapter = new ButtonAdapter(this, keys);
        gridView.setAdapter(mBtAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                String pre = mathFragments[currentPageNo].mTv5.getText().toString();
                char add = keys[position].charAt(0);
                String cur = pre;
                if(add=='s') {
                    final Intent mServiceIntent = new Intent(getApplicationContext(), BroadcastTimeCountService.class);
                    mServiceIntent.putExtra(INTENT_EXTRA_MINUTES, 1);
                    MathActivity.this.startService(mServiceIntent);
                    keys[13]="Done";
                    mBtAdapter.notifyDataSetChanged();
                }
                if (add == 'X' || (add >= '0' && add <= '9')) {

                } else return;
                if (add == 'X') {
                    cur = removeLastChar(cur);
                } else if (add >= '0' && add <= '9') {
                    cur += add;
                }
                mathFragments[currentPageNo].mTv5.setText(cur);
                if (cur.length() == 0) {
                    mNums[currentPageNo][2] = null;
                } else {
                    int curNum = Integer.parseInt(cur);
                    mNums[currentPageNo][2] = curNum;
//Toast.makeText(getBaseContext(),"Cselected" +curNum +" "+(mNums[currentPageNo][0] * mNums[currentPageNo][1]),Toast.LENGTH_SHORT).show();

                    if (operation == 0 && curNum == mNums[currentPageNo][0] * mNums[currentPageNo][1]) {
                        mathFragments[currentPageNo].mTv5.setBackgroundResource(R.drawable.rectangle_back2);
                        rightN++;
                        DecimalFormat df = new DecimalFormat("#.0");
                        tvScore.setText("Score: " + df.format(100.00 * rightN / NQS));
                        //Toast.makeText(getBaseContext(), "pic" + (position + 1) + " selected", Toast.LENGTH_SHORT).show();
                    }
                    if (operation == 1 && curNum == mNums[currentPageNo][0] / mNums[currentPageNo][1] &&
                            mNums[currentPageNo][0] % mNums[currentPageNo][1] == 0) {
                        mathFragments[currentPageNo].mTv5.setBackgroundResource(R.drawable.rectangle_back2);
                        rightN++;
                        DecimalFormat df = new DecimalFormat("#.0");
                       tvScore.setText("Score: " + df.format(100.00 * rightN / NQS));
                    }

                }
            }
        });
        mDemoCollectionPagerAdapter = new DemoCollectionPagerAdapter(getSupportFragmentManager());
        // Set up action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home button should show an "Up" caret, indicating that touching the
        // button will take the user one step up in the application's hierarchy.
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the ViewPager, attaching the adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                currentPageNo = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }
    public void onResume() {
        super.onResume();
       // startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastTimeCountService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(broadcastIntent);
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String time = intent.getStringExtra(BroadcastTimeCountService.TIMELEFT);
            Log.i("BroadcastReceiver:::", time);
            //mathFragments[currentPageNo].tvTimeCountDown.setText(time);
           tvTimeCountDown.setText(time);
        }
    };
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(NUMBERS, mNums);
        savedInstanceState.putInt(MainPageViewActivity.MATHOPERATION, operation);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        operation = savedInstanceState.getInt(MainPageViewActivity.MATHOPERATION);
        mNums = (Integer[][]) savedInstanceState.getSerializable(NUMBERS);
    }

    private String removeLastChar(String str) {
        str.trim();
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed in the action bar.
                // Create a simple intent that starts the hierarchical parent activity and
                // use NavUtils in the Support Package to ensure proper handling of Up.
                Intent upIntent = new Intent(this, MainActivity.class);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is not part of the application's task, so create a new task
                    // with a synthesized back stack.
                    TaskStackBuilder.from(this)
                            // If there are ancestor activities, they should be added here.
                            .addNextIntent(upIntent)
                            .startActivities();
                    finish();
                } else {
                    // This activity is part of the application's task, so simply
                    // navigate up to the hierarchical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link android.support.v4.app.FragmentStatePagerAdapter} that returns a fragment
     * representing an object in the collection.
     */
    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {

        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = MathFragment.instance(i);
            mathFragments[i] = (MathFragment) fragment;
            return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return NQS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "q " + (position + 1);
        }
    }

    public class ButtonAdapter extends BaseAdapter {
        private Context context;
        private String[] keys;
        public ButtonAdapter(Context c, String[] keys) {
            context = c;
            this.keys= keys;
        }
        public int getCount() {
            return keys.length;
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }
        public View getView(int position, View convertView, ViewGroup parent) {
            Button btn;
            if (convertView == null) {
                btn = new Button(context);
                btn.setLayoutParams(new GridView.LayoutParams(80, 80));
                btn.setPadding(5, 5, 5, 5);
                btn.setFocusable(false);
                btn.setClickable(false);
            } else {
                btn = (Button) convertView;
            }
            btn.setText(keys[position]);
            btn.setTextColor(Color.WHITE);
            btn.setId(position);
            return btn;
        }
    }

}
