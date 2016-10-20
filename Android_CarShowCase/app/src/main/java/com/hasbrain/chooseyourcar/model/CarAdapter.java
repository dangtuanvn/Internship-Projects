package com.hasbrain.chooseyourcar.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hasbrain.chooseyourcar.R;
import com.hasbrain.chooseyourcar.loader.BitmapWorkerTask;
import com.hasbrain.chooseyourcar.loader.ImageLoader;
import java.util.List;

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
        ((ViewHolder) holder).bind(car_item);
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    @Override
    public void displayImage(String uri, ImageView imageView) {
//        BitmapWorkerTask task = new BitmapWorkerTask(context, imageView, uri);
//        task.execute(1);
        loadBitmap(1, imageView, uri);

//        Uri _uri = Uri.parse(uri);
//        imageView.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(),
//        getResourceId(_uri.getLastPathSegment(), _uri.getPathSegments().get(0), context.getPackageName()), 70, 70));
    }


    public void loadBitmap(int resId, ImageView imageView, String uri) {
        if (cancelPotentialWork(resId, imageView)) {
            final BitmapWorkerTask task = new BitmapWorkerTask(context, imageView, uri);
            final BitmapWorkerTask.AsyncDrawable asyncDrawable =
                    new BitmapWorkerTask.AsyncDrawable(context.getResources(), null, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(resId);
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView car_image;
        private TextView car_name;

        private ViewHolder(View itemView) {
            super(itemView);
            car_image = (ImageView) itemView.findViewById(R.id.image_car);
            car_name = (TextView) itemView.findViewById(R.id.name_car);
        }

        public void bind(Car car_item){
            String name = car_item.getBrand() + " " + car_item.getName();
            displayImage(car_item.getImageUrl(), car_image);
            car_name.setText(name);
        }
    }

}
