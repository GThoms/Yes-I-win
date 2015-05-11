package com.jhua.assassin;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SignUpActivity extends Activity {

    private static final String TAG = "SignUpActivity";
    private static final int PICK_PHOTO_FOR_AVATAR = 0;

    static final int MENU_CAMERA = Menu.FIRST;
    static final int MENU_GALLERY = Menu.FIRST + 1;
    static final int CAPTURE_BEFORE = 13;
    static final int SELECT_BEFORE = 12;

    EditText username;
    EditText password;
    EditText c_password;
    EditText email;
    EditText name;

    Button sign_up;
    ImageView photo;
    private PopupMenu popup;

    ParseUser user;
    byte[] photoData;
    Uri beforeURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getActionBar().setDisplayHomeAsUpEnabled(false);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        c_password = (EditText) findViewById(R.id.c_password);
        name = (EditText) findViewById(R.id.name);
        photo = (ImageView) findViewById(R.id.photo);

        sign_up = (Button) findViewById(R.id.sign_up);
        buttonListeners();

        popup = new PopupMenu(this, findViewById(R.id.photo_button));
        popup.getMenu().add(Menu.NONE, MENU_CAMERA, Menu.NONE, "Take a Picture");
        popup.getMenu().add(Menu.NONE, MENU_GALLERY, Menu.NONE, "Choose From Gallery");

        photo = (ImageView) findViewById(R.id.photo);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case MENU_CAMERA:
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, CAPTURE_BEFORE);
                        }
                        break;
                    case MENU_GALLERY:
                        startActivityForResult(new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), SELECT_BEFORE);
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.photo_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.show();
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

        user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());

        // Email is not working for some reason, wont let me sign up with this
        // user.setEmail(email.getText().toString());
        user.put("eliminations", 0);
        // Numbers of wins
        user.put("wins", 0);
        // name
        user.put("name", name.getText().toString());
        saveImage();

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ParsePush.subscribeInBackground(username.getText().toString());
                    finish();
                } else {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Oops! That user already exists!", Toast.LENGTH_LONG).show();
                }
            }
        });

        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_BEFORE) {
            if (resultCode == Activity.RESULT_OK) {
                beforeURI = data.getData();
                Bitmap bitmap = Bitmap.createBitmap(120, 120, Bitmap.Config.ARGB_8888);
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), beforeURI);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                photo.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 120, 120, false));

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                photoData = stream.toByteArray();
            }
        } else if (requestCode == CAPTURE_BEFORE) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap) extras.get("data");

                photo.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 120, 120, false));
                beforeURI = data.getData();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                photoData = stream.toByteArray();
            }
        }
    }

    public boolean passwordsMatch() {
        if (!password.getText().toString().equals(c_password.getText().toString())) {
            return false;
        }
        return true;
    }

    public void saveImage() {
        // Save the scaled image to Parse
        final ParseFile photoFile = new ParseFile("profile_pic.jpg", photoData);
        photoFile.saveInBackground(new SaveCallback() {

            public void done(ParseException e) {
                if (e != null) {
                    Log.d("saving pic", "error");
                } else {
                    Log.d("saving pic", "success");
                    user.put("pic", photoFile);
                    user.saveInBackground();
                }
            }
        });
    }

    private void buttonListeners() {

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

        sign_up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) sign_up.getLayoutParams();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    lp.height = px(60);
                    lp.topMargin = px(10);
                    sign_up.setLayoutParams(lp);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    lp.topMargin = px(0);
                    lp.height = px(70);
                    sign_up.setLayoutParams(lp);
                }
                return false;
            }
        });

    }

    //Turns dps to pixels
    private int px(float dips) {
        float dp = getResources().getDisplayMetrics().density;
        return Math.round(dips * dp);
    }

}
