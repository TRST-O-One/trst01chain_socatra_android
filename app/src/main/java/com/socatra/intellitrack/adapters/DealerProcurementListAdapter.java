package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetDealerProcurementDetails;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerProcurementListAdapter extends RecyclerView.Adapter<DealerProcurementListAdapter.ViewHolder> {

    private List<GetDealerProcurementDetails> DealerProcurementList;

    List<GetDealerProcurementDetails> rawDataTableListFiltered;

    AppViewModel viewModel;

    AppHelper appHelper;

    String strLanguageId;



    public DealerProcurementListAdapter(AppHelper appHelper, List<GetDealerProcurementDetails> DealerProcurementList) {
        this.DealerProcurementList = DealerProcurementList;
        this.rawDataTableListFiltered = DealerProcurementList;
        this.appHelper = appHelper;

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");


    }

    @NonNull
    @Override
    public DealerProcurementListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dealer_procurement_individual, parent, false);
        return new DealerProcurementListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealerProcurementListAdapter.ViewHolder holder, int position) {
        GetDealerProcurementDetails   item = rawDataTableListFiltered.get(position);


        holder.txtNumberofTOtalProcurement.setText(String.valueOf(item.getNumberofTotalProcurement()));
        holder.txtSumofTotalProcurement.setText(String.valueOf(item.getSumofTotalProcurement()));


      //  changeLangMethod(holder);




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



                                    if ("".equals(jsonEngWord)) {
                                        holder.txtl_SumofTotalProcurement.setText(jsonEngWord);
                                    } else if ("".equals(jsonEngWord)) {
                                        holder.txtl_SumofTotalProcurement.setText(jsonEngWord);
                                    }





                                } else  {
                                    if ("".equals(jsonEngWord)) {
                                        holder.txtl_SumofTotalProcurement.setText(strConvertedWord);
                                    } else if ("".equals(jsonEngWord)) {
                                        holder.txtl_SumofTotalProcurement.setText(strConvertedWord);
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

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    rawDataTableListFiltered = DealerProcurementList;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = DealerProcurementList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = DealerProcurementList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetDealerProcurementDetails> filteredList = new ArrayList<>();
                    for (int i = DealerProcurementList.size(); i >= 1; i--) {
                        filteredList.add(DealerProcurementList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetDealerProcurementDetails> filteredList = new ArrayList<>();
                    for (GetDealerProcurementDetails row : DealerProcurementList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
//                        if (row.getNumberofTotalProcurement().toLowerCase().contains(charString.toLowerCase())  || row.getAddress().toLowerCase().contains(charString.toLowerCase())) {
//                            filteredList.add(row);
//                        }
                    }

                    rawDataTableListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = rawDataTableListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                rawDataTableListFiltered = (List<GetDealerProcurementDetails>) filterResults.values;
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

        public TextView txtSumofTotalProcurement;
        public TextView txtNumberofTOtalProcurement;


        public TextView txtl_NumberofTOtalProcurement ,txtl_SumofTotalProcurement;







        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtSumofTotalProcurement = itemView.findViewById(R.id.txtSumofTotalProcurement);
            txtNumberofTOtalProcurement = itemView.findViewById(R.id.txtNumberofTOtalProcurement);

            //language
            txtl_NumberofTOtalProcurement = itemView.findViewById(R.id.SumofTotalProcurement);
            txtl_SumofTotalProcurement = itemView.findViewById(R.id.NumberofTOtalProcurement);


        }
    }






}
