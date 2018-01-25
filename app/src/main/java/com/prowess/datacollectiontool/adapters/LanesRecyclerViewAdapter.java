package com.prowess.datacollectiontool.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prowess.datacollectiontool.R;
import com.prowess.datacollectiontool.models.Lane;
import com.prowess.datacollectiontool.services.LaneControllerService;
import com.prowess.datacollectiontool.services.ServiceFactory;

import java.util.List;

/**
 * Created by Prowess on 08/07/2017.
 */

public class LanesRecyclerViewAdapter extends RecyclerView.Adapter<LanesRecyclerViewAdapter.ViewHolder> {

    private List<Lane> laneData;

    public LanesRecyclerViewAdapter(List<Lane> lanes) {
        laneData = lanes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_select_lanes, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //this is done because services are called inside a singleton class (Service Factory)
        // so there can only be one instance of our service and we want to get it
        LaneControllerService laneControllerService = ServiceFactory.getInstance().getLaneControllerService();
        final Lane lane = laneData.get(position);

        holder.laneNameTextView.setText(lane.getName());
        holder.laneNumberTextView.setText(Integer.toString(lane.getNumber()));
        //if its a reversible lane show the reversible icon:
        holder.laneReversibleIndicatorIcon.setVisibility(lane.isReversible() ? View.VISIBLE : View.GONE);
        //get lane statistic method in LaneControllerService class
        // and use to calculate the percentage number of counts carried out so far on each lane
        holder.lanePercentTextView.setText(Integer.toString(lane.getPercentage()) + "%");
        holder.laneSelectedCheckBox.setChecked(lane.isSelected());
    }

    @Override
    public int getItemCount() {
        return laneData.size();
    }

    public void setLaneData(List<Lane> laneData) {
        //tells the adapter that the data set has changed
        //it sets the new data (which is the new list that is passed into the argument of this method)
        this.laneData = laneData;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox laneSelectedCheckBox;
        TextView laneNumberTextView;
        TextView laneNameTextView;
        TextView lanePercentTextView;
        ImageView laneReversibleIndicatorIcon;  //to indicate the lanes that are reversible (14, 15)

        ViewHolder(View itemView) {
            super(itemView);
            laneSelectedCheckBox = (CheckBox) itemView.findViewById(R.id.lane_selected_checkbox);
            laneNumberTextView = (TextView) itemView.findViewById(R.id.lane_number_textview);
            laneNameTextView = (TextView) itemView.findViewById(R.id.lane_name_textview);
            lanePercentTextView = (TextView) itemView.findViewById(R.id.lane_count_percent_textview);
            laneReversibleIndicatorIcon = (ImageView) itemView.findViewById(R.id.lane_reversible_indicator_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (toggleCurrentLaneState()) {
                        laneSelectedCheckBox.toggle();
                    }
                }
            });

            laneSelectedCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!toggleCurrentLaneState()){
                        laneSelectedCheckBox.setChecked(false);
                    }
                }
            });

            laneReversibleIndicatorIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //what happens when you click the reversible icon
                    Lane lane = laneData.get(getAdapterPosition());
                    // get the adapter position of the lane clicked
                    lane.swapDirection(); //swap the direction of that lane
                    laneData.remove(lane); //remove the lane from the data set of the current direction
                    notifyDataSetChanged();

                }
            });
        }

        private boolean toggleCurrentLaneState() {
            if (ServiceFactory.getInstance().getLaneControllerService().getSelectedLanes().size() >= 6 && !laneSelectedCheckBox.isChecked()) {
                Toast.makeText(laneNameTextView.getContext(), "Maximum number of lanes have been selected.", Toast.LENGTH_LONG).show();
                return false;
            }
            Lane lane = laneData.get(getAdapterPosition());
            lane.toggleSelected();
            checkLaneState(lane);
            return true;
        }

        private void checkLaneState(Lane lane) {
            LaneControllerService laneControllerService = ServiceFactory.getInstance().getLaneControllerService();
            if (lane.isSelected()) {
                laneControllerService.getSelectedLanes().add(lane);
            } else {
                if (laneControllerService.getSelectedLanes().contains(lane)) {
                    laneControllerService.getSelectedLanes().remove(lane);
                }
            }
        }
    }
}
