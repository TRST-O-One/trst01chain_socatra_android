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
import com.socatra.intellitrack.activity.customerflow.ViewFarmerRiskAssesmentSurveyDataActivity;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetDealerDetailsbyProcessorId;
import com.socatra.intellitrack.models.get.GetFarmerDetailsByInvoiceId;
import com.socatra.intellitrack.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public class FarmerInvoiceAdapter extends RecyclerView.Adapter<FarmerInvoiceAdapter.ViewHolder> implements Filterable {

    private List<GetFarmerDetailsByInvoiceId> FarmerInvoiceList;

    AppHelper appHelper;
    AppViewModel viewModel;


    List<GetFarmerDetailsByInvoiceId> rawDataTableListFiltered;

    private OnItemClickListener onItemClickListener;

    public FarmerInvoiceAdapter( List<GetFarmerDetailsByInvoiceId> FarmerInvoiceList  ) {

        this.FarmerInvoiceList = FarmerInvoiceList;
        this.rawDataTableListFiltered = FarmerInvoiceList;
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
                .inflate(R.layout.farmer_invoice_individual, parent, false);
        return new FarmerInvoiceAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FarmerInvoiceAdapter.ViewHolder holder, int position) {

        GetFarmerDetailsByInvoiceId item = rawDataTableListFiltered.get(position);
        holder.txtFarmerName.setText(item.getFarmerName());
        holder.txtFarmerCode.setText(String.valueOf(item.getFarmerCode()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FarmerCode = String.valueOf(item.getFarmerCode());
                Intent intent = new Intent(view.getContext(), ViewFarmerRiskAssesmentSurveyDataActivity.class);
                intent.putExtra("FarmerCode", FarmerCode);
                Log.d("FarmerCode","FarmerCode : " +FarmerCode);
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
                    rawDataTableListFiltered = FarmerInvoiceList;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = FarmerInvoiceList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = FarmerInvoiceList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetFarmerDetailsByInvoiceId> filteredList = new ArrayList<>();
                    for (int i = FarmerInvoiceList.size(); i >= 1; i--) {
                        filteredList.add(FarmerInvoiceList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetFarmerDetailsByInvoiceId> filteredList = new ArrayList<>();

                    for (GetFarmerDetailsByInvoiceId row : FarmerInvoiceList) {
                        String searchQuery = charString.toLowerCase();

                        String SubDealerName = row.getFarmerName();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (SubDealerName.toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "SubDealerName: " + row.getFarmerName());

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
                rawDataTableListFiltered = (List<GetFarmerDetailsByInvoiceId>) filterResults.values;
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
        public TextView txtFarmerCode;

        public TextView txtFarmerName;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFarmerCode = itemView.findViewById(R.id.txtInvoiceFarmerCode);
            txtFarmerName = itemView.findViewById(R.id.txtInvoiceFarmerName);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(GetDealerDetailsbyProcessorId Dealer);
    }




}
