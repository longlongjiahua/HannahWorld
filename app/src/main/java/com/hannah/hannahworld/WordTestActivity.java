package com.hannah.hannahworld;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class WordTestActivity extends AbstractSpeakActivity {
    private ArrayList<String> words = new ArrayList<String>();
    private int nth=0;
    private List<EditText> editTextList = new ArrayList<EditText>();
     private Button btnStart;
    Button btnDisplay;
    ImageButton btnAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_word);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        add(this, btnAdd);
        Bundle extra = getIntent().getBundleExtra("extra");
        words = (ArrayList<String>) extra.getSerializable(OneDayActivity.ONEDAY_WORDS);
       // btnStart = (Button) findViewById(R.id.btn_starttest);
    }
    @Override
    protected void onResume(){
        super.onResume();
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showInputMethodPicker();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.testactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.test_check) {
   markTest();
            return true;
        }
        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("MyInt", nth);
        savedInstanceState.putSerializable(OneDayActivity.ONEDAY_WORDS, words);
        // etc.
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        if (savedInstanceState != null) {
            nth = savedInstanceState.getInt("MyInt");
            words = (ArrayList<String>) savedInstanceState.getSerializable(OneDayActivity.ONEDAY_WORDS);
        }
    }


    public void speakWords(String speech) {

        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

 private void markTest(){
                    LinearLayout scrollViewlinerLayout = (LinearLayout) findViewById(R.id.linearLayoutForm);

                    java.util.ArrayList<String> msg = new ArrayList<String>();

                    for (int i = 0; i < scrollViewlinerLayout.getChildCount(); i++)
                    {
                        LinearLayout innerLayout = (LinearLayout) scrollViewlinerLayout.getChildAt(i);
                        EditText edit = (EditText) innerLayout.findViewById(R.id.editDescricao);
                        String mWord = edit.getText().toString();
                        ImageView image = (ImageView) innerLayout.findViewById(R.id.evaluate_img);
                        if(words.get(i).toLowerCase().equals(mWord.toLowerCase())) {
                            image.setImageResource(R.drawable.greentick);
                        }
                        image.setVisibility(View.VISIBLE);

                        msg.add(edit.getText().toString());

                    }
                    Toast t = Toast.makeText(getApplicationContext(), msg.toString(), Toast.LENGTH_SHORT);
                    t.show();

        }

        private void add(final Activity activity, ImageButton btn)
        {
            final LinearLayout linearLayoutForm = (LinearLayout) activity.findViewById(R.id.linearLayoutForm);;

            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(nth < words.size()) {
                        String mWord = words.get(nth);
                        speakWords(mWord);
                    }
                    nth++;
                    final LinearLayout newView = (LinearLayout)activity.getLayoutInflater().inflate(R.layout.rowdetail, null);
                    newView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    linearLayoutForm.addView(newView);
                }
            });
        }
    }