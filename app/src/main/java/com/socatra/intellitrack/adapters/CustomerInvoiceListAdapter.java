package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.DeviceCustomerId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.intellitrack.R;
import com.socatra.intellitrack.activity.customerflow.CustomerViewBatchListAct;
import com.socatra.intellitrack.activity.customerflow.FarmerListbyInvoiceActivity;
import com.socatra.intellitrack.activity.invoice.ShareInvoiceActivity;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetDealerDetailsbyProcessorId;
import com.socatra.intellitrack.models.get.GetInvoiceDetailsByCustomerId;
import com.socatra.intellitrack.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public class CustomerInvoiceListAdapter extends RecyclerView.Adapter<CustomerInvoiceListAdapter.ViewHolder> implements Filterable {


    private List<GetInvoiceDetailsByCustomerId> NavInvoicelist;


    AppHelper appHelper;
    AppViewModel viewModel;

    String Token, strDealerID, strDealerName, strProcessorId , strCustomerId;


    List<GetInvoiceDetailsByCustomerId> rawDataTableListFiltered;




    public CustomerInvoiceListAdapter(AppHelper appHelper, AppViewModel viewModel, List<GetInvoiceDetailsByCustomerId> NavInvoicelist, String strDealerID) {

        this.NavInvoicelist = NavInvoicelist;
        this.rawDataTableListFiltered = NavInvoicelist;
        this.appHelper = appHelper;
        this.viewModel = viewModel;
        this.strDealerID = strDealerID;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_invoice_individual, parent, false);
        return new CustomerInvoiceListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CustomerInvoiceListAdapter.ViewHolder holder, int position) {

        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strCustomerId = appHelper.getSharedPrefObj().getString(DeviceCustomerId,"");

        GetInvoiceDetailsByCustomerId item = rawDataTableListFiltered.get(position);
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

        holder.txtProduct.setText(item.getProduct());
        holder.txtAddress.setText(String.valueOf(item.getShippingAddress()));
        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));
        holder.txtInvoicenumber.setText(String.valueOf(item.getInvoiceNumber()));
        holder.txtCustomerName.setText(String.valueOf(item.getCustomerName()));


        holder.btnView.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                String strInvNumber = String.valueOf(item.getInvoiceNumber());
                String strInvDate = String.valueOf(item.getCreatedDate().split("T")[0]);
                String strShippingTO = String.valueOf(item.getCustomerName());
                String strShippingAdd = String.valueOf(item.getShippingAddress());
                String strProduct = String.valueOf(item.getProduct());
                String strQty = String.valueOf(item.getQuantity());
                Intent intent = new Intent(v.getContext(), ShareInvoiceActivity.class);

                intent.putExtra("key", "cust");
                intent.putExtra("INVNumber", strInvNumber);
                intent.putExtra("shippingTO", strShippingTO);
                intent.putExtra("shippingAdd", strShippingAdd);
                intent.putExtra("product", strProduct);
                intent.putExtra("qty", strQty);
                intent.putExtra("INvDate", strInvDate);
                intent.putExtra("vessel", item.getVessel());
                intent.putExtra("PODTiming", item.getPODTiming());
                intent.putExtra("PortofDis", item.getPortofDischarge());
                intent.putExtra("PortofLoad", item.getPortofLoading());
                intent.putExtra("ShippingToName", item.getCustomerName());
                intent.putExtra("ToCustomerId", item.getToCustomerId());
                intent.putExtra("processorName", item.getProcessorName());
                intent.putExtra("processorAdd", item.getProcessorAddress());

                v.getContext().startActivity(intent);

            }
        });


        holder.btnOrigin.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                String strInvoiceid = String.valueOf(item.getId());
                Intent intent = new Intent(v.getContext(), CustomerViewBatchListAct.class);
                intent.putExtra("INVNo", strInvoiceid);
                Log.d("INVNo","INVNo"  +strInvoiceid);
                v.getContext().startActivity(intent);

            }
        });


        holder.btnassesment.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {


                String strInvoiceid = String.valueOf(item.getId());
                Intent intent = new Intent(v.getContext(), FarmerListbyInvoiceActivity.class);
                intent.putExtra("INVNo", strInvoiceid);
                Log.d("INVNo","INVNo"  +strInvoiceid);
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
                    rawDataTableListFiltered = NavInvoicelist;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = NavInvoicelist;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = NavInvoicelist;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetInvoiceDetailsByCustomerId> filteredList = new ArrayList<>();
                    for (int i = NavInvoicelist.size(); i >= 1; i--) {
                        filteredList.add(NavInvoicelist.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetInvoiceDetailsByCustomerId> filteredList = new ArrayList<>();

                    for (GetInvoiceDetailsByCustomerId row : NavInvoicelist) {
                        String searchQuery = charString.toLowerCase();

                        String SubDealerName = row.getInvoiceNumber();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (SubDealerName.toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "SubDealerName: " + row.getInvoiceNumber());

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
                rawDataTableListFiltered = (List<GetInvoiceDetailsByCustomerId>) filterResults.values;
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

        public TextView txtDealerUser , txtProcessorUser;

        public TextView txtUserName;

        public TextView txtInvoicenumber;

        public TextView txtProduct;
        public TextView txtAddress;

        public TextView txtQuantity;

        public TextView txtCustomerName,txtCustomerNameTitle;

         public Button btnassesment , btnView ,btnOrigin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            txtDealerUser = itemView.findViewById(R.id.dealer_user);
//            txtProcessorUser = itemView.findViewById(R.id.processor_user);

//            txtUserName = itemView.findViewById(R.id.txtUser);
            txtInvoicenumber = itemView.findViewById(R.id.txtInvoicenumber);
            txtProduct = itemView.findViewById(R.id.txtproduct);
            txtAddress = itemView.findViewById(R.id.txtShippingAddress);
            txtQuantity = itemView.findViewById(R.id.txtquantity);
            txtCustomerName = itemView.findViewById(R.id.txtCustomerName);
            txtCustomerNameTitle = itemView.findViewById(R.id.txt_customer_title);
            btnassesment = itemView.findViewById(R.id.txtAssesment);
            btnView = itemView.findViewById(R.id.txtView);
            btnOrigin = itemView.findViewById(R.id.txtorigin);






        }
    }

    public interface OnItemClickListener {
        void onItemClick(GetDealerDetailsbyProcessorId Dealer);
    }




}


