package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.activity.grnflow.FarmerGrnDetailsList;
import com.socatra.intellitrack.activity.grnflow.GrnFarmerMapActivity;
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

public class FarmerByDealerIdAdapter extends RecyclerView.Adapter<FarmerByDealerIdAdapter.FarmerViewHolder> implements Filterable {

    List<GetFarmerbyDealerid> rawDataTableListFiltered;
    List<GetFarmerbyDealerid> farmerList;
    String strType;
    private Context context;

    String deviceRoleName;
    private OnItemClickListener itemClickListener;
    private AppHelper appHelper;
    private AppViewModel viewModel;

    String strLanguageId;

    public FarmerByDealerIdAdapter(AppHelper appHelper, AppViewModel viewModel, List<GetFarmerbyDealerid> farmerList, String key) {

        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");


        this.appHelper = appHelper;
        this.rawDataTableListFiltered = farmerList;
        this.viewModel = viewModel;
        this.farmerList = farmerList;
        this.itemClickListener = itemClickListener;
        this.strType = key;


    }

    @NonNull
    @Override
    public FarmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farmer_individual, parent, false);
        return new FarmerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerViewHolder holder, int position) {
        GetFarmerbyDealerid farmer = rawDataTableListFiltered.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(farmer);
                }
            }
        });
        holder.txtFarmerCode.setText(farmer.getFarmerCode());
        holder.txtFarmerName.setText(farmer.getFarmerName());
        holder.txtMobileNumber.setText(farmer.getPrimaryContactNo());
        holder.txtAddress.setText(farmer.getAddress());
        holder.txtVillage.setText(farmer.getVillageName());
        holder.txtPin.setText(farmer.getPinCode());

        if (deviceRoleName.equalsIgnoreCase("Trader")) {

            holder.ll_farmername.setVisibility(View.GONE);
            holder.ll_Mobile_no.setVisibility(View.GONE);
            holder.ll_Village.setVisibility(View.GONE);
            holder.ll_pin.setVisibility(View.GONE);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FarmerCode = String.valueOf(farmer.getFarmerCode());
                String FarmerName = String.valueOf(farmer.getFarmerName());
                String VillageName = String.valueOf(farmer.getVillageName());


                if (strType.equals("map")) {

                    Intent intent = new Intent(view.getContext(), GrnFarmerMapActivity.class);
                    intent.putExtra("farmercode", FarmerCode);
                    view.getContext().startActivity(intent);

                } else {
                    Intent intent = new Intent(view.getContext(), FarmerGrnDetailsList.class);
                    intent.putExtra("FarmerCode", FarmerCode);
                    intent.putExtra("FarmerName", FarmerName);
                    intent.putExtra("VillageName", VillageName);

                    Log.d("FarmerCode", "Code: " + FarmerCode);
                    Log.d("FarmerName", "Name: " + FarmerName);

                    view.getContext().startActivity(intent);


                }

            }
        });


       changeLangMethod(holder);

















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
                                    if ("FarmerCode".equals(jsonEngWord)) {
                                        holder.txtl_farmercode.setText(jsonEngWord);
                                    } else if ("Farmer Name".equals(jsonEngWord)) {
                                        holder.txtl_farmername.setText(jsonEngWord);
                                    }else if ("Mobile No".equals(jsonEngWord)) {
                                        holder.txtl_mobilenumber.setText(jsonEngWord);
                                    }else if ("Address".equals(jsonEngWord)) {
                                        holder.txtl_address.setText(jsonEngWord);
                                    }else if ("Village".equals(jsonEngWord)) {
                                        holder.txtl_village.setText(jsonEngWord);
                                    }else if ("Pincode".equals(jsonEngWord)) {
                                        holder.txtl_pin.setText(jsonEngWord);
                                    }

                                } else  {
                                    if ("FarmerCode".equals(jsonEngWord)) {
                                        holder.txtl_farmercode.setText(strConvertedWord);
                                    } else if ("Farmer Name".equals(jsonEngWord)) {
                                        holder.txtl_farmername.setText(strConvertedWord);
                                    }else if ("Mobile No".equals(jsonEngWord)) {
                                        holder.txtl_mobilenumber.setText(strConvertedWord);
                                    }else if ("Address".equals(jsonEngWord)) {
                                        holder.txtl_address.setText(strConvertedWord);
                                    }else if ("Village".equals(jsonEngWord)) {
                                        holder.txtl_village.setText(strConvertedWord);
                                    }else if ("Pincode".equals(jsonEngWord)) {
                                        holder.txtl_pin.setText(strConvertedWord);
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
        if (rawDataTableListFiltered != null) {
            return rawDataTableListFiltered.size();
        } else {
            return 0;
        }

    }


    public interface OnItemClickListener {
        void onClick(View view);

        void onItemClick(GetFarmerbyDealerid farmer);

    }

    public class FarmerViewHolder extends RecyclerView.ViewHolder {
        TextView txtFarmerCode;
        TextView txtFarmerName;
        TextView txtMobileNumber;
        TextView txtAddress;
        TextView txtVillage;
        TextView txtPin;

        LinearLayout ll_farmername , ll_Mobile_no,ll_Village,ll_pin;

        TextView txtl_farmercode,txtl_farmername,txtl_mobilenumber,txtl_address,txtl_village,txtl_pin;

        public FarmerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFarmerCode = itemView.findViewById(R.id.txtFarmerCode);
            txtFarmerName = itemView.findViewById(R.id.txtFarmerName);
            txtMobileNumber = itemView.findViewById(R.id.txtmobilenumber);
            txtAddress = itemView.findViewById(R.id.txtaddress);
            txtVillage = itemView.findViewById(R.id.txtvillage);
            txtPin = itemView.findViewById(R.id.txtpin);
            ll_farmername = itemView.findViewById(R.id.ll_farmername);
            ll_Mobile_no = itemView.findViewById(R.id.ll_mobile_no);
            ll_Village = itemView.findViewById(R.id.ll_village);
            ll_pin = itemView.findViewById(R.id.ll_pin);

            //Language
            txtl_farmercode = itemView.findViewById(R.id.farmercode);
            txtl_farmername = itemView.findViewById(R.id.FarmerName);
            txtl_mobilenumber = itemView.findViewById(R.id.mobilenumber);
            txtl_address = itemView.findViewById(R.id.address);
            txtl_village = itemView.findViewById(R.id.village);
            txtl_pin = itemView.findViewById(R.id.pin);

        }
    }
}
