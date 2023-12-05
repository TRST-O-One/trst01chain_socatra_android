package com.socatra.intellitrack.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.intellitrack.R;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetDrcGrnByProcessorId;
import com.socatra.intellitrack.view_models.AppViewModel;

import java.util.List;

public class DrcGrnAdapter extends RecyclerView.Adapter<DrcGrnAdapter.BatchGrnViewHolder> {

    AppHelper appHelper;
    AppViewModel viewModel;
    List<GetDrcGrnByProcessorId> getGrnByDealerIdList;
    Context context;
    SyncCallbackProcessorInterface syncCallbackProcessorInterface;
    public DrcGrnAdapter(AppHelper appHelper, AppViewModel viewModel, List<GetDrcGrnByProcessorId> getGrnByDealerIdList, SyncCallbackProcessorInterface syncCallbackProcessorInterface) {
        this.appHelper = appHelper;
        this.viewModel = viewModel;
        this.getGrnByDealerIdList = getGrnByDealerIdList;
        this.syncCallbackProcessorInterface = syncCallbackProcessorInterface;

    }

    @NonNull
    @Override
    public BatchGrnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BatchGrnViewHolder(LayoutInflater.from(context).inflate(R.layout.drc_individual_adapter, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BatchGrnViewHolder holder, int position) {
        GetDrcGrnByProcessorId getDrcGrnByProcessorId = getGrnByDealerIdList.get(position);
        holder.txtgrn.setText(getGrnByDealerIdList.get(position).getGRNnumber().toString());
        holder.txtactualquantity.setText(String.valueOf(getGrnByDealerIdList.get(position).getQuantity()));
        holder.txtDRC.setText(String.valueOf(getGrnByDealerIdList.get(position).getDRCValue()));


        // Add a click listener to the delete button
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                syncCallbackProcessorInterface.removeProcessorPosition(clickedPosition,getDrcGrnByProcessorId,getGrnByDealerIdList);

//                int clickedPosition = holder.getAdapterPosition();
//                if (clickedPosition != RecyclerView.NO_POSITION) {
//                    // Handle the delete button click here, for example, by removing the item from the list
//                    getGrnByDealerIdList.remove(clickedPosition);
//                    notifyItemRemoved(clickedPosition); // Refresh the RecyclerView
//                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return getGrnByDealerIdList.size();
    }

    public static class BatchGrnViewHolder extends RecyclerView.ViewHolder {

        TextView txtgrn, txtactualquantity , txtDRC;
        CardView cardgrndtldetails;
        ImageView btnDelete;

        public BatchGrnViewHolder(@NonNull View itemView) {
            super(itemView);

            txtgrn = itemView.findViewById(R.id.txtGrnNoNamegrnadpt);
            txtactualquantity = itemView.findViewById(R.id.txtActualQuantitygrnadpt);
            txtDRC = itemView.findViewById(R.id.txtDRCgrnadpt);


            cardgrndtldetails = itemView.findViewById(R.id.cardgrndtldetails);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface SyncCallbackProcessorInterface {
        void removeProcessorPosition(int position, GetDrcGrnByProcessorId getGrnByProcessorId, List<GetDrcGrnByProcessorId> getGrnByProcessorIdList);
    }
}
