package com.prowess.datacollectiontool.services;

import com.prowess.datacollectiontool.models.Lane;

import java.util.List;

/**
 * Created by Prowess on 07/07/2017.
 */
// this is a mock class for update service
//for test purposes without needing the internet
public class MockDataUploadService implements DataUploadService {
    @Override
    public void upload(List<Lane> lanes) {

    }
}
