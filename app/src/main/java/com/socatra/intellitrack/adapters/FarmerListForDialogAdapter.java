package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetFarmerbyDealerid;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmerListForDialogAdapter extends RecyclerView.Adapter<FarmerListForDialogAdapter.FarmerViewHolder> implements Filterable {

    List<GetFarmerbyDealerid> rawDataTableListFiltered;
    List<GetFarmerbyDealerid> farmerList;
    String strType;
    String deviceRoleName;

    String strLanguageId;
    SyncCallbackInterface syncCallbackInterface;
    private Context context;
    private AppHelper appHelper;
    private AppViewModel viewModel;

    public FarmerListForDialogAdapter(Context context, SyncCallbackInterface syncCallbackInterface, AppHelper appHelper, AppViewModel viewModel, List<GetFarmerbyDealerid> farmerList, String key) {

        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        this.appHelper = appHelper;
        this.context =context;
        this.rawDataTableListFiltered = farmerList;
        this.viewModel = viewModel;
        this.farmerList = farmerList;
        this.syncCallbackInterface = syncCallbackInterface;
        this.strType = key;

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");

    }

    @NonNull
    @Override
    public FarmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_farmer_individual, parent, false);
        return new FarmerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerViewHolder holder, int position) {
        GetFarmerbyDealerid farmer = rawDataTableListFiltered.get(position);
        GetFarmerbyDealerid getGrnByDealerIdTable = rawDataTableListFiltered.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                syncCallbackInterface.selectPosition(clickedPosition, getGrnByDealerIdTable, rawDataTableListFiltered);
            }
        });
        holder.cd_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                syncCallbackInterface.selectPosition(clickedPosition, getGrnByDealerIdTable, rawDataTableListFiltered);
            }
        });

        holder.txtFarmerName.setText(farmer.getFarmerName());
        holder.txtFarmerCode.setText(farmer.getFarmerCode());


        changeLangMethod(holder);





//        holder.ll_Mobile_no.setVisibility(View.GONE);
//        holder.ll_Village.setVisibility(View.GONE);
//        holder.ll_pin.setVisibility(View.GONE);
//        holder.txtMobileNumber.setText(farmer.getPrimaryContactNo());
//
//        holder.txtAddress.setText(farmer.getAddress());
//        holder.txtVillage.setText(farmer.getVillageName());
//        holder.txtPin.setText(farmer.getPinCode());
    }

    private void changeLangMethod(FarmerViewHolder holder) {
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



                                    if ("Farmer Name".equals(jsonEngWord)) {
                                        holder.txtl_FarmerName.setText(jsonEngWord);
                                    } else if ("Farmer Code".equals(jsonEngWord)) {
                                        holder.txtl_FarmerCode.setText(jsonEngWord);
                                    }





                                } else  {
                                    if ("Farmer Name".equals(jsonEngWord)) {
                                        holder.txtl_FarmerName.setText(strConvertedWord);
                                    } else if ("Farmer Code".equals(jsonEngWord)) {
                                        holder.txtl_FarmerCode.setText(strConvertedWord);
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
                    rawDataTableListFiltered = farmerList;
                } else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = farmerList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = farmerList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetFarmerbyDealerid> filteredList = new ArrayList<>();
                    for (int i = farmerList.size(); i >= 1; i--) {
                        filteredList.add(farmerList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetFarmerbyDealerid> filteredList = new ArrayList<>();
                    for (GetFarmerbyDealerid row : farmerList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFarmerName().toLowerCase().contains(charString.toLowerCase()) || row.getFarmerCode().toLowerCase().contains(charString.toLowerCase())) {
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
                rawDataTableListFiltered = (List<GetFarmerbyDealerid>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return rawDataTableListFiltered.size();
    }


    public interface SyncCallbackInterface {
        void selectPosition(int position, GetFarmerbyDealerid getGrnByDealerId, List<GetFarmerbyDealerid> getGrnByDealerIdList);
    }

    public class FarmerViewHolder extends RecyclerView.ViewHolder {
        TextView txtFarmerCode;
        TextView txtFarmerName;
        TextView txtMobileNumber;
        TextView txtAddress;
        TextView txtVillage;
        TextView txtPin;

        LinearLayout ll_farmername, ll_Mobile_no, ll_Village, ll_pin;
        CardView cd_dialog;

        TextView txtl_FarmerCode,txtl_FarmerName;

        public FarmerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFarmerCode = itemView.findViewById(R.id.txtFarmerCode_dig);
            txtFarmerName = itemView.findViewById(R.id.txtFarmerName_dig);
            txtMobileNumber = itemView.findViewById(R.id.txtmobilenumber);
            txtAddress = itemView.findViewById(R.id.txtaddress);
            txtVillage = itemView.findViewById(R.id.txtvillage);
            txtPin = itemView.findViewById(R.id.txtpin);
            ll_farmername = itemView.findViewById(R.id.ll_farmername);
            ll_Mobile_no = itemView.findViewById(R.id.ll_mobile_no);
            ll_Village = itemView.findViewById(R.id.ll_village);
            ll_pin = itemView.findViewById(R.id.ll_pin);
            cd_dialog = itemView.findViewById(R.id.card_dialog);


            //language
            txtl_FarmerCode = itemView.findViewById(R.id.farmercode);
            txtl_FarmerName = itemView.findViewById(R.id.FarmerName);

        }
    }
}
