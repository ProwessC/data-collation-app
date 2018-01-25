package com.prowess.datacollectiontool.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.prowess.datacollectiontool.models.Lane;
import com.prowess.datacollectiontool.models.LaneDirection;
import com.prowess.datacollectiontool.models.VehicleCount;

import java.util.ArrayList;
import java.util.List;

import static com.prowess.datacollectiontool.data.DbContract.CountEntries;
import static com.prowess.datacollectiontool.data.DbContract.LaneEntries;

/**
 * Created by Prowess on 07/07/2017.
 */

public class DataManager {
    //this class is used for CRUD operations i.e. Create, Read, Update, Delete
    private DbHelper dbHelper;

    public DataManager(Context context) {
        this.dbHelper = new DbHelper(context);
    }

    //region CREATE
    public void addLane(Lane lane) {
        //this method is only called once in the whole application life
        //because we only add the lanes once when the app runs for the first time

        SQLiteDatabase db = dbHelper.getWritableDatabase();// Gets the data repository in write mode

        // to add content to the lanes database:
        ContentValues values = new ContentValues();
        values.put(LaneEntries.COLUMN_LANE_NAME, lane.getName());
        values.put(LaneEntries.COLUMN_LANE_NUMBER, lane.getNumber());
        values.put(LaneEntries.COLUMN_DIRECTION, lane.getDirection().toString());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(LaneEntries.TABLE_NAME, null, values);
    }

    public void saveLaneData(Lane lane) {
        //after taking data for some minutes and phone screen goes of, it saves the data
        //i.e every time the app calls onStop, the data is saved

        // recall that in the whole application,
        // every date object represents a count

        for (VehicleCount count : lane.getVehicleCounts()) {
            addCount(lane.getNumber(), count);
        } //for each vehicle count in a lane, add the vehicle count to the count table
    }

    private void addCount(int laneNumber, VehicleCount vehicleCount) {
        // This method adds a time stamp to the count entries database
        // to add date and time information at which we take the  vehicle counts

        // Gets the data repository in write mode so we can write values into the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // to add content to the counts database:
        ContentValues values = new ContentValues();
        values.put(CountEntries.COLUMN_LANE_NUMBER, laneNumber);
        //we want to add milliseconds time (time stamp) for every time we click to add a vehicle
        values.put(CountEntries.COLUMN_TIMESTAMP, vehicleCount.getCountTime());
        //to ensure that we save the direction (east or west) the counts are taken in because
        //of the reversible lanes due to traffic
        values.put(CountEntries.COLUMN_DIRECTION,vehicleCount.getDirection().toString());

        //TODO: Skip counts data that is already in datatbase to avoid attempting to input an already existing value
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(CountEntries.TABLE_NAME, null, values);


    }
    //endregion

    // region READ
    // to load lane data from storage  to memory for use;
    public List<Lane> loadLaneData() {
        //use this method when we want to get all the data we have probably to export
        //to read the lane table and put the items in a list of lane objects
        List<Lane> laneList = new ArrayList<>();
        String query = "SELECT * FROM " + LaneEntries.TABLE_NAME;
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // gets database in readable form
        Cursor cursor = db.rawQuery(query, null); //cursors are objects that get data from database
        while (cursor.moveToNext()) {
            String laneName = cursor.getString(LaneEntries.INDEX_LANE_NAME);
            int laneNumber = cursor.getInt(LaneEntries.INDEX_LANE_NUMBER);
            LaneDirection laneDirection = LaneDirection.valueOf(cursor.getString(LaneEntries.INDEX_DIRECTION));

            Lane lane = new Lane(laneNumber, laneName, laneDirection);
            loadVehicleCount(lane);
            laneList.add(lane);

        }
        cursor.close();
        return laneList;

    }

    // TODO hope all this works so we don't have error of trying to read from an already closed cursor
    //to read a vehicle count
    private void loadVehicleCount(Lane lane) {
        String query = "SELECT * FROM " + CountEntries.TABLE_NAME + " WHERE " + CountEntries.COLUMN_LANE_NUMBER + " = " + lane.getNumber();
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // gets database in readable form
        Cursor cursor = db.rawQuery(query, null); //cursors are objects that get data from database
        while (cursor.moveToNext()) {
            //read the time (milliseconds) from the data base
            long timestamp = cursor.getLong(CountEntries.INDEX_TIMESTAMP);
            VehicleCount vehicleCount = new VehicleCount(timestamp, LaneDirection.valueOf(cursor.getString(CountEntries.INDEX_DIRECTION)));
            // this is to easily obtain the time of a vehicle count and add the vehicle count
            //to the list of vehicle counts for each lane
            lane.getVehicleCounts().add(vehicleCount);
        }
        cursor.close();
    }
    //endregion

    //region DELETE
    public void deleteCountData(int laneNumber) {
        //to delete vehicle counts we no longer need for lanes
        // to save phone storage space
        String query = "DELETE * FROM " + CountEntries.TABLE_NAME + " WHERE " + CountEntries.COLUMN_LANE_NUMBER + " = " + laneNumber;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(query);

    }
    //endregion
}
