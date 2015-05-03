package com.jhua.assassin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.InputStream;
import java.util.ArrayList;

public class SignUpActivity extends ActionBarActivity {

    private static final int PICK_PHOTO_FOR_AVATAR = 0;

    EditText username;
    EditText password;
    EditText c_password;
    EditText email;
    EditText name;

    Button sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
                    makeUser();
                }
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
        user.setEmail(email.getText().toString());
        user.put("name", name.getText().toString());

        user.put("currentGames", new ArrayList<Game>());
        user.put("pendingGames", new ArrayList<Game>());
        user.put("completedGames", new ArrayList<Game>());

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
            public void done(ParseException e) {
                if (e == null) {
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Oops! That user already exists!", Toast.LENGTH_LONG);
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
        if (password.getText().toString() != password.getText().toString())
            return false;
        return true;
    }
}
