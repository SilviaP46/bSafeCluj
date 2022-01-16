package com.example.bsafecluj;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Pop extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupwindow);

        TextView username=findViewById(R.id.editName);
        TextView year=findViewById(R.id.editYear);
        Button saveButton =findViewById(R.id.saveProfileButton);

        Database db = Database.getInstance(Pop.this);

        Bundle bundle = getIntent().getExtras();
        String phoneNr = bundle.getString("phoneNr");
        User user = db.getUserFromDb(phoneNr);

        //Toast.makeText(getApplicationContext(), user.getUsername(), Toast.LENGTH_SHORT).show();


        username.setText(user.getUsername());
        year.setText(user.getBirthYear().toString());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.editProfile(user.getPhoneNumber(),username.getText().toString(),Integer.parseInt(year.getText().toString()));
                Intent i = new Intent(Pop.this, ProfilePage.class);
                i.putExtra("phoneNr", user.getPhoneNumber());
                startActivity(i);
            }
        });




    }
}
