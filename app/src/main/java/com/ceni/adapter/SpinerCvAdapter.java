package com.ceni.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceni.model.Commune;
import com.ceni.model.Cv;

import java.util.List;

public class SpinerCvAdapter extends BaseAdapter {
    private LayoutInflater flater;
    private List<Cv> list;
    private int listItemLayoutResource;
    private int textViewLabelId;
    private int textViewCodeId;

    public SpinerCvAdapter(Activity context, int listItemLayoutResource,
                           int textViewLabelId, int textViewCodeId,
                           List<Cv> list) {
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
        Cv cv = (Cv) this.getItem(position);
        return cv.getId_cv();
        // return position; (Return position if you need).
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Cv cv = (Cv) getItem(position);
        View rowView = this.flater.inflate(this.listItemLayoutResource, null,true);
        TextView textViewItemName = (TextView) rowView.findViewById(this.textViewLabelId);
        textViewItemName.setText(cv.getLabel_cv());
        TextView textViewItemPercent = (TextView) rowView.findViewById(textViewCodeId);
        textViewItemPercent.setText(cv.getCode_cv());

        return rowView;
    }
}
