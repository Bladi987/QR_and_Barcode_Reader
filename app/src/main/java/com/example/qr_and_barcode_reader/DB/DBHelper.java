package com.example.qr_and_barcode_reader.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    private static final String BD_NAME="Servers.sqlite";
    private static final int BD_VERSION=1;

    public DBHelper(Context context ) {
        super(context, BD_NAME, null, BD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBManager.CREATE_TABLE_SERVER);
        db.execSQL(DBManager.CREATE_TABLE_RECORD);
        db.execSQL(DBManager.CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
