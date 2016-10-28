package com.hasbrain.chooseyourcar.datastore;

import android.os.Parcel;
import android.os.Parcelable;

import com.hasbrain.chooseyourcar.model.Car;

import java.util.List;

/**
 * Created by dangtuanvn on 10/24/16.
 */

public class CarListPassData implements Parcelable {
    private List<Car> carList;

    public CarListPassData(List<Car> carList) {
        super();
        this.carList = carList;
    }

    protected CarListPassData(Parcel in) {
        in.readList(carList, Car.class.getClassLoader());
    }

    public static final Creator<CarListPassData> CREATOR = new Creator<CarListPassData>() {
        @Override
        public CarListPassData createFromParcel(Parcel in) {
            return new CarListPassData(in);
        }

        @Override
        public CarListPassData[] newArray(int size) {
            return new CarListPassData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(carList);
    }

    public List<Car> getCarList() {
        return carList;
    }
}
