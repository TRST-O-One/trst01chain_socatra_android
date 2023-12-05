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
import com.socatra.intellitrack.activity.grnflow.GrnFarmerMapActivity;
import com.socatra.intellitrack.activity.grnflow.ShareGRNActivity;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GrnByFarmercode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrnByFarmercodeAdapter extends RecyclerView.Adapter<GrnByFarmercodeAdapter.ViewHolder> implements Filterable {
    List<GrnByFarmercode> rawDataTableListFiltered;
    private List<GrnByFarmercode> grnList;

    private AppHelper appHelper;

    String strLanguageId;


    public GrnByFarmercodeAdapter(AppHelper appHelper,List<GrnByFarmercode> grnList) {
        this.grnList = grnList;
        this.appHelper = appHelper;

        this.rawDataTableListFiltered = grnList;

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");


        // Corrected assignment
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grn_individual, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GrnByFarmercode item = rawDataTableListFiltered.get(position);

        holder.txtGrnNumber.setText(item.getGRNnumber());

        holder.txtQuantity.setText(String.valueOf(item.getQuantity()));
        holder.txtCurQuantity.setText(String.valueOf(item.getCurrentQty()));

        if (item.getDRCValue() == null) {
            holder.txtDRC.setText("Not available");
        } else {
            holder.txtDRC.setText(String.valueOf(item.getDRCValue()));
        }

//        holder.txtGrndate.setText(item.getGRNdate());
        String grnDate = item.getGRNdate().split("T")[0]; // Get the date part before "T"
        holder.txtGrndate.setText(grnDate);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String DealerId = String.valueOf(item.getId());
                String GRNNumber = String.valueOf(item.getGRNnumber());
                String FarmerName = String.valueOf(item.getFarmerName());
                String Address = String.valueOf(item.getFarmeradress());
                String GRNDate = String.valueOf(item.getGRNdate().split("T")[0]);
                String Quantity = String.valueOf(item.getQuantity());
                String ContactNo = String.valueOf(item.getPrimaryContactNo());
                String GRNDocument = item.getGRNDocument();


                Intent intent = new Intent(view.getContext(), ShareGRNActivity.class);

                intent.putExtra("key", "farmer");
                intent.putExtra("DealerId", DealerId);
                Log.d("DealerId", "Id: " + DealerId);

                intent.putExtra("GRNNumber", GRNNumber);
                Log.d("GRNNumber", "GRNNumber: " + GRNNumber);


                intent.putExtra("FarmerName", FarmerName);
                Log.d("FarmerName", "FarmerName: " + FarmerName);

                intent.putExtra("Farmeradress", Address);
                Log.d("Address", "Address: " + Address);


                intent.putExtra("Quantity", Quantity);
                Log.d("Quantity", "Quantity: " + Quantity);

                intent.putExtra("ContactNo", ContactNo);
                Log.d("ContactNo", "ContactNo: " + ContactNo);
                intent.putExtra("GRNDate", GRNDate);
                Log.d("GRNDate", "GRNDate: " + GRNDate);
                intent.putExtra("GRNDate", GRNDate);
                Log.d("GRNDate", "GRNDate: " + GRNDate);
                intent.putExtra("GRNDocument", GRNDocument);
                Log.d("GRNDocument", "GRNDocument: " + GRNDocument);
                view.getContext().startActivity(intent);
            }
        });

        holder.txtViewInovice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String DealerId = String.valueOf(item.getId());
                String GRNNumber = String.valueOf(item.getGRNnumber());
                String FarmerName = String.valueOf(item.getFarmerName());
                String Address = String.valueOf(item.getFarmeradress());
                String GRNDate = String.valueOf(item.getGRNdate().split("T")[0]);
                String Quantity = String.valueOf(item.getQuantity());
                String ContactNo = String.valueOf(item.getPrimaryContactNo());
                String GRNDocument = item.getGRNDocument();


                Intent intent = new Intent(view.getContext(), ShareGRNActivity.class);

                intent.putExtra("key", "farmer");
                intent.putExtra("DealerId", DealerId);
                Log.d("DealerId", "Id: " + DealerId);

                intent.putExtra("GRNNumber", GRNNumber);
                Log.d("GRNNumber", "GRNNumber: " + GRNNumber);


                intent.putExtra("FarmerName", FarmerName);
                Log.d("FarmerName", "FarmerName: " + FarmerName);

                intent.putExtra("Farmeradress", Address);
                Log.d("Address", "Address: " + Address);


                intent.putExtra("Quantity", Quantity);
                Log.d("Quantity", "Quantity: " + Quantity);

                intent.putExtra("ContactNo", ContactNo);
                Log.d("ContactNo", "ContactNo: " + ContactNo);
                intent.putExtra("GRNDate", GRNDate);
                Log.d("GRNDate", "GRNDate: " + GRNDate);
                intent.putExtra("GRNDate", GRNDate);
                Log.d("GRNDate", "GRNDate: " + GRNDate);
                intent.putExtra("GRNDocument", GRNDocument);
                Log.d("GRNDocument", "GRNDocument: " + GRNDocument);
                view.getContext().startActivity(intent);
            }
        });


        holder.txtMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strFarmerCode = String.valueOf(item.getFarmerCode());
                Intent intent = new Intent(v.getContext(), GrnFarmerMapActivity.class);
                intent.putExtra("farmercode", strFarmerCode);
                v.getContext().startActivity(intent);
            }
        });

        changLangeMethod(holder);






//        holder.itemView.setTag(position);
    }

    private void changLangeMethod(ViewHolder holder) {

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


                                    if ("GRN Number".equals(jsonEngWord)) {
                                        holder.txtl_Grnnumber.setText(jsonEngWord);
                                    } else if ("Quantity".equals(jsonEngWord)) {
                                        holder.txtl_Quantity.setText(jsonEngWord);
                                    }else if ("Current Quantity".equals(jsonEngWord)) {
                                        holder.txtCurl_Quantity.setText(jsonEngWord);
                                    }else if ("GRN Date".equals(jsonEngWord)) {
                                        holder.txtl_GrnDate.setText(jsonEngWord);
                                    }else if ("DRC Value".equals(jsonEngWord)) {
                                        holder.txtl_DRCValue.setText(jsonEngWord);
                                    }else if ("Origin".equals(jsonEngWord)) {
                                        holder.txtMapView.setText(jsonEngWord);
                                    }else if ("View".equals(jsonEngWord)) {
                                        holder.txtViewInovice.setText(jsonEngWord);
                                    }





                                } else  {
                                    if ("GRN Number".equals(jsonEngWord)) {
                                        holder.txtl_Grnnumber.setText(strConvertedWord);
                                    } else if ("Quantity".equals(jsonEngWord)) {
                                        holder.txtl_Quantity.setText(strConvertedWord);
                                    }else if ("Current Quantity".equals(jsonEngWord)) {
                                        holder.txtCurl_Quantity.setText(strConvertedWord);
                                    }else if ("GRN Date".equals(jsonEngWord)) {
                                        holder.txtl_GrnDate.setText(strConvertedWord);
                                    }else if ("DRC Value".equals(jsonEngWord)) {
                                        holder.txtl_DRCValue.setText(strConvertedWord);
                                    }else if ("Origin".equals(jsonEngWord)) {
                                        holder.txtMapView.setText(strConvertedWord);
                                    }else if ("View".equals(jsonEngWord)) {
                                        holder.txtViewInovice.setText(strConvertedWord);
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
                    rawDataTableListFiltered = grnList;
                } else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = grnList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = grnList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GrnByFarmercode> filteredList = new ArrayList<>();
                    for (int i = grnList.size(); i >= 1; i--) {
                        filteredList.add(grnList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GrnByFarmercode> filteredList = new ArrayList<>();

                    for (GrnByFarmercode row : grnList) {
                        String searchQuery = charString.toLowerCase();

                        String grnNumber = row.getGRNnumber();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (grnNumber.toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "GRNnumber: " + row.getGRNnumber());

                            filteredList.add(row);
                        } else {

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
                rawDataTableListFiltered = (List<GrnByFarmercode>) filterResults.values;
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
        public TextView txtGrnNumber;
        public TextView txtViewInovice,txtMapView;
        public TextView txtFarmerName;
        public TextView txtQuantity;
        public TextView txtCurQuantity;
        public TextView txtGrndate;

        public TextView txtDRC;

        public TextView txtl_Grnnumber,txtl_Quantity,txtCurl_Quantity,txtl_GrnDate,txtl_DRCValue,txtl_view,txtl_origin;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtGrnNumber = itemView.findViewById(R.id.txtGrnnumber);

            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtCurQuantity = itemView.findViewById(R.id.txtCurQuantity);
            txtGrndate = itemView.findViewById(R.id.txtGrndate);
            txtDRC = itemView.findViewById(R.id.txtDRC);

            // Initialize other views here


            //language
            txtl_Grnnumber = itemView.findViewById(R.id.Grnnumber);
            txtl_Quantity = itemView.findViewById(R.id.Quantity);
            txtCurl_Quantity = itemView.findViewById(R.id.CurQuantity);
            txtl_GrnDate = itemView.findViewById(R.id.Grndate);
            txtl_DRCValue = itemView.findViewById(R.id.Grnpercentage);
            txtMapView = itemView.findViewById(R.id.txt_origin);
            txtViewInovice = itemView.findViewById(R.id.txt_view);

        }
    }
}
