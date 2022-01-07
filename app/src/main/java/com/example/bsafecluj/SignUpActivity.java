package com.example.bsafecluj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class SignUpActivity extends AppCompatActivity {
    Button signUpButton;
    //for profile image
    private ImageView ProfileImage;
    private static final int PICK_IMAGE = 1;
    Uri imageUri;
    User user;
    EditText enterUserName;
    EditText enterBirthYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Database db = Database.getInstance(SignUpActivity.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_signin);

        signUpButton = findViewById(R.id.signUpbutton);
        enterUserName=findViewById(R.id.enterUserName);
        enterBirthYear=findViewById(R.id.enterBirthYear);

//        Bundle bundle = getIntent().getExtras();
//        String phoneNr = bundle.getString("phoneNr");
//
//        user=db.getUserFromDb(phoneNr).get(0);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user=extras.getParcelable("user");
        }


        Toast.makeText(SignUpActivity.this, user.getPhoneNumber()+"  "+user.getBirthYear(), Toast.LENGTH_SHORT).show();



//

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String birthYear = enterBirthYear.getText().toString();
                try {

                    user.setUsername(enterUserName.getText().toString());
                    user.setBirthYear(Integer.parseInt(enterBirthYear.getText().toString()));

                    SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
                    editor.putString("phoneNr", user.getPhoneNumber().toString());
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();

                    if(user.getBirthYear()==null || user.getUsername()==null ){
                        Toast.makeText(SignUpActivity.this, "Make sure you enter a valid username and birthdate!", Toast.LENGTH_SHORT).show();
                    }
                    else if (!birthYear.matches("[1-2][0-9]{3}")){
                        Toast.makeText(SignUpActivity.this, "Make sure you enter a valid birthdate!", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        Toast.makeText(SignUpActivity.this, "Signed up successfully!", Toast.LENGTH_SHORT).show();

                        db.storeUserLoginData(user);
                        Intent i = new Intent(SignUpActivity.this, MapPage.class);
                        i.putExtra("phoneNr", user.getPhoneNumber());
                        startActivity(i);

                    }


                }
                catch(Exception e){
                    Toast.makeText(SignUpActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //Profile Image

        ProfileImage = (ImageView) findViewById(R.id.Profile_Image);
        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery, "SelectPicture"), PICK_IMAGE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ProfileImage.setImageBitmap(bitmap);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }

        //db.storeNameAndBirthDate(user);


    }
}
