package com.socatra.excutivechain.adapters;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.excutivechain.AppHelper;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.database.entity.Plantation;
import com.socatra.excutivechain.database.entity.VillageTable;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.List;

public class PlantationAdapter extends RecyclerView.Adapter<PlantationAdapter.PlantationViewHolder> {

    AppHelper appHelper;
    AppViewModel viewModel;
    List<Plantation> plantations;
    Context context;
    Dialog dialog;

    SyncPlantationCallbackInterface syncPlantationCallbackInterface;

    public PlantationAdapter(AppHelper appHelper, AppViewModel viewModel, List<Plantation> plantations,
                             SyncPlantationCallbackInterface syncPlantationCallbackInterface) {
        this.appHelper = appHelper;
        this.viewModel = viewModel;
        this.plantations = plantations;
        this.syncPlantationCallbackInterface=syncPlantationCallbackInterface;
    }

    @NonNull
    @Override
    public PlantationAdapter.PlantationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        return new PlantationViewHolder(LayoutInflater.from(context).inflate(R.layout.plantation_individual,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlantationAdapter.PlantationViewHolder holder, int position) {
        holder.txtPlotNo.setText(plantations.get(position).getPlotCode());
        holder.txtFarmerCode.setText(plantations.get(position).getFarmerCode());
        holder.txtAreaPlotInd.setText(plantations.get(position).getAreaInHectors().toString());
        holder.txtGpsPlotInd.setText(String.valueOf(plantations.get(position).getLatitude())+","
                +String.valueOf(plantations.get(position).getLongitude()));
        if (plantations.get(position).getGeoboundariesArea()==0.0){
            holder.txtGeoAreaPlotInd.setText("n.a");
        } else {
//            holder.txtGeoAreaPlotInd.setText(plantations.get(position).getGeoboundariesArea().toString());
            double totalArea=plantations.get(position).getGeoboundariesArea();
            String decimalForm = String.format("%.5f", totalArea);
            decimalForm = decimalForm.replace(",", ".");
            holder.txtGeoAreaPlotInd.setText(decimalForm);
        }
//        Log.e("onBindViewHolder:vilageId ",plantations.get(position).getVillageId());

        String id=plantations.get(position).getVillageId();

//        Log.e( "villageId: " , id);
        //Todo:Village details based on Village Id
        try {
            viewModel.getVillageDetailsListFromLocalDBbyId(id);
            if (viewModel.getvillageDetailsByPincodeLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<VillageTable> villageTableList = (List<VillageTable>) o;
                        viewModel.getvillageDetailsByPincodeLiveData().removeObserver(this);

                        if (villageTableList != null && villageTableList.size() > 0) {
                            holder.txtVillagePlantInd.setText(villageTableList.get(0).getName());
                        }
                        else {
                            holder.txtVillagePlantInd.setText("");
                        }


                    }
                };
                viewModel.getvillageDetailsByPincodeLiveData().observe((LifecycleOwner) context, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //syncPlantationCallbackInterface.getVillageNameCallback(plantations.get(position).getVillageId(),holder.txtVillagePlantInd);


//        try {
//            viewModel.getVillageDetailsListFromLocalDB(plantations.get(position).getVillageId());
//            if (viewModel.getvillageDetailsByPincodeLiveData() != null) {
//                Observer getLeadRawDataObserver = new Observer() {
//                    @Override
//                    public void onChanged(@Nullable Object o) {
//                        List<VillageTable> villageTableList = (List<VillageTable>) o;
//                        viewModel.getvillageDetailsByPincodeLiveData().removeObserver(this);
//                        if (villageTableList != null && villageTableList.size() > 0) {
//
//                            holder.txtVillagePlantInd.setText(villageTableList.get(0).getName());
//
//                        } else {
//
//                        }
//                    }
//                };
//                viewModel.getvillageDetailsByPincodeLiveData().observe((LifecycleOwner) context, getLeadRawDataObserver);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
////            Log.e("Villagelist","Null catch");
//        }

        holder.cardPlantHone.setOnClickListener(view->{
            syncPlantationCallbackInterface.addPlantDetailsCallback(position,plantations.get(position),plantations.get(position).getFarmerCode(),plantations.get(position).getPlotCode());
        });
    }


    @Override
    public int getItemCount() {
        return plantations.size();
    }

    public class PlantationViewHolder extends RecyclerView.ViewHolder {

        TextView txtPlotNo,txtFarmerCode,txtAreaPlotInd,txtGpsPlotInd,txtVillagePlantInd,txtGeoAreaPlotInd;
        CardView cardPlantHone;
        public PlantationViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPlotNo=itemView.findViewById(R.id.txtPlotNo);
            txtFarmerCode=itemView.findViewById(R.id.txtFarmerCode);
            txtAreaPlotInd=itemView.findViewById(R.id.txtAreaPlotInd);
            txtGpsPlotInd=itemView.findViewById(R.id.txtGpsPlotInd);
            txtVillagePlantInd=itemView.findViewById(R.id.txtVillagePlantInd);
            cardPlantHone=itemView.findViewById(R.id.cardPlantHone);
            txtGeoAreaPlotInd=itemView.findViewById(R.id.txtGeoAreaPlotInd);


        }
    }


    public interface SyncPlantationCallbackInterface {
//        void openScreenCallback(int position, FarmersTable farmerTable, List<FarmersTable> farmer, String applicationType);
//
//        void updateItemCallback(int position, FarmersTable applicationStatusTable, String strFarmerID);
        void addPlantDetailsCallback(int position, Plantation applicationStatusTable, String strFarmercode, String mPlotCode);

        void getVillageNameCallback(String villageId,TextView txtVillageName);
    }
}
