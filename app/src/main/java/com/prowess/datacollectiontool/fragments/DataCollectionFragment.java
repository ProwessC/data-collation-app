package com.prowess.datacollectiontool.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.prowess.datacollectiontool.R;
import com.prowess.datacollectiontool.models.Lane;
import com.prowess.datacollectiontool.models.VehicleCount;
import com.prowess.datacollectiontool.services.ServiceFactory;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Prowess on 08/07/2017.
 */

public class DataCollectionFragment extends android.support.v4.app.Fragment {

    private List<Lane> selectedLanes;
    private GridLayout dataCollectionGridLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_collect_data, null);
        dataCollectionGridLayout = (GridLayout) view.findViewById(R.id.data_collection_grid_layout);
        return view;
    }

    @Override
    public void onResume() {
        //check the layout controller service for its currently selected lanes and display
        super.onResume();
        if (dataCollectionGridLayout == null) {
            //safety measure because we do not know who runs first between onCreateView and onResume
            return;
        }
        selectedLanes = ServiceFactory.getInstance().getLaneControllerService().getSelectedLanes();
        getActivity().findViewById(R.id.no_lanes_selected_textview)
                .setVisibility(selectedLanes.size() != 0 ? View.GONE : View.VISIBLE);

        //a code that works to help put the lanes in the grid designed in layout_collect_data.xml (dataCollectionGridLayout)
        //to display the views (designed in view_collect_data.xml) in the grid
        final int margin_dimension = 10;
        //display metrics gets the width and height of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels - 300;
        int width = displayMetrics.widthPixels;
        dataCollectionGridLayout.removeAllViews();

        int total = selectedLanes.size();
        final int column = 2;
        final int row = (total / column) + (total % column == 0 ? 0 : 1);
        dataCollectionGridLayout.setColumnCount(column);
        dataCollectionGridLayout.setRowCount(row);
        //setPadding(left,top,right,down)
        dataCollectionGridLayout.setPadding(margin_dimension, 0, margin_dimension, 0);
        final int space = margin_dimension * (column + 1);
        //NOTE the beautiful style of this for loop...Damn!!!
        for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
            if (c == column) {
                c = 0;
                r++;
            }
            View viewCollectData = LayoutInflater.from(getContext()).inflate(R.layout.view_collect_data, null);
            TextView laneNumberTextView = (TextView) (viewCollectData.findViewById(R.id.lane_number_textview));
            TextView laneNameTextView = (TextView) (viewCollectData.findViewById(R.id.lane_name_textview));
            final TextView laneCountTextView = (TextView) (viewCollectData.findViewById(R.id.lane_count_textview));

            final Lane lane = selectedLanes.get(i);
            laneNumberTextView.setText("Lane " + lane.getNumber());
            laneNameTextView.setText(lane.getName());
            laneCountTextView.setText(lane.getVehicleCounts().size() + "");

            GridLayout.LayoutParams param = new GridLayout.LayoutParams();

            param.height = height / row;
            if (i + 1 == total && total % column != 0) {
                param.width = (width - (space - margin_dimension));
                param.columnSpec = GridLayout.spec(c, column);
            } else {
                param.width = (width - space) / column;
                param.columnSpec = GridLayout.spec(c);
            }
            param.rightMargin = margin_dimension;
            param.topMargin = margin_dimension;
            param.setGravity(Gravity.CENTER);
            param.rowSpec = GridLayout.spec(r);
            viewCollectData.setLayoutParams(param);
            viewCollectData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: this is where we take vehicle count
                    lane.getVehicleCounts().add(new VehicleCount(Calendar.getInstance().getTime(), lane.getDirection()));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ServiceFactory.getInstance().getLaneControllerService().calculateLaneStatistics();
                        }
                    }).start();
                    laneCountTextView.setText(lane.getVehicleCounts().size() + "");
                }
            });
            dataCollectionGridLayout.addView(viewCollectData);
        }
    }
}
