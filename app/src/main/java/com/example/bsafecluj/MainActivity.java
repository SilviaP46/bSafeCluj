package com.example.bsafecluj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int SMS_PERMISSION_CODE = 1;
    Button nextButton;
    EditText enterPhone;
    private String phoneNr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Database db = Database.getInstance(MainActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nextButton=findViewById(R.id.nextButton);
        enterPhone=findViewById(R.id.editTextPhone);

        SharedPreferences prefs = getSharedPreferences("name", MODE_PRIVATE);
        boolean isLoggedIn= prefs.getBoolean("isLoggedIn", false);

        if(isLoggedIn){
            Intent i = new Intent(MainActivity.this, MapPage.class);
            i.putExtra("phoneNr",enterPhone.getText().toString());
            startActivity(i);
            finish();
            return;
        }


        //button listeners
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user;

                try {
                    user = new User(1, enterPhone.getText().toString());
                    Toast.makeText(MainActivity.this, user.getPhoneNumber().toString(), Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(MainActivity.this, ConfirmPhoneNumber.class);
                    i.putExtra("phoneNr",enterPhone.getText().toString());
                    i.putExtra("user", (Parcelable) user);
                    startActivity(i);


                }
                catch(Exception e){
                    Toast.makeText(MainActivity.this, "Enter phone number!!", Toast.LENGTH_SHORT).show();
                    user=new User(-1,"error","?",0);


                }

            }
        });
    }


}