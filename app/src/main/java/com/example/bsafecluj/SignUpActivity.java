package com.example.bsafecluj;

import android.content.Intent;
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

        Bundle extras = getIntent().getExtras();
        Intent i=getIntent();
        if (extras != null) {
            user=extras.getParcelable("user");
            //The key argument here must match that used in the other activity
        }

        user.setBirthYear(Integer.parseInt(String.valueOf(enterBirthYear)));
        user.setUsername(String.valueOf(enterUserName));

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(SignUpActivity.this,SignUpActivity.class));
                    db.storeNameAndBirthDate(user);
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
    }
}
