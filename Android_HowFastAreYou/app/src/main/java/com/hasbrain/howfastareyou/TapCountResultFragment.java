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
    private long current_timeAtPause;
    private int current_score;
    private boolean current_start;
    DataPass dataPasser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        if (savedInstanceState != null) {
            // Restore last state
            listTime = savedInstanceState.getStringArrayList("list_time");
            listScore = savedInstanceState.getIntegerArrayList("list_score");
            dataPasser.passDataToActivity(savedInstanceState.getBoolean("current_start"),
                    savedInstanceState.getInt("current_score"), savedInstanceState.getLong("current_time"));
//            current_timeAtPause = savedInstanceState.getLong("current_time");
//            current_score = savedInstanceState.getInt("current_score");
//            current_start = savedInstanceState.getBoolean("current_start");
        } else{
            listTime = new ArrayList<>();
            listScore = new ArrayList<>();
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
        outState.putLong("current_time", ((TapCountActivity) this.getActivity()).getTimeAtPause());
        outState.putBoolean("current_start", ((TapCountActivity) this.getActivity()).isStart());
        outState.putInt("current_score", ((TapCountActivity) this.getActivity()).getTap_count());
    }

    @Deprecated
    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        dataPasser = (DataPass) a;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (DataPass) context;
    }

    public interface DataPass {
        public void passDataToActivity(Boolean current_start, int current_score, long current_time);
    }
}
