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
            holder.txtCoordinates.setText(coordinatesLists.get(i).toString());
            holder.txtCoord.setText(i + 1 +".Coordinates");
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
