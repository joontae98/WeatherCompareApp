package com.example.weathercompareapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.weathercompareapp.DataBases.CreateDB.*;

public class DBOpenHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateDB._CREATE0);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateDB._TABLENAME0);
            onCreate(db);
        }
    }

    public DBOpenHelper(Context context) {           //생성자
        this.mCtx = context;
    }

    public DBOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create() {
        mDBHelper.onCreate(mDB);
    }

    public void close() {
        mDB.close();
    }

    public void insertColumn(String insertTime, String iconId, String temp, int differTemp) {
        Cursor cursor = mDB.query(_TABLENAME0, new String[]{TIME}, null, null, null, null, null);
        boolean checkDB = false;
        while (cursor.moveToNext()) {
            if ((cursor.getString(0)).equals(insertTime)) {
                checkDB = true;
                break;
            }
        }

        if (checkDB == false) {
            ContentValues values = new ContentValues();
            values.put(TIME, insertTime);
            values.put(ICONID, iconId);
            values.put(TEMP, temp);
            values.put(DIFFERTEMP, differTemp);
            mDB.insert(_TABLENAME0, null, values);
        }
    }
    public void deleteAllColumns() {
        mDB.delete(DataBases.CreateDB._TABLENAME0, null, null);
    }
}

