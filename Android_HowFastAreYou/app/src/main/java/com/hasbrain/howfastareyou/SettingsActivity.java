package com.hasbrain.howfastareyou;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/14/15.
 */
public class SettingsActivity extends AppCompatActivity {
    private int timeLimit = 10;
    private boolean saveData;
    SharedPreferences pref;
    TextView time;
    SeekBar seekbar;
    SwitchCompat switch_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.settings_text);

        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        time = (TextView) findViewById(R.id.setting_time);
        seekbar = (SeekBar) findViewById(R.id.setting_seekbar_time);
        seekbar.setMax(55);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarProgress = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarProgress = progress + 5;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                time.setText(seekBarProgress + " sec");
                timeLimit = seekBarProgress;
            }
        });

        switch_setting = (SwitchCompat) findViewById(R.id.setting_record);
        switch_setting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    saveData = true;
                }
                else{
                    saveData = false;
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        switch_setting.setChecked(pref.getBoolean("save_data", true));
        seekbar.setProgress(pref.getInt("time_limit", 10));
        time.setText(pref.getInt("time_limit", 10) + " sec");
        timeLimit  = pref.getInt("time_limit", 10);
    }

    @Override
    public void onPause(){
        super.onPause();
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("time_limit", timeLimit);
        editor.putBoolean("save_data", saveData);
        // System.out.println("" + timeLimit + " " + saveData);
        editor.apply();
    }
}
