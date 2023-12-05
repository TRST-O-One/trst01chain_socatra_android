package com.socatra.intellitrack.adapters;

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
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetCustomerInvoicesByTraderId;
import com.socatra.intellitrack.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerListbyTraderAdapter extends RecyclerView.Adapter<CustomerListbyTraderAdapter.ViewHolder> implements Filterable {

    private List<GetCustomerInvoicesByTraderId> CustomerList;
    AppHelper appHelper;
    AppViewModel viewModel;


    List<GetCustomerInvoicesByTraderId> rawDataTableListFiltered;

    private OnItemClickListener onItemClickListener;

    public CustomerListbyTraderAdapter(List<GetCustomerInvoicesByTraderId> Dealerslist  ) {

        this.CustomerList = Dealerslist;
        this.rawDataTableListFiltered = Dealerslist;
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
                .inflate(R.layout.customer_list_by_trader_individual, parent, false);
        return new CustomerListbyTraderAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CustomerListbyTraderAdapter.ViewHolder holder, int position) {

        GetCustomerInvoicesByTraderId item = rawDataTableListFiltered.get(position);


        holder.txtCustomerName.setText(item.getCustomer());
        holder.txtMonth.setText(String.valueOf(item.getMonth()));
        holder.txtMonthlyVolume.setText(String.valueOf(item.getMonthlyVolume()));
        holder.txtTotalInvoices.setText(String.valueOf(item.getTotalInvoices()));

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    rawDataTableListFiltered = CustomerList;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = CustomerList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = CustomerList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetCustomerInvoicesByTraderId> filteredList = new ArrayList<>();
                    for (int i = CustomerList.size(); i >= 1; i--) {
                        filteredList.add(CustomerList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetCustomerInvoicesByTraderId> filteredList = new ArrayList<>();

                    for (GetCustomerInvoicesByTraderId row : CustomerList) {
                        String searchQuery = charString.toLowerCase();

                        String CustomerName = row.getCustomer();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (CustomerName.toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "GRNnumber: " + row.getCustomer());

                            filteredList.add(row);
                        }

                        else {

                            System.out.println("CustomerName: " + CustomerName + ", SearchQuery: " + searchQuery);
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
                rawDataTableListFiltered = (List<GetCustomerInvoicesByTraderId>) filterResults.values;
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

        public TextView txtCustomerName;

        public TextView txtMonth;

        public TextView txtMonthlyVolume;

        public TextView txtTotalInvoices;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCustomerName = itemView.findViewById(R.id.txtCustomer);
            txtMonth = itemView.findViewById(R.id.txtMonth);
            txtMonthlyVolume = itemView.findViewById(R.id.txtMonthlyVolume);
            txtTotalInvoices = itemView.findViewById(R.id.txtTotalInvoices);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(GetCustomerInvoicesByTraderId Dealer);
    }




}
