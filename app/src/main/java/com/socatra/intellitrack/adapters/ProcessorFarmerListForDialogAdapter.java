package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.intellitrack.R;
import com.socatra.intellitrack.database.entity.GetFarmerListFromServerTable;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProcessorFarmerListForDialogAdapter extends RecyclerView.Adapter<ProcessorFarmerListForDialogAdapter.FarmerViewHolder> implements Filterable {

    List<GetFarmerListFromServerTable> rawDataTableListFiltered;
    List<GetFarmerListFromServerTable> farmerList;
    String strType;
    String deviceRoleName;
    SyncCallbackInterface syncCallbackInterface;
    private Context context;
    private AppHelper appHelper;
    private AppViewModel viewModel;

    public ProcessorFarmerListForDialogAdapter(Context context, SyncCallbackInterface syncCallbackInterface, AppHelper appHelper, AppViewModel viewModel, List<GetFarmerListFromServerTable> farmerList, String key) {

        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        this.appHelper = appHelper;
        this.context =context;
        this.rawDataTableListFiltered = farmerList;
        this.viewModel = viewModel;
        this.farmerList = farmerList;
        this.syncCallbackInterface = syncCallbackInterface;
        this.strType = key;
    }

    @NonNull
    @Override
    public FarmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_farmer_individual, parent, false);
        return new FarmerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerViewHolder holder, int position) {
        GetFarmerListFromServerTable farmer = rawDataTableListFiltered.get(position);
        GetFarmerListFromServerTable getFarmerListFromServerTable = rawDataTableListFiltered.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                syncCallbackInterface.selectPosition(clickedPosition, getFarmerListFromServerTable, rawDataTableListFiltered);
            }
        });
        holder.cd_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                syncCallbackInterface.selectPosition(clickedPosition, getFarmerListFromServerTable, rawDataTableListFiltered);
            }
        });

        holder.txtFarmerName.setText(farmer.getFarmerName());
        holder.txtFarmerCode.setText(farmer.getFarmerCode());

//        holder.ll_Mobile_no.setVisibility(View.GONE);
//        holder.ll_Village.setVisibility(View.GONE);
//        holder.ll_pin.setVisibility(View.GONE);
//        holder.txtMobileNumber.setText(farmer.getPrimaryContactNo());
//
//        holder.txtAddress.setText(farmer.getAddress());
//        holder.txtVillage.setText(farmer.getVillageName());
//        holder.txtPin.setText(farmer.getPinCode());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    rawDataTableListFiltered = farmerList;
                } else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = farmerList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = farmerList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetFarmerListFromServerTable> filteredList = new ArrayList<>();
                    for (int i = farmerList.size(); i >= 1; i--) {
                        filteredList.add(farmerList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetFarmerListFromServerTable> filteredList = new ArrayList<>();
                    for (GetFarmerListFromServerTable row : farmerList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFarmerName().toLowerCase().contains(charString.toLowerCase()) || row.getFarmerCode().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
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
                rawDataTableListFiltered = (List<GetFarmerListFromServerTable>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getItemCount() {
        return rawDataTableListFiltered.size();
    }


    public interface SyncCallbackInterface {
        void selectPosition(int position, GetFarmerListFromServerTable getFarmerListFromServerTable, List<GetFarmerListFromServerTable> getFarmerListFromServerTableList);
    }

    public class FarmerViewHolder extends RecyclerView.ViewHolder {
        TextView txtFarmerCode;
        TextView txtFarmerName;
        TextView txtMobileNumber;
        TextView txtAddress;
        TextView txtVillage;
        TextView txtPin;

        LinearLayout ll_farmername, ll_Mobile_no, ll_Village, ll_pin;
        CardView cd_dialog;

        public FarmerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFarmerCode = itemView.findViewById(R.id.txtFarmerCode_dig);
            txtFarmerName = itemView.findViewById(R.id.txtFarmerName_dig);
            txtMobileNumber = itemView.findViewById(R.id.txtmobilenumber);
            txtAddress = itemView.findViewById(R.id.txtaddress);
            txtVillage = itemView.findViewById(R.id.txtvillage);
            txtPin = itemView.findViewById(R.id.txtpin);
            ll_farmername = itemView.findViewById(R.id.ll_farmername);
            ll_Mobile_no = itemView.findViewById(R.id.ll_mobile_no);
            ll_Village = itemView.findViewById(R.id.ll_village);
            ll_pin = itemView.findViewById(R.id.ll_pin);
            cd_dialog = itemView.findViewById(R.id.card_dialog);
        }
    }
}
