package com.example.bsafecluj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    Button nextButton;
    EditText enterPhone;

    String phoneNo;
    String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                    //startActivity(new Intent(MainActivity.this, ConfirmPhoneNumber.class));
                    sendSMSMessage();
                }
                catch(Exception e){
                    Toast.makeText(MainActivity.this, "Enter phone number!!", Toast.LENGTH_SHORT).show();
                    user=new User(-1,"error",Long.parseLong("0"));


                }

                Database db = new Database(MainActivity.this);
                db.addOne(user);
            }
        });
    }

    protected void sendSMSMessage() {
        phoneNo = enterPhone.getText().toString();
        message = "Salut Silvia!";

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }




}