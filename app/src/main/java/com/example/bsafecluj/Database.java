package com.example.bsafecluj;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {

    public static final String USER_TABLE = "USER";
    public static final String ID_USER_COLUMN = "idUser";
    public static final String USERNAME_COLUMN = "username";
    public static final String PHONE_NUMBER_COLUMN = "phoneNumber";



    public Database(@Nullable Context context) {
        super(context, "bSafeCluj.db", null, 1);
    }

    //first time app is created
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStm= "CREATE TABLE " + USER_TABLE + " ( " + ID_USER_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME_COLUMN + " TEXT, " + PHONE_NUMBER_COLUMN + " INTEGER)";
        db.execSQL(createTableStm);
    }

    //version changes

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(User user){

        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(USERNAME_COLUMN,user.getUsername());
        cv.put(PHONE_NUMBER_COLUMN,user.getPhoneNumber());

        long insert= db.insert(USER_TABLE,null ,cv);

        return insert != -1;

    }
}
