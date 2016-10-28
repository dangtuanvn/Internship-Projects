package com.hasbrain.chooseyourcar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by dangtuanvn on 10/21/16.
 */

public class ScreenSlidePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_car_detail, container, false);

        ImageView carImage = (ImageView) rootView.findViewById(R.id.image_car);
        TextView carName = (TextView) rootView.findViewById(R.id.detail_name);
        TextView carBrand = (TextView) rootView.findViewById(R.id.detail_brand);

        displayCarList_Picasso(carImage, getArguments().getString("uri"));
        carName.setText(getArguments().getString("car_name"));
        carBrand.setText(getArguments().getString("car_brand"));
        return rootView;
    }

    public void displayCarList_Picasso(ImageView imageView, String uri){
        Uri _uri = Uri.parse(uri);
        Picasso.with(getActivity())
                .load(_uri)
                .resize(1280, 960)
                .into(imageView);
    }
}
