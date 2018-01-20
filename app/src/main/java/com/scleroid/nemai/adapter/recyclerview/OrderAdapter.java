package com.scleroid.nemai.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scleroid.nemai.R;
import com.scleroid.nemai.viewholders.OrderHolder;

/**
 * @author Ganesh Kaple
 * @since 20-01-2018
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderHolder> {
    @Override
    public OrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_partner_view, parent, false);
        return new OrderHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
