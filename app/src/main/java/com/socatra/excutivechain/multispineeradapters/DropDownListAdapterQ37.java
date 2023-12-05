package com.socatra.excutivechain.multispineeradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.socatra.excutivechain.R;
import com.socatra.excutivechain.activity.RiskAssessmentActivity;

import java.util.ArrayList;

public class DropDownListAdapterQ37 extends BaseAdapter {

	private ArrayList<String> mListItems;
	private LayoutInflater mInflater;
	private TextView mSelectedItems;
	private static int selectedCount = 0;
	private static String firstSelected = "";
	private ViewHolder holder;
	private static String selected = "";	//shortened selected values representation

	public static String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		DropDownListAdapterQ37.selected = selected;
	}

	public DropDownListAdapterQ37(Context context, ArrayList<String> items,
                                  TextView tv) {
		mListItems = new ArrayList<String>();
		mListItems.addAll(items);
		mInflater = LayoutInflater.from(context);
		mSelectedItems = tv;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.drop_down_list_row, null);
			holder = new ViewHolder();
			holder.tv = (TextView) convertView.findViewById(R.id.select_option);
			holder.chkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv.setText(mListItems.get(position));

		final int position1 = position;
		
		//whenever the checkbox is clicked the selected values textview is updated with new selected values
		holder.chkbox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setText(position1);
			}
		});

		if(RiskAssessmentActivity.checkSelected37[position])
			holder.chkbox.setChecked(true);
		else
			holder.chkbox.setChecked(false);	 
		return convertView;
	}


	/*
	 * Function which updates the selected values display and information(checkSelected[])
	 * */
	private void setText(int position1){
		if (!RiskAssessmentActivity.checkSelected37[position1]) {
			RiskAssessmentActivity.checkSelected37[position1] = true;
			selectedCount++;
		} else {
			RiskAssessmentActivity.checkSelected37[position1] = false;
			selectedCount--;
		}

		if (selectedCount == 0) {
			mSelectedItems.setText(R.string.select_string);
		} else if (selectedCount == 1) {
			for (int i = 0; i < RiskAssessmentActivity.checkSelected37.length; i++) {
				if (RiskAssessmentActivity.checkSelected37[i] == true) {
					firstSelected = mListItems.get(i);
					break;
				}
			}
			mSelectedItems.setText(firstSelected);
			setSelected(firstSelected);
		} else if (selectedCount > 1) {
			StringBuilder selectedItemsText = new StringBuilder();

			for (int i = 0; i < RiskAssessmentActivity.checkSelected37.length; i++) {
				if (RiskAssessmentActivity.checkSelected37[i]) {
					String selectedItem = mListItems.get(i);

					if (selectedItemsText.length() > 0) {
						selectedItemsText.append(", ");
					}
					selectedItemsText.append(selectedItem);
				}
			}

			mSelectedItems.setText(selectedItemsText.toString());
			setSelected(selectedItemsText.toString());
//			for (int i = 0; i < RiskAssessmentActivity.checkSelected37.length; i++) {
//				if (RiskAssessmentActivity.checkSelected37[i] == true) {
//					firstSelected = mListItems.get(i);
//
//					break;
//				}
//			}
//		    mSelectedItems.setText(firstSelected + " & "+ (selectedCount - 1) + " more");
//			setSelected(firstSelected + " & "+ (selectedCount - 1) + " more");


//			List<StateVO> selectedItems = myAdapter.getSelectedItems();
//			StringBuilder selectedText = new StringBuilder();
//			selectedText.append(mListItems.get(position1)).append(", ");
//			for (int i=position1 ; i<mListItems.size();i++)
//			{
//				selectedText.append(mListItems.get(position1)).append(", ");
//			}


		//	Log.d(TAG, "onItemSelected: selectItems" + selectedText);
			// Remove trailing comma and space
//			if (selectedText.length() > 2) {
//				selectedText.setLength(selectedText.length() - 2);
//				mSelectedItems.setText(selectedText.toString());
//			//	Log.d(TAG, "onItemSelected: select" + selectedText);
//			}


			//     spActivities.set
			// (  (TextView) findViewById(R.id.txt_data)).setText(selectedText.toString());
		}
	}

	private class ViewHolder {
		TextView tv;
		CheckBox chkbox;
	}
}
