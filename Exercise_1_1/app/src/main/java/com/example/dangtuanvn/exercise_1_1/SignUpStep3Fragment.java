package com.example.dangtuanvn.exercise_1_1;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.File;

/**
 * Created by dangtuanvn on 10/11/16.
 */

public class SignUpStep3Fragment extends Fragment {
    SignUpData data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_sign_up_step3, container, false);

        Button buttonSend = (Button) view.findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendEmail();
            }
        });

        Button buttonRestart = (Button) view.findViewById(R.id.buttonRestart);
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                restart();
            }
        });


        return view;
    }

    public void saveData(SignUpData data){
        this.data = data;
    }

    // http://stackoverflow.com/questions/3132889/action-sendto-for-sending-an-email
    public void sendEmail(){
        Intent intent = getActivity().getIntent();
        String firstName = data.getFirstName();
        String lastName = data.getLastName();
        String email = data.getEmail();
        String phone = data.getPhone();
        int salary = data.getSalary();
        String uriText = "mailto:" + email +
                "?subject=" + Uri.encode("User's registration info") +
                "&body=" + Uri.encode(firstName + "_" + lastName + "\n" + phone + "\n" + salary + " dollars");

        Uri uri = Uri.parse(uriText);
        Intent intentNew = new Intent(Intent.ACTION_SENDTO);

//        intentNew.setType("text/plain");
//        intentNew.putExtra(Intent.EXTRA_EMAIL, email);
//        intentNew.putExtra(Intent.EXTRA_SUBJECT, "User's registration info");
//        intentNew.putExtra(Intent.EXTRA_TEXT,
//                firstName + "_" + lastName + "\n" + phone + "\n" + salary + " dollars");

        intentNew.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(data.getPhotoPath())));
        intentNew.setData(uri);
        startActivity(Intent.createChooser(intentNew, "Send email"));
    }

    public void restart(){
        Intent intent = getActivity().getIntent();
//        Bundle extras = intent.getExtras();
//        String firstName = extras.getString("first name");
//        String lastName = extras.getString("last name");
//        String email = extras.getString("email");
//        String phone = extras.getString("phone");
//        String gender = extras.getString("gender");
//        int salary = extras.getInt("salary");
//        boolean[] sports = extras.getBooleanArray("sports");

//        String firstName = data.getFirstName();
//        String lastName = data.getLastName();
//        String email = data.getEmail();
//        String phone = data.getPhone();
//        String gender = data.getGender();
//        int salary = data.getSalary();
//        boolean[] sports = data.getSports();

        SignUpStep1Fragment fragmentOne = (SignUpStep1Fragment) getActivity().getFragmentManager().findFragmentByTag("fragment_one");
        fragmentOne.resetPhotoPath();
        Intent intentNew = new Intent(getActivity(), UserSignUpFragmentActivity.class);
        intentNew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentNew);
    }
}
