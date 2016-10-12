package com.hasbrain.howfastareyou;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerListAdapter adapter = new RecyclerListAdapter(this, postList);
            recycleListView.setAdapter(adapter);
            recycleListView.setLayoutManager(new LinearLayoutManager(this));
        }

        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            RecyclerGridAdapter adapter = new RecyclerGridAdapter(this, postList);
            recycleListView.setAdapter(adapter);
            recycleListView.setLayoutManager(new GridLayoutManager(this, 3));
            recycleListView.addItemDecoration(new ItemOffsetDecoration(30));
        }
        return view;
    }

}
