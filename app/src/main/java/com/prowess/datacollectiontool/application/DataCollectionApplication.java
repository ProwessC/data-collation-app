package com.prowess.datacollectiontool.application;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by Prowess on 10/07/2017.
 */

public class DataCollectionApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
