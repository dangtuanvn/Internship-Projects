package com.hasbrain.chooseyourcar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.hasbrain.chooseyourcar.datastore.AssetBasedCarDatastoreImpl;
import com.hasbrain.chooseyourcar.datastore.CarDatastore;
import com.hasbrain.chooseyourcar.datastore.OnCarReceivedListener;
import com.hasbrain.chooseyourcar.model.Car;
import com.hasbrain.chooseyourcar.model.CarAdapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

public class CarListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        Gson gson = new GsonBuilder().create();
        CarDatastore carDatastore = new AssetBasedCarDatastoreImpl(this, "car_data.json", gson);
        carDatastore.getCarList(new OnCarReceivedListener() {
            @Override
            public void onCarReceived(List<Car> cars, Exception ex) {
                displayCarList(cars);


            }
        });
    }

    public void displayCarList(List<Car> carList){
        RecyclerView recycleListView = (RecyclerView) findViewById(R.id.recyclerview);

        CarAdapter adapter = new CarAdapter(this, carList);
        recycleListView.setAdapter(adapter);
        recycleListView.setLayoutManager(new LinearLayoutManager(this));
    }
}