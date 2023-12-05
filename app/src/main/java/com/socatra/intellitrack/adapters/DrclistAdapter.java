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
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetDrcByProcessor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrclistAdapter extends RecyclerView.Adapter<DrclistAdapter.BatchDrcViewHolder> implements Filterable {

    private List<GetDrcByProcessor> getDrcByProcessor;

    AppHelper appHelper;

    String strLanguageId;

    List<GetDrcByProcessor> rawDataTableListFiltered;
    private Context context;

    public DrclistAdapter(AppHelper appHelper, List<GetDrcByProcessor> getDrcByProcessor) {
        this.getDrcByProcessor = getDrcByProcessor;
        this.rawDataTableListFiltered = getDrcByProcessor;
        this.appHelper = appHelper;

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");




    }

    @NonNull
    @Override
    public BatchDrcViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BatchDrcViewHolder(LayoutInflater.from(context).inflate(R.layout.drc_list_individual, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BatchDrcViewHolder holder, int position) {
        GetDrcByProcessor drc = rawDataTableListFiltered.get(position);

        String createddate = drc.getCreatedDate().split("T")[0];

        holder.txtCreatedDate.setText(createddate);
        holder.txtDrcnumber.setText(drc.getDRCNo());
//        holder.drcdocument

        Picasso.get().load(drc.getDRCDocument()).error( R.drawable.logotrack ).placeholder(R.drawable.animation_new ).into(holder.drcdocument);
        holder.drcdocument.setRotation(90.0f);
      //  Picasso.get().load(drc.getDRCDocument()).into( holder.drcdocument);

      // changeLangMethod(holder);









    }

    private void changeLangMethod(BatchDrcViewHolder holder) {
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


                                    if ("DRC Number".equals(jsonEngWord)) {
                                        holder.txtl_DrcNumber.setText(jsonEngWord);
                                    } else if ("Created Date".equals(jsonEngWord)) {
                                        holder.txtl_CreatedDate.setText(jsonEngWord);                                    }else if ("Quantity ".equals(jsonEngWord)) {
                                    }



                                } else {
                                    if ("DRC Number".equals(jsonEngWord)) {
                                        holder.txtl_DrcNumber.setText(strConvertedWord);
                                    } else if ("Created Date".equals(jsonEngWord)) {
                                        holder.txtl_CreatedDate.setText(strConvertedWord);                                    }else if ("Quantity ".equals(jsonEngWord)) {
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
                    rawDataTableListFiltered = getDrcByProcessor;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = getDrcByProcessor;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = getDrcByProcessor;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetDrcByProcessor> filteredList = new ArrayList<>();
                    for (int i = getDrcByProcessor.size(); i >= 1; i--) {
                        filteredList.add(getDrcByProcessor.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetDrcByProcessor> filteredList = new ArrayList<>();

                    for (GetDrcByProcessor row : getDrcByProcessor) {
                        String searchQuery = charString.toLowerCase();

                        String grnNumber = row.getDRCNo();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (grnNumber.toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "GRNnumber: " + row.getDRCNo());

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
                rawDataTableListFiltered = (List<GetDrcByProcessor>) filterResults.values;
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

    public static class BatchDrcViewHolder extends RecyclerView.ViewHolder {

        TextView txtDrcnumber, txtCreatedDate;
        ImageView drcdocument;

        TextView txtl_DrcNumber , txtl_CreatedDate;

        public BatchDrcViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDrcnumber = itemView.findViewById(R.id.txtDrcnumber);
            txtCreatedDate = itemView.findViewById(R.id.txtCreatedDate);
            drcdocument = itemView.findViewById(R.id.drcdocument);

            //language
            txtl_DrcNumber = itemView.findViewById(R.id.Drcnumber);
            txtl_CreatedDate = itemView.findViewById(R.id.drc_CreatedDate);


        }
    }
}