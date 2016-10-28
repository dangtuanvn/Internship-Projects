package com.hasbrain.howfastareyou;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;


import java.io.File;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TapCountActivity extends AppCompatActivity {

    public static int TIME_COUNT = 10000; //10s
    @Bind(R.id.bt_tap)
    Button btTap;
    @Bind(R.id.bt_start)
    Button btStart;
    @Bind(R.id.tv_time)
    Chronometer tvTime;
    @Bind(R.id.count)
    TextView count;

    private int tap_count = 0;
    public static final int DEFAULT_TIME_LIMIT = 10;
    public static final boolean DEFAULT_SAVE_DATA = true;
    private long startTime;
    private boolean start = false;
    private boolean change = false;
    private boolean saveData;
    private long timeAtPause = 0;
    TapCountResultFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        // PreferenceManager.setDefaultValues(this, R.xml.preference, false);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            fragment = (TapCountResultFragment) getFragmentManager().findFragmentByTag("fragment_result");
            start = savedInstanceState.getBoolean("last_start");
            tap_count = savedInstanceState.getInt("last_score");
            timeAtPause = savedInstanceState.getLong("last_timeAtPause");
            saveData = savedInstanceState.getBoolean("save_data");
            if(this.start){
                btStart.setText("RESUME");
            }
            count.setText("" + tap_count);
            long a = (SystemClock.elapsedRealtime() + timeAtPause);
            tvTime.setBase(a);
//        tvTime.setText("" + (SystemClock.elapsedRealtime() + timeAtPause));

        }
        else{
            fragment = new TapCountResultFragment();
            fragment.setArguments(new Bundle());
            getFragmentManager().beginTransaction().add(R.id.fl_result_fragment, fragment, "fragment_result").commit();
        }
        tvTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (SystemClock.elapsedRealtime() - startTime >= TIME_COUNT) {
                    pauseTapping();
                }
//                Log.i("timer", tvTime.getId() + " : " + tvTime.getText());
//                Log.i("startTime", "Start Time: " + startTime);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            Intent showSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(showSettingsActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.bt_start)
    public void onStartBtnClicked(View v) {
        startTapping();
    }

    @OnClick(R.id.bt_tap)
    public void onTapBtnClicked(View v) {
        tap_count++;
        count.setText("" + tap_count);
//        Log.i("READING INPUT FILE", read_file(this, filename));
    }

    private void startTapping() {
        if(!start){
            start = true;
            change = false;
            tap_count = 0;
            timeAtPause = 0;
            startTime = SystemClock.elapsedRealtime();
            tvTime.setBase(SystemClock.elapsedRealtime());
            tvTime.start();
            btTap.setEnabled(true);
            btStart.setEnabled(false);
        }
        else {
            change = false;
            btStart.setText("START");
            startTime = SystemClock.elapsedRealtime() + timeAtPause;
            tvTime.setBase(SystemClock.elapsedRealtime() + timeAtPause);
            tvTime.start();
            btStart.setEnabled(false);
            btTap.setEnabled(true);
        }
    }

    private void pauseTapping() {
        start = false;
        change = false;
        btTap.setEnabled(false);
        tvTime.stop();
        btTap.setEnabled(false);
        btStart.setEnabled(true);
        passDataToFragment();
        updateFragmentView();
        fragment.saveDataToInternalStorage();
        fragment.saveDataToExternalStorage();
    }

    private void passDataToFragment(){
        Date now = Calendar.getInstance().getTime();
        String now_str = DateFormat.format("dd/MM/yyyy HH:mm:ss", now).toString();
        fragment.getArguments().putString("time", now_str);
        fragment.getArguments().putInt("score", tap_count);
    }

    private void updateFragmentView(){
          fragment.saveData();
          fragment.updateView();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (start && !change) {
            change = true;
            timeAtPause = tvTime.getBase() - SystemClock.elapsedRealtime();
            tvTime.stop();
            btTap.setEnabled(false);
            btStart.setEnabled(true);
            btStart.setText("RESUME");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("last_start", start);
        outState.putInt("last_score", tap_count);
        outState.putLong("last_timeAtPause", timeAtPause);
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        TIME_COUNT = pref.getInt("time_limit", DEFAULT_TIME_LIMIT) * 1000;
        // System.out.println("TIME COUNT: " + TIME_COUNT);
        saveData = pref.getBoolean("save_data", DEFAULT_SAVE_DATA);
    }

    public boolean isSaveData() {
        return saveData;
    }

    @OnClick(R.id.bt_reset)
    public void onResetBtnClicked(View v) {
        resetRecords();
    }

    public void resetRecords(){
        // get the path to sdcard
        File sdcard = Environment.getExternalStorageDirectory();
        // to this path add a new directory path
        File dir = new File(sdcard.getAbsolutePath() + "/HowFastAreYou/");
        // create this directory if not already created
        dir.mkdir();
        // create the file in which we will write the contents

        File file_1 = new File(dir, "list_time.txt");

        File file_2 = new File(dir, "list_score.txt");

        file_1.delete();
        file_2.delete();
        fragment.clearData();
        fragment.clearDatabase();
    }
}
