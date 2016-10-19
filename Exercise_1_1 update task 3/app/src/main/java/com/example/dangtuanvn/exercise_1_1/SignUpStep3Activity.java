package com.example.dangtuanvn.exercise_1_1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SignUpStep3Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_step3);

        Button buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendEmail();
            }
        });

        Button buttonRestart = (Button) findViewById(R.id.buttonRestart);
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                restart();
            }
        });
    }

    // http://stackoverflow.com/questions/3132889/action-sendto-for-sending-an-email
    public void sendEmail(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String firstName = extras.getString("first name");
        String lastName = extras.getString("last name");
        String email = extras.getString("email");
        String phone = extras.getString("phone");
        int salary = extras.getInt("salary");

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

        intentNew.setData(uri);
        startActivity(Intent.createChooser(intentNew, "Send email"));

    }

    public void restart(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String firstName = extras.getString("first name");
        String lastName = extras.getString("last name");
        String email = extras.getString("email");
        String phone = extras.getString("phone");
        String gender = extras.getString("gender");
        int salary = extras.getInt("salary");
        boolean[] sports = extras.getBooleanArray("sports");


        Intent intentNew = new Intent(this, SignUpStep1Activity.class);
        intentNew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentNew);
    }
}
