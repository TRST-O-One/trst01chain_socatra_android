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

import com.socatra.intellitrack.activity.Traderflow.DealerDetailsinTrader;
import com.socatra.intellitrack.activity.Traderflow.TraderProcessorFarmerList;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetDealerDetailsbyProcessorId;
import com.socatra.intellitrack.models.get.GetProcessorDetailsByTraderId;
import com.socatra.intellitrack.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public class

TraderProcessorListAdapter extends RecyclerView.Adapter<TraderProcessorListAdapter.ViewHolder> implements Filterable {

    private List<GetProcessorDetailsByTraderId> processorList;

    AppHelper appHelper;
    AppViewModel viewModel;


    List<GetProcessorDetailsByTraderId> rawDataTableListFiltered;

    private OnItemClickListener onItemClickListener;

    public TraderProcessorListAdapter( List<GetProcessorDetailsByTraderId> processorList  ) {

        this.processorList = processorList;
        this.rawDataTableListFiltered = processorList;
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
                .inflate(R.layout.trader_processor_individual, parent, false);
        return new TraderProcessorListAdapter.ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull TraderProcessorListAdapter.ViewHolder holder, int position) {

        GetProcessorDetailsByTraderId item = rawDataTableListFiltered.get(position);
        holder.txtProcessorName.setText(item.getProcessorName());
        holder.txtFarmercount.setText(String.valueOf(item.getDealerCount()));
        holder.txtDealercount.setText(String.valueOf(item.getDealerCount()));

        holder.txtDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ProcessorId = String.valueOf(item.getProcessorId());

                Intent intent = new Intent(v.getContext(), DealerDetailsinTrader.class);

                intent.putExtra("ProcessorId", ProcessorId);
                Log.d("ProcessorId", "Id: " + ProcessorId);

                v.getContext().startActivity(intent);



            }
        });

        holder.txtFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ProcessorId = String.valueOf(item.getProcessorId());

                Intent intent = new Intent(v.getContext(), TraderProcessorFarmerList.class);

                intent.putExtra("ProcessorId", ProcessorId);
                Log.d("ProcessorId", "Id: " + ProcessorId);

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
                    rawDataTableListFiltered = processorList;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = processorList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = processorList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetProcessorDetailsByTraderId> filteredList = new ArrayList<>();
                    for (int i = processorList.size(); i >= 1; i--) {
                        filteredList.add(processorList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetProcessorDetailsByTraderId> filteredList = new ArrayList<>();

                    for (GetProcessorDetailsByTraderId row : processorList) {
                        String searchQuery = charString.toLowerCase();

                        String SubDealerName = row.getProcessorName();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (SubDealerName.toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "SubDealerName: " + row.getProcessorName());

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
                rawDataTableListFiltered = (List<GetProcessorDetailsByTraderId>) filterResults.values;
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
        public TextView txtProcessorName;

        public TextView txtDealercount;

        public TextView txtFarmercount;

        public TextView txtDealer;

        public TextView txtFarmer;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProcessorName = itemView.findViewById(R.id.txtProcessorName);
            txtDealercount = itemView.findViewById(R.id.txtDealercount);
            txtFarmercount = itemView.findViewById(R.id.txtfarmercount);
            txtDealer = itemView.findViewById(R.id.txt_dealer);
            txtFarmer = itemView.findViewById(R.id.txt_farmer);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(GetDealerDetailsbyProcessorId Dealer);
    }




}

