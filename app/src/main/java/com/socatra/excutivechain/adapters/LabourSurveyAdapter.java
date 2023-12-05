package com.socatra.excutivechain.adapters;

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
import com.socatra.excutivechain.database.entity.PlantationLabourSurvey;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.List;

public class LabourSurveyAdapter extends RecyclerView.Adapter<LabourSurveyAdapter.LabourSurveyViewHolder> {

    AppHelper appHelper;
    AppViewModel viewModel;
    List<PlantationLabourSurvey> plantations;
    Context context;

    public LabourSurveyAdapter(AppHelper appHelper, AppViewModel viewModel, List<PlantationLabourSurvey> plantations) {
        this.appHelper = appHelper;
        this.viewModel = viewModel;
        this.plantations = plantations;

    }

    @NonNull
    @Override
    public LabourSurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        return new LabourSurveyViewHolder(LayoutInflater.from(context).inflate(R.layout.labour_adapter_individual,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LabourSurveyViewHolder holder, int position) {
        holder.txtFarmerCode.setText(plantations.get(position).getFarmerCode());
        holder.txtNoOfFieldWorker.setText(plantations.get(position).getNoOfFieldWorkers().toString());
        Log.e("labSurAdp",plantations.get(position).getPlantationCode());
        try {
            viewModel.getPlantationDetailsFromLocalDbBymId(plantations.get(position).getPlantationCode());
            if (viewModel.getPlantationDetailsByIdLiveData() != null) {
                Observer getLeadRawDataObserver = new Observer() {
                    @Override
                    public void onChanged(@Nullable Object o) {
                        List<Plantation> plantationList = (List<Plantation>) o;
                        viewModel.getPlantationDetailsByIdLiveData().removeObserver(this);
                        if (plantationList != null && plantationList.size() > 0) {

                            holder.txtPlotNo.setText(plantationList.get(0).getPlotCode());
                            Log.e("labSurAdp",plantationList.get(0).getPlotCode());

                        } else {
                            Log.e("labSurAdp","else");
                        }
                    }
                };
                viewModel.getPlantationDetailsByIdLiveData().observe((LifecycleOwner) context, getLeadRawDataObserver);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("labSurAdp","Null catch");
        }

        holder.cardLabHome.setOnClickListener(view->{
        });
    }


    @Override
    public int getItemCount() {
        return plantations.size();
    }

    public class LabourSurveyViewHolder extends RecyclerView.ViewHolder {

        TextView txtPlotNo,txtFarmerCode,txtNoOfFieldWorker;
        CardView cardLabHome;
        public LabourSurveyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPlotNo=itemView.findViewById(R.id.txtPlotNoLabAdp);
            txtFarmerCode=itemView.findViewById(R.id.txtFarmerCodeLabAdp);
            txtNoOfFieldWorker=itemView.findViewById(R.id.txtNoOfFieldWorkerLabAdp);
            cardLabHome=itemView.findViewById(R.id.cardLabHome);


        }
    }

}
