package com.socatra.excutivechain.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.socatra.excutivechain.AppHelper;
import com.socatra.excutivechain.R;
import com.socatra.excutivechain.database.entity.PlantationDocuments;
import com.socatra.excutivechain.view_models.AppViewModel;

import java.io.File;
import java.util.List;

public class DocHomeAdapter extends RecyclerView.Adapter<DocHomeAdapter.DocViewHolder> {

    AppHelper appHelper;
    AppViewModel viewModel;
    List<PlantationDocuments> documents;
    Context context;

    SyncDocCallbackInterface syncDocCallbackInterface;

    public DocHomeAdapter(AppHelper appHelper, AppViewModel viewModel, List<PlantationDocuments> documents,
                          SyncDocCallbackInterface syncDocCallbackInterface) {
        this.appHelper = appHelper;
        this.viewModel = viewModel;
        this.documents = documents;
        this.syncDocCallbackInterface = syncDocCallbackInterface;
    }

    @NonNull
    @Override
    public DocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context= parent.getContext();
        return new DocViewHolder(LayoutInflater.from(context).inflate(R.layout.document_individual,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DocViewHolder holder, int position) {
        //,
        holder.txtFarmerCode.setText(documents.get(position).getFarmerCode());
        holder.txtDocType.setText(documents.get(position).getDocType());
        holder.txtUid.setText(documents.get(position).getDocUrlValue());



        //Image
        if (documents.get(position).getDocURL().contains("https://")){
            if (documents.get(position).getDocURL().endsWith(".pdf") ){
                Picasso.get()
                        .load(R.drawable.baseline_picture_as_pdf)
                        .placeholder(R.drawable.baseline_downloading_24)
                        .error(R.drawable.baseline_picture_as_pdf)//ic_baseline_agriculture_24
                        .into(holder.imgDoc);
            }else {
                Picasso.get()
                        .load(documents.get(position).getDocURL())
                        .placeholder(R.drawable.baseline_downloading_24)
                        .error(R.drawable.ic_baseline_agriculture_24)
                        .into(holder.imgDoc);
            }

        } else if (documents.get(position).getDocURL().contains("/storage/emulated/")) {
            if (documents.get(position).getDocURL().endsWith(".pdf")){
                Picasso.get()
                        .load(R.drawable.baseline_picture_as_pdf)
                        .placeholder(R.drawable.baseline_downloading_24)
                        .error(R.drawable.baseline_picture_as_pdf)
                        .into(holder.imgDoc);
            }else {
                File imgFile = new File(documents.get(position).getDocURL());
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    holder.imgDoc.setImageBitmap(bitmap);
                } else {
                    Picasso.get()
                            .load(R.drawable.baseline_broken_image_24)
                            .error(R.drawable.baseline_picture_as_pdf)
                            .into(holder.imgDoc);
                }
            }
        }

        holder.cardDocHome.setOnClickListener(view->{
            syncDocCallbackInterface.addDocDetailsCallback(position, documents.get(position), documents.get(position).getFarmerCode(), documents.get(position).getPlotCode());
        });
    }


    @Override
    public int getItemCount() {
        return documents.size();
    }

    public class DocViewHolder extends RecyclerView.ViewHolder {

        TextView txtFarmerCode,txtDocType,txtUid;

        ImageView imgDoc;
        CardView cardDocHome;
        public DocViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFarmerCode=itemView.findViewById(R.id.txtFarmerCodeDocRec);
            txtDocType=itemView.findViewById(R.id.txtDocTypeDocRec);
            txtUid=itemView.findViewById(R.id.txtDocUidDocRec);
            imgDoc=itemView.findViewById(R.id.imgDocRec);
            cardDocHome =itemView.findViewById(R.id.cardDocRec);

        }
    }


    public interface SyncDocCallbackInterface {
//        void openScreenCallback(int position, FarmersTable farmerTable, List<FarmersTable> farmer, String applicationType);
//
//        void updateItemCallback(int position, FarmersTable applicationStatusTable, String strFarmerID);
        void addDocDetailsCallback(int position, PlantationDocuments applicationStatusTable, String strFarmerCode, String mDocId);
    }
}
