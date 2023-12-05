package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
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
import com.socatra.intellitrack.activity.main_dash_board.PlotsListActivity;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetPlotsByDealerId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlotsByDealerIdAdapter extends RecyclerView.Adapter<PlotsByDealerIdAdapter.ViewHolder> implements Filterable {
    private List<GetPlotsByDealerId>  plotList;

    List<GetPlotsByDealerId> rawDataTableListFiltered;

    private AppHelper appHelper;

    private OnItemClickListener onItemClickListener;

    String strLanguageId;

    public PlotsByDealerIdAdapter(AppHelper appHelper,List<GetPlotsByDealerId> plotsList) {

        this.plotList = plotsList;
        this.appHelper = appHelper;

        this.rawDataTableListFiltered = plotsList;
        this.onItemClickListener = onItemClickListener;

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plots_main_individual, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetPlotsByDealerId item = rawDataTableListFiltered.get(position);
        holder.txtFarmerCode.setText(item.getFarmerCode());
        holder.txtFarmerName.setText(String.valueOf(item.getFarmerName()));
        holder.txtNoofplots.setText(String.valueOf(item.getTotalPlots()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FarmerCode = String.valueOf(item.getFarmerCode());
                String FarmerName = String.valueOf(item.getFarmerName());


                Intent intent = new Intent(view.getContext(), PlotsListActivity.class);
                intent.putExtra("FarmerCode", FarmerCode);
                intent.putExtra("FarmerName", FarmerName);

                Log.d("FarmerCode","Code: " +FarmerCode);
                Log.d("FarmerName","Name: " +FarmerName);

                view.getContext().startActivity(intent);
            }
        });

      // changeLangMedthod(holder);



    }

    private void changeLangMedthod(ViewHolder holder) {
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


                                    if ("FarmerCode".equals(jsonEngWord)) {
                                        holder.txtl_FarmerCode.setText(jsonEngWord);
                                    } else if ("Farmer Name".equals(jsonEngWord)) {
                                        holder.txtl_FarmerName.setText(jsonEngWord);
                                    }else if ("No of Plots".equals(jsonEngWord)) {
                                        holder.txtl_Noofplots.setText(jsonEngWord);
                                    }




                                } else  {
                                    if ("FarmerCode".equals(jsonEngWord)) {
                                        holder.txtl_FarmerCode.setText(strConvertedWord);
                                    } else if ("Farmer Name".equals(jsonEngWord)) {
                                        holder.txtl_FarmerName.setText(strConvertedWord);
                                    }else if ("No of Plots".equals(jsonEngWord)) {
                                        holder.txtl_Noofplots.setText(strConvertedWord);
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
                    rawDataTableListFiltered = plotList;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = plotList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = plotList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetPlotsByDealerId> filteredList = new ArrayList<>();
                    for (int i = plotList.size(); i >= 1; i--) {
                        filteredList.add(plotList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetPlotsByDealerId> filteredList = new ArrayList<>();
                    for (GetPlotsByDealerId row : plotList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFarmerName().toLowerCase().contains(charString.toLowerCase()) || row.getFarmerCode().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
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
                rawDataTableListFiltered = (List<GetPlotsByDealerId>) filterResults.values;
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
        public TextView txtFarmerCode;
        public TextView txtFarmerName;
        public TextView txtNoofplots;

        public TextView txtl_FarmerName,txtl_FarmerCode,txtl_Noofplots;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFarmerName = itemView.findViewById(R.id.txtfarmerName);
            txtFarmerCode = itemView.findViewById(R.id.txtfarmercode);
            txtNoofplots = itemView.findViewById(R.id.txtNoofplots);

            //language
            txtl_FarmerName = itemView.findViewById(R.id.FarmerName);
            txtl_FarmerCode = itemView.findViewById(R.id.farmercode);
            txtl_Noofplots = itemView.findViewById(R.id.Noofplots);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(GetPlotsByDealerId farmer);
    }
}
