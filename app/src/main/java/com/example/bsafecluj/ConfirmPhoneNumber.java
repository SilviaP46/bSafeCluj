package com.example.bsafecluj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfirmPhoneNumber extends AppCompatActivity {

    Button confirmPhoneNrBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_phone_number);

        confirmPhoneNrBtn =findViewById(R.id.confirmPhoneButton);


        confirmPhoneNrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {

                    startActivity(new Intent(ConfirmPhoneNumber.this, SignUpActivity.class));
                }
                catch(Exception e){
                    Toast.makeText(ConfirmPhoneNumber.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();



                }

            }
        });

    }

}

