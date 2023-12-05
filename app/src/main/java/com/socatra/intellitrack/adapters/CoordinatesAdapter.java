package com.socatra.intellitrack.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.intellitrack.R;
import com.socatra.intellitrack.models.get.GetLatLongListfromPlot;

import java.util.List;

public class CoordinatesAdapter extends RecyclerView.Adapter<CoordinatesAdapter.ViewHolder> {

    private Context context;
    TextView txtCoordinateslat;
    private List<GetLatLongListfromPlot> latLngList;

    public CoordinatesAdapter(Context context, List<GetLatLongListfromPlot> latLngList) {
        this.context = context;
        this.latLngList = latLngList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coordinate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetLatLongListfromPlot  latLng = latLngList.get(position);
        Log.d("CoordinatesAdapter", "onBindViewHolder: latLng = " + latLng);
        holder.bindData(latLng);
    }

    @Override
    public int getItemCount() {
        return latLngList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtCoordinates;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCoordinates = itemView.findViewById(R.id.txtCoordinateslat);
        }

        public void bindData(GetLatLongListfromPlot latLng) {
            // Retrieve the latLong string and split it into latitude and longitude
            String latLong = latLng.getLatLong();
            String[] latLngParts = latLong.split(",");

            if (latLngParts.length == 2) {
                String latitude = latLngParts[0].trim();
                String longitude = latLngParts[1].trim();

                // Set the TextView with the formatted text
                String coordinateText = "Latitude: " + latitude + ", Longitude: " + longitude;
                txtCoordinates.setText(coordinateText);
            } else {
                // Handle the case where the latLong string is not in the expected format
                txtCoordinates.setText("Invalid coordinates");
            }
        }
    }
}
