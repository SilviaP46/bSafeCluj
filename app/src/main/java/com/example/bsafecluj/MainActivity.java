package com.example.bsafecluj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int SMS_PERMISSION_CODE = 1;
    Button nextButton;
    EditText enterPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Database db = Database.getInstance(MainActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextButton=findViewById(R.id.nextButton);
        enterPhone=findViewById(R.id.editName);

        if(db.getLoggedUserFromDb().getLoggedStatus()!=null && db.getLoggedUserFromDb().getLoggedStatus().equals("true")){
            Intent i = new Intent(MainActivity.this, MapPage.class);
            i.putExtra("phoneNr", db.getLoggedUserFromDb().getPhoneNumber());
            startActivity(i);
            finish();
           return;
        }


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkSMSPermission()) {
                    User user;
                    final String phoneNr = enterPhone.getText().toString();
                    try {

                        if (!phoneNr.matches("^07[0-9]{8}")) {
                            Toast.makeText(MainActivity.this, "Enter a valid phone number!", Toast.LENGTH_SHORT).show();
                        } else {
                            user = new User(enterPhone.getText().toString());
                            Toast.makeText(MainActivity.this, user.getPhoneNumber(), Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(MainActivity.this, ConfirmPhoneNumber.class);
                            i.putExtra("phoneNr", enterPhone.getText().toString());
                            i.putExtra("user", (Parcelable) user);
                            startActivity(i);
                        }

                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Enter phone number!!", Toast.LENGTH_SHORT).show();
                        user = new User("error");


                    }
                }
                else
                    Toast.makeText(MainActivity.this, "Please grand permission, then press again the button!", Toast.LENGTH_LONG).show();
                    requestSMSPermission();
                }

        });

    }


    // Function to check and request permission.
    private boolean checkSMSPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == (PackageManager.PERMISSION_GRANTED);
    }


    private void requestSMSPermission() {
        String[] permission = {Manifest.permission.SEND_SMS};
        ActivityCompat.requestPermissions(this, permission, SMS_PERMISSION_CODE);
        boolean allowed = false;
        if(checkSMSPermission())
            allowed = true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "SMS Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(MainActivity.this, "SMS Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }

    }

}

