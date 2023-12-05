package com.socatra.intellitrack.multiSelectAdapter;



import static com.socatra.intellitrack.activity.invoice.InvoiceManufactureActivity.checkSelectedBatchProcessor;
import static com.socatra.intellitrack.activity.invoice.InvoiceManufactureActivity.strSelectProcessorBatchId;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.socatra.intellitrack.R;
import com.socatra.intellitrack.database.entity.GetBatchDataFromServer;

import java.util.ArrayList;

public class ProcessorBatchDropDownListAdapter extends BaseAdapter {

	String TAG = ProcessorBatchDropDownListAdapter.class.getCanonicalName();

	private ArrayList<GetBatchDataFromServer> mListItems;
	private LayoutInflater mInflater;
	private TextView mSelectedItems;
	private static int selectedCount = 0;
	private static String firstSelected = "";
	private ViewHolder holder;
	private static String selected = "";	//shortened selected values representation

	TextView txtFinalSelectQuntity;

	String strSelectBatchId;




	public static String getSelected() {
		return selected;
	}

	public void setSelected(String selected) {
		ProcessorBatchDropDownListAdapter.selected = selected;
	}

	public ProcessorBatchDropDownListAdapter(Context context, ArrayList<GetBatchDataFromServer> items,
                                             TextView txtBatch, TextView txtQuantity, String SelectBatchId) {
		mListItems = new ArrayList<GetBatchDataFromServer>();
		mListItems.addAll(items);
		mInflater = LayoutInflater.from(context);
		mSelectedItems = txtBatch;
		txtFinalSelectQuntity = txtQuantity;
		strSelectBatchId = SelectBatchId;
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

		holder.tv.setText(mListItems.get(position).getBatchNo());




		final int position1 = position;

		//whenever the checkbox is clicked the selected values textview is updated with new selected values
		holder.chkbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setText(position1);
				updateTotalQuantity();
				updateSelectedValues();
			}
		});


		if(checkSelectedBatchProcessor[position])
		{
			holder.chkbox.setChecked(true);
		} else {
			holder.chkbox.setChecked(false);
		}


		return convertView;
	}

	private void updateTotalQuantity() {
		int qtyValue = 0;
		for (int i = 0; i < mListItems.size(); i++) {
			if (checkSelectedBatchProcessor[i]) {
				qtyValue += mListItems.get(i).getTotalQuantity();
			}
		}
		txtFinalSelectQuntity.setText(String.valueOf(qtyValue));
	}

	private void updateSelectedValues() {
		StringBuilder selectedValues = new StringBuilder();
		for (int i = 0; i < mListItems.size(); i++) {
			if (checkSelectedBatchProcessor[i]) {
				selectedValues.append(mListItems.get(i).getId()).append(", ");
			}
		}

		if (selectedValues.length() > 2) {
			// Remove the trailing comma and space
			selectedValues.setLength(selectedValues.length() - 2);
		}
		strSelectProcessorBatchId = selectedValues.toString();

		Log.d(TAG, "updateSelectedValues: batch" + strSelectProcessorBatchId);
	//	txtFinalSelectQuntity.setText(selectedValues.toString());
	}
	/*
	 * Function which updates the selected values display and information(checkSelected[])
	 * */
	private void setText(int position1){
		if (!checkSelectedBatchProcessor[position1]) {
			checkSelectedBatchProcessor[position1] = true;
			selectedCount++;
		} else {
			checkSelectedBatchProcessor[position1] = false;
			selectedCount--;
		}

		if (selectedCount == 0) {
			mSelectedItems.setText("select");
		} else if (selectedCount == 1) {
			for (int i = 0; i < checkSelectedBatchProcessor.length; i++) {
				if (checkSelectedBatchProcessor[i] == true) {
					firstSelected = mListItems.get(i).getBatchNo();
					break;
				}
			}
			mSelectedItems.setText(firstSelected);
			setSelected(firstSelected);
		} else if (selectedCount > 1) {
			StringBuilder selectedItemsText = new StringBuilder();

			for (int i = 0; i < checkSelectedBatchProcessor.length; i++) {
				if (checkSelectedBatchProcessor[i]) {
					String selectedItem = mListItems.get(i).getBatchNo();

					if (selectedItemsText.length() > 0) {
						selectedItemsText.append(", ");
					}
					selectedItemsText.append(selectedItem);
				}
			}

			mSelectedItems.setText(selectedItemsText.toString());
			setSelected(selectedItemsText.toString());

//			for (int i = 0; i < checkSelectedBatchProcessor.length; i++) {
//				if (checkSelectedBatchProcessor[i] == true) {
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
