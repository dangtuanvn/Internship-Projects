package com.example.dangtuanvn.exercise_1_1;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dangtuanvn on 10/11/16.
 */

public class SignUpStep1Fragment extends Fragment {
    private SignUpStep1FragmentListener fragmentOneListener;
    private boolean updateAvatar = false;
    private static final int CAMERA_IMAGE_REQUEST = 1;
    private static Uri fileUri = null;
    private static String photoPath = null;
    private ImageView profilePicture;
    private static int targetH, targetW;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_sign_up_step1, container, false);

        Button buttonNext = (Button) view.findViewById(R.id.buttonNext);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextStep(v);
            }
        });
        profilePicture = (ImageView) view.findViewById(R.id.inputImage);
        if (photoPath != null) {
            Bitmap bitmap = createAvatar();
            bitmap = rotateBitmap90(bitmap);
            profilePicture.invalidate();
            profilePicture.setImageBitmap(bitmap);
            updateAvatar = true;
            // Toast.makeText(getActivity(), "Avatar changed", Toast.LENGTH_LONG).show();
            // Log.i("REMAKE AVATAR", "Avatar changed");
        }

//    if(!updateAvatar) {
//        profilePicture.setBackgroundResource(R.drawable.dummy);
//    }

        addProfilePicture(view);

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

            SignUpData extras = new SignUpData(firstName, lastName, email, phone, gender, photoPath);
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

        if(!updateAvatar){
            check = false;
            Toast.makeText(getActivity(), "You must choose an avatar", Toast.LENGTH_LONG).show();
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

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmentOneListener = (SignUpStep1FragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SignUpStep1FragmentListener");
        }

    }

//    @TargetApi(23)
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            fragmentOneListener = (SignUpStep1FragmentListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement SignUpStep1FragmentListener");
//        }
//    }

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



    public void addProfilePicture(View view) {
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File image;
                try {
                    image = createImageFile();
                    fileUri = Uri.fromFile(image);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Avatar_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        photoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case CAMERA_IMAGE_REQUEST:
                    try {

                        // Get the dimensions of the View
                        targetH = profilePicture.getHeight();
                        targetW = profilePicture.getWidth();

//                      targetW = profilePicture.getDrawable().getIntrinsicWidth();
//                      targetH = profilePicture.getDrawable().getIntrinsicHeight();

//                        Toast.makeText(getActivity(), photoPath, Toast.LENGTH_LONG).show();
//                        Toast.makeText(getActivity(), "Width: " + targetW + " --- Height: " + targetH, Toast.LENGTH_LONG).show();

                        // Get the dimensions of the bitmap
//                        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                        bmOptions.inJustDecodeBounds = true;
//                        BitmapFactory.decodeFile(photoPath, bmOptions);
//                        int photoW = bmOptions.outWidth;
//                        int photoH = bmOptions.outHeight;
//
//                        // Determine how much to scale down the image
//                        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
//
//                        // Decode the image file into a Bitmap sized to fill the View
//                        bmOptions.inJustDecodeBounds = false;
//                        bmOptions.inSampleSize = scaleFactor;
//                        bmOptions.inPurgeable = true;

                        Bitmap bitmap = createAvatar();
                        bitmap = rotateBitmap90(bitmap);

                        // Setting image image icon on the imageview
                        profilePicture.invalidate();
                        profilePicture.setImageBitmap(bitmap);
                        updateAvatar = true;

                        // Save new image to file

                        File file = new File(photoPath);
                        try {
                            // Rescale the image if it is larger than 400 pixels
                            boolean rescale = false;
                            if(targetH > 400){
                                //Log.i("BEFORE", "W: " + targetW + " H: " + targetH);
                                Double newW = targetW /((double) targetH / 400);
                                targetW = newW.intValue();
                                targetH = 400;
                                //Log.i("RESIZE", "W: " + targetW + " H: " + targetH);
                                rescale = true;
                            } else if(targetW > 400){
                                //Log.i("BEFORE", "W: " + targetW + " H: " + targetH);
                                Double newH = targetH / ((double) targetW / 400);
                                targetH = newH.intValue();
                                targetW = 400;
                                //Log.i("RESIZE", "W: " + targetW + " H: " + targetH);
                                rescale = true;
                            }

                            if(rescale){
                                // Toast.makeText(getActivity(), "Width: " + targetW + " --- Height: " + targetH, Toast.LENGTH_LONG).show();
                                bitmap = createAvatar();
                            }

                            // Save the new bitmap to file
                            OutputStream outStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                            outStream.flush();
                            outStream.close();
                        } catch(Exception e) {
                            Log.i("Exception", e.getMessage());
                        }
                    } catch (Exception e1) {
                        Toast.makeText(getActivity(), "Something went wrong..." + e1, Toast.LENGTH_SHORT).show();
                    }
                    break;

                default:
                    Toast.makeText(getActivity(), "Something went wrong...", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    public Bitmap createAvatar(){
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Log.i("Measures", "W " + photoW + " H " + photoH + " targetW " + targetW + " targetH " + targetH);
        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, bmOptions);
        return bitmap;
    }

    public Bitmap rotateBitmap90(Bitmap bitmap){
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

//                        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        return bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//                        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState)  {
//        super.onSaveInstanceState(outState);
//        outState.putString("photo_path", photoPath);
//    }
}
