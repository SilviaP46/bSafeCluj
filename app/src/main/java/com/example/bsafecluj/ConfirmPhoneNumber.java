package com.example.bsafecluj;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class ConfirmPhoneNumber extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    Button confirmPhoneNrBtn;
    String phoneNo;
    String message;
    String randomCode;
    EditText enterCode;
    Database db;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Database db = Database.getInstance(ConfirmPhoneNumber.this);
        Random random=new Random();
        randomCode=String.format("%04d", random.nextInt(10000));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_phone_number);
        confirmPhoneNrBtn =findViewById(R.id.confirmPhoneButton);
        enterCode=findViewById(R.id.enterCode);



//        Bundle bundle = getIntent().getExtras();
//        String phoneNr = bundle.getString("phoneNr");
//
//        user=db.getUserFromDb(phoneNr).get(0);

        Bundle extras = getIntent().getExtras();
        Intent i=getIntent();
        if (extras != null) {
            phoneNo = extras.getString("phoneNr");
            user=extras.getParcelable("user");

        }

        sendSMSMessage();



        confirmPhoneNrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(randomCode.equals(enterCode.getText().toString())){
//                        if( db.checkExistingUser(phoneNo) != null)
//                        {
//                            Toast.makeText(ConfirmPhoneNumber.this, "You already have an account", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(ConfirmPhoneNumber.this, MapPage.class);
//                        }
//                        else {
                            Intent i = new Intent(ConfirmPhoneNumber.this, SignUpActivity.class);
                            i.putExtra("user", (Parcelable) user);
                            startActivity(i);
//                        }
                    }
                    else{
                        Toast.makeText(ConfirmPhoneNumber.this, "Code is incorrect!!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e){
                    Toast.makeText(ConfirmPhoneNumber.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();

                }

            }
        });
        //db.storePhoneNr(user);
    }


    protected void sendSMSMessage() {
        //phoneNo = user.getPhoneNumber().toString();
        message = "Your security code is: "+randomCode;


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) { } }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            Toast.makeText(ConfirmPhoneNumber.this, "ok", Toast.LENGTH_SHORT).show();
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
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }


}


