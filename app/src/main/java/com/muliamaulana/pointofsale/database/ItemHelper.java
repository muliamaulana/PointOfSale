package com.muliamaulana.pointofsale.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.muliamaulana.pointofsale.database.DatabaseContract.NoteColumns.IMAGE;
import static com.muliamaulana.pointofsale.database.DatabaseContract.NoteColumns.NAME;
import static com.muliamaulana.pointofsale.database.DatabaseContract.NoteColumns.PRICE;
import static com.muliamaulana.pointofsale.database.DatabaseContract.TABLE_ITEM;

/**
 * Created by muliamaulana on 9/9/2018.
 */
public class ItemHelper {
    private static String DATABASE_TABLE = TABLE_ITEM;
    private Context mcontext;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public ItemHelper(Context context) {
        this.mcontext = context;
    }

    public ItemHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(mcontext);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public long create(ItemModel itemModel){
        ContentValues initValues = new ContentValues();
        initValues.put(NAME, itemModel.getName());
        initValues.put(PRICE, itemModel.getPrice());
        initValues.put(IMAGE, itemModel.getImage());
        return database.insert(DATABASE_TABLE,null,initValues);
    }

    public ArrayList<ItemModel> read() {
        ArrayList<ItemModel> noteArrayList = new ArrayList<ItemModel>();
        Cursor cursor = database.query(DATABASE_TABLE, null, null,
                null, null, null, _ID + " DESC", null);

        cursor.moveToFirst();
        ItemModel itemModel;
        if (cursor.getCount() > 0) {
            do {
                itemModel = new ItemModel();
                itemModel.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                itemModel.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME)));
                itemModel.setPrice(cursor.getString(cursor.getColumnIndexOrThrow(PRICE)));
                itemModel.setImage(cursor.getBlob(cursor.getColumnIndexOrThrow(IMAGE)));

                noteArrayList.add(itemModel);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return noteArrayList;
    }

    public int update(ItemModel itemModel){
        ContentValues args = new ContentValues();
        args.put(NAME, itemModel.getName());
        args.put(PRICE, itemModel.getPrice());
        args.put(IMAGE, itemModel.getImage());
        return database.update(DATABASE_TABLE,args,_ID + "= '"+itemModel.getId()+"'",null);
    }

    public int delete(int id){
        return database.delete(TABLE_ITEM, _ID + "= '"+id+"'",null);
    }

    public Cursor getItemImage(int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_ITEM +
                " WHERE _ID " + " = '" + id + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
}
