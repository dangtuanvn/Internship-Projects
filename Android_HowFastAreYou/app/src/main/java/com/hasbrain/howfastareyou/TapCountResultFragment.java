package com.hasbrain.howfastareyou;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        if (savedInstanceState != null) {
            // Restore last state
            listTime = savedInstanceState.getStringArrayList("list_time");
            listScore = savedInstanceState.getIntegerArrayList("list_score");
        } else{
            listTime = new ArrayList<>();
            listScore = new ArrayList<>();

//            for(int i = 0; i < 1000000000; i ++){
//                listTime.add("4/10/2015 01:17:38");
//                listScore.add(i);
//            }
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
    }

    public void updateView(){
//        adapter.notifyDataSetChanged();
        adapter.notifyItemInserted(adapter.getItemCount() + 1);
//        adapter.notifyItemChanged(0);
//        recycleListView.invalidate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("list_time", listTime);
        outState.putIntegerArrayList("list_score", listScore);
    }
}
