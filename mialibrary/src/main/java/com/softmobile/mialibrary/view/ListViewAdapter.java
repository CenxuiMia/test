package com.softmobile.mialibrary.view;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

abstract public class ListViewAdapter implements ListAdapter {
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public int getCount() {
        return getItemCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = onCreateViewHolder(parent,0);
            viewHolder.getView().setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        onBindViewHolder(viewHolder,position);
        return viewHolder.getView();
    }

    public static class ViewHolder{
        private View view;

        public ViewHolder(View v){
            this.view = v;
        }
        final View getView(){
            return view;
        }
    }

    abstract public ViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewType);

    abstract public void onBindViewHolder(ViewHolder viewHolder,final int position);

    abstract public int getItemCount();
}
