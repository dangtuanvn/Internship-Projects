package com.example.dangtuanvn.exercise_1_1;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dangtuanvn on 10/11/16.
 */

public class SignUpStep1Fragment extends Fragment {
    private SignUpStep1FragmentListener fragmentOneListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_sign_up_step1, container, false);

        Button buttonNext = (Button) view.findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextStep(v);
            }
        });

        return view;
    }

    public void nextStep(View view) {
        if (validate()) {
            EditText inputFirstName = (EditText) getView().findViewById(R.id.inputFirstName);
            EditText inputLastName = (EditText) getView().findViewById(R.id.inputLastName);
            EditText inputEmail = (EditText) getView().findViewById(R.id.inputEmail);
            EditText inputPhone = (EditText) getView().findViewById(R.id.inputPhone);

            // Intent intent = new Intent(this, SignUpStep2Activity.class);
            String firstName = inputFirstName.getText().toString();
            String lastName = inputLastName.getText().toString();
            String email = inputEmail.getText().toString();
            String phone = inputPhone.getText().toString();

            String gender = "";
            RadioGroup genderCheck = (RadioGroup) getView().findViewById(R.id.sectionGender);
            switch (genderCheck.getCheckedRadioButtonId()){
                case R.id.inputMaleButton:
                    gender = "Male";
                    break;
                case R.id.inputFemaleButton:
                    gender = "Female";
                    break;
            }

//            Bundle extras = new Bundle();
//            extras.putString("first name", firstName);
//            extras.putString("last name", lastName);
//            extras.putString("email", email);
//            extras.putString("phone", phone);
//            extras.putString("gender", gender);

            SignUpData extras = new SignUpData(firstName, lastName, email, phone, gender);
            sendDataStep1(extras);

            // intent.putExtras(extras);
            // startActivity(intent);
        }
    }

    public boolean validate() {
        boolean check = true;

        EditText inputFirstName = (EditText) getView().findViewById(R.id.inputFirstName);
        EditText inputLastName = (EditText) getView().findViewById(R.id.inputLastName);
        EditText inputEmail = (EditText) getView().findViewById(R.id.inputEmail);
        EditText inputPhone = (EditText) getView().findViewById(R.id.inputPhone);

        if (inputFirstName.getText().toString().length() == 0) {
            check = false;
            inputFirstName.setError("First name is required");
        }
        if (inputLastName.getText().toString().length() == 0) {
            check = false;
            inputLastName.setError("Last name is required");
        }

        if(inputEmail.getText().toString().length() == 0){
            check = false;
            inputEmail.setError("Email is required");
        }
        else if(!isEmailValid(inputEmail.getText().toString())){
            check = false;
            inputEmail.setError("Your email is invalid");
        }

        if(inputPhone.getText().toString().length() == 0){
            check = false;
            inputPhone.setError("Phone number is required");
        }
        else if(inputPhone.getText().toString().length() < 10 || inputPhone.getText().toString().length() > 11){
            check = false;
            inputPhone.setError("Phone number is invalid");
        }
        return check;
    }

    // http://stackoverflow.com/questions/9355899/android-email-edittext-validation
    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentOneListener = (SignUpStep1FragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ " must implement SignUpStep1FragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentOneListener = null;
    }

    private void sendDataStep1(SignUpData data) {
        if  (fragmentOneListener != null) {
            fragmentOneListener.sendDataStep1(data);
        }
    }

    public interface SignUpStep1FragmentListener {
        void sendDataStep1(SignUpData data);
    }
}
