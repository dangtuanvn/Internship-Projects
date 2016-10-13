package com.hasbrain.howfastareyou;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dangtuanvn on 10/12/16.
 */

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {
    private ArrayList<String> listTime;
    private ArrayList<Integer> listScore;
    private Context context;

    public ResultAdapter(Context context, ArrayList<String> listTime, ArrayList<Integer> listScore) {
        this.context = context;
        this.listTime = listTime;
        this.listScore = listScore;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View listView = inflater.inflate(R.layout.result_item, parent, false);
        return new ResultViewHolder(listView);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        if(position > listScore.size() || position < 0){
            return;
        }

        TextView textTime = holder.textTime;
        TextView textScore = holder.textScore;

        textTime.setText(listTime.get(position));
        textScore.setText("" + listScore.get(position));
    }

    @Override
    public int getItemCount() {
        return listTime.size();
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder {
        public TextView textScore, textTime;

        public ResultViewHolder(View view) {
            super(view);
            textTime = (TextView) view.findViewById(R.id.item_time);
            textScore = (TextView) view.findViewById(R.id.item_score);
        }
    }
}