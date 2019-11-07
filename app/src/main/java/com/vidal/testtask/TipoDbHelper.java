package com.vidal.testtask;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TipoDbHelper extends SQLiteOpenHelper {


        public String TABLE_NAME="test_table";
        public String FIELD_CODE1="response_field";

        public TipoDbHelper(Context ct, String nm, SQLiteDatabase.CursorFactory cf, int vs){
            super(ct, nm, cf, vs);
        }

        @Override
        public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
            Log.d("myLogs","| Upgrade |"+DB.toString());
        }

        @Override
        public void onCreate(SQLiteDatabase DB) {
            Log.d("myLogs","| Create |"+DB.toString());
            String query = "CREATE TABLE " + TABLE_NAME + " ( " +
                    FIELD_CODE1  + " TEXT)";
            DB.execSQL(query);
        }


}
