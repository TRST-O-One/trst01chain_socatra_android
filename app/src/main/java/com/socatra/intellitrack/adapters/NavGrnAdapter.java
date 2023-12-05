package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.activity.grnflow.ShareDealerGRNReceipt;
import com.socatra.intellitrack.activity.grnflow.ShareGRNActivity;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetGrndetails;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavGrnAdapter extends RecyclerView.Adapter<NavGrnAdapter.ViewHolder> implements Filterable {

    AppHelper appHelper;

    String strLanguageId;
    AppViewModel viewModel;
    String Token, strDealerID, strDealerName, strProcessorId;
    List<GetGrndetails> rawDataTableListFiltered;
    private List<GetGrndetails> NavGrnlist;


    public NavGrnAdapter(AppHelper appHelper, AppViewModel viewModel, List<GetGrndetails> NavGrnlist) {

        this.NavGrnlist = NavGrnlist;
        this.rawDataTableListFiltered = NavGrnlist;
        this.appHelper = appHelper;
        this.viewModel = viewModel;

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");






    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nav_grn_individual, parent, false);
        return new NavGrnAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NavGrnAdapter.ViewHolder holder, int position) {

        strDealerID = appHelper.getSharedPrefObj().getString(DeviceDealerID, "");
        strProcessorId = appHelper.getSharedPrefObj().getString(DeviceProcessorId, "");

        GetGrndetails item = rawDataTableListFiltered.get(position);

//        if (strDealerID.equals("")) {
//            holder.txtUserName.setText(item.getProcessor());
//            holder.txtDealerUser.setVisibility(View.GONE);
//            holder.txtProcessorUser.setVisibility(View.VISIBLE);
//
//
//
//        } else {
//            holder.txtUserName.setText(item.getDealer());
//            holder.txtDealerUser.setVisibility(View.VISIBLE);
//            holder.txtProcessorUser.setVisibility(View.GONE);
//        }

        holder.txtGrnnumber.setText(item.getGRNnumber());
        holder.txtAddress.setText(String.valueOf(item.getFarmeradress()));
        holder.txtfarmercode.setText(String.valueOf(item.getFarmerCode()));
        holder.txtfarmername.setText(String.valueOf(item.getFarmerName()));

        holder.txtquantity.setText(String.valueOf(item.getQuantity()));
        holder.txtcurquantity.setText(String.valueOf(item.getCurrentQty()));
        holder.txtContactno.setText(String.valueOf(item.getPrimaryContactNo()));
        String grnDate = item.getGRNdate().split("T")[0]; // Get the date part before "T"
        holder.txtgrndate.setText(grnDate);

        if (item.getFarmerCode().equals("No Farmer Data")) {
            holder.llFarmerDataLayout.setVisibility(View.GONE);
            holder.llDealerName.setVisibility(View.VISIBLE);
            if (!item.getToDealerName().isEmpty()) {
                holder.txtDelaerName.setText(String.valueOf(item.getToDealerName()));
            }
        } else {
            holder.llFarmerDataLayout.setVisibility(View.VISIBLE);
            holder.llDealerName.setVisibility(View.GONE);

        }

        // Set a placeholder image
        // Picasso.get().load(item.getGRNDocument()).placeholder(R.drawable.ic_baseline_agriculture_24).into(holder.txtImage);

        Log.d("Image URL", "URL" + item.getGRNDocument());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String DealerId = String.valueOf(item.getId());
                String GRNNumber = String.valueOf(item.getGRNnumber());
                String FarmerName = String.valueOf(item.getFarmerName());
                String Address = String.valueOf(item.getFarmeradress());
                String GRNDate = String.valueOf(item.getGRNdate().split("T")[0]);
                String Quantity = String.valueOf(item.getQuantity());
                String ContactNo = String.valueOf(item.getPrimaryContactNo());
                String DealerName = String.valueOf(item.getToDealerName());
//               String strShippingTO = String.valueOf(item.get());
//               String strShippingAdd = String.valueOf(item.getToDealerName());
                String GRNDocument = item.getGRNDocument();


                if (item.getFarmerCode().equals("No Farmer Data")) {

                    Intent intent = new Intent(view.getContext(), ShareDealerGRNReceipt.class);
                    intent.putExtra("DealerId", DealerId);
                    Log.d("DealerId", "Id: " + DealerId);

                    intent.putExtra("GRNNumber", GRNNumber);
                    Log.d("GRNNumber", "GRNNumber: " + GRNNumber);

                    intent.putExtra("DealerName", DealerName);
                    Log.d("DealerName", "DealerName: " + DealerName);


                    intent.putExtra("Quantity", Quantity);
                    Log.d("Quantity", "Quantity: " + Quantity);


                    intent.putExtra("GRNDate", GRNDate);
                    Log.d("GRNDate", "GRNDate: " + GRNDate);

                    intent.putExtra("GRNDate", GRNDate);
                    Log.d("GRNDate", "GRNDate: " + GRNDate);

                    intent.putExtra("GRNDocument", GRNDocument);
                    Log.d("GRNDocument", "GRNDocument: " + GRNDocument);


//                    intent.putExtra("shippingTO", strShippingTO);
//                    intent.putExtra("shippingAdd", strShippingAdd);
                    view.getContext().startActivity(intent);


                }
                else {

                    Intent intent = new Intent(view.getContext(), ShareGRNActivity.class);
                    intent.putExtra("key", "role");
                    intent.putExtra("DealerId", DealerId);
                    Log.d("DealerId", "Id: " + DealerId);

                    intent.putExtra("GRNNumber", GRNNumber);
                    Log.d("GRNNumber", "GRNNumber: " + GRNNumber);

                    intent.putExtra("DealerName", DealerName);
                    Log.d("DealerName", "DealerName: " + DealerName);

                    intent.putExtra("FarmerName", FarmerName);
                    Log.d("FarmerName", "FarmerName: " + FarmerName);

                    intent.putExtra("Farmeradress", Address);
                    Log.d("Address", "Address: " + Address);


                    intent.putExtra("Quantity", Quantity);
                    Log.d("Quantity", "Quantity: " + Quantity);

                    intent.putExtra("ContactNo", ContactNo);
                    Log.d("ContactNo", "ContactNo: " + ContactNo);

                    intent.putExtra("GRNDate", GRNDate);
                    Log.d("GRNDate", "GRNDate: " + GRNDate);

                    intent.putExtra("GRNDate", GRNDate);
                    Log.d("GRNDate", "GRNDate: " + GRNDate);

                    intent.putExtra("GRNDocument", GRNDocument);
                    Log.d("GRNDocument", "GRNDocument: " + GRNDocument);

                    view.getContext().startActivity(intent);
                }

            }
        });

     //changLangMethod(holder);


    }

    private void changLangMethod(ViewHolder holder) {
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


                                    if ("GRN Number".equals(jsonEngWord)) {
                                        holder.txtl_Grnnumber.setText(jsonEngWord);
                                    } else if ("GRN Date".equals(jsonEngWord)) {
                                        holder.txtl_Grndate.setText(jsonEngWord);
                                    }
                                    else if ("Quantity".equals(jsonEngWord)) {
                                        holder.txtl_quantity.setText(jsonEngWord);
                                    }
                                    else if ("Current Quantity".equals(jsonEngWord)) {
                                        holder.txtl_currentquantity.setText(jsonEngWord);
                                    }
                                    else if ("FarmerCode".equals(jsonEngWord)) {
                                        holder.txtl_farmercode.setText(jsonEngWord);

                                    }
                                    else if ("Farmer Name".equals(jsonEngWord)) {
                                        holder.txtl_farmername.setText(jsonEngWord);
                                    }else if ("Contact No".equals(jsonEngWord)) {
                                        holder.txtl_contactno.setText(jsonEngWord);
                                    }else if ("Address".equals(jsonEngWord)) {
                                        holder.txtl_address.setText(jsonEngWord);
                                    }
                                    else if ("Dealer Name".equals(jsonEngWord)) {
                                        holder.txtl_dealername.setText(jsonEngWord);
                                    }


                                } else  {
                                    if ("GRN Number".equals(jsonEngWord)) {
                                        holder.txtl_Grnnumber.setText(strConvertedWord);
                                    } else if ("GRN Date".equals(jsonEngWord)) {
                                        holder.txtl_Grndate.setText(strConvertedWord);
                                    }else if ("Quantity".equals(jsonEngWord)) {
                                        holder.txtl_quantity.setText(strConvertedWord);
                                    }else if ("Current Quantity".equals(jsonEngWord)) {
                                        holder.txtl_currentquantity.setText(strConvertedWord);
                                    }
                                    else if ("FarmerCode".equals(jsonEngWord)) {
                                        holder.txtl_farmercode.setText(strConvertedWord);
                                    }
                                    else if ("Farmer Name".equals(jsonEngWord)) {
                                        holder.txtl_farmername.setText(strConvertedWord);
                                    }else if ("Contact No".equals(jsonEngWord)) {
                                        holder.txtl_contactno.setText(strConvertedWord);
                                    }else if ("Address".equals(jsonEngWord)) {
                                        holder.txtl_address.setText(strConvertedWord);
                                    }else if ("Dealer Name".equals(jsonEngWord)) {
                                        holder.txtl_dealername.setText(strConvertedWord);
                                    }

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
                    rawDataTableListFiltered = NavGrnlist;
                } else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = NavGrnlist;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = NavGrnlist;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetGrndetails> filteredList = new ArrayList<>();
                    for (int i = NavGrnlist.size(); i >= 1; i--) {
                        filteredList.add(NavGrnlist.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetGrndetails> filteredList = new ArrayList<>();

                    for (GetGrndetails row : NavGrnlist) {
                        String searchQuery = charString.toLowerCase();

                        String SubDealerName = row.getGRNnumber();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (SubDealerName.toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "SubDealerName: " + row.getGRNnumber());

                            filteredList.add(row);
                        } else {

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
                rawDataTableListFiltered = (List<GetGrndetails>) filterResults.values;
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

        public TextView txtDealerUser, txtProcessorUser;

        public TextView txtUserName;

        public TextView txtGrnnumber;

        public TextView txtfarmercode;

        public TextView txtfarmername;

        public TextView txtAddress;

        public TextView txtquantity;

        public TextView txtcurquantity;

        public TextView txtgrndate, txtDelaerName;

        public TextView txtContactno;

        public ImageView txtImage;

        public LinearLayout llFarmerDataLayout, llDealerName;


        public TextView txtl_Grnnumber,txtl_Grndate,txtl_quantity,txtl_dealername,txtl_currentquantity,txtl_farmercode,txtl_farmername,txtl_contactno,txtl_address;






        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            txtDealerUser = itemView.findViewById(R.id.dealer_user);
//            txtProcessorUser = itemView.findViewById(R.id.processor_user);
//            txtUserName = itemView.findViewById(R.id.txtUser);

            txtGrnnumber = itemView.findViewById(R.id.txtGrnnumber);
            txtfarmercode = itemView.findViewById(R.id.txtfarmercode);
            txtfarmername = itemView.findViewById(R.id.txtfarmername);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtquantity = itemView.findViewById(R.id.txtquantity);
            txtcurquantity = itemView.findViewById(R.id.txtcur_quantity);
            txtgrndate = itemView.findViewById(R.id.txtgrndate);
            txtContactno = itemView.findViewById(R.id.txtcontact_no);
            llFarmerDataLayout = itemView.findViewById(R.id.ll_farmer_data);
            llDealerName = itemView.findViewById(R.id.ll_dealer_name);
            txtDelaerName = itemView.findViewById(R.id.txt_dealer_name_grn);

            //language
            txtl_Grnnumber = itemView.findViewById(R.id.Grnnumber);
            txtl_Grndate = itemView.findViewById(R.id.grn_date);
            txtl_quantity = itemView.findViewById(R.id.quantity);
            txtl_dealername = itemView.findViewById(R.id.DealerName);
            txtl_currentquantity = itemView.findViewById(R.id.cur_quantity);
            txtl_farmercode = itemView.findViewById(R.id.farmerCode);
            txtl_farmername = itemView.findViewById(R.id.farmername);
            txtl_contactno = itemView.findViewById(R.id.contact_no);
            txtl_address = itemView.findViewById(R.id.Address);


//            txtImage = itemView.findViewById(R.id.grndocument);


        }
    }


}


