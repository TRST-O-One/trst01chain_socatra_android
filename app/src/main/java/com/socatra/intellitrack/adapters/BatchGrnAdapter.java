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
import com.socatra.intellitrack.models.get.GetGrnByDealerId;
import com.socatra.intellitrack.view_models.AppViewModel;

import java.util.List;

public class BatchGrnAdapter extends RecyclerView.Adapter<BatchGrnAdapter.BatchGrnViewHolder> {

    AppHelper appHelper;
    AppViewModel viewModel;
    List<GetGrnByDealerId> getGrnByDealerIdList;
    Context context;
    SyncCallbackInterface syncCallbackInterface;


    public BatchGrnAdapter(Context context, SyncCallbackInterface syncCallbackInterface, AppHelper appHelper, AppViewModel viewModel, List<GetGrnByDealerId> getGrnByDealerIdList) {
        this.appHelper = appHelper;
        this.viewModel = viewModel;
        this.context = context;
        this.getGrnByDealerIdList = getGrnByDealerIdList;
        this.syncCallbackInterface = syncCallbackInterface;
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
        GetGrnByDealerId getGrnByDealerIdTable = getGrnByDealerIdList.get(position);

        holder.txtgrn.setText(getGrnByDealerIdList.get(position).getGRNnumber().toString());
        holder.txtactualquantity.setText(getGrnByDealerIdList.get(position).getQuantity().toString());

        // Add a click listener to the delete button
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                syncCallbackInterface.removePosition(clickedPosition,getGrnByDealerIdTable,getGrnByDealerIdList);
//                if (clickedPosition != RecyclerView.NO_POSITION) {
//                    // Handle the delete button click here, for example, by removing the item from the list
//                    getGrnByDealerIdList.remove(clickedPosition);
//                    notifyItemRemoved(clickedPosition); // Refresh the RecyclerView
//                }
            }
        });

//        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int clickedPosition = holder.getAdapterPosition();
//                if (clickedPosition != RecyclerView.NO_POSITION) {
//                    // Handle the delete button click here, for example, by removing the item from the list
//                    GetGrnByDealerId removedItem = getGrnByDealerIdList.remove(clickedPosition);
//                    notifyItemRemoved(clickedPosition); // Refresh the RecyclerView
//
//                    // Add the removed item back to the filteredGrnList
//                    filteredGrnList.add(removedItem);
//
//                    // Update the Spinner's adapter to reflect the changes
//                    ArrayAdapter<GetGrnByDealerId> dataAdapterCont = new ArrayAdapter<>(BatchProcessingActivity.this,
//                            android.R.layout.simple_spinner_item, filteredGrnList);
//                    dataAdapterCont.setDropDownViewResource(android.R.layout.simple_spinner_item);
//                    spGrnNo.setAdapter(dataAdapterCont);
//                }
//            }
//        });


    }


    @Override
    public int getItemCount() {
        return getGrnByDealerIdList.size();
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
    public interface SyncCallbackInterface {
        void removePosition(int position, GetGrnByDealerId getGrnByDealerId, List<GetGrnByDealerId> getGrnByDealerIdList);
    }
}
