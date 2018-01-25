package com.prowess.datacollectiontool.services;

import android.content.Context;

import com.prowess.datacollectiontool.data.DataManager;
import com.prowess.datacollectiontool.models.Lane;
import com.prowess.datacollectiontool.models.LaneDirection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prowess on 08/07/2017.
 */

public class LaneControllerService {
    //one list of lanes controlled by this service; we can create methods to obtain eastbound
    // and westbound lanes
    private List<Lane> lanes;
    private DataManager dataManager;
    private List<Lane> selectedLanes;

    public LaneControllerService(Context context) {
        dataManager = new DataManager(context);
        lanes = dataManager.loadLaneData();
        selectedLanes = new ArrayList<>();
        for (Lane lane : lanes) {
            if (lane.isSelected()) {
                selectedLanes.add(lane);
            }
        }
    }

    //to create all the lanes in existence at admiralty circle toll plaza
    public void initializeLanes() {
        for (int laneNumber = 0; laneNumber < 30; laneNumber++) {
            // due to structural problems skip, lanes 3,4,5 (east) and lanes 24, 25, 26 (west)
            if ((laneNumber > 2 && laneNumber < 6) || (laneNumber > 23 && laneNumber < 27)) {
                continue;
            }

            // lane direction-> east bound or west bound?
            LaneDirection laneDirection = laneNumber < 15 ? LaneDirection.Eastbound : LaneDirection.Westbound;
            //To get the lane names/toll collection methods
            String laneName;
            switch (laneNumber) {
                case 0:
                case 29:{
                    laneName = "MERGING AREA";
                    break;
                }
                case 1:
                case 28: {
                    laneName = "EXPRESS";
                    break;
                }
                case 2:
                case 27: {
                    laneName = "MOTORCYCLE";
                    break;
                }
                case 6:
                case 7:
                case 22:
                case 23: {
                    laneName = "E-TAG";
                    break;
                }
                case 13:
                case 14:
                case 15:
                case 16: {
                    laneName = "EXACT";
                    break;
                }
                default: {
                    laneName = "CASH";
                    break;
                }
            }

            //create the lanes
            Lane lane = new Lane(laneNumber, laneName, laneDirection);
            lanes.add(lane);
            //add lanes to database
            dataManager.addLane(lane);

        }
    }

    // getting all the lanes of a specific direction
    public List<Lane> getDirectionLanes(final LaneDirection laneDirection) {
        List<Lane> results = new ArrayList<>();
        for (Lane lane : lanes) {
            if (lane.getDirection() != laneDirection) {
                continue;
            }
            results.add(lane);
        }
        return results;
    }

    // to enable user search by lane name from a list of lanes in a particular direction:
    public static List<Lane> findLanes(final List<Lane> inputLanes, String search) {
        List<Lane> results = new ArrayList<>();
        for (Lane lane : inputLanes) {
            if (lane.getName().toLowerCase().contains(search.toLowerCase()) || Integer.toString(lane.getNumber()).contains(search)) {
                results.add(lane);
            }
        }
        return results;
    }

    //to save lanes data
    public void saveData() {
        for (Lane lane : lanes) {
            dataManager.saveLaneData(lane);
        }
    }

    //to export the data to google sheets
    public void exportData() {
        //this method calls the upload service from the service factory
        ServiceFactory.getInstance().getDataUploadService().upload(lanes);
    }

    // lane statistics method
    private int getLaneStatistics(Lane lane) {
        int currentLaneCount = lane.getVehicleCounts().size();

        double total = 0;
        for (Lane l: lanes) {
            total += l.getVehicleCounts().size();
        }
        if (total == 0) {
            return 0;
        }
        return (int) Math.round((currentLaneCount/total) * 100);
    }

    public List<Lane> getSelectedLanes() {
        return selectedLanes;
    }

    public void calculateLaneStatistics() {
        for (Lane lane
                : lanes) {
            lane.setPercentage(getLaneStatistics(lane));
        }
    }
}
