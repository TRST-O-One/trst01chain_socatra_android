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
import com.socatra.intellitrack.models.get.GetGrnByProcessorId;
import com.socatra.intellitrack.view_models.AppViewModel;

import java.util.List;

public class BatchGrnProcessorAdapter extends RecyclerView.Adapter<BatchGrnProcessorAdapter.BatchGrnViewHolder> {

    AppHelper appHelper;
    AppViewModel viewModel;
    List<GetGrnByProcessorId> processorIds;
    Context context;

    SyncCallbackProcessorInterface syncCallbackProcessorInterface;

    public BatchGrnProcessorAdapter(Context context,SyncCallbackProcessorInterface syncCallbackProcessorInterface,AppHelper appHelper, AppViewModel viewModel, List<GetGrnByProcessorId> processorIds) {
        this.appHelper = appHelper;
        this.viewModel = viewModel;
        this.processorIds = processorIds;
        this.context =context;
        this.syncCallbackProcessorInterface = syncCallbackProcessorInterface;
    }

    @NonNull
    @Override
    public BatchGrnViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new BatchGrnViewHolder(LayoutInflater.from(context).inflate(R.layout.grn_individual_adapter, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BatchGrnViewHolder holder, int position) {
        GetGrnByProcessorId getGrnByProcessorId = processorIds.get(position);
        holder.txtgrn.setText(processorIds.get(position).getGRNnumber().toString());
        holder.txtactualquantity.setText(String.valueOf(processorIds.get(position).getQuantity()));

        // Add a click listener to the delete button
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                syncCallbackProcessorInterface.removeProcessorPosition(clickedPosition,getGrnByProcessorId,processorIds);

//                if (clickedPosition != RecyclerView.NO_POSITION) {
//                    // Handle the delete button click here, for example, by removing the item from the list
//                    processorIds.remove(clickedPosition);
//                    notifyItemRemoved(clickedPosition); // Refresh the RecyclerView
//                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return processorIds.size();
    }

    public static class BatchGrnViewHolder extends RecyclerView.ViewHolder {

        TextView txtgrn, txtactualquantity;
        CardView cardgrndtldetails;
        ImageView btnDelete;

        public BatchGrnViewHolder(@NonNull View itemView) {
            super(itemView);

            txtgrn = itemView.findViewById(R.id.txtGrnNoNamegrnadpt);
            txtactualquantity = itemView.findViewById(R.id.txtActualQuantitygrnadpt);
            cardgrndtldetails = itemView.findViewById(R.id.cardgrndtldetails);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface SyncCallbackProcessorInterface {
        void removeProcessorPosition(int position, GetGrnByProcessorId getGrnByProcessorId, List<GetGrnByProcessorId> getGrnByProcessorIdList);
    }
}
