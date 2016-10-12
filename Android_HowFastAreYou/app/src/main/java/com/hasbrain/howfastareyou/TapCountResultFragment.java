package com.hasbrain.howfastareyou;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/14/15.
 */
public class TapCountResultFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        RecyclerView recycleListView = (RecyclerView) view.findViewById(R.id.recyclerview);
        ResultAdapter adapter = new ResultAdapter(this, postList);
        recycleListView.setAdapter(adapter);
        recycleListView.setLayoutManager(new LinearLayoutManager(this));

    savedInstanceState.put

        return view;
    }

}
