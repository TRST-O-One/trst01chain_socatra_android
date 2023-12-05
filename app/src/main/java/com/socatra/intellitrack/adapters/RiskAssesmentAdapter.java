package com.socatra.intellitrack.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socatra.intellitrack.R;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetDealerDetailsbyProcessorId;
import com.socatra.intellitrack.models.get.GetRiskAssessmentForViewByFarmerCode;
import com.socatra.intellitrack.view_models.AppViewModel;

import java.util.ArrayList;
import java.util.List;


public class RiskAssesmentAdapter extends RecyclerView.Adapter<RiskAssesmentAdapter.ViewHolder> implements Filterable {

    static List<GetRiskAssessmentForViewByFarmerCode> SurveyList;

    AppHelper appHelper;
    AppViewModel viewModel;


    List<GetRiskAssessmentForViewByFarmerCode> rawDataTableListFiltered;

    private OnItemClickListener onItemClickListener;

    public RiskAssesmentAdapter( List<GetRiskAssessmentForViewByFarmerCode> SurveyList  ) {

        this.SurveyList = SurveyList;
        this.rawDataTableListFiltered = SurveyList;
        this.appHelper = appHelper;
        this.viewModel = viewModel;
        this.onItemClickListener = onItemClickListener;


    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.risk_assesment_individual, parent, false);
        return new RiskAssesmentAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RiskAssesmentAdapter.ViewHolder holder, int position) {

        GetRiskAssessmentForViewByFarmerCode item = rawDataTableListFiltered.get(position);









//        String rn = item.getRn();
////        String category = item.getQuestionCategory();
////
////        if (rn.equals("1")) {
////            holder.txtCategory.setText(category);
////            holder.txtCategory.setVisibility(View.GONE);
////
////        } else {
////
////            holder.txtCategory.setVisibility(View.VISIBLE);
////
////        }



        holder.txtQuestion.setText(String.valueOf(item.getQuestionCategory()));
        holder.txtQuestion.setText(String.valueOf(item.getQuestions()));
        holder.txtAnswer.setText(String.valueOf(item.getAnswers()));

//        else {
//            holder.txtCategory.setVisibility(View.GONE);
//        }

        boolean isExpanded = SurveyList.get(position).isExpanded();

        holder.expandablelayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    rawDataTableListFiltered = SurveyList;
                }

                else if (charString.equalsIgnoreCase("All")) {
                    rawDataTableListFiltered = SurveyList;
                } else if (charString.equalsIgnoreCase("ASC")) {
                    rawDataTableListFiltered = SurveyList;
                } else if (charString.equalsIgnoreCase("DESC")) {
                    List<GetRiskAssessmentForViewByFarmerCode> filteredList = new ArrayList<>();
                    for (int i = SurveyList.size(); i >= 1; i--) {
                        filteredList.add(SurveyList.get(i - 1));
                    }
                    rawDataTableListFiltered = filteredList;

                } else {
//                    List<Contact> filteredList = new ArrayList<>();
                    List<GetRiskAssessmentForViewByFarmerCode> filteredList = new ArrayList<>();

                    for (GetRiskAssessmentForViewByFarmerCode row : SurveyList) {
                        String searchQuery = charString.toLowerCase();

                        String SubDealerName = row.getAnswers();

                        // Check if the GRNnumber contains the searchQuery (case-insensitive)
                        if (SubDealerName.toLowerCase().contains(charString.toLowerCase())) {
                            Log.d("Searched GRN", "SubDealerName: " + row.getAnswers());

                            filteredList.add(row);
                        }

                        else {

                            System.out.println("GRNnumber: " + SubDealerName + ", SearchQuery: " + searchQuery);
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
                rawDataTableListFiltered = (List<GetRiskAssessmentForViewByFarmerCode>) filterResults.values;
                notifyDataSetChanged(); // Ensure you call this
            }

        };
    }
    @Override
    public int getItemCount() {
        if (rawDataTableListFiltered != null) {
            return rawDataTableListFiltered.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtCategory;

        public TextView txtQuestion;

        public TextView txtAnswer;

        public RelativeLayout expandablelayout;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.Category_name);
            txtQuestion = itemView.findViewById(R.id.Question);
            txtAnswer = itemView.findViewById(R.id.Answer);
            expandablelayout = itemView.findViewById(R.id.expandablelayout);




            txtCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetRiskAssessmentForViewByFarmerCode Survey = SurveyList.get(getAdapterPosition());
                    Survey.setExpanded(!Survey.isExpanded());
                    notifyDataSetChanged();

                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(GetDealerDetailsbyProcessorId Dealer);
    }




}
