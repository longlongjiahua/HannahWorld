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

import com.hannah.hannahworld.Data.OneRow;
import com.hannah.hannahworld.Data.WordDB;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class OneDayActivity extends Activity  implements AdapterView.OnItemClickListener,OnInitListener {
    private ProgressDialog progressDialog;
    private final String RTTAG = "WKWRODS";
    static  final  int TOTAL_ROW = 12;
    private boolean dbRightAge = true;
    private static final Logger log = LoggerFactory.getLogger(OneDayActivity.class);
    private WordDB wordDB;
    private int week;
    //private final String urlLink = "http://192.168.1.65/word.json";
    private final String urlLink = "https://instacoin.ca/json/test.json";
    private HashMap<Integer, ArrayList<String>> weekWords = new HashMap<Integer, ArrayList<String>>();

    private ArrayList<Integer> list=new ArrayList<Integer>();
    private ListView lv;
    private LvAdapter adapter;
    private RetainedFragment rtFragment;

    //TTS object
    private TextToSpeech myTTS;
    //status check code
    private int MY_DATA_CHECK_CODE = 0;

    @Override
public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        Bundle b = in.getExtras();
        if(b!=null) {
            week = b.getInt("tagcontent");
        }
        setContentView(R.layout.one_day);

        //check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

        FragmentManager fm = getFragmentManager();
        rtFragment = (RetainedFragment) fm.findFragmentByTag(RTTAG);
        if(rtFragment==null){
         //if(savedInstanceState==null) {
             //loadWords(final String urlLink,final String dbName,final String tableName, final String createTimePref)
             rtFragment = new RetainedFragment();
             fm.beginTransaction().add(rtFragment, RTTAG).commit();
             loadWords(urlLink, "myActivities", "myWords");
         //}
        }
        if(rtFragment!=null && rtFragment.getWkWords()!=null && weekWords==null){
            weekWords = rtFragment.getWkWords();

        }


        //init();


  }
@Override
protected void onResume() {
        super.onResume();


        }
    //act on result of TTS data check
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                //the user has the necessary data - create the TTS
                myTTS = new TextToSpeech(this, this);
            }
            else {
                //no data - install it now
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
    public void onInit(int initStatus) {

        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
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
                    log.info("week::" + week + one.word);
                    if(weekWords.get(one.week)==null){
                        weekWords.put(one.week, new ArrayList<String>());
                    }
                    weekWords.get(one.week).add(one.word);

                }
                FragmentManager fm = getFragmentManager();
                rtFragment = (RetainedFragment) fm.findFragmentByTag(RTTAG);
                rtFragment.setWkWords(weekWords);
                init();
             progressDialog.dismiss();
        }
        }.execute();

   }


public void init() {
    if(weekWords.get(week)==null) {

        Button btSave = (Button) findViewById(R.id.words_save);
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
    lv = (ListView) findViewById(R.id.listView_oneday);

    if (weekWords.get(week) != null) {
        adapter = new LvAdapter(weekWords.get(week));
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);


    } else {
        adapter = new LvAdapter();
        lv.setItemsCanFocus(true);

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        lv.setAdapter(adapter);
    }

}

    /**
     * Callback for ListView
     */
    public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), "Click", Toast.LENGTH_LONG).show();
      TextView oneTV =  (TextView) view.findViewById((R.id.editText12));
        if(!oneTV.getText().toString().isEmpty()){

            String oneW = oneTV.getText().toString();
            speakWords(oneW);
        }

    }

    private void speakWords(String speech) {

        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

        public class LvAdapter extends BaseAdapter {
        private ArrayList<String> words;
        private LayoutInflater mInflater;
        public LvAdapter(ArrayList<String> words){
            this.words = words;
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public LvAdapter(){
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            if(words!=null){
                return words.size();
            }
            else {
                return list.size();
            }
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            final ViewHolder holder;
            //convertView=null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.one_day_words, null); // this is your cell
                holder.tvId = (TextView) convertView.findViewById(R.id.sid);
                holder.etSpace = (EditText) convertView.findViewById(R.id.editText12);
                if(words!=null){

                    holder.tvId.setText(""+position);
                    if(words.get(position)!=null) {
                        holder.etSpace.setText(words.get(position));
                        //holder.etSpace.setFocusable(false);
                        holder.etSpace.setEnabled(false);
                        //holder.etSpace.setCursorVisible(false);
                        //holder.etSpace.setKeyListener(null);
                       // holder.etSpace.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
                else {
                    holder.etSpace.setTag(position);
                    holder.tvId.setText(list.get(position).toString());
                }
                convertView.setTag(holder);

            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            if(words == null) {
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
                            Toast.makeText(getApplicationContext(), "Please enter some value", Toast.LENGTH_SHORT).show();
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


            return convertView;
        }

    }
    public class ViewHolder {
        EditText etSpace;
        TextView tvWord;
        TextView tvId;
    }
    public class RetainedFragment extends Fragment {
        // data object we want to retain
        private HashMap<Integer, ArrayList<String>> wkWords = new HashMap<Integer, ArrayList<String>>();
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // retain this fragment
            setRetainInstance(true);
        }
        public void setWkWords(HashMap<Integer, ArrayList<String>> wkWords){
                this.wkWords = wkWords;
        }
        public HashMap<Integer, ArrayList<String>> getWkWords(){

                return wkWords;
        }

    }



}