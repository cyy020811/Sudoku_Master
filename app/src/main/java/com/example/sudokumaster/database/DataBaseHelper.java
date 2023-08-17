package com.example.sudokumaster.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String PROGRESS_TABLE = "PROGRESS_TABLE";
    public static final String ID = "ID";
    public static final String EASY = "EASY";
    public static final String MEDIUM = "MEDIUM";
    public static final String HARD = "HARD";
    public static final String POINT = "POINT";

    public DataBaseHelper(@Nullable Context context) {
        super(context, "progress.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + PROGRESS_TABLE +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                EASY + " INTEGER NOT NULL, " +
                MEDIUM + " INTEGER NOT NULL, " +
                HARD + " INTEGER NOT NULL, " +
                POINT + " INTEGER NOT NULL)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Create initial user data
    public boolean createInitialProfile() {
        long insert;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EASY, 0);
        cv.put(MEDIUM, 0);
        cv.put(HARD, 0);
        cv.put(POINT, 0);
        insert = db.insert(PROGRESS_TABLE, null, cv);
        if (insert < 0) return false;
        return true;
    }

    // Obtain data from the database
    public HashMap<String, Integer> getData() {
        HashMap<String, Integer> data = new HashMap<>();
        String query = "SELECT * FROM " + PROGRESS_TABLE;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            data.put(EASY, cursor.getInt(1));
            data.put(MEDIUM, cursor.getInt(2));
            data.put(HARD, cursor.getInt(3));
            data.put(POINT, cursor.getInt(4));
        }
        cursor.close();
        db.close();
        return data;
    }

    // Update data in the database
    public void updateData(String column, int value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(column, value);
        db.update(PROGRESS_TABLE, cv, null, null);

    }

    public void resetData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EASY, 0);
        cv.put(MEDIUM, 0);
        cv.put(HARD, 0);
        cv.put(POINT, 0);
        db.update(PROGRESS_TABLE, cv, null, null);
    }
}
