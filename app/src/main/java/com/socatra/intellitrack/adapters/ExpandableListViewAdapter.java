package com.socatra.intellitrack.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.socatra.intellitrack.R;
import com.socatra.intellitrack.models.get.GetRiskAssessmentForViewByFarmerCode;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    ArrayList<GetRiskAssessmentForViewByFarmerCode> questionsMainCategoryList;
    ArrayList<GetRiskAssessmentForViewByFarmerCode> questionSurveyList;
    private Context context;

    public ExpandableListViewAdapter(Context activity, ArrayList<GetRiskAssessmentForViewByFarmerCode> cafeCatModels, ArrayList<GetRiskAssessmentForViewByFarmerCode> surveylist_child) {
        this.context = activity;
        this.questionsMainCategoryList = cafeCatModels;
        this.questionSurveyList =surveylist_child ;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
//        return this.listDataChild.get(this.listDataGroup.get(groupPosition))
//                .get(childPosititon);
//        ArrayList<CafeSubCatModel> productList = arrayList.get(groupPosition).getCafeSubCatModels();
//        return productList.get(childPosititon);
        return questionSurveyList.get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

       final GetRiskAssessmentForViewByFarmerCode detailInfo = (GetRiskAssessmentForViewByFarmerCode) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_child, null);
        }


        TextView textViewChild = convertView.findViewById(R.id.txt_title);
        TextView txtQuestionSN = convertView.findViewById(R.id.txt_question_sn);
        TextView txtAnswer = convertView.findViewById(R.id.txt_ans);
        capitalize(textViewChild.getText().toString() );
        capitalize(txtQuestionSN.getText().toString());
        capitalize(txtAnswer.getText().toString());
        textViewChild.setText(detailInfo.getQuestions() + " ?");
        String strSerialNo = String.valueOf(detailInfo.getSerialNo());
        txtQuestionSN.setText(strSerialNo);
        txtAnswer.setText(detailInfo.getAnswers());
//        final String s_id = detailInfo.getId();
//        final String link = detailInfo.getWebLink();
//        final String sub_cat_name = detailInfo.getSub_categorie();
//        final String image_url = DZ_URL.IMAGE_BASEURL + detailInfo.getImage();
//        final String st_desc = detailInfo.getDescription();

                Typeface regular = Typeface.createFromAsset(context.getAssets(),
                "font/montserrat_regular.ttf");
        textViewChild.setTypeface(regular, Typeface.NORMAL);
        txtQuestionSN.setTypeface(regular, Typeface.NORMAL);
        txtAnswer.setTypeface(regular, Typeface.NORMAL);

//        textViewChild.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (detailInfo.getId().equalsIgnoreCase("1")) {
//
////                    Intent i = new Intent(context, ALL_About_Activity.class);
////                    i.putExtra("id",s_id);
////                    i.putExtra("web_link",link);
////                    i.putExtra("cat_name",sub_cat_name);
////                    i.putExtra("image_url",image_url);
////                    i.putExtra("desc",st_desc);
////                    context.startActivity(i);
//
//
//            }
//        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        return this.listDataChild.get(this.listDataGroup.get(groupPosition))
//                .size();
//        ArrayList<CafeSubCatModel> productList = arrayList.get(groupPosition).getCafeSubCatModels();
//        return productList.size();
        return questionSurveyList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.questionsMainCategoryList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.questionsMainCategoryList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
//        String headerTitle = (String) getGroup(groupPosition);
        String headerTitle = questionsMainCategoryList.get(groupPosition).getQuestionCategory();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_group, null);
        }

        TextView textViewGroup = convertView.findViewById(R.id.textViewGroup);
        ImageView expandable_icon = convertView.findViewById(R.id.imgViewGroup);

        textViewGroup.setTypeface(null, Typeface.BOLD);
        textViewGroup.setText(headerTitle);
        capitalize(textViewGroup.getText().toString());
        Typeface regular = Typeface.createFromAsset(context.getAssets(),
                "font/montserrat_regular.ttf");
        textViewGroup.setTypeface(regular, Typeface.NORMAL);

        return convertView;
    }
    private String capitalize(String capString){
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()){
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
