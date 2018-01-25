package com.prowess.datacollectiontool.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.prowess.datacollectiontool.fragments.DataCollectionFragment;
import com.prowess.datacollectiontool.fragments.LaneSelectionFragment;

/**
 * Created by Prowess on 08/07/2017.
 */

public class DashboardPagerAdapter extends FragmentStatePagerAdapter {
    // pager adapter gets the fragment and gives it to whoever calls for it
    // helps know the appropriate views to show
    private LaneSelectionFragment laneSelectionFragment;
    private DataCollectionFragment dataCollectionFragment;

    public DashboardPagerAdapter(FragmentManager fm) {
        super(fm);
        laneSelectionFragment = new LaneSelectionFragment();
        dataCollectionFragment = new DataCollectionFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                return laneSelectionFragment;
            }
            case 1: {
                return dataCollectionFragment;
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
