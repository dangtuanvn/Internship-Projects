package com.example.dangtuanvn.fragmentbonus;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by dangtuanvn on 10/13/16.
 */

public class Fragment3 extends Fragment {
    ViewGroup container;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_3, container, false);
        this.container = container;

        Button button1 = (Button) view.findViewById(R.id.bt_1_f3);
        Button button2 = (Button) view.findViewById(R.id.bt_2_f3);

        View.OnClickListener replaceFragment = new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                Fragment fragment = null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(view == view.findViewById(R.id.bt_1_f3)){
                    fragment = ((MainActivity) getActivity()).getFragment1();
                    fragmentTransaction.replace(container.getId(), fragment, "fragment_1");
                    fragmentTransaction.addToBackStack("f3");
                    fragmentTransaction.commit();
                }
                else {
                    fragment = ((MainActivity) getActivity()).getFragment2();
                    fragmentTransaction.replace(container.getId(), fragment, "fragment_2");
                    fragmentTransaction.addToBackStack("f3");
                    fragmentTransaction.commit();
                }
            }
        };

        button1.setOnClickListener(replaceFragment);
        button2.setOnClickListener(replaceFragment);

        return view;
    }
}
