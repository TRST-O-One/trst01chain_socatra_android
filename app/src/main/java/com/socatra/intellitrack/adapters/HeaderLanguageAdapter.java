package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.DeviceLanguageID;
import static com.socatra.intellitrack.constants.AppConstant.DeviceLanguageName;
import static com.socatra.intellitrack.constants.AppConstant.DeviceUserRole;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.intellitrack.R;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetLanguageHeaderList;
import com.socatra.intellitrack.models.get.GetTransalteLanguageWordsByLanguageId;
import com.socatra.intellitrack.view_models.AppViewModel;

import java.util.List;

public class HeaderLanguageAdapter extends RecyclerView.Adapter<HeaderLanguageAdapter.FarmerViewHolder> {

    List<GetLanguageHeaderList> languagesHeaderList;

    List<GetTransalteLanguageWordsByLanguageId> languagesList;

    String strType;
    String deviceRoleName;
    SyncCallbackInterface syncCallbackInterface;
    private Context context;
    private AppHelper appHelper;
    private AppViewModel viewModel;

    int selectedPosition = -1;
    int lastSelectedPosition = -1;

    public HeaderLanguageAdapter(Context context, SyncCallbackInterface syncCallbackInterface, AppHelper appHelper, AppViewModel viewModel, List<GetLanguageHeaderList> farmerList, String key) {

        deviceRoleName = appHelper.getSharedPrefObj().getString(DeviceUserRole, "");
        this.appHelper = appHelper;
        this.context = context;
        this.languagesHeaderList = farmerList;
        this.viewModel = viewModel;
        this.syncCallbackInterface = syncCallbackInterface;
    }

    @NonNull
    @Override
    public FarmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialogue_language_ada_list, parent, false);
        return new FarmerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerViewHolder holder, int position) {

        GetLanguageHeaderList languageHeaderListData = languagesHeaderList.get(position);



        holder.txtTitleLang.setText(languageHeaderListData.getLanguageName());

        appHelper.getSharedPrefObj().edit().remove(DeviceLanguageID).apply();
        appHelper.getSharedPrefObj().edit().remove(DeviceLanguageName).apply();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition == 0)
                {
                    syncCallbackInterface.selectPosition(clickedPosition, languageHeaderListData, languagesHeaderList);

//                     Toast.makeText(context, "working in progress will update soon", Toast.LENGTH_SHORT).show();

                }else {
               syncCallbackInterface.selectPosition(clickedPosition, languageHeaderListData, languagesHeaderList);
            }}

        });
        holder.cdDigLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition == 0)
                {
                    syncCallbackInterface.selectPosition(clickedPosition, languageHeaderListData, languagesHeaderList);

//                    Toast.makeText(context, "working in progress will update soon", Toast.LENGTH_SHORT).show();

                }else {
                    syncCallbackInterface.selectPosition(clickedPosition, languageHeaderListData, languagesHeaderList);
//
                }
            }
        });

//        if(position == selectedPosition) {
//            holder.txtTitleLang.setTextColor(Color.parseColor("#00aaff"));
//            holder.cdDigLang.setBackgroundResource(R.drawable.rectangular_box);
//        } else {
//            holder.txtTitleLang.setTextColor(Color.parseColor("#000000")); //actually you should set to the normal text color
//            holder.cdDigLang.setBackgroundResource(0);
//        }
    }


    @Override
    public int getItemCount() {
        return languagesHeaderList.size();
    }


    public interface SyncCallbackInterface {
        void selectPosition(int position, GetLanguageHeaderList getLanguageHeaderData, List<GetLanguageHeaderList> GetLanguageHeaderList);
    }

    public class FarmerViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitleLang;
        CardView cdDigLang;

        public FarmerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitleLang = itemView.findViewById(R.id.txt_language);
            cdDigLang = itemView.findViewById(R.id.cd_dig_lang);
        }
    }
}
