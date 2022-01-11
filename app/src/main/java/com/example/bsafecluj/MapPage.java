package com.example.bsafecluj;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapPage extends FragmentActivity implements OnMapReadyCallback {

    User user;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    FloatingActionButton viewProfile;
    RadioButton dangerButton, safeButton;
    Database db = Database.getInstance(MapPage.this);
    boolean checked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_main_page);
        viewProfile = findViewById(R.id.profileButton);
        dangerButton = findViewById(R.id.danger);
        safeButton = findViewById(R.id.safe);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Bundle bundle = getIntent().getExtras();
        String phoneNr = bundle.getString("phoneNr");
        user=db.getUserFromDb(phoneNr);
        User finalUser1 = user;


        viewProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapPage.this, ProfilePage.class);
                i.putExtra("phoneNr", finalUser1.getPhoneNumber());
                startActivity(i);
            }
        });
        fetchLastLocation();

        dangerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 1000);
            }
        });

        safeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSafeSMStoGuardians();
                checked = true;
            }
        });


    }

    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + " " + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
                    //assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(MapPage.this);

                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here.");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }


    protected void sendSMSMessage(String phoneNr, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNr, null, message, null, null);
    }

    protected void sendDangerSMStoGuardians(){
        fetchLastLocation();
        String message = "Hello i m in danger pls come fast http://maps.google.com/?q=" + currentLocation.getLatitude()  + "," + currentLocation.getLongitude();
        for( Guardian guardian: db.getGuardianList(user))
            sendSMSMessage(guardian.getPhoneNumber(),message );
    }

    protected void sendSafeSMStoGuardians(){
        fetchLastLocation();
        String message = "I am safe now!!!";
        for( Guardian guardian: db.getGuardianList(user))
            sendSMSMessage(guardian.getPhoneNumber(),message );
    }

    final Handler handler = new Handler();
    final Runnable runnable = new Runnable() {
        public void run() {
            if(checked){ // just remove call backs
                handler.removeCallbacks(this);
            } else { // post again
               // Log.d("Runnable","Handler is working");
                sendDangerSMStoGuardians();
                handler.postDelayed(this, 30*1000);
            }
        }
    };






}
