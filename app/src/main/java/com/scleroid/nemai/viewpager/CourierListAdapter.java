package com.scleroid.nemai.viewpager;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.scleroid.nemai.R;
import com.scleroid.nemai.models.Courier;

import java.util.List;

/**
 * Created by ganesh on 2/1/18.
 */
@Deprecated
public class CourierListAdapter extends RecyclerView.Adapter<CourierListHolder> {
    private final List<Courier> couriers;

    public CourierListAdapter(List<Courier> couriers) {
        this.couriers = couriers;
    }

    @Override
    public CourierListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_courier_card_view, parent, false);
        //    Log.d("innerItem", "data " + mData.size());


        //    Log.d("innerItem", "is it here? onCreateViewHolder" + mData.size());
        return new CourierListHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(CourierListHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
