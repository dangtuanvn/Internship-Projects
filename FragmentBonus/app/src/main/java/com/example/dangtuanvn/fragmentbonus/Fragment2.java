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

public class Fragment2 extends Fragment {
    ViewGroup container;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        this.container = container;

        Button button1 = (Button) view.findViewById(R.id.bt_1_f2);
        Button button3 = (Button) view.findViewById(R.id.bt_3_f2);


        View.OnClickListener replaceFragment = new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                Fragment fragment = null;
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if(view == view.findViewById(R.id.bt_1_f2)){
                    fragment =   ((MainActivity) getActivity()).getFragment1();
                    fragmentTransaction.replace(container.getId(), fragment, "fragment_1");
                    fragmentTransaction.addToBackStack("f2");
                    fragmentTransaction.commit();
                }
                else {

                    fragment =   ((MainActivity) getActivity()).getFragment3();
                        fragmentTransaction.replace(container.getId(), fragment, "fragment_3");
                        fragmentTransaction.addToBackStack("f2");
                        fragmentTransaction.commit();

                }

            }
        };

        button1.setOnClickListener(replaceFragment);
        button3.setOnClickListener(replaceFragment);



//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(getActivity().getFragmentManager().findFragmentByTag("fragment_3") == null){
//                    FragmentManager fragmentManager = getActivity().getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    Fragment3 newFragment = new Fragment3();
////                    fragmentTransaction.detach(getActivity().getFragmentManager().findFragmentByTag("fragment_2"));
//
//                    fragmentTransaction.replace(container.getId(), newFragment, "fragment_3");
//
//                    fragmentTransaction.addToBackStack("f2");
//                    fragmentTransaction.commit();
//                    for(int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++){
//                        Log.i("BACKSTACK", "Found fragment: " + fragmentManager.getBackStackEntryAt(entry).getId());
//                    }
//                    if(   fragmentManager.getBackStackEntryCount() < 1){
//                        Log.i("BACKSTACK", "Found fragment: " + fragmentManager.getBackStackEntryCount() + " NONE");
//                    }
//                }
//
//                else{
//                    FragmentManager fragmentManager = getActivity().getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    getActivity().getFragmentManager().findFragmentById(container.getId()).onDetach();
//                    fragmentTransaction.replace(container.getId(), getActivity().getFragmentManager().findFragmentByTag("fragment_3"), "fragment_3");
//                    fragmentTransaction.addToBackStack("f2");
//                    fragmentTransaction.commit();
//                    for(int entry = 0; entry < fragmentManager.getBackStackEntryCount(); entry++){
//                        Log.i("BACKSTACK", "Found fragment: " + fragmentManager.getBackStackEntryAt(entry).getId());
//                    }
//                }
//            }
//        });
        return view;
    }
}
