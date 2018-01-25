package com.prowess.datacollectiontool.services;

import com.prowess.datacollectiontool.models.Lane;

import java.util.List;

/**
 * Created by Prowess on 07/07/2017.
 */

public interface DataUploadService {
    // in interface we don't specify the access modifier
    void upload(List<Lane> lanes); //we only declare methods, we don't implement them
}
