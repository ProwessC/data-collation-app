package com.prowess.datacollectiontool.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.prowess.datacollectiontool.R;
import com.prowess.datacollectiontool.activities.DashBoardActivity;
import com.prowess.datacollectiontool.adapters.LanesRecyclerViewAdapter;
import com.prowess.datacollectiontool.models.Lane;
import com.prowess.datacollectiontool.models.LaneDirection;
import com.prowess.datacollectiontool.services.LaneControllerService;
import com.prowess.datacollectiontool.services.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prowess on 08/07/2017.
 */

public class LaneSelectionFragment extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener {
    private LanesRecyclerViewAdapter recyclerViewAdapter;
    private List<Lane> lanes;
    private OnLaneDirectionChangedListener laneDirectionChangedListener;
    //Note: the lane direction changed listener has been set to be the dashboard (main) activity


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true); // We do this to say that this Fragment can create a menu
        // called when the fragment is creating its view
        // inflating: to convert xml to java object so that we can do things in our layout from our java code
        View view = inflater.inflate(R.layout.layout_select_lanes, null); //inflating the layout into a container
        //inflate returns a view

        //Here we get the dropdown and recycler view from the xml to use them as objects in this fragment
        //NOTE: FIND BY ID => converting VIEWS to java objects
        AppCompatSpinner directionsDropdown = (AppCompatSpinner) view.findViewById(R.id.directions_dropdown);
        final RecyclerView lanesRecyclerView = (RecyclerView) view.findViewById(R.id.lanes_recycler_view);

        lanes = new ArrayList<>(); //Initialize the lanes to empty

        //Now we initialize the recycler view's adapter
        recyclerViewAdapter = new LanesRecyclerViewAdapter(lanes);

        //We need a layout manager so that android will know how to arrange the recycler view's items,
        //We want it to be arranged in linear form.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        lanesRecyclerView.setLayoutManager(layoutManager);
        lanesRecyclerView.setAdapter(recyclerViewAdapter);

        directionsDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //get the selected direction from the adapter, use it to filter the lanes from LaneControllerService
                //Also use the obtained lanes to reset the recycler view's adapter,
                //We may need to send the new direction to the dashboard
                LaneDirection selectedDirection = LaneDirection.valueOf(adapterView.getSelectedItem().toString());
                //we pass in the selected direction (obtained from the dropdown selection list in the app)
                //into the argument of the lane direction changed method
                laneDirectionChangedListener.onLaneDirectionChanged(selectedDirection);

                lanes = ServiceFactory.getInstance().getLaneControllerService().getDirectionLanes(selectedDirection);
                recyclerViewAdapter.setLaneData(lanes);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerViewAdapter == null) {
            return;
        }
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        laneDirectionChangedListener = (DashBoardActivity) context;
    }

    // For our search, we'll need to override onCreateOptionsMenu
    // the method is what happens when the fragment/activity is creating the options menu we see
    // on the top right hand corner of the UI/screen
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        //inflating the menu_search xml file in the menu resource folder into the menu java object
        //to be able to use them in our app as we see fit for them to function
        //NOTE: class R is the java class that holds the resources (pardon me, I'm learning!)
        //Menu and Layout both have inflater classes cos they are the resource folders that contain
        // xml files u probably want to make changes that correspond with the functionality of your app
        // inflating makes this possible
        //NOTE: INFLATE XML FILES and FIND VIEWS BY ID => conversion to java objects
        inflater.inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
        //you need to pass in an interface to the setOnQueryTextListener method
        //putting "this" here will make this class implement the required interface
    }

    //these are the methods that listen to the search view
    //comes along with the interface that has been implemented
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // we will implement our search here, as the user is searching, the lanes list will be responding
        recyclerViewAdapter.setLaneData(LaneControllerService.findLanes(lanes, newText));
        return false;
    }

    public interface OnLaneDirectionChangedListener {
        void onLaneDirectionChanged(LaneDirection newDirection);
        //this method is implemented in the Dashboard activity class where the selected direction
        //becomes the new direction
    }
}
