package com.pain.orderdrinks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class MyDatabase extends SQLiteOpenHelper {

    private static String DBNAME = "drinks.db";
    private static int DBVERSION = 1;
    private String table = "CREATE TABLE drink(name TEXT PRIMARY KEY NOT NULL, cost INTEGER NOT NULL, type TEXT)";

    public MyDatabase(Context context) {
        super(context, DBNAME, null, DBVERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        arg0.execSQL(table);

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}
