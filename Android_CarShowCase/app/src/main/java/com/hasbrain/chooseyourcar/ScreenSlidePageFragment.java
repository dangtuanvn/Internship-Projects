package com.hasbrain.chooseyourcar;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by dangtuanvn on 10/21/16.
 */

public class ScreenSlidePageFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_car_detail, container, false);

        ImageView carImage = (ImageView) rootView.findViewById(R.id.image_car);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.raw.bmw_650i_gran_coupe);
        carImage.setImageBitmap(bitmap);
        return rootView;
    }
}
