package com.scleroid.nemai.inner;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scleroid.nemai.R;
import com.scleroid.nemai.databinding.ItemInnerAddressCardBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ganesh on 15-11-2017.
 */

public class InnerAdapter extends com.ramotion.garlandview.inner.InnerAdapter<InnerItem> {

    private static final int EMPTY_VIEW = 10;
    private List<InnerModel> mData = new ArrayList<>();
    private View mEmptyView;
    private View innerLayout;

    /*public class EmptyViewHolder extends InnerItem {
        public EmptyViewHolder(View itemView) {
            super(itemView);
            innerLayout =  ((ViewGroup) itemView).getChildAt(0);;
        }

        @Override
        protected View getInnerLayout() {
            return innerLayout;
        }
    }*/

    @Override
    public InnerItem onCreateViewHolder(ViewGroup parent, int viewType) {
       /* if (viewType == EMPTY_VIEW) {
            final ItemEmptyAddressViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_empty_address_view,parent,false);
            return new EmptyViewHolder(binding.getRoot());
        }*/
        //    mData = new ArrayList<>();
        final ItemInnerAddressCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        Log.d("innerItem", "data " + mData.size());
        binding.setDataset(mData);
        Log.d("innerItem", "is it here? onCreateViewHolder" + mData.size());
        return new InnerItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(InnerItem holder, int position) {
        if (!mData.isEmpty())
            holder.setContent(mData.get(position));
        Log.d("innerItem", "is it here? onBindViewHolder" + mData.size());

    }

    @Override
    public void onViewRecycled(InnerItem holder) {
        Log.d("innerItem", " onVIewRecycled" + mData.size());
        holder.clearContent();
    }

    @Override
    public int getItemCount() {
        Log.d("innerItem", "is it here? getItemCOunt" + mData.size());
        return mData.size();
    }

    //TODO getItemCOunt, onBindViewHolder, & onCreateViewHolder doesn'tget called at all.
    @Override
    public int getItemViewType(int position) {
        Log.d("innerItem", "is it here? getItemViewType" + mData.size());
        return R.layout.item_inner_address_card;
    }

    public void addData(@Nullable List<InnerModel> innerDataList) {
        //final int size = mData.size();
        mData = innerDataList;
        Log.d("innerItem", "is it here? addData" + mData.size());

        notifyDataSetChanged();
        //notifyItemRangeInserted(size, innerDataList.size());
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }


 /*   private void checkAdapterIsEmpty() {

        if (getAdapter().getItemCount() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }*/


}
