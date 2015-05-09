package com.jhua.assassin;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.InputStream;
import java.util.ArrayList;

public class SignUpActivity extends Activity {

    private static final String TAG = "SignUpActivity";
    private static final int PICK_PHOTO_FOR_AVATAR = 0;

    EditText username;
    EditText password;
    EditText c_password;
    EditText email;
    EditText name;

    Button sign_up;
    ImageButton take_photo;
    ParseFile photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ParseObject hello = new ParseObject("Hello");

        hello.put("greeting", "hello");

        hello.saveInBackground();

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        c_password = (EditText) findViewById(R.id.c_password);
        email = (EditText) findViewById(R.id.email);
        name = (EditText) findViewById(R.id.name);

        sign_up = (Button) findViewById(R.id.sign_up);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordsMatch()) {

                    Log.d(TAG, "Passwords Match");
                    makeUser();
                } else {
                    Toast.makeText(getApplicationContext(), "Something is incorrect, please check your information!", Toast.LENGTH_LONG).show();
                }
            }
        });

        take_photo = (ImageButton) findViewById(R.id.photo_button);
        take_photo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                /*
                InputMethodManager imm = (InputMethodManager) getActivity()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(c_password.getWindowToken(), 0);
                */
                startCamera();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void makeUser() {

        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());

        //Email is not working for some reason, wont let me sign up with this
        //user.setEmail(email.getText().toString());

        user.put("eliminations", 0);

        //Nuumbers of wins
        user.put("wins", 0);


        user.put("name", name.getText().toString());



        // pickImage();
        /*
        ParseFile file = new ParseFile("profile_pic.jpg", image);

        file.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                user.put("pic", file);
            }
        });
        */
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Oops! That user already exists!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    /*
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                // Display an error
                return;
            }
            InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
            // Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }
    */
    public boolean passwordsMatch() {
        if (!password.getText().toString().equals(c_password.getText().toString())) {
            return false;
        }
        return true;
    }

    public void setPhoto(ParseFile pic) {
        photo = pic;
    }

    public void startCamera() {
        Fragment cameraFragment = new CameraFragment();
        FragmentTransaction transaction = this.getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragmentContainer, cameraFragment);
        transaction.addToBackStack("NewMealFragment");
        transaction.commit();
    }
}
