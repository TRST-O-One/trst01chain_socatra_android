package com.socatra.excutivechain.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.socatra.excutivechain.R;

import java.util.List;

public class CoordinatesAdapter extends RecyclerView.Adapter<CoordinatesAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    List<LatLng> coordinatesLists;
    int lastCheckedPosition = -1;

    public CoordinatesAdapter(Context context, List<LatLng> coordinatesLists) {
        this.layoutInflater = LayoutInflater.from(context);
        this.coordinatesLists = coordinatesLists;
        this.context =context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.coordinate_card, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int i) {
        try {
//            if (coordinatesLists.size()>0) {
         //       holder.txtCoordinates.setText(coordinatesLists.get(coordinatesLists.size() - 1).toString());

//                for (i=0;i<=coordinatesLists.size()-1; i++){
                    holder.txtCoordinates.setText(coordinatesLists.get(i).toString());
                    holder.txtC.setText("coordinatesLists" + i);
//                }
//                holder.txtCoordinates.setText(coordinatesLists.get(coordinatesLists.size()-1));
//            }else {
//                Toast.makeText(context, "Please add coordinates from map", Toast.LENGTH_SHORT).show();
//            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
//        return 6;
//        System.out.println(coordinatesLists.size());
        return coordinatesLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCoordinates,txtC;
        ImageView imgNotification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCoordinates = itemView.findViewById(R.id.txtCoordinates);
            txtC = itemView.findViewById(R.id.txtC);

        }
    }

}





