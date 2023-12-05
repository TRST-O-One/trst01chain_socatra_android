package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceCustomerId;
import static com.socatra.intellitrack.constants.AppConstant.DeviceDealerID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceProcessorId;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

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

import com.google.gson.JsonElement;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.activity.invoice.ShareInvoiceActivity;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetDealerDetailsbyProcessorId;
import com.socatra.intellitrack.models.get.GetInvoiceDetailsByCustomerId;
import com.socatra.intellitrack.models.get.GetInvoicedetails;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavInvoiceAdapter extends RecyclerView.Adapter<NavInvoiceAdapter.ViewHolder> implements Filterable {

    private List<GetInvoicedetails> NavInvoicelist;

    private List<GetInvoiceDetailsByCustomerId> NavInvoicelist1;


    AppHelper appHelper;
    AppViewModel viewModel;

    String Token, strDealerID, strDealerName, strProcessorId , strCustomerId;

    String strLanguageId;


    List<GetInvoicedetails> rawDataTableListFiltered;




    public NavInvoiceAdapter(AppHelper appHelper, AppViewModel viewModel, List<GetInvoicedetails> NavInvoicelist, String strDealerID) {

        this.NavInvoicelist = NavInvoicelist;
        this.rawDataTableListFiltered = NavInvoicelist;
        this.appHelper = appHelper;
        this.viewModel = viewModel;
        this.strDealerID = strDealerID;

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");


    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nav_invoice_individual, parent, false);
        return new NavInvoiceAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NavInvoiceAdapter.ViewHolder holder, int position) {

        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");
        strCustomerId = appHelper.getSharedPrefObj().getString(DeviceCustomerId,"");

        GetInvoicedetails item = rawDataTableListFiltered.get(position);
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
        holder.txtCustomerName.setText(String.valueOf(item.getShippingToName()));



        if (!strDealerID.isEmpty())
        {
            holder.txtCustomerNameTitle.setText("Processor Name");

        }else if(!strProcessorId.isEmpty() || !strCustomerId.isEmpty()) {
            holder.txtCustomerNameTitle.setText("Customer Name");
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strInvNumber = String.valueOf(item.getInvoiceNumber());
                String strInvDate = String.valueOf(item.getCreatedDate().split("T")[0]);
                String strShippingTO = String.valueOf(item.getShippingToName());
                String strShippingAdd = String.valueOf(item.getShippingAddress());
                String strProduct = String.valueOf(item.getProduct());
                String strQty = String.valueOf(item.getQuantity());
                Intent intent = new Intent(view.getContext(), ShareInvoiceActivity.class);


                intent.putExtra("key","role");
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
                intent.putExtra("ShippingToName", item.getShippingToName());
                intent.putExtra("ToCustomerId", item.getToCustomerId());


                view.getContext().startActivity(intent);
            }
        });

       changeLangMethod(holder);




    }

    private void changeLangMethod(ViewHolder holder) {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        Call<JsonElement> callRetrofit = null;

        callRetrofit = service.getTransalteLanguageWordsByLanguageIdFromServer(strLanguageId,appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    String strResponse = String.valueOf(response.body());
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        try {


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);

                                String jsonEngWord = jsonObjectFarmerPD.getString("SelectedWord");
                                String strConvertedWord = jsonObjectFarmerPD.getString("TranslatedWord");


                                if (strLanguageId.equals("1")) {


                                    if ("Invoice Number".equals(jsonEngWord)) {
                                        holder.txtl_InvoiceNo.setText(jsonEngWord);
                                    } else if ("Product".equals(jsonEngWord)) {
                                        holder.txtl_product.setText(jsonEngWord);
                                    }
                                    else if ("Quantity ".equals(jsonEngWord)) {
                                        holder.txtl_Quantity.setText(jsonEngWord);
                                    }
                                    else if ("Shipping Address".equals(jsonEngWord)) {
                                        holder.txtl_ShippingAddress.setText(jsonEngWord);
                                    }

                                    else if ("Processor Name".equals(jsonEngWord))
                                    {
                                        holder.txtCustomerNameTitle.setText(jsonEngWord);


                                    }
//
//



                                } else  {

                                    if ("Invoice Number".equals(jsonEngWord)) {
                                        holder.txtl_InvoiceNo.setText(strConvertedWord);
                                    } else if ("Product".equals(jsonEngWord)) {
                                        holder.txtl_product.setText(strConvertedWord);
                                    }
                                    else if ("Quantity".equals(jsonEngWord)) {
                                        holder.txtl_Quantity.setText(strConvertedWord);
                                    }
                                    else if ("Shipping Address".equals(jsonEngWord)) {
                                        holder.txtl_ShippingAddress.setText(strConvertedWord);
                                    }
                                    else if ("Processor Name".equals(jsonEngWord))
                                    {

                                        holder.txtCustomerNameTitle.setText(strConvertedWord);


                                    }
//

                                }

                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();

                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();


                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {


                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
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
                    List<GetInvoicedetails> filteredList = new ArrayList<>();
                    for (int i = NavInvoicelist.size(); i >= 1; i--) {
                        filteredList.add(NavInvoicelist.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetInvoicedetails> filteredList = new ArrayList<>();

                    for (GetInvoicedetails row : NavInvoicelist) {
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
                rawDataTableListFiltered = (List<GetInvoicedetails>) filterResults.values;
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

        public TextView txtl_InvoiceNo,txtl_product,txtl_Quantity,txtl_ShippingAddress,txtl_CustomerName,txtl_CustomerNameTitle;





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


            //language
            txtl_InvoiceNo = itemView.findViewById(R.id.Invoicenumber);
            txtl_product = itemView.findViewById(R.id.product);
            txtl_Quantity = itemView.findViewById(R.id.quantity);
            txtl_ShippingAddress = itemView.findViewById(R.id.ShippingAddress);
            txtl_CustomerNameTitle = itemView.findViewById(R.id.txt_customer_title);





        }
    }

    public interface OnItemClickListener {
        void onItemClick(GetDealerDetailsbyProcessorId Dealer);
    }




}

