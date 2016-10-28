package com.example.dangtuanvn.exercise_1_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class SignUpStep2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step2);

        SeekBar salaryBar = (SeekBar) findViewById(R.id.salaryBar);
        salaryBar.setMax(10000);
        salaryBar.incrementProgressBy(100);

        final TextView salaryText = (TextView) findViewById(R.id.salaryText);

        salaryBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress;
                seekBarProgress = seekBarProgress / 100;
                seekBarProgress = seekBarProgress * 100;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                salaryText.setText("Your Salary: " + seekBarProgress + " dollars");
            }
        });

        Button buttonDone = (Button) findViewById(R.id.buttonDone);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                done(v);
            }
        });
    }

    public void done(View view) {
        boolean check = false;

        boolean[] sports = new boolean[6];
        Arrays.fill(sports, Boolean.FALSE);
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        CheckBox checkBox5 = (CheckBox) findViewById(R.id.checkBox5);
        CheckBox checkBox6 = (CheckBox) findViewById(R.id.checkBox6);

        if (checkBox1.isChecked()) {
            check = true;
            sports[0] = true;
        }
        if (checkBox2.isChecked()) {
            check = true;
            sports[1] = true;
        }
        if (checkBox3.isChecked()) {
            check = true;
            sports[2] = true;
        }
        if (checkBox4.isChecked()) {
            check = true;
            sports[3] = true;
        }
        if (checkBox5.isChecked()) {
            check = true;
            sports[4] = true;
        }
        if (checkBox6.isChecked()) {
            check = true;
            sports[5] = true;
        }
        if(!check){
            Toast.makeText(SignUpStep2Activity.this, "Please select at least one sport", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = getIntent();
            Intent intentNew = new Intent(this, SignUpStep3Activity.class);

            SeekBar salaryBar = (SeekBar) findViewById(R.id.salaryBar);
            int salary = salaryBar.getProgress();

            Bundle extrasNew = intent.getExtras();
            extrasNew.putInt("salary", salaryBar.getProgress());
            extrasNew.putBooleanArray("sports", sports);

            intentNew.putExtras(extrasNew);
            startActivity(intentNew);
        }
    }
}
