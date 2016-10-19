package com.example.dangtuanvn.exercise_1_1;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dangtuanvn on 10/11/16.
 */

public class UserSignUpFragmentActivity extends AppCompatActivity implements SignUpStep1Fragment.SignUpStep1FragmentListener, SignUpStep2Fragment.SignUpStep2FragmentListener {
    private SignUpStep1Fragment fragmentOne;
    private SignUpStep2Fragment fragmentTwo;
    private SignUpStep3Fragment fragmentThree;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SignUpStep1Fragment fragmentOne = new SignUpStep1Fragment();
        getFragmentManager().beginTransaction().add(R.id.signup_fragment, fragmentOne, "fragment_one").commit();
    }

    @Override
    public void sendDataStep1(SignUpData data) {
        fragmentOne = (SignUpStep1Fragment) getFragmentManager().findFragmentById(R.id.signup_fragment);
        SignUpStep2Fragment fragmentTwo = new SignUpStep2Fragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.signup_fragment, fragmentTwo);
        transaction.addToBackStack(null);
        transaction.commit();
        fragmentTwo.saveData(data);
    }

    @Override
    public void sendDataStep2(SignUpData data) {
        SignUpStep3Fragment fragmentThree = new SignUpStep3Fragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.signup_fragment, fragmentThree);
        transaction.addToBackStack(null);
        transaction.commit();
        fragmentThree.saveData(data);
    }

}
