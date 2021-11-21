package com.example.bsafecluj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Database db = new Database(MainActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nextButton=findViewById(R.id.nextButton);
        enterPhone=findViewById(R.id.editTextPhone);


        //button listeners
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user;

                try {
                    user = new User(1, Long.parseLong(enterPhone.getText().toString()));
                    Toast.makeText(MainActivity.this, user.getPhoneNumber().toString(), Toast.LENGTH_SHORT).show();


                    Intent i = new Intent(MainActivity.this, ConfirmPhoneNumber.class);
                    i.putExtra("phoneNr",enterPhone.getText().toString());
                    //i.putExtra("db", (Parcelable) db);
                    i.putExtra("user", (Parcelable) user);
                    startActivity(i);


                }
                catch(Exception e){
                    Toast.makeText(MainActivity.this, "Enter phone number!!", Toast.LENGTH_SHORT).show();
                    user=new User(-1,"error",Long.parseLong("0"),0);


                }

                db.storePhoneNr(user);

            }
        });
    }
}