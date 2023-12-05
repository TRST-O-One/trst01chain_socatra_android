package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.content.Intent;
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
import com.socatra.intellitrack.activity.main_dash_board.MapActivity;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetPlotsDetailsByFarmercode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlotsDetailsByFarmercodeAdapter extends RecyclerView.Adapter<PlotsDetailsByFarmercodeAdapter.ViewHolder> {

    private List<GetPlotsDetailsByFarmercode> plotsdetailsList;
    List<GetPlotsDetailsByFarmercode> rawDataTableListFiltered;

    String mToken;

    private AppHelper appHelper;

    String strLanguageId;




    public PlotsDetailsByFarmercodeAdapter(AppHelper appHelper,List<GetPlotsDetailsByFarmercode> plotsdetailsList) {
        this.plotsdetailsList = plotsdetailsList;
        this.rawDataTableListFiltered = plotsdetailsList;
        this.mToken=mToken;
        this.appHelper = appHelper;

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plots_list_individual, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetPlotsDetailsByFarmercode item = rawDataTableListFiltered.get(position);


        holder.txtPlotCode.setText(item.getPlotCode());
        holder.txtTypeOfOwnership.setText(String.valueOf(item.getTypeOfOwnership()));
        holder.txtAreaInHectors.setText(String.valueOf(item.getAreaInHectors()));
        holder.txtGeoboundariesArea.setText(String.valueOf(item.getGeoboundariesArea()));
        holder.txtAddress.setText(item.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String plotCode = item.getPlotCode();
                String Area = String.valueOf(item.getAreaInHectors());
                String geoBoundariesArea = item.getGeoboundariesArea();                // Toast the plot code
//                Toast.makeText(view.getContext(), "Plot Code: " + plotCode, Toast.LENGTH_SHORT).show();
//                Toast.makeText(view.getContext(), "Area: " + Area, Toast.LENGTH_SHORT).show();
                // Create an Intent to start the DestinationActivity
                Intent intent = new Intent(view.getContext(), MapActivity.class);
                intent.putExtra("Area", Area);
                intent.putExtra("plotCode", plotCode);
                intent.putExtra("mToken",mToken);
                intent.putExtra("geoBoundariesArea", geoBoundariesArea);
                view.getContext().startActivity(intent);
            }
        });


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



                                    if ("Plot Code".equals(jsonEngWord)) {
                                        holder.txtl_PlotCode.setText(jsonEngWord);
                                    } else if ("Type of Ownership".equals(jsonEngWord)) {
                                        holder.txtl_TypeOfOwnership.setText(jsonEngWord);
                                    }else if ("Area in Hec".equals(jsonEngWord)) {
                                        holder.txtl_AreaInHectors.setText(jsonEngWord);
                                    }else if ("Geoboundaries Area".equals(jsonEngWord)) {
                                        holder.txtl_GeoboundariesArea.setText(jsonEngWord);
                                    }else if ("Address".equals(jsonEngWord)) {
                                        holder.txtl_Address.setText(jsonEngWord);
                                    }





                                } else {
                                    if ("Plot Code".equals(jsonEngWord)) {
                                        holder.txtl_PlotCode.setText(strConvertedWord);
                                    } else if ("Type of Ownership".equals(jsonEngWord)) {
                                        holder.txtl_TypeOfOwnership.setText(strConvertedWord);
                                    }else if ("Area in Hec".equals(jsonEngWord)) {
                                        holder.txtl_AreaInHectors.setText(strConvertedWord);
                                    }else if ("Geoboundaries Area".equals(jsonEngWord)) {
                                        holder.txtl_GeoboundariesArea.setText(strConvertedWord);
                                    }else if ("Address".equals(jsonEngWord)) {
                                        holder.txtl_Address.setText(strConvertedWord);
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
                    rawDataTableListFiltered = plotsdetailsList;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = plotsdetailsList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = plotsdetailsList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetPlotsDetailsByFarmercode> filteredList = new ArrayList<>();
                    for (int i = plotsdetailsList.size(); i >= 1; i--) {
                        filteredList.add(plotsdetailsList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetPlotsDetailsByFarmercode> filteredList = new ArrayList<>();
                    for (GetPlotsDetailsByFarmercode row : plotsdetailsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getPlotCode().toLowerCase().contains(charString.toLowerCase())  || row.getAddress().toLowerCase().contains(charString.toLowerCase())) {
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
                rawDataTableListFiltered = (List<GetPlotsDetailsByFarmercode>) filterResults.values;
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
        public TextView txtPlotCode;
        public TextView txtTypeOfOwnership;
        public TextView txtAreaInHectors;
        public TextView txtGeoboundariesArea;

        public TextView txtAddress;


        public TextView txtl_PlotCode,txtl_TypeOfOwnership,txtl_AreaInHectors,txtl_GeoboundariesArea,txtl_Address;






        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPlotCode = itemView.findViewById(R.id.txtPlotCode_value);
            txtTypeOfOwnership = itemView.findViewById(R.id.txtTypeOfOwnership);
            txtAreaInHectors = itemView.findViewById(R.id.txtAreaInHectors);
            txtGeoboundariesArea = itemView.findViewById(R.id.txtGeoboundariesArea);
            txtAddress = itemView.findViewById(R.id.txtAddress);

            //language
            txtl_PlotCode = itemView.findViewById(R.id.txt_plot_code_n);
            txtl_TypeOfOwnership = itemView.findViewById(R.id.TypeOfOwnership);
            txtl_AreaInHectors = itemView.findViewById(R.id.AreaInHectors);
            txtl_GeoboundariesArea = itemView.findViewById(R.id.GeoboundariesArea);
            txtl_Address = itemView.findViewById(R.id.Address);

        }
    }
}