package com.example.dangtuanvn.fragmentbonus;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private Fragment fragment1, fragment2, fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        getFragmentManager().beginTransaction().add(R.id.space_1, fragment1, "fragment_1").commit();
        getFragmentManager().beginTransaction().add(R.id.space_2, fragment2, "fragment_2").commit();
    }

    public Fragment getFragment1() {
        fragment1 = new Fragment1();
        return fragment1;
    }

    public Fragment getFragment2() {
        fragment2 = new Fragment2();
        return fragment2;
    }

    public Fragment getFragment3() {
        getFragmentManager().
        fragment3 = new Fragment3();
        return fragment3;
    }
}
