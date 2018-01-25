package com.prowess.datacollectiontool.models;

import java.util.Date;

/**
 * Created by Prowess on 07/07/2017.
 */

public class VehicleCount {
    private Date countTime;  //the time at which a vehicle is counted
    private LaneDirection direction;

    public VehicleCount(Date countTime, LaneDirection direction) {
        this.countTime = countTime;
        this.direction = direction;
    }

    public VehicleCount(long milliseconds, LaneDirection direction) {
        // this overloaded constructor creates a date object with the milliseconds
        //to be able to easily read our timestamp values in database values
        countTime = new Date(milliseconds);
        this.direction = direction;
    }

    public long getCountTime() {
        //to get the time stamp (milliseconds) for that point in time when a vehicle is counted
        return countTime.getTime();
    }

    public LaneDirection getDirection() {
        return direction;
    }
}
