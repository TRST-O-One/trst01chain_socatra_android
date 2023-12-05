package com.socatra.excutivechain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.excutivechain.AppHelper;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.database.entity.FarmerHouseholdChildrenSurvey;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.util.List;

public class ChildSurveyAdapter extends RecyclerView.Adapter<ChildSurveyAdapter.ChildSurveyViewHolder> {

    AppHelper appHelper;
    AppViewModel viewModel;
    List<FarmerHouseholdChildrenSurvey> farmerChildSurveys;
    Context context;

    public ChildSurveyAdapter(AppHelper appHelper, AppViewModel viewModel, List<FarmerHouseholdChildrenSurvey> farmerChildSurveys) {
        this.appHelper = appHelper;
        this.viewModel = viewModel;
        this.farmerChildSurveys = farmerChildSurveys;

    }

    @NonNull
    @Override
    public ChildSurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        return new ChildSurveyViewHolder(LayoutInflater.from(context).inflate(R.layout.child_adapter_individual,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChildSurveyViewHolder holder, int position) {

        holder.txtChildName.setText(farmerChildSurveys.get(position).getChildrenName());
        holder.txtChildAge.setText(farmerChildSurveys.get(position).getChildrenAge().toString());
        holder.txtChildGender.setText(farmerChildSurveys.get(position).getChildrenGender());
        holder.txtChildOcc.setText(farmerChildSurveys.get(position).getChildrenOccupation());
    }


    @Override
    public int getItemCount() {
        return farmerChildSurveys.size();
    }

    public class ChildSurveyViewHolder extends RecyclerView.ViewHolder {

        TextView txtChildName, txtChildAge, txtChildGender, txtChildOcc;
        CardView cardChildSur;
        public ChildSurveyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtChildName =itemView.findViewById(R.id.txtChildNameCadpt);
            txtChildAge =itemView.findViewById(R.id.txtChildAgeCadpt);
            txtChildGender =itemView.findViewById(R.id.txtChildGenderCadpt);
            txtChildOcc =itemView.findViewById(R.id.txtChildOccupCadpt);
            cardChildSur=itemView.findViewById(R.id.cardChildSur);

        }
    }

}
