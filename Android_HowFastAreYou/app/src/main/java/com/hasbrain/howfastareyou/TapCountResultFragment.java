package com.hasbrain.howfastareyou;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/14/15.
 */
public class TapCountResultFragment extends Fragment {
    ArrayList<String> listTime;
    ArrayList<Integer> listScore;
    public static ResultAdapter adapter;
    RecyclerView recycleListView;
    boolean first_start = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        if(first_start){
            first_start = false;
            loadDatabase();
            // readDataFromExternalStorage();
            // readDataFromInternalStorage();
        }

        else {
            if (savedInstanceState != null) {
                // Restore last state
                listTime = savedInstanceState.getStringArrayList("list_time");
                listScore = savedInstanceState.getIntegerArrayList("list_score");
            } else {
                listTime = new ArrayList<>();
                listScore = new ArrayList<>();

//            for(int i = 0; i < 1000000000; i ++){
//                listTime.add("4/10/2015 01:17:38");
//                listScore.add(i);
//            }
            }
        }
        recycleListView = (RecyclerView) view.findViewById(R.id.recyclerview);


//        listTime.add("14/10/2015 01:17:38");
//        listScore.add(50);
        adapter = new ResultAdapter(getActivity(), listTime, listScore);
        recycleListView.setAdapter(adapter);
        recycleListView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public void saveData(){
        listTime.add(this.getArguments().getString("time"));
        listScore.add(this.getArguments().getInt("score"));
        dbHelper.insertScore(this.getArguments().getString("time"), this.getArguments().getInt("score"));
    }

    public void updateView(){
        adapter.notifyDataSetChanged();
//        adapter.notifyItemInserted(adapter.getItemCount() + 1);
//        adapter.notifyItemChanged(0);
//        recycleListView.invalidate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("list_time", listTime);
        outState.putIntegerArrayList("list_score", listScore);
    }


    public void saveDataToInternalStorage(){
        FileOutputStream fos_1, fos_2;
        try {
            fos_1 = getActivity().openFileOutput("list_time", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos_1);
            oos.writeObject(listTime);
            oos.close();

            fos_2 = getActivity().openFileOutput("list_score", Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos_2);
            oos.writeObject(listScore);
            oos.close();
            // ArrayList<Object> returnlist = (ArrayList<Object>) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readDataFromInternalStorage(){
        FileInputStream fis_1, fis_2;
        try {
            fis_1 = getActivity().openFileInput("list_time");
            ObjectInputStream ois = new ObjectInputStream(fis_1);
            listTime = (ArrayList<String>) ois.readObject();
            ois.close();

            fis_2 = getActivity().openFileInput("list_score");
            ois = new ObjectInputStream(fis_2);
            listScore = (ArrayList<Integer>) ois.readObject();
            ois.close();

        } catch (Exception e) {
            e.printStackTrace();
            listTime = new ArrayList<>();
            listScore = new ArrayList<>();
        }
    }

    public void saveDataToExternalStorage(){
        // get the path to sdcard
        File sdcard = Environment.getExternalStorageDirectory();
        // to this path add a new directory path
        File dir = new File(sdcard.getAbsolutePath() + "/HowFastAreYou/");
        // create this directory if not already created
        dir.mkdir();
        // create the file in which we will write the contents

        try {
            FileOutputStream file_1 = new FileOutputStream(new File(dir, "list_time.txt"));
            ObjectOutputStream oos = new ObjectOutputStream(file_1);
            oos.writeObject(listTime);
            oos.close();

            FileOutputStream file_2 = new FileOutputStream(new File(dir, "list_score.txt"));
            oos = new ObjectOutputStream(file_2);
            oos.writeObject(listScore);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("WRITE OUPUT","File didn't write");
        }
    }

    public void readDataFromExternalStorage(){
        // get the path to sdcard
        File sdcard = Environment.getExternalStorageDirectory();
        // to this path add a new directory path
        File dir = new File(sdcard.getAbsolutePath() + "/HowFastAreYou/");
        // create this directory if not already created
        dir.mkdir();
        // create the file in which we will write the contents

        FileInputStream fis_1, fis_2;
        try {
            fis_1 = new FileInputStream(new File(dir, "list_time.txt"));
            ObjectInputStream ois = new ObjectInputStream(fis_1);
            listTime = (ArrayList<String>) ois.readObject();
            ois.close();

            fis_2 = new FileInputStream(new File(dir, "list_score.txt"));
            ois = new ObjectInputStream(fis_2);
            listScore = (ArrayList<Integer>) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
            listTime = new ArrayList<>();
            listScore = new ArrayList<>();
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("PERMISSION_WRITE_EXT", "Permission is granted");
                return true;
            } else {

                Log.v("PERMISSION_WRITE_EXT", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("PERMISSION_WRITE_EXT", "Permission is granted");
            return true;
        }
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void clearData(){
        listTime.clear();
        listScore.clear();
        adapter.notifyDataSetChanged();
    }

    DBScore dbHelper;
    String [] columns = new String[] {
            dbHelper.COLUMN_TIME,
            dbHelper.COLUMN_SCORE,
            dbHelper.COLUMN_ID
    };
    public void loadDatabase(){
        listTime = new ArrayList<>();
        listScore = new ArrayList<>();

        dbHelper = new DBScore(getActivity());

        final Cursor cursor = dbHelper.getAllScores();



        for(int j = 0; j < cursor.getCount(); j++){
            Log.i("COUNT", cursor.getCount() + "");
            cursor.moveToPosition(j);
            Log.i("COLUMN COUNT", cursor.getColumnCount()+ "");
            listTime.add(cursor.getString(cursor.getColumnIndex(columns[0])));
            listScore.add(cursor.getInt(cursor.getColumnIndex(columns[1])));
        }
    }

    public void clearDatabase(){
        final Cursor cursor = dbHelper.getAllScores();
        for(int j = 0; j < cursor.getCount(); j++){
            cursor.moveToPosition(j);
            dbHelper.deleteScore(cursor.getInt(cursor.getColumnIndex(columns[2])));
        }
    }
}
