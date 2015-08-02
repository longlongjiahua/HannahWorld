package com.hannah.hannahworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by xuyong1 on 01/08/15.
 */
public class MainMathActivity extends Activity {
    private Button btMultification;
    private Button btDivision;
    private RadioGroup rgMath;
    private Button btChoose;
    public static final String MAMKNUMBERMETHODS = "makenumbermethods";
    public static final String NUMBEROFINPUT = "number of input";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttontomath);
        btMultification = (Button) findViewById(R.id.bt_multification);
        btMultification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMathActivity.this, MathActivity.class);
                intent.putExtra(MAMKNUMBERMETHODS, 0);
                startActivity(intent);
            }
        });
        btDivision = (Button) findViewById(R.id.bt_division);
        btDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMathActivity.this, MathActivity.class);
                intent.putExtra(MAMKNUMBERMETHODS, 1);
                startActivity(intent);
            }
        });
        rgMath = (RadioGroup) findViewById(R.id.radiogroup_makenumber);
        rgMath.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
            }
        });
        btChoose = (Button) findViewById(R.id.chooseBtn);
        btChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = rgMath.getCheckedRadioButtonId();
                Intent intent = new Intent(MainMathActivity.this, MakeNumberActivity.class);

                startActivity(intent);
                if (checkedId == R.id.rd_maketwo) {
                    intent.putExtra(MAMKNUMBERMETHODS, 12);
                    intent.putExtra(NUMBEROFINPUT, 3);
                } else if (checkedId == R.id.rd_makeeight) {
                    intent.putExtra(MAMKNUMBERMETHODS, 18);
                    intent.putExtra(NUMBEROFINPUT, 3);
                } else {
                    intent.putExtra(MAMKNUMBERMETHODS, 24);
                    intent.putExtra(NUMBEROFINPUT, 4);
                }
                startActivity(intent);
            }
        });
    }
}