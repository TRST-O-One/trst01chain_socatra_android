package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;
import static com.socatra.intellitrack.constants.AppConstant.LanguageId;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetLatLongListfromBatchId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BatchCoordinatesAdapter extends RecyclerView.Adapter<BatchCoordinatesAdapter.ViewHolder> {

    private Context context;
    TextView txtCoordinateslat;

    String strLanguageId;

    private AppHelper appHelper;
    private List<GetLatLongListfromBatchId> latLngList;

    String strLatWord,strLangWord;

    public BatchCoordinatesAdapter(AppHelper appHelper,Context context, List<GetLatLongListfromBatchId> latLngList) {
        this.context = context;
        this.latLngList = latLngList;
        this.appHelper = appHelper;

        strLanguageId = appHelper.getSharedPrefObj().getString(LanguageId, "");

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coordinate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetLatLongListfromBatchId  latLng = latLngList.get(position);
        Log.d("CoordinatesAdapter", "onBindViewHolder: latLng = " + latLng);
        strLatWord = "latitude";
        strLangWord = "longitude";


       // ChangeLangMethod(holder);




        holder.bindData(latLng);






    }


    public void ChangeLangMethod(ViewHolder holder)
    {
        final AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("loading  data...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        Call<JsonElement> callRetrofit = null;


        callRetrofit = service.getTransalteLanguageWordsByLanguageIdFromServer(strLanguageId,appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    progressDialog.dismiss();
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

                                    if ("Coordinates".equals(jsonEngWord)) {
                                        holder.txtl_Coordinates.setText(jsonEngWord);
                                    }else if (context.getResources().getString(R.string.lat).equals(jsonEngWord))
                                    {
                                        strLatWord = jsonEngWord;
                                    }else if (context.getResources().getString(R.string.lang).equals(jsonEngWord))
                                    {
                                        strLangWord = jsonEngWord;
                                    }
                                } else {
                                    if ("Coordinates".equals(jsonEngWord)) {
                                        holder.txtl_Coordinates.setText(strConvertedWord);
                                    }else if (context.getResources().getString(R.string.lat).equals(jsonEngWord))
                                    {
                                        strLatWord = strConvertedWord;
                                    }else if (context.getResources().getString(R.string.lang).equals(jsonEngWord))
                                    {
                                        strLangWord = strConvertedWord;
                                    }

                                }
                            }


                        } catch (Exception ex) {
                            progressDialog.dismiss();
                            ex.printStackTrace();

                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        progressDialog.dismiss();
                    }
                } catch (Exception ex) {
                    progressDialog.dismiss();
                    ex.printStackTrace();


                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                progressDialog.dismiss();


                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return latLngList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtCoordinates;

        private TextView txtl_Coordinates;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCoordinates = itemView.findViewById(R.id.txtCoordinateslat);

            //language
            txtl_Coordinates = itemView.findViewById(R.id.txtCoordTxtKmlAdp);

        }

        public void bindData(GetLatLongListfromBatchId latLng) {
            // Retrieve the latLong string and split it into latitude and longitude
            String latLong = latLng.getLatLong();
            String[] latLngParts = latLong.split(",");

            if (latLngParts.length == 2) {
                String latitude = latLngParts[0].trim();
                String longitude = latLngParts[1].trim();

                // Set the TextView with the formatted text
                String coordinateText = strLatWord +" : " + latitude +"," + strLangWord + " : " + longitude;
                txtCoordinates.setText(coordinateText);
            } else {
                // Handle the case where the latLong string is not in the expected format
                txtCoordinates.setText("Invalid coordinates");
            }
        }
    }
}
