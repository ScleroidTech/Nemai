package com.scleroid.nemai.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scleroid.nemai.R;
import com.scleroid.nemai.viewholders.PartnerViewHolder;

import java.util.ArrayList;

/**
 * @author Ganesh Kaple
 * @since 19-01-2018
 */

public class PartnerAdapter extends RecyclerView.Adapter<PartnerViewHolder> {
    private final ArrayList logos;
    private final Context context;

    public PartnerAdapter(Context context, ArrayList logoList) {

        this.logos = logoList;
        this.context = context;
    }

    @Override
    public PartnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_partner_view, parent, false);
        return new PartnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PartnerViewHolder holder, int position) {
        holder.setPartnerLogo((Integer) logos.get(position));


    }

    @Override
    public int getItemCount() {
        return logos.size();
    }
}
