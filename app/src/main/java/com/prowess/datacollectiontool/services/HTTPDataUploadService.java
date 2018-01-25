package com.prowess.datacollectiontool.services;

import com.prowess.datacollectiontool.models.Lane;

import java.util.List;

/**
 * Created by Prowess on 07/07/2017.
 */
//note: we extend a class, we implement an interface
public class HTTPDataUploadService implements DataUploadService{
    @Override
    public void upload(List<Lane> lanes) {
        //to write all the code that will help upload data to google sheets
    }
}
