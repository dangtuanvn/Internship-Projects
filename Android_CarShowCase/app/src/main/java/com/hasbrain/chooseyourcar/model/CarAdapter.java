package com.hasbrain.chooseyourcar.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hasbrain.chooseyourcar.CarDetailActivity;
import com.hasbrain.chooseyourcar.R;
import com.hasbrain.chooseyourcar.datastore.CarListPassData;
import com.hasbrain.chooseyourcar.loader.BitmapWorkerTask;
import com.hasbrain.chooseyourcar.loader.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.hasbrain.chooseyourcar.CarListActivity.getBitmapFromMemCache;
import static com.hasbrain.chooseyourcar.loader.BitmapWorkerTask.cancelPotentialWork;

/**
 * Created by dangtuanvn on 10/20/16.
 */

public class CarAdapter extends RecyclerView.Adapter implements ImageLoader{
    private Context context;
    private List<Car> carList;

    public CarAdapter(Context context, List<Car> carList) {
        this.carList = carList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View listView = inflater.inflate(R.layout.car_list_item, parent, false);
        return new ViewHolder(listView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Car car_item = carList.get(position);
        ((ViewHolder) holder).bind(car_item, position);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    @Override
    public void displayImage(String uri, ImageView imageView, int position) {

        // loadBitmap(imageView, uri, position);
        displayCarList_Picasso(imageView, uri);
    }



    public void loadBitmap(ImageView imageView, String uri, int position) {
        while(position > getItemCount()){
            position -= getItemCount();
        }
        final String imageKey = String.valueOf(position);

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        }
        else {
            if (cancelPotentialWork(position, imageView)) {
                Log.i("PROCESSING", "image at position: " + position + " " + getItemCount());
                final BitmapWorkerTask task = new BitmapWorkerTask(context, imageView, uri);
                final BitmapWorkerTask.AsyncDrawable asyncDrawable =
                        new BitmapWorkerTask.AsyncDrawable(context.getResources(), null, task);
                imageView.setImageDrawable(asyncDrawable);
                task.execute(position);
            }
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView car_image;
        private TextView car_name;

        private ViewHolder(View itemView) {
            super(itemView);
            car_image = (ImageView) itemView.findViewById(R.id.image_car);
            car_name = (TextView) itemView.findViewById(R.id.name_car);
//            car_image.setMaxHeight(70);
//            car_image.setMaxWidth(70);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CarDetailActivity.class);
//                    CarListPassData data = new CarListPassData(carList);
//                    intent.putExtra("car_list", data);
                    intent.putExtra("position", getAdapterPosition());
                    context.startActivity(intent);
                }
            });
        }

        public void bind(Car car_item, int position){
            String name = car_item.getBrand() + " " + car_item.getName();
            displayImage(car_item.getImageUrl(), car_image, position);
            car_name.setText(name);
        }
    }

    public void displayCarList_Picasso(ImageView imageView, String uri){
        Uri _uri = Uri.parse(uri);
        Picasso.with(context)
                .load(_uri)
                .resize(500, 400)
                .into(imageView);
    }
}
