package com.example.bsafecluj;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;
import android.widget.Toast;

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
    EditText enterPhone;


    public static synchronized Database getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Database(context.getApplicationContext());
        }
        return sInstance;
    }


    public Database(@Nullable Context context) {
        super(context, "bSafeClujDB.db", null, 3);
    }

    //first time app is created
    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTableStm = "CREATE TABLE " + USER_TABLE + " ( " + ID_USER_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME_COLUMN + " , " + PHONE_NUMBER_COLUMN +
                " TEXT, " + BIRTH_YEAR_COLUMN + " INTEGER, loggedStatus TEXT)";
        String createTableGuardians = "CREATE TABLE Guardian ( idGuardian INTEGER PRIMARY KEY AUTOINCREMENT, usernameGuardian TEXT, phoneNrGuardian TEXT )";
        String createTableUserGuardians = "CREATE TABLE UserGuardian ( idUser INTEGER , idGuardian INTEGER, FOREIGN KEY (idUser) REFERENCES USER (idUser) ,FOREIGN KEY (\n" +
                "idGuardian) REFERENCES Guardian (idGuardian))";
        db.execSQL(createTableStm);
        db.execSQL(createTableGuardians);
        db.execSQL(createTableUserGuardians);
    }

    //version changes

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean storeUserLoginData(User user) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(USERNAME_COLUMN, user.getUsername());
        cv.put(BIRTH_YEAR_COLUMN, user.getBirthYear());
        cv.put(PHONE_NUMBER_COLUMN, user.getPhoneNumber());
        long insert = db.insert(USER_TABLE, null, cv);
        return insert != -1;

    }

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


   // @SuppressLint("Range")
    public List<Guardian> getGuardianList(User user) {

        SQLiteDatabase db = this.getReadableDatabase();
        final String ID_USER=String.valueOf(user.getIdUser());
        Cursor cursor = db.rawQuery("SELECT * FROM "+ GUARDIAN_TABLE+ " JOIN "+USER_GUARDIAN_TABLE + " ON "+GUARDIAN_TABLE+"."+ID_GUARDIAN_COLUMN+"="+USER_GUARDIAN_TABLE+"."+ID_GUARDIAN_COLUMN2+ " WHERE "+ USER_GUARDIAN_TABLE+"."+ ID_USER_COLUMN2+"= "+ID_USER,null);
        List<Guardian>guardianList = new ArrayList<>();

        if (cursor.moveToFirst()){
            guardianList.add(new Guardian(Integer.parseInt(cursor.getString(cursor.getColumnIndex("idGuardian"))),cursor.getString(cursor.getColumnIndex("usernameGuardian")), cursor.getString(cursor.getColumnIndex("phoneNrGuardian"))));
            while(cursor.moveToNext()){
                guardianList.add(new Guardian(Integer.parseInt(cursor.getString(cursor.getColumnIndex("idGuardian"))),cursor.getString(cursor.getColumnIndex("usernameGuardian")), cursor.getString(cursor.getColumnIndex("phoneNrGuardian"))));
            }
        }
        cursor.close();
        return guardianList;

    }

// wont need to sing up again
    @SuppressLint("Range")
    public User checkExistingUser(String phoneNr){
        SQLiteDatabase db = this.getReadableDatabase();
        User user = new User();
        boolean userFound=false;
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE + " WHERE " + PHONE_NUMBER_COLUMN + " = " + phoneNr, null);
        if(cursor.moveToFirst()){
            userFound=true;
            do {
                user= new User(Integer.parseInt(cursor.getString(cursor.getColumnIndex("idUser"))), cursor.getString(cursor.getColumnIndex("username")), cursor.getString(cursor.getColumnIndex("phoneNumber")), Integer.parseInt(cursor.getString(cursor.getColumnIndex("birthYear"))),(cursor.getString(cursor.getColumnIndex("loggedStatus"))));
            }
            while ( cursor.moveToNext());
        }
        cursor.close();
        if(userFound)
            return user;
        else return null;
    }


    @SuppressLint("Range")
    public void removeGuardian(Guardian guardian){

        SQLiteDatabase db = this.getWritableDatabase();

        String query="DELETE FROM UserGuardian WHERE idGuardian="+guardian.getIdGuardian();
        db.execSQL(query);

        String query2="DELETE FROM Guardian WHERE idGuardian="+guardian.getIdGuardian();
        db.execSQL(query2);

    }


    public User getUserFromDb(String phoneNr){

        SQLiteDatabase db = this.getReadableDatabase();
        String query="SELECT * FROM USER WHERE phoneNumber="+phoneNr;
        Cursor cursor= db.rawQuery(query,null);
        User user=new User(){};

        if (cursor.moveToFirst()) {
            do {

                if(cursor.getString(cursor.getColumnIndex("birthYear"))==null || cursor.getString(cursor.getColumnIndex("phoneNumber") )== null){
                    System.out.println("ERROR");
                }
                else{
                    user= new User(Integer.parseInt(cursor.getString(cursor.getColumnIndex("idUser"))), cursor.getString(cursor.getColumnIndex("username")), cursor.getString(cursor.getColumnIndex("phoneNumber")), Integer.parseInt(cursor.getString(cursor.getColumnIndex("birthYear"))),(cursor.getString(cursor.getColumnIndex("loggedStatus"))));
                }
            } while (cursor.moveToNext());


        }
        cursor.close();
        return user;

    }

    public User getLoggedUserFromDb(){

        SQLiteDatabase db = this.getReadableDatabase();

        String query="SELECT * FROM USER WHERE loggedStatus='true'";
        Cursor cursor= db.rawQuery(query,null);


        User user=new User(){};

        if (cursor.moveToFirst()) {

            user= new User(Integer.parseInt(cursor.getString(cursor.getColumnIndex("idUser"))), cursor.getString(cursor.getColumnIndex("username")), cursor.getString(cursor.getColumnIndex("phoneNumber")), Integer.parseInt(cursor.getString(cursor.getColumnIndex("birthYear"))),(cursor.getString(cursor.getColumnIndex("loggedStatus"))));

        } while (cursor.moveToNext());


        cursor.close();
        return user;

    }

    public void changeLoggedStatus(String phoneNr,String status){

        SQLiteDatabase db = this.getWritableDatabase();

        String query="UPDATE USER SET loggedStatus = '"+status+"' WHERE phoneNumber ="+phoneNr;
        db.execSQL(query);

    }

    public void editProfile(String phoneNr,String username, int year){

        SQLiteDatabase db = this.getWritableDatabase();

        String query="UPDATE USER SET username = '"+username+"' , birthYear= "+year+" WHERE phoneNumber ="+phoneNr;
        db.execSQL(query);

    }
}



