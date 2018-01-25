package com.prowess.datacollectiontool.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prowess on 07/07/2017.
 */
// this class is used to create lane objects

public class Lane {
    private int number;
    private String name;
    private LaneDirection direction;
    private boolean isSelected; //checker to keep track of a lane selected status
    private List<VehicleCount> vehicleCounts;
    private int percentage;


    public Lane(int number, String name, LaneDirection direction) {
        this.number = number;
        this.name = name;
        this.direction = direction;
        vehicleCounts = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public LaneDirection getDirection() {
        return direction;
    }

    public List<VehicleCount> getVehicleCounts() {
        return vehicleCounts;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public boolean isReversible() {
        return number == 14 || number == 15;
        //lanes 14 and 15 are the reversible lanes at LCC
    }

    public void swapDirection(){
        //method is to change directions of lanes
        switch (direction){

            case Eastbound:
                direction = LaneDirection.Westbound;
                break;
            case Westbound:
                direction = LaneDirection.Eastbound;
                break;
        }
    }

    public void toggleSelected() {
        // check indicates if the checkbox in the UI has been clicked
        // this method is to change the value of the isSelected checker to notify the app when a lane
        // is selected
        isSelected = !isSelected;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public int getPercentage() {
        return percentage;
    }
}
