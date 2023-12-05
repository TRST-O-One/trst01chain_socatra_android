package com.socatra.intellitrack.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.intellitrack.R;

import com.socatra.intellitrack.activity.Traderflow.FarmerDetailsbyDealerinTrader;
import com.socatra.intellitrack.activity.Traderflow.SubDealerDetailsinTrader;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetDealerDetailsbyProcessorId;
import com.socatra.intellitrack.models.get.GetDealersDetailsByTraderId;
import com.socatra.intellitrack.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public class TraderDealerListAdapter extends RecyclerView.Adapter<TraderDealerListAdapter.ViewHolder> implements Filterable {

    private List<GetDealersDetailsByTraderId> DealerList;
    AppHelper appHelper;
    AppViewModel viewModel;


    List<GetDealersDetailsByTraderId> rawDataTableListFiltered;

    private OnItemClickListener onItemClickListener;

    public TraderDealerListAdapter( List<GetDealersDetailsByTraderId> DealerList  ) {

        this.DealerList = DealerList;
        this.rawDataTableListFiltered = DealerList;
        this.appHelper = appHelper;
        this.viewModel = viewModel;
        this.onItemClickListener = onItemClickListener;


    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trader_dealer_individual, parent, false);
        return new TraderDealerListAdapter.ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull TraderDealerListAdapter.ViewHolder holder, int position) {

        GetDealersDetailsByTraderId item = rawDataTableListFiltered.get(position);
        holder.txtDealerName.setText(item.getDealerName());
        holder.txtFarmercount.setText(String.valueOf(item.getFarmerCount()));
        holder.txtSubDealercount.setText(String.valueOf(item.getSubDealerCount()));

        holder.txtSubDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String DealerId = String.valueOf(item.getDealerId());

                Intent intent = new Intent(v.getContext(), SubDealerDetailsinTrader.class);

                intent.putExtra("DealerId", DealerId);
                Log.d("DealerId", "Id: " + DealerId);

                v.getContext().startActivity(intent);



            }
        });

        holder.txtFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String DealerId = String.valueOf(item.getDealerId());

                Intent intent = new Intent(v.getContext(), FarmerDetailsbyDealerinTrader.class);

                intent.putExtra("DealerId", DealerId);
                Log.d("DealerId", "Id: " + DealerId);

                v.getContext().startActivity(intent);



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
                    rawDataTableListFiltered = DealerList;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = DealerList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = DealerList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetDealersDetailsByTraderId> filteredList = new ArrayList<>();
                    for (int i = DealerList.size(); i >= 1; i--) {
                        filteredList.add(DealerList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetDealersDetailsByTraderId> filteredList = new ArrayList<>();

                    for (GetDealersDetailsByTraderId row : DealerList) {
                        String searchQuery = charString.toLowerCase();

                        String SubDealerName = row.getDealerName();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (SubDealerName.toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "SubDealerName: " + row.getDealerName());

                            filteredList.add(row);
                        }

                        else {

                            System.out.println("GRNnumber: " + SubDealerName + ", SearchQuery: " + searchQuery);
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
                rawDataTableListFiltered = (List<GetDealersDetailsByTraderId>) filterResults.values;
                notifyDataSetChanged(); // Ensure you call this
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
        public TextView txtDealerName;

        public TextView txtSubDealercount;

        public TextView txtFarmercount;

        public TextView txtSubDealer;

        public TextView txtFarmer;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDealerName = itemView.findViewById(R.id.txtDealerName);
            txtSubDealercount = itemView.findViewById(R.id.txtsubDealercount);
            txtFarmercount = itemView.findViewById(R.id.txtfarmercount);
            txtSubDealer = itemView.findViewById(R.id.txt_sub_dealer);
            txtFarmer = itemView.findViewById(R.id.txt_farmer);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(GetDealerDetailsbyProcessorId Dealer);
    }




}


