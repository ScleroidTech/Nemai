package com.scleroid.nemai.outer;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ramotion.garlandview.TailAdapter;
import com.scleroid.nemai.R;
import com.scleroid.nemai.databinding.ItemOuterBinding;
import com.scleroid.nemai.models.Address;
import com.scleroid.nemai.models.Parcel;

import java.util.List;

/**
 * Created by Ganesh on 15-11-2017.
 */

public class OuterAdapter extends TailAdapter<OuterItem> {

    private static final int EMPTY_VIEW = 10;
    private final int POOL_SIZE = 16;
    private final List<List<Address>> mData;
    private final List<Parcel> parcels;
    private final RecyclerView.RecycledViewPool mPool;
    ItemOuterBinding binding;

    public OuterAdapter(List<List<Address>> mData, List<Parcel> parcels) {
        this.mData = mData;
        this.parcels = parcels;
        mPool = new RecyclerView.RecycledViewPool();
        mPool.setMaxRecycledViews(0, POOL_SIZE);
    }

    @Override
    public OuterItem onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        Log.d("innerItem", "data " + mData.size());
        //  binding.setDataset(mData);

        return new OuterItem(view, mPool);
    }

    @Override
    public void onBindViewHolder(OuterItem holder, int position) {


        if (mData.size() == 0) holder.setContent(parcels.get(position), position, parcels.size());
        else
            holder.setContent(mData.get(position), parcels.get(position), position, parcels.size());
    }

    @Override
    public void onViewRecycled(OuterItem holder) {
        holder.clearContent();
    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }

    @Override
    public int getItemViewType(int position) {

        return R.layout.item_outer;
    }


}


