package com.prowess.datacollectiontool.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.prowess.datacollectiontool.R;
import com.prowess.datacollectiontool.adapters.DashboardPagerAdapter;
import com.prowess.datacollectiontool.fragments.LaneSelectionFragment;
import com.prowess.datacollectiontool.models.LaneDirection;
import com.prowess.datacollectiontool.services.ServiceFactory;


public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LaneSelectionFragment.OnLaneDirectionChangedListener {

    private LaneDirection selectedDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // this method is called when the activity is created
        super.onCreate(savedInstanceState);
        initializeActivity();

        checkDatabaseInitialized();

        initializeTabs();
    }

    private void initializeTabs() {
        // time to add everything concerning the tab layout to the app
        //making the tab layout in the content dashboard layout xml file into a java object:
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        TabLayout.Tab selectPanelTab = tabLayout.newTab().setText(R.string.select_lanes);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.select_lanes));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.collect_data));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //making view pager in xml layout file into a java object:
        // view pager is a container that lets us swipe between views
        final ViewPager viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
        // pager adapter supplies the views to view pager (adapters supply things)
        final DashboardPagerAdapter adapter = new DashboardPagerAdapter
                (getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //use the tab's position to set what displays in the action bar,
                // we may need to get the currently selected direction

                switch (tab.getPosition()) {
                    case 0: {
                        setTitle(R.string.dashboard);
                        adapter.getItem(0).onResume();
                        break;
                    }
                    case 1: {
                        setTitle(selectedDirection != null ? selectedDirection.toString() : getResources().getString(R.string.dashboard));
                        // every time we enter the collect data tab page call onResume
                        // to enable us display the grid views for counting vehicles for the lanes
                        adapter.getItem(1).onResume();
                        break;
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void checkDatabaseInitialized() {
        //We initialize the lane controller service here.
        ServiceFactory.getInstance().startLaneControllerService(getApplicationContext());


        //Check if lanes have been added to the database already, if not add them.
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.app_shared_preferences), MODE_PRIVATE);
        if (preferences.getAll().isEmpty()) {
            ServiceFactory.getInstance().getLaneControllerService().initializeLanes();
            preferences.edit().putBoolean(getResources().getString(R.string.app_shared_preferences), true).apply();
        }
    }

    private void initializeActivity() {
        setContentView(R.layout.activity_dash_board);
        setTitle(R.string.dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_export_data:{
                Toast.makeText(getApplicationContext(), "Exporting data...", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //We need a way to receive the direction from our lane selection fragment and set it in this activity
    @Override
    public void onLaneDirectionChanged(LaneDirection newDirection) {
        //this method is actually created in the onLaneDirectionChangedListener (lane selection fragment class)
        // interface but implemented here hence "override"
        //when the lane direction changes, the selected direction becomes the new one
        selectedDirection = newDirection;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //when ever the application is stopped, data is saved.
        ServiceFactory.getInstance().getLaneControllerService().saveData();
    }
}
