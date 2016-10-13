package com.hasbrain.howfastareyou;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TapCountActivity extends AppCompatActivity implements TapCountResultFragment.DataPass {

    public static final int TIME_COUNT = 10000; //10s
    @Bind(R.id.bt_tap)
    Button btTap;
    @Bind(R.id.bt_start)
    Button btStart;
    @Bind(R.id.tv_time)
    Chronometer tvTime;
    @Bind(R.id.count)
    TextView count;
    private int tap_count = 0;
    private long startTime;
    private boolean start;
    private long timeAtPause = 0;

    //    private ArrayList<String> listTime;
//    private ArrayList<Integer> listScore;
    TapCountResultFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
//        listTime = new ArrayList<>();
//        listScore = new ArrayList<>();
            fragment = new TapCountResultFragment();
            fragment.setArguments(new Bundle());
            getFragmentManager().beginTransaction().add(R.id.fl_result_fragment, fragment, "fragment_result").commit();
        }
        else{
            fragment = (TapCountResultFragment) getFragmentManager().findFragmentByTag("fragment_result");
        }
        tvTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (SystemClock.elapsedRealtime() - startTime >= TIME_COUNT) {
                    pauseTapping();
                }
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
    }

    private void startTapping() {
        if(!start){
            start = true;
            tap_count = 0;
            timeAtPause = 0;
            startTime = SystemClock.elapsedRealtime();
            tvTime.setBase(SystemClock.elapsedRealtime());
            tvTime.start();
            btTap.setEnabled(true);
            btStart.setEnabled(false);
        }
        else {
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
        btTap.setEnabled(false);
        tvTime.stop();
        btTap.setEnabled(false);
        btStart.setEnabled(true);
//        saveData();
        passDataToFragment();
        updateFragmentView();
    }

//    private void saveData(){
//        listScore.add(tap_count);
//
//        Date now = Calendar.getInstance().getTime();
//        String now_str = DateFormat.format("dd/MM/yyyy HH:mm:ss", now).toString();
//        listTime.add(now_str);
//    }

    private void passDataToFragment(){

//        Gson gson = new Gson();
//        String stringTime = gson.toJson(listTime);
//        String stringScore = gson.toJson(listScore);

//        Bundle bundle = new Bundle();
//        bundle.putStringArrayList("time", listTime);
//        bundle.putIntegerArrayList("score", listScore);

//        fragment.getArguments().putStringArrayList("time", listTime);
//        fragment.getArguments().putIntegerArrayList("score", listScore);
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
        if (start) {
            timeAtPause = tvTime.getBase() - SystemClock.elapsedRealtime();
            tvTime.stop();

            btTap.setEnabled(false);
            btStart.setEnabled(true);
            btStart.setText("RESUME");
        }
    }

//    @Override
//    public void finish() {
//        super.finish();
//        fragment.getArguments().putInt("current_score", tap_count);
//        fragment.getArguments().putLong("current_time", timeAtPause);
//        fragment.getArguments().putBoolean("current_start", start);
//    }

    @Override
    public void passDataToActivity(Boolean current_start, int current_score, long current_time) {
        timeAtPause = current_time;
        tap_count = current_score;
        start = current_start;

        if(this.start){
            btStart.setText("RESUME");
        }
        count.setText("" + tap_count);
        long a = (SystemClock.elapsedRealtime() + timeAtPause);
//        tvTime.setBase(a);
//        tvTime.setText("" + (SystemClock.elapsedRealtime() + timeAtPause));
    }

    public int getTap_count() {
        return tap_count;
    }

    public long getTimeAtPause() {
        return timeAtPause;
    }

    public boolean isStart() {
        return start;
    }
}
