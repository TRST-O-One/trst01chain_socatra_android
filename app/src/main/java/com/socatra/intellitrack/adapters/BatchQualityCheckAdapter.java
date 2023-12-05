package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.socatra.intellitrack.models.get.GetQualityCheckDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BatchQualityCheckAdapter extends RecyclerView.Adapter<BatchQualityCheckAdapter.BatchQualityCheckViewHolder> {

    private List<GetQualityCheckDetails> QualityCheckList;

    AppHelper appHelper;


    String strLanguageId;

    List<GetQualityCheckDetails> rawDataTableListFiltered;
    private Context context;


    public BatchQualityCheckAdapter(AppHelper appHelper,List<GetQualityCheckDetails> QualityCheckList) {
        this.QualityCheckList = QualityCheckList;
        this.rawDataTableListFiltered = QualityCheckList;
        this.appHelper = appHelper;

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");

    }

    @NonNull
    @Override
    public BatchQualityCheckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BatchQualityCheckViewHolder(LayoutInflater.from(context).inflate(R.layout.batch_quality_list_individual, parent, false));
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BatchQualityCheckViewHolder holder, int position) {
        GetQualityCheckDetails Batch = rawDataTableListFiltered.get(position);

        String Batchcreateddate = Batch.getQualityCheckDate().split("T")[0];
        Double quantityRate = Batch.getQuantityRate();
        String quantityRateString = String.valueOf(quantityRate);
        holder.txtCreatedDate.setText(Batchcreateddate);
        holder.txtBatchnumber.setText(Batch.getBatchCode());
        holder.txtQuantityRate.setText(quantityRateString);

        //changeLangMethod(holder);







    }

    private void changeLangMethod(BatchQualityCheckViewHolder holder) {

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


                                    if ("BatchCode".equals(jsonEngWord)) {
                                        holder.txtl_BatchCode.setText(jsonEngWord);
                                    } else if ("Quantity Rate".equals(jsonEngWord)) {
                                        holder.txtl_QuantityRate.setText(jsonEngWord);
                                    } else if ("Quality Check Date".equals(jsonEngWord)) {
                                        holder.txtl_QualityCheckDate.setText(jsonEngWord);
                                    }



                                } else  {
                                    if ("BatchCode".equals(jsonEngWord)) {
                                        holder.txtl_BatchCode.setText(strConvertedWord);
                                    } else if ("Quantity Rate".equals(jsonEngWord)) {
                                        holder.txtl_QuantityRate.setText(strConvertedWord);
                                    } else if ("Quality Check Date".equals(jsonEngWord)) {
                                        holder.txtl_QualityCheckDate.setText(strConvertedWord);
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
                    rawDataTableListFiltered = QualityCheckList;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = QualityCheckList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = QualityCheckList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetQualityCheckDetails> filteredList = new ArrayList<>();
                    for (int i = QualityCheckList.size(); i >= 1; i--) {
                        filteredList.add(QualityCheckList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetQualityCheckDetails> filteredList = new ArrayList<>();

                    for (GetQualityCheckDetails row : QualityCheckList) {
                        String searchQuery = charString.toLowerCase();

                        String grnNumber = row.getBatchCode();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (grnNumber.toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "GRNnumber: " + row.getBatchCode());

                            filteredList.add(row);
                        }

                        else {

                            System.out.println("GRNnumber: " + grnNumber + ", SearchQuery: " + searchQuery);
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
                rawDataTableListFiltered = (List<GetQualityCheckDetails>) filterResults.values;
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

    public static class BatchQualityCheckViewHolder extends RecyclerView.ViewHolder {
        TextView txtBatchnumber, txtCreatedDate , txtQuantityRate;

        TextView txtl_BatchCode,txtl_QuantityRate,txtl_QualityCheckDate;

        public BatchQualityCheckViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBatchnumber = itemView.findViewById(R.id.txtBatchnumber);
            txtCreatedDate = itemView.findViewById(R.id.txtBatch_date);
            txtQuantityRate = itemView.findViewById(R.id.txtquantityrate);

            //language
            txtl_BatchCode = itemView.findViewById(R.id.BatchCode);
            txtl_QuantityRate = itemView.findViewById(R.id.Quantity_rate);
            txtl_QualityCheckDate = itemView.findViewById(R.id.QualityCheckDate);

        }
    }
}
