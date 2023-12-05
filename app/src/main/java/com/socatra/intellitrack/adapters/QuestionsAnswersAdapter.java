package com.socatra.intellitrack.adapters;

import static com.socatra.intellitrack.constants.AppConstant.AUTHORIZATION_TOKEN_KEY;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonElement;
import com.socatra.intellitrack.R;
import com.socatra.intellitrack.api.AppAPI;
import com.socatra.intellitrack.api.webservice.Retrofit_funtion_class;
import com.socatra.intellitrack.helper.AppHelper;
import com.socatra.intellitrack.models.get.GetRiskAssessmentForViewByFarmerCode;
import com.socatra.intellitrack.view_models.AppViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsAnswersAdapter extends RecyclerView.Adapter<QuestionsAnswersAdapter.ViewHolder> {


    AppHelper appHelper;
    AppViewModel viewModel;

    String Token, strDealerID, strDealerName, strProcessorId, strCustomerId;


    ArrayList<GetRiskAssessmentForViewByFarmerCode> questionsMainCategoryList;
    ArrayList<GetRiskAssessmentForViewByFarmerCode> Surveylist_child;
    String strGetFarmerCode;
    ChildAnswersAdapter childAnswersAdapter;
    private Context context;


    public QuestionsAnswersAdapter(Context context, String strGetFarmerCode, AppHelper appHelper, AppViewModel viewModel, ArrayList<GetRiskAssessmentForViewByFarmerCode> questionsMainCategoryList) {

        this.strGetFarmerCode = strGetFarmerCode;
        this.context = context;
        this.questionsMainCategoryList = questionsMainCategoryList;
        this.appHelper = appHelper;
        this.viewModel = viewModel;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.qns_answer, parent, false);
        return new QuestionsAnswersAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull QuestionsAnswersAdapter.ViewHolder holder, int position) {
        GetRiskAssessmentForViewByFarmerCode item = questionsMainCategoryList.get(position);
        holder.txtManiCatQuestions.setText(item.getQuestionCategory());
        holder.recQnsAnslist.setLayoutManager(new LinearLayoutManager(context));
        holder.recQnsAnslist.hasFixedSize();
        getSubQuestionsAndAnswerList(holder.recQnsAnslist, item.getQuestionCategory());

    }


    @Override
    public int getItemCount() {
        if (questionsMainCategoryList != null) {
            return questionsMainCategoryList.size();
        } else {
            return 0;
        }
    }

    public void getSubQuestionsAndAnswerList(RecyclerView recQnsAnslist, String strQuestionCat) {
        AppAPI service = Retrofit_funtion_class.getClient().create(AppAPI.class);
        //  String FarmerCode = getIntent().getStringExtra("FarmerCode");
        Call<JsonElement> callRetrofit = service.getRiskAssessmentForViewByFarmerCodeFromServer(strGetFarmerCode, appHelper.getSharedPrefObj().getString(AUTHORIZATION_TOKEN_KEY, ""));

        callRetrofit.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                try {
                    String strResponse = String.valueOf(response.body());
                    JSONObject jsonObject = new JSONObject(strResponse);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    ArrayList<GetRiskAssessmentForViewByFarmerCode> Surveylist_child = new ArrayList<>();
                    Surveylist_child.clear();
                    if (jsonArray.length() > 0) {

                        try {
                            ArrayList<GetRiskAssessmentForViewByFarmerCode> getSurveyListTableArrayList = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectFarmerPD = jsonArray.getJSONObject(i);
                                GetRiskAssessmentForViewByFarmerCode CustomerList = new GetRiskAssessmentForViewByFarmerCode();
                                if (jsonObjectFarmerPD.getString("QuestionCategory").equals(strQuestionCat)) {
                                    CustomerList.setQuestions((jsonObjectFarmerPD.getString("Questions")));
                                    CustomerList.setAnswers((jsonObjectFarmerPD.getString("Answers")));
                                    CustomerList.setQuestionNo(jsonObjectFarmerPD.getString("QuestionNo"));
                                    CustomerList.setSerialNo(Integer.valueOf((jsonObjectFarmerPD.getString("SerialNo"))));
                                    getSurveyListTableArrayList.add(CustomerList);
                                }
                            }

                            Surveylist_child.addAll(getSurveyListTableArrayList);
                            childAnswersAdapter = new ChildAnswersAdapter(context, Surveylist_child  );
                            recQnsAnslist.setAdapter(childAnswersAdapter);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d("Error", ">>>>" + ex.toString());
                        }
                    } else {
                        Toast.makeText(context, "no records found", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Log.d("Error", ">>>>" + ex.toString());
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("Error Call", ">>>>" + call.toString());
                Log.d("Error", ">>>>" + t.toString());
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView txtManiCatQuestions;

        public RecyclerView recQnsAnslist;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtManiCatQuestions = itemView.findViewById(R.id.txt_question_title);
            recQnsAnslist = itemView.findViewById(R.id.rec_ada_answer_list);


        }
    }


}


