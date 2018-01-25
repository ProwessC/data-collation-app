package com.prowess.datacollectiontool.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.prowess.datacollectiontool.data.DbContract.*;

/**
 * Created by Prowess on 07/07/2017.
 */

// this class is used to call database methods

class DbHelper extends SQLiteOpenHelper{
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DataCollector.db";
    private static final String SQL_CREATE_LANE_TABLE =
            "CREATE TABLE " + LaneEntries.TABLE_NAME + " (" +
                    LaneEntries._ID + " INTEGER PRIMARY KEY," +
                    LaneEntries.COLUMN_LANE_NAME + " TEXT," +
                    LaneEntries.COLUMN_LANE_NUMBER + " INTEGER UNIQUE," +
                    LaneEntries.COLUMN_DIRECTION + " TEXT)";

    private static final String SQL_CREATE_COUNT_TABLE =
            "CREATE TABLE " + CountEntries.TABLE_NAME + " (" +
                    CountEntries._ID + " INTEGER PRIMARY KEY," +
                    CountEntries.COLUMN_LANE_NUMBER + " INTEGER," +
                    CountEntries.COLUMN_TIMESTAMP + " TEXT UNIQUE,"+
            CountEntries.COLUMN_DIRECTION + " TEXT)";

    private static final String SQL_DELETE_LANE_TABLE =
            "DROP TABLE IF EXISTS " + LaneEntries.TABLE_NAME;

    private static final String SQL_DELETE_COUNT_TABLE =
            "DROP TABLE IF EXISTS " + CountEntries.TABLE_NAME;

    public DbHelper(Context context) {
        //java calls the base class the 'super' class
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LANE_TABLE);
        db.execSQL(SQL_CREATE_COUNT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db , int i, int i1) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_LANE_TABLE);
        db.execSQL(SQL_DELETE_COUNT_TABLE);
        onCreate(db);
    }
}
