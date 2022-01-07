package com.example.bsafecluj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button nextButton;
    EditText enterPhone;
    private String phoneNr;

    SharedPreferences sharedPreferences;

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
            startActivity(new Intent(getApplicationContext(),MapPage.class));
            finish();
            return;
        }


        //button listeners
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user;
                final String phoneNr = enterPhone.getText().toString();
                try {
                    if (!phoneNr.matches("^07[0-9]{8}")){
                        Toast.makeText(MainActivity.this, "Enter a valid phone number!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        user = new User(1, enterPhone.getText().toString());
                        Toast.makeText(MainActivity.this, user.getPhoneNumber().toString(), Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(MainActivity.this, ConfirmPhoneNumber.class);
                        i.putExtra("phoneNr", enterPhone.getText().toString());
                        i.putExtra("user", (Parcelable) user);
                        startActivity(i);
                    }

                }
                catch(Exception e){
                    Toast.makeText(MainActivity.this, "Enter phone number!!", Toast.LENGTH_SHORT).show();
                    user=new User(-1,"error","?",0);


                }


                //db.storePhoneNr(user);

            }
        });
    }
}