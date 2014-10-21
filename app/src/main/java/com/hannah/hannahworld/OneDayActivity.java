package com.hannah.hannahworld;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.hannah.hannahworld.Data.OneRow;
import com.hannah.hannahworld.Data.WordDB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class OneDayActivity extends Activity{
    private ProgressDialog progressDialog;
    static  final  int TOTAL_ROW = 12;
    private boolean dbRightAge = true;
    private static final Logger log = LoggerFactory.getLogger(OneDayActivity.class);
    private WordDB wordDB;
    private int week;
    private final String urlLink = "http://192.168.1.65/word.json";
    private HashMap<Integer, List<String>> weekWords = new HashMap<Integer, List<String>>();

    @Override
public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            //loadWords(final String urlLink,final String dbName,final String tableName, final String createTimePref)
            loadWords(urlLink,"myActivities", "myWords");
        }
        setContentView(R.layout.one_day);
        Intent in = getIntent();
        Bundle b = in.getExtras();
        if(b!=null) {
            week = b.getInt("tagcontent");
        }
        if(weekWords.get(week)!=null) {
            init();
            Button btSave = (Button) findViewById(R.id.button_save);
            btSave.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              for (int i = 0; i < TOTAL_ROW; i++) {
                                                  EditText onerow = (EditText) findViewById(i);
                                                  if (onerow != null && onerow.getText() != null) {
                                                      String word = onerow.getText().toString().trim();
                                                      if (!word.isEmpty()) {

                                                      }

                                                  }
                                              }

                                          }


                                      }

            );

        }

  }
@Override
protected void onResume() {
        super.onResume();


        }


   private void loadWords(final String urlLink,final String dbName,final String tableName){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.load_data));
        if(dbRightAge){
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        new AsyncTask<Void, String, Void>() {
            private OneRow[] tmp;
            private List<OneRow> listWords;
            @Override
            protected Void doInBackground(Void... voids) {

                wordDB = new WordDB(OneDayActivity.this, progressDialog,dbName, urlLink,tableName);

                wordDB.open();

                listWords = wordDB.getAllEntries();
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                for(OneRow one : listWords){
                    if(weekWords.get(one.week)==null){
                        weekWords.put(one.week, new ArrayList<String>());
                    }
                    weekWords.get(one.week).add(one.word);

                }
             progressDialog.hide();
        }
        }.execute();
        //prepare Markers for CoinMap
   }


public void init() {
        TableLayout stk = (TableLayout) findViewById(R.id.table_vocabulary);
        if(weekWords.get(week)!=null){


        }

        for (int i = 0; i < TOTAL_ROW; i++) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText("" + i);
            //t1v.setTextColor(Color.WHITE);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
           final EditText t2v = new EditText(this);
            //t2v.setImeOptions(EditorInfo.IME_ACTION_DONE);
            t2v.setOnEditorActionListener(new t2v.OnEditorActionListener() {
               // @Override
                public boolean onEditorAction(EditText v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        //do here your stuff f
                        return true;
                    }
                    return false;
                } });


            t2v.setHint("Enter word Here");
            t2v.setId(i);
            tbrow.addView(t2v);
            stk.addView(tbrow);
        }



    }


}