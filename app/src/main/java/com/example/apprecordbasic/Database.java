package com.example.apprecordbasic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database  extends SQLiteOpenHelper {

    private static final String LOG_TAG = "Database";

    public Database(@Nullable Context context) {
        super(context, "data.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User (email TEXT PRIMARY KEY , password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
    }

    //inserting database
    public boolean insert(String email , String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email" , email);
        contentValues.put("password",password);
        long ins = db.insert("User" , null, contentValues);
        if (ins == -1) return false;
        else return true;
    }

    //checking email
    public boolean checkEmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE email = ?" , new String[]{email});
        if(cursor.getCount() > 0 ) return false;
        else return true;
    }

    //check email and pass
    public boolean emailPass(String email , String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE email = ? AND password = ?" , new String[]{email,password});
        if(cursor.getCount() > 0 ) return true;
        else return false;
    }

}
