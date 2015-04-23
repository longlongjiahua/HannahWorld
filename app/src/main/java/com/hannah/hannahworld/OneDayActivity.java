package com.hannah.hannahworld;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import com.hannah.hannahworld.data.OneRow;
import com.hannah.hannahworld.data.WordDB;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class OneDayActivity extends Activity  implements AdapterView.OnItemClickListener,OnInitListener {
    private String TAG = "OneDayActivity";
    private ProgressDialog progressDialog;
    private final String RTTAG = "WKWORDS";

    static final int TOTAL_ROW = 12;
    private boolean dbRightAge = true;
    private WordDB wordDB;
    private int week;
    //private final String urlLink = "http://192.168.1.65/word.json";
    private final String urlLink = "https://docs.google.com/spreadsheets/d/1onnIEllDdnts89nPIqhATsQ5EokpM5_ZFqERPOiG_5U/gviz/tq";
    private HashMap<Integer, ArrayList<String>> weekWords = new HashMap<Integer, ArrayList<String>>();

    private ArrayList<Integer> list = new ArrayList<Integer>();
    private ListView lv;
    Button btSave;
    private WordArrayAdapter adapter;
    private KeepDataFragment rtFragment;

    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        Bundle b = in.getExtras();
        if (b != null) {
            week = b.getInt("tagcontent");
        }
        setContentView(R.layout.one_day);
        btSave = (Button) findViewById(R.id.words_save);
        lv = (ListView) findViewById(R.id.listView_oneday);


        //check for TTS data
        Intent checkTTSIntent = new Intent();
       checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        FragmentManager fm = getFragmentManager();
        rtFragment = (KeepDataFragment) fm.findFragmentByTag(RTTAG);
        if (rtFragment == null) {
            //if(savedInstanceState==null) {
            //loadWords(final String urlLink,final String dbName,final String tableName, final String createTimePref)
            rtFragment = new KeepDataFragment();
            fm.beginTransaction().add(rtFragment, RTTAG).commit();
            Log.i("LoadData:: ","Start ");
            loadWords(urlLink, "myActivities", "myWords");
        }

        else if (rtFragment != null && rtFragment.getWkWords() != null && (weekWords == null||weekWords.size()==0)) {
            Log.i("HaveData:: ", "HaveData ");
            weekWords = rtFragment.getWkWords();
            init();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        rtFragment.setWkWords(weekWords);

    }

    //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
               Log.i(TAG, "the user has the necessary data - create the TTS");
                myTTS = new TextToSpeech(this, this);
            } else {
                Log.i(TAG, "no data - install it now");
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
@Override
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if (myTTS.isLanguageAvailable(Locale.US) == TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        } else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
            Log.i(TAG,  "Not Installed");
        }
    }

    private void loadWords(final String urlLink, final String dbName, final String tableName) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.load_data));
        if (dbRightAge) {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        new AsyncTask<Void, String, Void>() {
            private OneRow[] tmp;
            private List<OneRow> listWords;

            @Override
            protected Void doInBackground(Void... voids) {

                wordDB = new WordDB(OneDayActivity.this, progressDialog, dbName, urlLink, tableName);

                wordDB.open();

                listWords = wordDB.getAllEntries();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                for (OneRow one : listWords) {
                    Log.i("ListWord::", "week::" + week + one.word);
                    if (weekWords.get(one.week) == null) {
                        weekWords.put(one.week, new ArrayList<String>());
                    }
                    weekWords.get(one.week).add(one.word);

                }
                FragmentManager fm = getFragmentManager();
                rtFragment = (KeepDataFragment) fm.findFragmentByTag(RTTAG);
                rtFragment.setWkWords(weekWords);
                init();
                progressDialog.dismiss();
            }
        }.execute();

    }

    public void init() {
        if (weekWords.get(week) == null) {
//save new words
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

        if (weekWords.get(week) != null) {
            Log.i(TAG, "HasWods::");
            adapter = new WordArrayAdapter(this, weekWords.get(week));
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(this);


        }
        /*else {
            adapter = new WordArrayAdapter();
            lv.setItemsCanFocus(true);

            for (int i = 0; i < 10; i++) {
                list.add(i);
            }
            lv.setAdapter(adapter);
        }
        */

    }

    /**
     * Callback for ListView
     */
    public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_LONG).show();
        Log.i(TAG, "CLICK Here");
        TextView oneTV = (TextView) view.findViewById((R.id.editText12));
        if (!oneTV.getText().toString().isEmpty()) {

            String oneW = oneTV.getText().toString();
            speakWords(oneW);
        }

    }

    private void speakWords(String speech) {

        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }


}