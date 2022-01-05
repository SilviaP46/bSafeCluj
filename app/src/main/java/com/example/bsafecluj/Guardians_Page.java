package com.example.bsafecluj;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Guardians_Page extends AppCompatActivity {

    private static final int CONTACT_PERMISSION_CODE = 1;
    private static final int CONTACT_PICK_CODE = 2;
    TextView contactTextView;
    User user;
    ListView contactsListView;
    List<Guardian> contacts = new ArrayList<>();
    Database db = Database.getInstance(Guardians_Page.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.guardians_page);
        getSupportActionBar().setTitle("Manage Guardians");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contactTextView = findViewById(R.id.textView9);
        contactsListView = findViewById(R.id.guardiansList);

        //ImageView addContactImageView =findViewById(R.id.addContactIcon);
        FloatingActionButton addContact = findViewById(R.id.profileButton);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getParcelable("user");
        }

        user.setGuardianList(db.getGuardianList(user));

        showGuardianList();

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkContactPermission()) {
                    pickContacts();
                } else {
                    requestContactPermission();
                }

                showGuardianList();
            }
        });


    }

    private void showGuardianList() {

        List<String> guardianNames = new ArrayList<>();
        //Toast.makeText(this,"ianinte"+guardianNames.size(),Toast.LENGTH_SHORT).show();
        for (int i = 0; i <= user.getGuardianList().size() - 1; i++) {
            guardianNames.add(user.getGuardianList().get(i).getUsername());
        }


        //Toast.makeText(this,"dupa"+guardianNames.size(),Toast.LENGTH_SHORT).show();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, guardianNames);
        contactsListView.setAdapter(arrayAdapter);

    }

    private boolean checkContactPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == (PackageManager.PERMISSION_GRANTED);

    }

    private void requestContactPermission() {
        String[] permission = {Manifest.permission.READ_CONTACTS};
        ActivityCompat.requestPermissions(this, permission, CONTACT_PERMISSION_CODE);
    }

    private void pickContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CONTACT_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickContacts();
            } else {
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @SuppressLint("Recycle")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            contactTextView.setText("");
            Cursor c1, c2;
            assert data != null;
            Uri uri = data.getData();

            c1 = getContentResolver().query(uri, null, null, null, null);

            if (c1.moveToFirst()) {
                @SuppressLint("Range") String contactId = c1.getString(c1.getColumnIndex(ContactsContract.Contacts._ID));
                @SuppressLint("Range") String contactName = c1.getString(c1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                @SuppressLint("Range") String idResults = c1.getString(c1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                int idResultHold = Integer.parseInt(idResults);

                if (idResultHold == 1) {
                    c2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);


                    while (c2.moveToNext()) {
                        @SuppressLint("Range") String contactNumber = c2.getString(c2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        if (!checkIfContactExists(contactNumber)) {
                            contacts.add(new Guardian(Integer.parseInt(contactId), contactName, contactNumber));
                            db.storeGuardianList(user,new Guardian(Integer.parseInt(contactId), contactName, contactNumber));

                        } else {
                            Toast.makeText(this, "Contact already exits. Choose another!", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    }
                    user.setGuardianList(contacts);
                    //contactTextView.setText(user.getGuardianList().get(0).getPhoneNumber().toString());
                    c2.close();
                }
                c1.close();
                showGuardianList();
            }


        } else {
            Toast.makeText(this, "No new guardian selected", Toast.LENGTH_SHORT).show();

        }
    }


    public boolean checkIfContactExists(String phoneNr) {

        if(user.getGuardianList().size()==0)
            return false;

        for (Guardian g : user.getGuardianList()) {
            if (g.getPhoneNumber().equals(phoneNr)) {

                return true;
            }
        }
        return false;
    }
}
