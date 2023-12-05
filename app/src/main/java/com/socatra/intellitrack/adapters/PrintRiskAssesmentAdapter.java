package com.socatra.intellitrack.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.intellitrack.R;
import com.socatra.intellitrack.models.get.GetDealerDetailsbyProcessorId;
import com.socatra.intellitrack.models.get.GetRiskAssessmentForViewByFarmerCode;

import java.util.List;


public class PrintRiskAssesmentAdapter extends RecyclerView.Adapter<PrintRiskAssesmentAdapter.ViewHolder> {

    private List<GetRiskAssessmentForViewByFarmerCode> SurveyList;

    private String farmerCode;


        public PrintRiskAssesmentAdapter(String farmerCode, List<GetRiskAssessmentForViewByFarmerCode> SurveyList  ) {

        this.SurveyList = SurveyList;
        this.farmerCode = farmerCode;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.print_risk_assesment_individual, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GetRiskAssessmentForViewByFarmerCode item = SurveyList.get(position);

        // Set the serial number
        holder.txtSerialno.setText(String.valueOf(item.getSerialNo()));

        // Set the question and answer
        holder.txtQuestion.setText(String.valueOf(item.getQuestions()));
        holder.txtAnswer.setText(String.valueOf(item.getAnswers()));

        // Display farmer code only for the first item
        if (position == 0) {
            holder.txtFarmerName.setText(farmerCode);
            holder.txtFarmerName.setVisibility(View.VISIBLE);
//            holder.txtheading.setVisibility(View.VISIBLE);
//            holder.txtcolumn.setVisibility(View.VISIBLE);
        } else {
            holder.txtFarmerName.setVisibility(View.GONE);
//            holder.txtheading.setVisibility(View.GONE);
//            holder.txtcolumn.setVisibility(View.GONE);


        }
    }




    @Override
    public int getItemCount() {

        return SurveyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCategory;

        public TextView txtQuestion;

        public TextView txtAnswer;

        public TextView txtSerialno;

        public TextView txtFarmerName ;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuestion = itemView.findViewById(R.id.Question);
            txtAnswer = itemView.findViewById(R.id.Answer);
            txtSerialno = itemView.findViewById(R.id.Question_serial_no);
            txtFarmerName = itemView.findViewById(R.id.txtFarmerName);
//            txtheading = itemView.findViewById(R.id.txt_heading);
//            txtcolumn = itemView.findViewById(R.id.txt_col);







        }


    }

    public interface OnItemClickListener {
        void onItemClick(GetDealerDetailsbyProcessorId Dealer);
    }




}

