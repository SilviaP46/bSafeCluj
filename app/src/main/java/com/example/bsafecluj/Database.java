package com.example.bsafecluj;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final String GUARDIAN_TABLE = "Guardian";
    private static final String ID_GUARDIAN_COLUMN = "idGuardian";
    private static final String USER_GUARDIAN_TABLE = "UserGuardian";
    private static final String USERNAME_GUARDIAN = "usernameGuardian";
    private static final String PHONE_NUMBER_GUARDIAN = "phoneNrGuardian";
    private static final String ID_USER_COLUMN2 = "idUser";
    private static final String ID_GUARDIAN_COLUMN2 = "idGuardian";
    private static Database sInstance;

    public static final String USER_TABLE = "USER";
    public static final String ID_USER_COLUMN = "idUser";
    public static final String USERNAME_COLUMN = "username";
    public static final String PHONE_NUMBER_COLUMN = "phoneNumber";
    public static final String BIRTH_YEAR_COLUMN = "birthYear";


    public static synchronized Database getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Database(context.getApplicationContext());
        }
        return sInstance;
    }


    public Database(@Nullable Context context) {
        super(context, "bSafeClujDB.db", null, 1);
    }

    //first time app is created
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStm = "CREATE TABLE " + USER_TABLE + " ( " + ID_USER_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME_COLUMN + " , " + PHONE_NUMBER_COLUMN + " INTEGER, " + BIRTH_YEAR_COLUMN + " INTEGER)";
        String createTableGuardians = "CREATE TABLE " + GUARDIAN_TABLE + " ( " + ID_GUARDIAN_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME_GUARDIAN + " TEXT, " + PHONE_NUMBER_GUARDIAN + ")";
        String createTableUserGuardians = "CREATE TABLE " + USER_GUARDIAN_TABLE + " ( " + ID_USER_COLUMN2 + " , " + ID_GUARDIAN_COLUMN2 + " TEXT, " +" FOREIGN KEY (" + ID_USER_COLUMN2 + ") REFERENCES " + USER_TABLE + "(" + ID_USER_COLUMN + ") ," +
                "FOREIGN KEY (" + ID_GUARDIAN_COLUMN2 + ") REFERENCES " + GUARDIAN_TABLE + "(" + ID_GUARDIAN_COLUMN + "))";
        db.execSQL(createTableStm);
        db.execSQL(createTableGuardians);
        db.execSQL(createTableUserGuardians);
    }

    //version changes

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean storePhoneNr(User user) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USERNAME_COLUMN, user.getUsername());
        cv.put(PHONE_NUMBER_COLUMN, user.getPhoneNumber());
        long insert = db.insert(USER_TABLE, null, cv);
        return insert != -1;

    }

    public boolean storeUserLoginData(User user) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USERNAME_COLUMN, user.getUsername());
        cv.put(BIRTH_YEAR_COLUMN, user.getBirthYear());
        cv.put(PHONE_NUMBER_COLUMN, "+40" + user.getPhoneNumber());
        long insert = db.insert(USER_TABLE, null, cv);
        return insert != -1;

    }

    /*public void storeGuardianList(List<Guardian> guardianList, User user) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorCourses = db.rawQuery("SELECT " + ID_GUARDIAN_COLUMN + " FROM " + GUARDIAN_TABLE, null);
        ArrayList<Integer> guardiansIds = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                guardiansIds.add(Integer.valueOf(cursorCourses.getString(1)));
            } while (cursorCourses.moveToNext());


        }
        cursorCourses.close();

        for(Guardian g:guardianList){
            if(g.getIdGuardian())
        }

    }*/


    public void storeGuardianList(User user, Guardian guardian) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues guardianTableEntry = new ContentValues();
        guardianTableEntry.put(ID_GUARDIAN_COLUMN, guardian.getIdGuardian());
        guardianTableEntry.put(USERNAME_GUARDIAN, guardian.getUsername());
        guardianTableEntry.put(PHONE_NUMBER_GUARDIAN, guardian.getPhoneNumber());
        db.insert(GUARDIAN_TABLE, null, guardianTableEntry);

        ContentValues userGuardianEntry = new ContentValues();
        userGuardianEntry.put(ID_USER_COLUMN, user.getIdUser());
        userGuardianEntry.put(ID_GUARDIAN_COLUMN, guardian.getIdGuardian());
        db.insert(USER_GUARDIAN_TABLE, null, userGuardianEntry);
    }


    public List<Guardian> getGuardianList(User user) {

        SQLiteDatabase db = this.getReadableDatabase();
        final String ID_USER=String.valueOf(user.getIdUser());
        Cursor cursorCourses = db.rawQuery("SELECT * FROM "+ GUARDIAN_TABLE+ " JOIN "+USER_GUARDIAN_TABLE + " ON "+GUARDIAN_TABLE+"."+ID_GUARDIAN_COLUMN+"="+USER_GUARDIAN_TABLE+"."+ID_GUARDIAN_COLUMN2+ " WHERE "+ USER_GUARDIAN_TABLE+"."+ ID_GUARDIAN_COLUMN2+"= "+ID_USER,null);
        List<Guardian>guardianList = new ArrayList<>();

        if (cursorCourses.moveToFirst()) {
            do {
                guardianList.add(new Guardian(Integer.parseInt(cursorCourses.getString(1)),cursorCourses.getString(2), cursorCourses.getString(3)));

            } while (cursorCourses.moveToNext());


        }
        cursorCourses.close();
        return guardianList;

    }

    @SuppressLint("Range")
    public User checkExistingUser(String phoneNr){
        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();

        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + "WHERE" + PHONE_NUMBER_COLUMN + "=" + phoneNr, null);

        if(cursor.moveToFirst()){
            do {
                user= new User(Integer.parseInt(cursor.getString(cursor.getColumnIndex("idUser"))), cursor.getString(cursor.getColumnIndex("username")), cursor.getString(cursor.getColumnIndex("phoneNumber")), Integer.parseInt(cursor.getString(cursor.getColumnIndex("birthYear"))));
            }
            while ( cursor.moveToNext());
        }
        cursor.close();
        return user;
    }
}



