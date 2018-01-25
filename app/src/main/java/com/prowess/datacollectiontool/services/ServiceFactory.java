package com.prowess.datacollectiontool.services;

import android.content.Context;

/**
 * Created by Prowess on 07/07/2017.
 */
public class ServiceFactory {
    // this class holds all our services - to perform operations in the application
    // it is advisable that all services have one instance
    private static ServiceFactory ourInstance = new ServiceFactory();

    public static ServiceFactory getInstance() {
        return ourInstance;
    }

    private DataUploadService dataUploadService;
    private LaneControllerService laneControllerService;

    private ServiceFactory() {
        dataUploadService = new MockDataUploadService();
    }

    public DataUploadService getDataUploadService() {
        return dataUploadService;
    }

    public void startLaneControllerService(Context context) {
        if (laneControllerService != null) {
            return;
        }
        laneControllerService = new LaneControllerService(context);
    }

    public LaneControllerService getLaneControllerService() {
        return laneControllerService;
    }
}
