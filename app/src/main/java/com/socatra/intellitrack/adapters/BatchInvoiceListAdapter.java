package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.DeviceCustomerId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;

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
import com.socatra.intellitrack.activity.customerflow.CustomerBatchFarmerListAct;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetBatchDetailsByInvoiceNumber;
import com.socatra.intellitrack.models.get.GetDealerDetailsbyProcessorId;
import com.socatra.intellitrack.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public class BatchInvoiceListAdapter extends RecyclerView.Adapter<BatchInvoiceListAdapter.ViewHolder> implements Filterable {


    private List<GetBatchDetailsByInvoiceNumber> BatchInvoicelist;


    AppHelper appHelper;
    AppViewModel viewModel;

    String Token, strDealerID, strDealerName, strProcessorId , strCustomerId;


    List<GetBatchDetailsByInvoiceNumber> rawDataTableListFiltered;




    public BatchInvoiceListAdapter(AppHelper appHelper, AppViewModel viewModel, List<GetBatchDetailsByInvoiceNumber> BatchInvoicelist, String strDealerID) {

        this.BatchInvoicelist = BatchInvoicelist;
        this.rawDataTableListFiltered = BatchInvoicelist;
        this.appHelper = appHelper;
        this.viewModel = viewModel;
        this.strDealerID = strDealerID;

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.batch_invoice_individual, parent, false);
        return new BatchInvoiceListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull BatchInvoiceListAdapter.ViewHolder holder, int position) {

        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strCustomerId = appHelper.getSharedPrefObj().getString(DeviceCustomerId,"");

        GetBatchDetailsByInvoiceNumber item = rawDataTableListFiltered.get(position);
//        if (strDealerID.equals("")) {
//            holder.txtUserName.setText(item.getManufacturerName());
//            holder.txtDealerUser.setVisibility(View.GONE);
//            holder.txtProcessorUser.setVisibility(View.VISIBLE);
//
//
//
//        } else {
//            holder.txtUserName.setText(item.getDealerName());
//            holder.txtDealerUser.setVisibility(View.VISIBLE);
//            holder.txtProcessorUser.setVisibility(View.GONE);
//        }

        holder.txtBatchInvoicenumber.setText(item.getBatchNo());


        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

        String grnDate = item.getCreatedDate().split("T")[0]; // Get the date part before "T"
        holder.txtDate.setText(grnDate);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strBatchid = String.valueOf(item.getBatchId());

                Intent intent = new Intent(view.getContext(), CustomerBatchFarmerListAct.class);
                intent.putExtra("BatchId", strBatchid);
                Log.d("BatchId","BatchId"  +strBatchid);
                view.getContext().startActivity(intent);



            }
        });





//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                holder.btnassesment.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        String strInvNumber = String.valueOf(item.getInvoiceNumber());
//
//                        Intent intent = new Intent(view.getContext(), CustomerInvoiceList.class);
//
//                        intent.putExtra("INVNumber", strInvNumber);
//
//
//
//                    }
//                });
//
//
//
//
//
////                String strInvNumber = String.valueOf(item.getInvoiceNumber());
////                String strInvDate = String.valueOf(item.getCreatedDate().split("T")[0]);
////                String strShippingTO = String.valueOf(item.getCustomerName());
////                String strShippingAdd = String.valueOf(item.getShippingAddress());
////                String strProduct = String.valueOf(item.getProduct());
////                String strQty = String.valueOf(item.getQuantity());
////                Intent intent = new Intent(view.getContext(), ShareInvoiceActivity.class);
////
////                intent.putExtra("INVNumber", strInvNumber);
////                intent.putExtra("shippingTO", strShippingTO);
////                intent.putExtra("shippingAdd", strShippingAdd);
////                intent.putExtra("product", strProduct);
////                intent.putExtra("qty", strQty);
////                intent.putExtra("INvDate", strInvDate);
////                intent.putExtra("vessel", item.getVessel());
////                intent.putExtra("PODTiming", item.getPODTiming());
////                intent.putExtra("PortofDis", item.getPortofDischarge());
////                intent.putExtra("PortofLoad", item.getPortofLoading());
////                intent.putExtra("ShippingToName", item.getCustomerName());
////                intent.putExtra("ToCustomerId", item.getToCustomerId());
////
////
////                view.getContext().startActivity(intent);
//            }
//        });
//
//
//
////        holder.btnassesment.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                String strInvNumber = String.valueOf(item.getInvoiceNumber());
////
////                Intent intent = new Intent(view.getContext(), CustomerInvoiceList.class);
////
////                intent.putExtra("INVNumber", strInvNumber);
////
////
////
////            }
////        });


    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    rawDataTableListFiltered = BatchInvoicelist;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = BatchInvoicelist;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = BatchInvoicelist;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetBatchDetailsByInvoiceNumber> filteredList = new ArrayList<>();
                    for (int i = BatchInvoicelist.size(); i >= 1; i--) {
                        filteredList.add(BatchInvoicelist.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetBatchDetailsByInvoiceNumber> filteredList = new ArrayList<>();

                    for (GetBatchDetailsByInvoiceNumber row : BatchInvoicelist) {
                        String searchQuery = charString.toLowerCase();

                        String SubDealerName = row.getBatchNo();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (SubDealerName.toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "SubDealerName: " + row.getBatchNo());

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
                rawDataTableListFiltered = (List<GetBatchDetailsByInvoiceNumber>) filterResults.values;
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



        public TextView txtBatchInvoicenumber;

        public TextView txtQuantity;

        public TextView txtDate;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtBatchInvoicenumber = itemView.findViewById(R.id.txtBatchnumberinvoice);
            txtQuantity = itemView.findViewById(R.id.txtQuantityinvoice);
            txtDate = itemView.findViewById(R.id.txtDateinvoice);




        }
    }

    public interface OnItemClickListener {
        void onItemClick(GetDealerDetailsbyProcessorId Dealer);
    }




}


