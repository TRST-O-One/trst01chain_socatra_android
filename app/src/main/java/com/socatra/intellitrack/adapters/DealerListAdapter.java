package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.content.Context;
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
import com.socatra.intellitrack.activity.main_dash_board.DealerProcumentData;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetDealerDetailsbyProcessorId;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DealerListAdapter extends RecyclerView.Adapter<DealerListAdapter.ViewHolder> implements Filterable {

    private List<GetDealerDetailsbyProcessorId> DealerList;
    AppHelper appHelper;
    AppViewModel viewModel;

    String strLanguageId;


    List<GetDealerDetailsbyProcessorId> rawDataTableListFiltered;

    private OnItemClickListener onItemClickListener;
    Context context;

    public DealerListAdapter(Context context,AppHelper appHelper, AppViewModel viewModel, List<GetDealerDetailsbyProcessorId> Dealerslist  ) {

        this.DealerList = Dealerslist;
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
                .inflate(R.layout.dealer_list_individual, parent, false);
        return new DealerListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DealerListAdapter.ViewHolder holder, int position) {

        GetDealerDetailsbyProcessorId item = rawDataTableListFiltered.get(position);
        holder.txtDealerName.setText(item.getDealerName());
        holder.txtAddress.setText(String.valueOf(item.getaddress()));
        holder.txtContactNo.setText(String.valueOf(item.getPrimaryContactNo()));




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String DealerId = String.valueOf(item.getId());
                Intent intent = new Intent(view.getContext(), DealerProcumentData.class);
                intent.putExtra("DealerId", DealerId);
                Log.d("DealerId","Id: " +DealerId);
                view.getContext().startActivity(intent);
            }
        });


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


                                    if ("Dealer Name".equals(jsonEngWord)) {
                                        holder.txtl_Dealer.setText(jsonEngWord);
                                    } else if ("Address".equals(jsonEngWord)) {
                                        holder.txtl_address.setText(jsonEngWord);
                                    } else if ("Contact No".equals(jsonEngWord)) {
                                        holder.txtl_Contact.setText(jsonEngWord);
                                    }

                                    } else {
                                        if ("Dealer Name".equals(jsonEngWord)) {
                                            holder.txtl_Dealer.setText(strConvertedWord);
                                        } else if ("Address".equals(jsonEngWord)) {
                                            holder.txtl_address.setText(strConvertedWord);
                                        } else if ("Contact No".equals(jsonEngWord)) {
                                            holder.txtl_Contact.setText(strConvertedWord);
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
                    rawDataTableListFiltered = DealerList;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = DealerList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = DealerList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetDealerDetailsbyProcessorId> filteredList = new ArrayList<>();
                    for (int i = DealerList.size(); i >= 1; i--) {
                        filteredList.add(DealerList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetDealerDetailsbyProcessorId> filteredList = new ArrayList<>();

                    for (GetDealerDetailsbyProcessorId row : DealerList) {
                        String searchQuery = charString.toLowerCase();

                        String DealerName = row.getDealerName();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (DealerName.toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "GRNnumber: " + row.getDealerName());

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
                rawDataTableListFiltered = (List<GetDealerDetailsbyProcessorId>) filterResults.values;
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

        public TextView txtDealerId;

        public TextView txtAddress;
        public TextView txtContactNo;

        public TextView txtl_Dealer,txtl_address,txtl_Contact;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDealerName = itemView.findViewById(R.id.txtDealerName);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtContactNo = itemView.findViewById(R.id.txtContactNo);

            //language
            txtl_Dealer = itemView.findViewById(R.id.DealerName);
            txtl_address = itemView.findViewById(R.id.Address);
            txtl_Contact = itemView.findViewById(R.id.ContactNo);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(GetDealerDetailsbyProcessorId Dealer);
    }




}
