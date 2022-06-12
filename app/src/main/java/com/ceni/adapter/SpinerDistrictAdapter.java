package com.ceni.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceni.model.District;

import java.util.List;

public class SpinerDistrictAdapter extends BaseAdapter {
    private LayoutInflater flater;
    private List<District> list;
    private int listItemLayoutResource;
    private int textViewLabelId;
    private int textViewCodeId;

    public SpinerDistrictAdapter(Activity context, int listItemLayoutResource,
                                 int textViewLabelId, int textViewCodeId,
                                 List<District> list) {
        this.listItemLayoutResource = listItemLayoutResource;

        this.textViewLabelId = textViewLabelId;
        this.textViewCodeId = textViewCodeId;
        this.list = list;
        this.flater = context.getLayoutInflater();
    }


    @Override
    public int getCount() {
        if(this.list == null)  {
            return 0;
        }
        return this.list.size();
    }

    @Override
    public Object getItem(int position){
        return this.list.get(position);
    }

    @Override
    public long getItemId(int position) {
        District district = (District) this.getItem(position);
        return district.getId_district();
        // return position; (Return position if you need).
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        District district = (District) getItem(position);
        View rowView = this.flater.inflate(this.listItemLayoutResource, null,true);
        TextView textViewItemName = (TextView) rowView.findViewById(this.textViewLabelId);
        textViewItemName.setText(district.getLabel_district());
        TextView textViewItemPercent = (TextView) rowView.findViewById(textViewCodeId);
        textViewItemPercent.setText(district.getCode_district());

        return rowView;
    }
}
