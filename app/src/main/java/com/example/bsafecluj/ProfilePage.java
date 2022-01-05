package com.example.bsafecluj;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Database db = Database.getInstance(ProfilePage.this);
        User user=new User();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getSupportActionBar().setTitle("Your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView logOutImageView=findViewById(R.id.logOutImageView);
        ImageView manageGuardiansImageView=findViewById(R.id.manageGuardiansImageView);
        ImageView ediProfileImageView=findViewById(R.id.editProfileImageView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user=extras.getParcelable("user");
        }

        User finalUser1 = user;
        logOutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
                editor.putString("phoneNr", "");
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("finish", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //intent.putExtra("user", (Parcelable) finalUser1);
                startActivity(intent);

                finish();
            }
        });


        User finalUser = user;
        manageGuardiansImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, Guardians_Page.class);
                intent.putExtra("user", (Parcelable) finalUser);
                startActivity(intent);

            }
        });





    }
}
