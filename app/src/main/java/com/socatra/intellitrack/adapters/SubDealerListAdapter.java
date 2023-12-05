package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

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
import com.socatra.intellitrack.models.get.GetDealerDetailsbyProcessorId;
import com.socatra.intellitrack.models.get.GetSubDealerByDealerId;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubDealerListAdapter extends RecyclerView.Adapter<SubDealerListAdapter.ViewHolder> implements Filterable {

    private List<GetSubDealerByDealerId> SubDealerList;

    AppHelper appHelper;
    AppViewModel viewModel;

    String strLanguageId;


    List<GetSubDealerByDealerId> rawDataTableListFiltered;

    private OnItemClickListener onItemClickListener;

    public SubDealerListAdapter(AppHelper appHelper, AppViewModel viewModel, List<GetSubDealerByDealerId> SubDealerList  ) {

        this.SubDealerList = SubDealerList;
        this.rawDataTableListFiltered = SubDealerList;
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
                .inflate(R.layout.sub_dealer_list_individual, parent, false);
        return new SubDealerListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SubDealerListAdapter.ViewHolder holder, int position) {

        GetSubDealerByDealerId item = rawDataTableListFiltered.get(position);
        holder.txtDealerName.setText(item.getSubDealerName());
        holder.txtAddress.setText(String.valueOf(item.getSubDealerAddress()));
        holder.txtContactNo.setText(String.valueOf(item.getSubDealerContact()));

        //changeLangMethod(holder);



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

                                    if ("Sub Dealer Name".equals(jsonEngWord)) {
                                        holder.txtl_DealerName.setText(jsonEngWord);
                                    } else if ("Address".equals(jsonEngWord)) {
                                        holder.txtl_Address.setText(jsonEngWord);
                                    }else if ("Contact No".equals(jsonEngWord)) {
                                        holder.txtl_ContactNo.setText(jsonEngWord);
                                    }



                                } else  {
                                    if ("Sub Dealer Name".equals(jsonEngWord)) {
                                        holder.txtl_DealerName.setText(strConvertedWord);
                                    } else if ("Address".equals(jsonEngWord)) {
                                        holder.txtl_Address.setText(strConvertedWord);
                                    }else if ("Contact No".equals(jsonEngWord)) {
                                        holder.txtl_ContactNo.setText(strConvertedWord);
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
                    rawDataTableListFiltered = SubDealerList;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = SubDealerList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = SubDealerList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetSubDealerByDealerId> filteredList = new ArrayList<>();
                    for (int i = SubDealerList.size(); i >= 1; i--) {
                        filteredList.add(SubDealerList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetSubDealerByDealerId> filteredList = new ArrayList<>();

                    for (GetSubDealerByDealerId row : SubDealerList) {
                        String searchQuery = charString.toLowerCase();

                        String SubDealerName = row.getSubDealerName();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (SubDealerName.toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "SubDealerName: " + row.getSubDealerName());

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
                rawDataTableListFiltered = (List<GetSubDealerByDealerId>) filterResults.values;
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
        public TextView txtDealerName;

        public TextView txtl_DealerName,txtl_Address,txtl_ContactNo;

        public TextView txtAddress;
        public TextView txtContactNo;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDealerName = itemView.findViewById(R.id.txtSubDealerName);
            txtAddress = itemView.findViewById(R.id.txtS_Address);
            txtContactNo = itemView.findViewById(R.id.txtS_ContactNo);

            //language

            txtl_DealerName = itemView.findViewById(R.id.S_DealerName);
            txtl_Address = itemView.findViewById(R.id.S_Address);
            txtl_ContactNo = itemView.findViewById(R.id.S_ContactNo);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(GetDealerDetailsbyProcessorId Dealer);
    }




}