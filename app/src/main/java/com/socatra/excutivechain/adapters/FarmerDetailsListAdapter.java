package com.socatra.excutivechain.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.socatra.excutivechain.AppHelper;
import com.socatra.excutivechain.R;

import com.socatra.excutivechain.database.entity.PlantationDocuments;
import com.socatra.excutivechain.database.entity.VillageTable;
import com.socatra.excutivechain.view_models.AppViewModel;
import com.socatra.excutivechain.database.entity.FarmersTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FarmerDetailsListAdapter extends RecyclerView.Adapter<FarmerDetailsListAdapter.LoanTypeViewHolder> implements Filterable {
    private Context context;
    List<FarmersTable> rawDataTableList;
    List<FarmersTable> rawDataTableListFiltered;
    SyncCallbackInterface syncCallbackInterface;
    AppHelper appHelper;
    AppViewModel viewModel;

    public FarmerDetailsListAdapter(Context context, List<FarmersTable> rawDataTableList,
                                    SyncCallbackInterface syncCallbackInterface,
                                    AppHelper appHelper, AppViewModel viewModel) {
        this.context = context;
        this.rawDataTableList = rawDataTableList;
        this.rawDataTableListFiltered = rawDataTableList;
        this.syncCallbackInterface = syncCallbackInterface;
        this.appHelper = appHelper;
        this.viewModel = viewModel;

    }

    @NonNull
    @Override
    public LoanTypeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.farmer_individual, viewGroup, false);
        return new LoanTypeViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull LoanTypeViewHolder loanTypeViewHolder, @SuppressLint("RecyclerView") int i) {
        try {
            if (rawDataTableListFiltered != null && rawDataTableListFiltered.size() > 0) {
                FarmersTable farmerTable = rawDataTableListFiltered.get(i);

                loanTypeViewHolder.txtFarmerName.setText(farmerTable.getFirstName()+" "+farmerTable.getLastName());
                loanTypeViewHolder.txtFatherName.setText(farmerTable.getFatherName());
                loanTypeViewHolder.txtMobileNo.setText(farmerTable.getPrimaryContactNo());
                loanTypeViewHolder.txtFarmerCode.setText(farmerTable.getFarmerCode());

//                String pin=farmerTable.getPinCode();
                String id=farmerTable.getVillageId();

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
                                    loanTypeViewHolder.txtVillage.setText(villageTableList.get(0).getName());
                                }
                                else {
                                    loanTypeViewHolder.txtVillage.setText("");
                                }


                            }
                        };
                        viewModel.getvillageDetailsByPincodeLiveData().observe((LifecycleOwner) context, getLeadRawDataObserver);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                //Todo:Farmer Image

                try {
                    viewModel.getLocalDocIdentificationFromLocalDBByFidandDtype(farmerTable.getFarmerCode(),"farmer image");
                    if (viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData() != null) {
                        Observer getLeadRawDataObserver = new Observer() {
                            @Override
                            public void onChanged(@Nullable Object o) {
                                List<PlantationDocuments> odVisitSurveyTableList = (List<PlantationDocuments>) o;
                                viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().removeObserver(this);
                                if (odVisitSurveyTableList != null && odVisitSurveyTableList.size() > 0) {


//                            Log.e(mTag,odVisitSurveyTableList.toString());

                                    if (odVisitSurveyTableList.get(0).getDocURL()!=null){

                                        if (odVisitSurveyTableList.get(0).getDocURL().contains("https://")){
                                            Picasso.get()
                                                    .load(odVisitSurveyTableList.get(0).getDocURL())
                                                    .placeholder(R.drawable.baseline_downloading_24)
                                                    .error(R.drawable.ic_baseline_agriculture_24)
                                                    .into(loanTypeViewHolder.imgFarmer);
                                        }else if (odVisitSurveyTableList.get(0).getDocURL().contains("/storage/emulated/")){
                                            File imgFile = new File(odVisitSurveyTableList.get(0).getDocURL());
                                            if (imgFile.exists()) {
                                                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                                loanTypeViewHolder.imgFarmer.setImageBitmap(bitmap);
                                            } else {
                                                // Handle the case where the image file doesn't exist
                                                Picasso.get()
                                                        .load(R.drawable.baseline_broken_image_24)
                                                                .into(loanTypeViewHolder.imgFarmer);
                                            }
                                        }
                                    }
                                } else {
                                }
                            }
                        };
                        viewModel.getDocIdentiFicationDeatilsTableFromLocalLiveData().observe((LifecycleOwner) context, getLeadRawDataObserver);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
//            Log.e(mTag,"Getting Null Img 1");
                }

                loanTypeViewHolder.imgFarmer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        syncCallbackInterface.addPlotDetailsCallback(i, farmerTable, farmerTable.getFarmerCode(),loanTypeViewHolder.imgFarmer);
                    }
                });

                loanTypeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        syncCallbackInterface.addPlotDetailsCallback(i, farmerTable, farmerTable.getFarmerCode(),loanTypeViewHolder.imgFarmer);
                    }
                });


//                String imagePathCheck = "";
//
//                try{
//                    imagePathCheck = farmerTable.getImage().substring(0, 4);
//                } catch (Exception exception){
//                    imagePathCheck = "";
//                }
//
//                if (imagePathCheck.equalsIgnoreCase("http")) {
////                    loanTypeViewHolder.farmer_image_new.setVisibility(View.GONE);
//                    Picasso.get()
//                            .load(farmerTable.getImage())
//                            .error(R.drawable.ic_baseline_agriculture_24)
////                            .error(R.drawable.placeholder_image)
////                            .placeholder(R.drawable.loading_icon)
////                            .placeholder(android.R.drawable.progress_indeterminate_horizontal)
//                            .placeholder(R.drawable.ic_baseline_agriculture_24)
//                            .into(loanTypeViewHolder.imgFarmer);
//                } else {
//                    Uri uri = null;
//                    File imgFile = new File(farmerTable.getImage());
//                    if (imgFile.exists()) {
//                        uri = Uri.fromFile(imgFile);
////                        loanTypeViewHolder.imgFarmer.setVisibility(View.GONE);
//                        Picasso.get()
//                                .load(uri)
////                                .error(R.drawable.round_image_shape)
//                                .error(R.drawable.ic_baseline_agriculture_24)
////                                .placeholder(R.drawable.round_image_shape)
//                                .placeholder(R.drawable.ic_baseline_agriculture_24)
//                                .into(loanTypeViewHolder.imgFarmer);
//                    }
//
//                }

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        if (rawDataTableListFiltered != null) {
            return rawDataTableListFiltered.size();
        } else {
            return 0;
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    rawDataTableListFiltered = rawDataTableList;
                } else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = rawDataTableList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = rawDataTableList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<FarmersTable> filteredList = new ArrayList<>();
                    for (int i = rawDataTableList.size(); i >= 1; i--) {
                        filteredList.add(rawDataTableList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
                    List<FarmersTable> filteredList = new ArrayList<>();
                    for (FarmersTable row : rawDataTableList) {
                        if (row.getFirstName().toLowerCase().contains(charString.toLowerCase())||row.getLastName().toLowerCase().contains(charString.toLowerCase())||row.getFarmerCode().toLowerCase().contains(charString.toLowerCase())) {
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
                rawDataTableListFiltered = (List<FarmersTable>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class LoanTypeViewHolder extends RecyclerView.ViewHolder {

        TextView txtFarmerCode,txtFarmerName,txtMobileNo,txtFatherName,txtVillage,txtDateOfSowing,txtDateOfPlantation,txtPipeInstallation,txtPlotBoundStatus,txtWaterCyclesStatus;
        ImageView imgFarmer;


        public LoanTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFarmerCode = (TextView) itemView.findViewById(R.id.txtFarmerCode);
            txtFarmerName = (TextView) itemView.findViewById(R.id.txtFarmerName);
            txtMobileNo = (TextView) itemView.findViewById(R.id.txtMobileNo);
            txtFatherName = (TextView) itemView.findViewById(R.id.txtFatherName);
            txtVillage = (TextView) itemView.findViewById(R.id.txtState);
//            txtDateOfSowing = (TextView) itemView.findViewById(R.id.txtDateOfSowing);
//            txtDateOfPlantation = (TextView) itemView.findViewById(R.id.txtDateOfPlantation);
//            txtPipeInstallation = (TextView) itemView.findViewById(R.id.txtPipeInstallation);
//            txtPlotBoundStatus = (TextView) itemView.findViewById(R.id.txtPlotBoundStatus);
//            txtWaterCyclesStatus = (TextView) itemView.findViewById(R.id.txtWaterCyclesStatus);
            imgFarmer = itemView.findViewById(R.id.imgFarmer);

        }
    }




    public interface SyncCallbackInterface {
        void openScreenCallback(int position, FarmersTable farmerTable, List<FarmersTable> farmer, String applicationType);

        void updateItemCallback(int position, FarmersTable applicationStatusTable, String strFarmerID);

        void addPlotDetailsCallback(int position, FarmersTable applicationStatusTable, String strFarmercode, ImageView imgFarmer);
    }

}
