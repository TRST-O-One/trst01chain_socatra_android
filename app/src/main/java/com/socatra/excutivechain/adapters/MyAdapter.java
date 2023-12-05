package com.socatra.excutivechain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.socatra.excutivechain.R;
import com.socatra.excutivechain.models.StateVO;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<StateVO> {
    private Context mContext;
    private ArrayList<StateVO> listState;
    private MyAdapter myAdapter;
    private boolean isFromView = false;
    TextView txtDataValue;

    private static String selected = "";	//shortened selected values representation

    public static String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
       selected = selected;
    }


    public MyAdapter(Context context, int resource, List<StateVO> objects,TextView txtData) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<StateVO>) objects;
        this.myAdapter = this;
        this.txtDataValue =txtData;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.multi_spineer_check, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getTitle());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();

                if (!isFromView) {
                    listState.get(position).setSelected(isChecked);
                }
            }
        });


//        final StateVO stateVO = listState.get(position);
//        holder.checkBox.setText(stateVO.getTitle());
//        holder.checkBox.setChecked(stateVO.isSelected());
//
//        holder.checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stateVO.setSelected(holder.checkBox.isChecked());
//            }
//        });
        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
    public List<StateVO> getSelectedItems() {
        List<StateVO> selectedItems = new ArrayList<>();
        for (StateVO stateVO : listState) {
            if (stateVO.isSelected()) {
               // Log.e( "getSelectedItems: " ,stateVO.getTitle());
               // Log.d(TAG, "Selected Item: " + stateVO.getTitle());

                selectedItems.add(stateVO);
            }
        }
        return selectedItems;
    }

//    public interface SyncDocCallbackInterface {
//        //        void openScreenCallback(int position, FarmersTable farmerTable, List<FarmersTable> farmer, String applicationType);
////
////        void updateItemCallback(int position, FarmersTable applicationStatusTable, String strFarmerID);
//        void addDocDetailsCallback( String mDocId);
//    }
}
