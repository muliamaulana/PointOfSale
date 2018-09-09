package com.muliamaulana.pointofsale.database;

import android.provider.BaseColumns;

/**
 * Created by muliamaulana on 9/9/2018.
 */
public class DatabaseContract {
    static String TABLE_ITEM = "item";
    static final class NoteColumns implements BaseColumns {
        static String NAME = "name";
        static String PRICE = "price";
        static String IMAGE = "image";
    }
}
