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
    public static final String BIRTH_YEAR_COLUMN = "birthYear";

    public Database(@Nullable Context context) {
        super(context, "bSafeCluj.db", null, 1);
    }

    //first time app is created
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStm= "CREATE TABLE " + USER_TABLE + " ( " + ID_USER_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME_COLUMN + " TEXT, " + PHONE_NUMBER_COLUMN + " INTEGER, "+BIRTH_YEAR_COLUMN + " INTEGER)";
        db.execSQL(createTableStm);
    }

    //version changes

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean storePhoneNr(User user){

        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(USERNAME_COLUMN,user.getUsername());
        cv.put(PHONE_NUMBER_COLUMN,user.getPhoneNumber());

        long insert= db.insert(USER_TABLE,null ,cv);

        return insert != -1;

    }

    public boolean storeNameAndBirthDate(User user){

        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues cv= new ContentValues();

        cv.put(USERNAME_COLUMN,user.getUsername());
        cv.put(BIRTH_YEAR_COLUMN,user.getBirthYear());

        long insert= db.insert(USER_TABLE,null ,cv);

        return insert != -1;

    }
}
