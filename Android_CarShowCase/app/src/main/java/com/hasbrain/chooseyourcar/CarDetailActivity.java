package com.hasbrain.chooseyourcar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasbrain.chooseyourcar.datastore.AssetBasedCarDatastoreImpl;
import com.hasbrain.chooseyourcar.datastore.CarDatastore;
import com.hasbrain.chooseyourcar.datastore.CarListPassData;
import com.hasbrain.chooseyourcar.datastore.OnCarReceivedListener;
import com.hasbrain.chooseyourcar.model.Car;

import java.util.List;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/19/15.
 */
public class CarDetailActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

//        CarListPassData data = getIntent().getParcelableExtra("car_list");
//        this.carList = data.getCarList();
        Gson gson = new GsonBuilder().create();
        CarDatastore carDatastore = new AssetBasedCarDatastoreImpl(this, "car_data.json", gson);
        carDatastore.getCarList(new OnCarReceivedListener() {
            @Override
            public void onCarReceived(List<Car> cars, Exception ex) {
                mPager = (ViewPager) findViewById(R.id.pager);
                mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), cars);
                mPager.setAdapter(mPagerAdapter);
                position = getIntent().getExtras().getInt("position");
                mPager.setCurrentItem(position, true);
            }
        });
        // Instantiate a ViewPager and a PagerAdapter.

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        List<Car> carList;

        public ScreenSlidePagerAdapter(FragmentManager fm, List<Car> carList) {
            super(fm);
            this.carList = carList;
        }

        @Override
        public Fragment getItem(int position) {
            carList.get(position).getImageUrl();

            ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
            Bundle fragmentData = new Bundle();
            fragmentData.putString("uri", carList.get(position).getImageUrl());
            fragmentData.putString("car_name", carList.get(position).getName());
            fragmentData.putString("car_brand", carList.get(position).getBrand());
            fragment.setArguments(fragmentData);
            return fragment;
        }

        @Override
        public int getCount() {
            return carList.size();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK && mPager.getCurrentItem() == 0)) {
            return super.onKeyDown(keyCode, event);
        } else {
            mPager.setCurrentItem(0, true);
            return super.onKeyDown(keyCode, event);
        }
    }
}

