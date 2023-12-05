package com.socatra.excutivechain.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.excutivechain.R;

import java.util.List;

public class CoordinatesKmlAdapter   extends RecyclerView.Adapter<CoordinatesKmlAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
    List<String> coordinatesLists;
    int lastCheckedPosition = -1;

    public CoordinatesKmlAdapter(Context context, List<String> coordinatesLists) {
        this.layoutInflater = LayoutInflater.from(context);
        this.coordinatesLists = coordinatesLists;
        this.context =context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.coordinate_kml_ada, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int i) {
        try {
//            if (coordinatesLists.size()>0) {
            //       holder.txtCoordinates.setText(coordinatesLists.get(coordinatesLists.size() - 1).toString());

//                for (i=0;i<=coordinatesLists.size()-1; i++){

          //  if (coordinatesLists.get(0).toString() .equals(coordinatesLists.size()))
            holder.txtCoordinates.setText(coordinatesLists.get(i).toString());
            holder.txtCoord.setText(String.valueOf(i+1)+".Coordinates");

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
        System.out.println(coordinatesLists.size());
        return coordinatesLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCoordinates,txtCoord;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCoordinates = itemView.findViewById(R.id.txtCoordinatesKmlAdp);
            txtCoord = itemView.findViewById(R.id.txtCKml);
        }
    }

}
