package com.socatra.excutivechain.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.excutivechain.R;

public class DocsAdapter extends RecyclerView.Adapter<DocsAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater layoutInflater;
//    List<DocIdentiFicationDeatilsTable> coordinatesLists;
    int lastCheckedPosition = -1;

    public DocsAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
//        this.coordinatesLists = coordinatesLists;
        this.context =context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.plot_individual, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int i) {
        try {
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context, PlotDetailsActivity.class);
//                    context.startActivity(intent);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 6;
//        System.out.println(coordinatesLists.size());
//        return coordinatesLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        TextView txtImageType;
//        ImageView img_picture_passbook;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card);
//            img_picture_passbook = itemView.findViewById(R.id.img_picture_passbook);

        }
    }

}





