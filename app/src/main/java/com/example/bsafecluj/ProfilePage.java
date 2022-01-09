package com.example.bsafecluj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilePage extends AppCompatActivity {

    User user;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         db = Database.getInstance(ProfilePage.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getSupportActionBar().setTitle("Your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView logOutImageView = findViewById(R.id.logOutImageView);
        ImageView manageGuardiansImageView = findViewById(R.id.manageGuardiansImageView);
        ImageView ediProfileImageView = findViewById(R.id.editProfileImageView);

        Bundle bundle = getIntent().getExtras();
        String phoneNr = bundle.getString("phoneNr");

        user = db.getUserFromDb(phoneNr);


        logOutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.setLoggedStatus("false");
                db.changeLoggedStatus(user.getPhoneNumber(),"false");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                finish();
            }
        });


        User finalUser = user;

        manageGuardiansImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, Guardians_Page.class);
                intent.putExtra("phoneNr", finalUser.getPhoneNumber());
                startActivityForResult(intent,1);

            }
        });


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String phoneNr=null;
        db = Database.getInstance(ProfilePage.this);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                phoneNr = data.getStringExtra("phoneNr");
            }
        }
        if(phoneNr!=null)
            user=db.getUserFromDb(phoneNr);

    }
}
