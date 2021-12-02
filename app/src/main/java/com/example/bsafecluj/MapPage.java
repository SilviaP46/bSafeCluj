package com.example.bsafecluj;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;

import com.google.android.gms.maps.model.MarkerOptions;

public class MapPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Database db = Database.getInstance(MapPage.this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_main_page);

        Fragment fragment=new MapFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();

    }
}
