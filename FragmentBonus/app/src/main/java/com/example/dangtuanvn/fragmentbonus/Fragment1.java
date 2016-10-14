package com.example.dangtuanvn.fragmentbonus;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by dangtuanvn on 10/13/16.
 */

public class Fragment1 extends Fragment {

    ViewGroup container;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        this.container = container;
        Button button2 = (Button) view.findViewById(R.id.bt_2_f1);
        Button button3 = (Button) view.findViewById(R.id.bt_3_f1);

        View.OnClickListener replaceFragment = new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                Fragment fragment = null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(view == view.findViewById(R.id.bt_2_f1)){
                    fragment = ((MainActivity) getActivity()).getFragment2();
                    fragmentTransaction.replace(container.getId(), fragment, "fragment_2");
                    fragmentTransaction.addToBackStack("f1");
                    fragmentTransaction.commit();
                }
                else {
                    fragment =  ((MainActivity) getActivity()).getFragment3();
                        fragmentTransaction.replace(container.getId(), fragment, "fragment_3");
                        fragmentTransaction.addToBackStack("f1");
                        fragmentTransaction.commit();
                    for(int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++){
                        Log.i("BACKSTACK", "Found fragment: " + fragmentManager.getBackStackEntryAt(entry).getId());
                    }
                    if(   fragmentManager.getBackStackEntryCount() < 1){
                        Log.i("BACKSTACK", "Found fragment: " + fragmentManager.getBackStackEntryCount() + " NONE");
                    }
                }

            }
        };

        button2.setOnClickListener(replaceFragment);
        button3.setOnClickListener(replaceFragment);

        return view;
    }
}
