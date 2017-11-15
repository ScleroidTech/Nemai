package com.scleroid.nemai.outer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ramotion.garlandview.TailAdapter;
import com.scleroid.nemai.R;
import com.scleroid.nemai.inner.InnerModel;

import java.util.List;

/**
 * Created by Ganesh on 15-11-2017.
 */

public class OuterAdapter extends TailAdapter<OuterItem> {

    private final int POOL_SIZE = 16;

    private final List<List<InnerModel>> mData;
    private final RecyclerView.RecycledViewPool mPool;

    public OuterAdapter(List<List<InnerModel>> mData) {
        this.mData = mData;
        mPool = new RecyclerView.RecycledViewPool();
        mPool.setMaxRecycledViews(0, POOL_SIZE);
    }

    @Override
    public OuterItem onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new OuterItem(view, mPool);
    }

    @Override
    public void onBindViewHolder(OuterItem holder, int position) {
        holder.setContent(mData.get(position));
    }

    @Override
    public void onViewRecycled(OuterItem holder) {
        holder.clearContent();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_outer;
    }

}


