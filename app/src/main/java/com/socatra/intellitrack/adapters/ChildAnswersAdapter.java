package com.socatra.intellitrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.intellitrack.R;
import com.socatra.intellitrack.models.get.GetRiskAssessmentForViewByFarmerCode;

import java.util.ArrayList;

public class ChildAnswersAdapter extends RecyclerView.Adapter<ChildAnswersAdapter.ViewHolder> {

    ArrayList<GetRiskAssessmentForViewByFarmerCode> Surveylist_child;
    private Context context;

    public ChildAnswersAdapter(Context context, ArrayList<GetRiskAssessmentForViewByFarmerCode> questionsMainCategoryList) {
        this.context = context;
        this.Surveylist_child = questionsMainCategoryList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_child, parent, false);
        return new ChildAnswersAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ChildAnswersAdapter.ViewHolder holder, int position) {
        GetRiskAssessmentForViewByFarmerCode item = Surveylist_child.get(position);
        holder.textViewChild.setText(item.getQuestions() + " ?");
        String strSerialNo = String.valueOf(item.getSerialNo());
        holder.txtQuestionSN.setText(strSerialNo);
        holder.txtAnswer.setText(item.getAnswers());


    }


    @Override
    public int getItemCount() {
        if (Surveylist_child != null) {
            return Surveylist_child.size();
        } else {
            return 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        TextView textViewChild, txtQuestionSN, txtAnswer;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewChild = itemView.findViewById(R.id.txt_title);
            txtQuestionSN = itemView.findViewById(R.id.txt_question_sn);
            txtAnswer = itemView.findViewById(R.id.txt_ans);

        }
    }


}


