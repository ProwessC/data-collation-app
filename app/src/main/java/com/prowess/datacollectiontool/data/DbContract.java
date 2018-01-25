package com.prowess.datacollectiontool.data;

import android.provider.BaseColumns;

/**
 * Created by Prowess on 07/07/2017.
 */

// this is the database schema to define how the database is structured
final class DbContract {
    private static DbContract ourInstance = new DbContract();

    public static DbContract getInstance() {
        return ourInstance;
    }

    private DbContract() {
    }

    public static class LaneEntries implements BaseColumns {
        public static final String TABLE_NAME = "lane_table";
        public static final String COLUMN_LANE_NAME = "name";
        public static final String COLUMN_LANE_NUMBER = "lane_number";
        public static final String COLUMN_DIRECTION = "lane_direction";
        public static final int INDEX_LANE_NAME = 1;
        public static final int INDEX_LANE_NUMBER = 2;
        public static final int INDEX_DIRECTION = 3;
    }

    public static class CountEntries implements BaseColumns{
        public static final String TABLE_NAME = "counts_table";
        public static final String COLUMN_LANE_NUMBER = "count_lane_number";
        public static final String COLUMN_TIMESTAMP = "count_timestamp";
        public static final String COLUMN_DIRECTION = "count_direction";
        public static final int INDEX_LANE_NUMBER = 1;
        public static final int INDEX_TIMESTAMP = 2;
        public static final int INDEX_DIRECTION = 3;
        // index 0 has already been taking by the id column in the database tables
    }
}
