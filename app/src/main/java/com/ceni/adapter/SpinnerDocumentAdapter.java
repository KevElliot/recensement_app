package com.ceni.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ceni.model.Commune;
import com.ceni.model.Document;

import java.util.List;

public class SpinnerDocumentAdapter extends BaseAdapter {
    private LayoutInflater flater;
    private List<Document> list;
    private int listItemLayoutResource;
    private int textViewLabelId;

    public SpinnerDocumentAdapter(Activity context, int listItemLayoutResource,
                                int textViewLabelId,
                                List<Document> list) {
        this.listItemLayoutResource = listItemLayoutResource;
        this.textViewLabelId = textViewLabelId;
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
        Document doc = (Document) this.getItem(position);
        //return doc.getDocreference();
         return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Document doc = (Document) this.getItem(position);
        View rowView = this.flater.inflate(this.listItemLayoutResource, null,true);
        TextView textViewItemName = (TextView) rowView.findViewById(this.textViewLabelId);
//        Log.i("xxx", "textview : "+textViewItemName);
//        Log.i("xxx", "commune : "+commune.getLabel_commune());
        textViewItemName.setText(doc.getDocreference());

        return rowView;
    }
}
