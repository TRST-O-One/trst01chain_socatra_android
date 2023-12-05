package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.content.Context;
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
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetCustomersbyProcessorId;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.ViewHolder> implements Filterable {

    private List<GetCustomersbyProcessorId> CustomerList;
    AppHelper appHelper;
    AppViewModel viewModel;

    private Context context;


    String strLanguageId;


    List<GetCustomersbyProcessorId> rawDataTableListFiltered;

    private OnItemClickListener onItemClickListener;

    public CustomerListAdapter(AppHelper appHelper, Context context, List<GetCustomersbyProcessorId> Dealerslist  ) {

        this.CustomerList = Dealerslist;
        this.context = context;
        this.rawDataTableListFiltered = Dealerslist;
        this.appHelper = appHelper;
        this.viewModel = viewModel;
        this.onItemClickListener = onItemClickListener;

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");



    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_list_individual, parent, false);
        return new CustomerListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CustomerListAdapter.ViewHolder holder, int position) {

        GetCustomersbyProcessorId item = rawDataTableListFiltered.get(position);


        holder.txtCustomerName.setText(item.getEntityName());
        holder.txtAddress.setText(String.valueOf(item.getAddress()));
        holder.txtphoneno.setText(String.valueOf(item.getPhone()));
        holder.txtEmail.setText(item.getEmail());
        holder.txtvillage.setText(String.valueOf(item.getVillageName()));
        holder.txtsubdistrict.setText(String.valueOf(item.getSubDistrictname()));
        holder.txtDistrict.setText(item.getDistrictorRegencyName());
        holder.txtstate.setText(String.valueOf(item.getStateorProvinceName()));
        holder.txtcountry.setText(String.valueOf(item.getCountryName()));

     //   changeLangMethod(holder);





    }

    private void changeLangMethod(ViewHolder holder) {

        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
//        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("loading  data...");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();
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

                                    if ("Customer Name".equals(jsonEngWord)) {
                                        holder.txtl_Customer.setText(jsonEngWord);
                                    } else if ("Address".equals(jsonEngWord)) {
                                        holder.txtl_address.setText(jsonEngWord);
                                    }else if ("Email".equals(jsonEngWord)) {
                                        holder.txtl_email.setText(jsonEngWord);
                                    }else if ("Phone No".equals(jsonEngWord)) {
                                        holder.txtl_phoneno.setText(jsonEngWord);
                                    }else if ("Village".equals(jsonEngWord)) {
                                        holder.txtl_village.setText(jsonEngWord);
                                    }else if ("Sub-District".equals(jsonEngWord)) {
                                        holder.txtl_subdistrict.setText(jsonEngWord);
                                    }else if ("District or Regency".equals(jsonEngWord)) {
                                        holder.txtl_district.setText(jsonEngWord);
                                    }else if ("State or Province".equals(jsonEngWord)) {
                                        holder.txtl_state.setText(jsonEngWord);
                                    }else if ("Country".equals(jsonEngWord)) {
                                        holder.txtl_country.setText(jsonEngWord);
                                    }




                                } else {
                                    if ("Customer Name".equals(jsonEngWord)) {
                                        holder.txtl_Customer.setText(strConvertedWord);
                                    } else if ("Address".equals(jsonEngWord)) {
                                        holder.txtl_address.setText(strConvertedWord);
                                    }else if ("Email".equals(jsonEngWord)) {
                                        holder.txtl_email.setText(strConvertedWord);
                                    }else if ("Phone No".equals(jsonEngWord)) {
                                        holder.txtl_phoneno.setText(strConvertedWord);
                                    }else if ("Village".equals(jsonEngWord)) {
                                        holder.txtl_village.setText(strConvertedWord);
                                    }else if ("Sub-District".equals(jsonEngWord)) {
                                        holder.txtl_subdistrict.setText(strConvertedWord);
                                    }else if ("District or Regency".equals(jsonEngWord)) {
                                        holder.txtl_district.setText(strConvertedWord);
                                    }else if ("State or Province".equals(jsonEngWord)) {
                                        holder.txtl_state.setText(strConvertedWord);
                                    }else if ("Country".equals(jsonEngWord)) {
                                        holder.txtl_country.setText(strConvertedWord);
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
                    rawDataTableListFiltered = CustomerList;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = CustomerList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = CustomerList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetCustomersbyProcessorId> filteredList = new ArrayList<>();
                    for (int i = CustomerList.size(); i >= 1; i--) {
                        filteredList.add(CustomerList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetCustomersbyProcessorId> filteredList = new ArrayList<>();

                    for (GetCustomersbyProcessorId row : CustomerList) {
                        String searchQuery = charString.toLowerCase();

                        String DealerName = row.getEntityName();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (DealerName.toLowerCase().contains(charString.toLowerCase()) || row.getEmail().toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "GRNnumber: " + row.getEntityName());

                            filteredList.add(row);
                        }

                        else {

                            System.out.println("GRNnumber: " + DealerName + ", SearchQuery: " + searchQuery);
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
                rawDataTableListFiltered = (List<GetCustomersbyProcessorId>) filterResults.values;
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

        public TextView txtAddress;

        public TextView txtEmail;

        public TextView txtphoneno;

        public TextView txtvillage;

        public TextView txtsubdistrict;

        public TextView txtDistrict;

        public TextView txtstate;

        public TextView txtcountry;

        public TextView txtl_Customer,txtl_address,txtl_email,txtl_phoneno,txtl_village,txtl_subdistrict,txtl_district,txtl_state,txtl_country;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCustomerName = itemView.findViewById(R.id.txtCustomerName);
            txtAddress = itemView.findViewById(R.id.txtCAddress);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtphoneno = itemView.findViewById(R.id.txtPhone);
            txtvillage = itemView.findViewById(R.id.txtCVillage);
            txtsubdistrict = itemView.findViewById(R.id.txtCSubDistrict);
            txtDistrict = itemView.findViewById(R.id.txtCDistrict);
            txtstate = itemView.findViewById(R.id.txtCState);
            txtcountry = itemView.findViewById(R.id.txtCCountry);




            //language
            txtl_Customer = itemView.findViewById(R.id.CustomerName);
            txtl_address = itemView.findViewById(R.id.Address);
            txtl_email = itemView.findViewById(R.id.Email);
            txtl_phoneno = itemView.findViewById(R.id.Phone);
            txtl_village = itemView.findViewById(R.id.Village);
            txtl_subdistrict = itemView.findViewById(R.id.SubDistrict);
            txtl_district = itemView.findViewById(R.id.District);
            txtl_state = itemView.findViewById(R.id.State);
            txtl_country = itemView.findViewById(R.id.Country);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(GetCustomersbyProcessorId Dealer);
    }




}
