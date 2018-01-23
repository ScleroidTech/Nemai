package com.scleroid.nemai.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scleroid.nemai.R;
import com.scleroid.nemai.models.OrderedCourier;
import com.scleroid.nemai.viewholders.OrderHolder;

import java.util.List;

/**
 * @author Ganesh Kaple
 * @since 20-01-2018
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderHolder> {
    private List<OrderedCourier> orderedCouriers;

    public OrderAdapter(List<OrderedCourier> orderedCouriers) {
        this.orderedCouriers = orderedCouriers;
    }

    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_view, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return orderedCouriers.size();
    }

    public void updateOrderList(List<OrderedCourier> parcels) {
        this.orderedCouriers = parcels;
        notifyDataSetChanged();
    }
}
