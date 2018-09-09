package com.muliamaulana.pointofsale.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.muliamaulana.pointofsale.database.DatabaseContract.TABLE_ITEM;

/**
 * Created by muliamaulana on 9/9/2018.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static  String DATABASE_NAME = "itemdb";
    public static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_ITEM = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s VARCHAR," +
                    " %s blob)",
            TABLE_ITEM,
            DatabaseContract.NoteColumns._ID,
            DatabaseContract.NoteColumns.NAME,
            DatabaseContract.NoteColumns.PRICE,
            DatabaseContract.NoteColumns.IMAGE
    );
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_ITEM);
        onCreate(sqLiteDatabase);
    }


}
