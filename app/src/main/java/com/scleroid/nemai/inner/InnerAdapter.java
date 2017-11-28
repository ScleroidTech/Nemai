package com.scleroid.nemai.inner;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scleroid.nemai.R;
import com.scleroid.nemai.databinding.ItemInnerAddressCardBinding;
import com.scleroid.nemai.models.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ganesh on 15-11-2017.
 */

public class InnerAdapter extends com.ramotion.garlandview.inner.InnerAdapter<InnerItem> {

    private static final int EMPTY_VIEW = 10;
    public static int lastSelectedPosition = -1;
    ItemInnerAddressCardBinding binding;
    private List<Address> mData = new ArrayList<>();
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
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        //    Log.d("innerItem", "data " + mData.size());

        //    Log.d("innerItem", "is it here? onCreateViewHolder" + mData.size());
        return new InnerItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(InnerItem holder, final int position) {

        holder.setIsRecyclable(false);
        Log.d("innerItem", "is it here? onBindViewHolder" + mData.size() + "  position " + position);
        if (position < mData.size() && !mData.isEmpty())
            holder.setContent(mData.get(position));

        binding.setDiff((position >= mData.size() || mData.isEmpty()) ? 1 : 0);
        // bindViewHolder(holder,position);
        holder.radioButton.setChecked(lastSelectedPosition == position);
        holder.innerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRadioSelected(position);
            }
        });


    }


    public void setRadioSelected(int position) {
        lastSelectedPosition = position;
        notifyDataSetChanged();

    }


    @Override
    public void onViewRecycled(InnerItem holder) {
        // Log.d("innerItem", " onVIewRecycled" + mData.size());
        holder.clearContent();
    }

    @Override
    public int getItemCount() {

        //       Log.d("innerItem", "is it here? getItemCOunt" + mData.size());
        return mData.size() + 1;
    }

    //TODO getItemCOunt, onBindViewHolder, & onCreateViewHolder doesn'tget called at all.
    @Override
    public int getItemViewType(int position) {
        //     Log.d("innerItem", "is it here? getItemViewType" + mData.size());
        return R.layout.item_inner_address_card;
    }

    public void addData(@Nullable List<Address> innerDataList) {
        final int size = mData.size();
        mData = innerDataList;
        //      Log.d("innerItem", "is it here? addData" + mData.size());

        notifyDataSetChanged();
        // notifyItemRangeInserted(size, innerDataList.size());
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

    public void changeOfDataset() {
        notifyDataSetChanged();

    }

}
