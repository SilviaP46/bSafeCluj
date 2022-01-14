package com.example.bsafecluj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfilePage extends AppCompatActivity {

    User user;
    Database db;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private TextView editName,editYear;
    Button saveButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
         db = Database.getInstance(ProfilePage.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getSupportActionBar().setTitle("Your Profile");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView logOutImageView = findViewById(R.id.logOutImageView);
        ImageView manageGuardiansImageView = findViewById(R.id.manageGuardiansImageView);
        ImageView ediProfileImageView = findViewById(R.id.editProfileImageView);
        TextView textView=findViewById(R.id.usernameTextView);

        Bundle bundle = getIntent().getExtras();
        String phoneNr = bundle.getString("phoneNr");

        user = db.getUserFromDb(phoneNr);

        textView.setText(user.getUsername());



        ediProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ProfilePage.this, Pop.class);
                i.putExtra("phoneNr", user.getPhoneNumber());
                startActivity(i);

            }
        });

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

   /* public void createPopUp(){
        dialogBuilder=new AlertDialog.Builder(this);
        final View contactPopupView =getLayoutInflater().inflate(R.layout.popupwindow,null);
        editName= contactPopupView.findViewById(R.id.textViewName);
        editYear=contactPopupView.findViewById(R.id.textViewYear);
        saveButton=contactPopupView.findViewById(R.id.saveButton);


        DisplayMetrics dm =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;


        dialogBuilder.setView(contactPopupView);
        dialog=dialogBuilder.create();

        dialog.show();
        dialog.getWindow().setLayout((int)(width*.8),(int)(height*.4));




    }*/
}
