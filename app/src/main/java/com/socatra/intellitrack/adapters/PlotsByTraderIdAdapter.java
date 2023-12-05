package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.intellitrack.R;
import com.socatra.intellitrack.activity.main_dash_board.PlotsListActivity;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetPlotDetailsBasedOnTraderId;
import com.socatra.intellitrack.models.get.GetPlotsByDealerId;

import java.util.ArrayList;
import java.util.List;

public class PlotsByTraderIdAdapter extends RecyclerView.Adapter<PlotsByTraderIdAdapter.ViewHolder> implements Filterable {


    private List<GetPlotDetailsBasedOnTraderId>  plotList;

    List<GetPlotDetailsBasedOnTraderId> rawDataTableListFiltered;

    private OnItemClickListener onItemClickListener;

    private AppHelper appHelper;

    String deviceRoleName;

    public PlotsByTraderIdAdapter(AppHelper appHelper,List<GetPlotDetailsBasedOnTraderId> plotsList) {

        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        this.appHelper = appHelper;
        this.plotList = plotsList;
        this.rawDataTableListFiltered = plotsList;

        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plots_main_individual, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetPlotDetailsBasedOnTraderId item = rawDataTableListFiltered.get(position);
        holder.txtFarmerCode.setText(item.getFarmerCode());
        holder.txtNoofplots.setText(String.valueOf(item.getTotalPlots()));

        if (deviceRoleName.equalsIgnoreCase("Trader")) {

            holder.llFarmerName.setVisibility(View.GONE);


        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FarmerCode = String.valueOf(item.getFarmerCode());



                Intent intent = new Intent(view.getContext(), PlotsListActivity.class);
                intent.putExtra("FarmerCode", FarmerCode);


                Log.d("FarmerCode","Code: " +FarmerCode);

                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    rawDataTableListFiltered = plotList;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = plotList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = plotList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetPlotDetailsBasedOnTraderId> filteredList = new ArrayList<>();
                    for (int i = plotList.size(); i >= 1; i--) {
                        filteredList.add(plotList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetPlotDetailsBasedOnTraderId> filteredList = new ArrayList<>();
                    for (GetPlotDetailsBasedOnTraderId row : plotList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFarmerCode().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    rawDataTableListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = rawDataTableListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                rawDataTableListFiltered = (List<GetPlotDetailsBasedOnTraderId>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        if (rawDataTableListFiltered != null) {
            return rawDataTableListFiltered.size();
        } else {
            return 0;
        }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFarmerCode;
        public TextView txtFarmerName;
        public TextView txtNoofplots;

        public LinearLayout llFarmerName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFarmerCode = itemView.findViewById(R.id.txtfarmercode);
            txtNoofplots = itemView.findViewById(R.id.txtNoofplots);
            llFarmerName = itemView.findViewById(R.id.ll_farmername);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(GetPlotsByDealerId farmer);
    }
}
