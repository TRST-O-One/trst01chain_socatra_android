package com.socatra.intellitrack.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;

import com.socatra.intellitrack.models.get.GetGrnByDealerId;

import java.util.ArrayList;
import java.util.List;


import android.widget.Filter;

public class CustomSpinnerAdapter extends ArrayAdapter<GetGrnByDealerId> implements Filterable {
    private List<GetGrnByDealerId> originalData;
    private List<GetGrnByDealerId> filteredData;
    private ItemFilter filter = new ItemFilter();

    public CustomSpinnerAdapter(Context context, int resource, List<GetGrnByDealerId> items) {
        super(context, resource, items);
        originalData = new ArrayList<>(items);
        filteredData = new ArrayList<>();
        filteredData.addAll(originalData);
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public GetGrnByDealerId getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Implement your custom view for the selected item here
        // Example: TextView textView = (TextView) super.getView(position, convertView, parent);
        // Customize textView to display what you want
        // Return the customized view
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Implement your custom view for the dropdown items here
        // Example: TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        // Customize textView to display what you want
        // Return the customized view
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = originalData;
                results.count = originalData.size();
            } else {
                String filterString = constraint.toString().toLowerCase();
                List<GetGrnByDealerId> filteredList = new ArrayList<>();
                for (GetGrnByDealerId item : originalData) {
                    if (item.getGRNnumber().toLowerCase().contains(filterString)) {
                        filteredList.add(item);
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData.clear();
            filteredData.addAll((List<GetGrnByDealerId>) results.values);
            notifyDataSetChanged();
        }
    }
}


