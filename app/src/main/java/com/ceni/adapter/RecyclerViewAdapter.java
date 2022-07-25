package com.ceni.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ceni.recensementnumerique.R;
//import com.google.android.ads.mediationtestsuite.viewmodels.ItemViewHolder;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    final int VIEW_TYPE_LOADING = 0;
    final int VIEW_TYPE_ITEM = 1;
    public List<String> mItemList;

    public RecyclerViewAdapter(List<String> itemList){
        mItemList = itemList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_electeur, parent, false);
            return new ItemViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof ItemViewHolder)
        populateItemRows((ItemViewHolder) viewHolder, position);
    }

    public int getItemViewType(int position){
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    @Override
    public int getItemCount() {
        return mItemList==null ? 0 : mItemList.size();
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.nomElect);
        }
    }

    private class LoadingHolder extends RecyclerView.ViewHolder {

        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private void populateItemRows(ItemViewHolder viewHolder, int position){
        String item =mItemList.get(position);
        viewHolder.tvItem.setText(item);
    }
}
