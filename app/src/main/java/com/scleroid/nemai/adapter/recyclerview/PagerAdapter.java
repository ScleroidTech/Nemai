package com.scleroid.nemai.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.scleroid.nemai.R;
import com.scleroid.nemai.data.models.Parcel;
import com.scleroid.nemai.viewholders.ParcelHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ganesh on 31-10-2017.
 */

public class PagerAdapter extends RecyclerView.Adapter<ParcelHolder> {
    private static final String STATE_BUFFERS = "buffers";
    private static final int PAGE_COUNT = 10;
    private final RecyclerViewPager pager;
    private final LayoutInflater inflater;
    private final Context context;
    public ParcelHolder holder;
    private List<Parcel> parcels;
    private ArrayList<String> buffers = new ArrayList<>();

    public PagerAdapter(RecyclerViewPager pager, LayoutInflater inflater, Context context, List<Parcel> parcels) {
        this.pager = pager;
        this.inflater = inflater;
        this.parcels = parcels;
        this.context = context;
    }

    @Override
    public ParcelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        holder = new ParcelHolder(inflater.inflate(R.layout.list_parcel_recyclerview, parent, false), context);
        return holder;
    }


    @Override
    public void onBindViewHolder(ParcelHolder holder, int position) {
        this.holder = holder;
        holder.itemView.setTag(parcels.get(position));
        holder.setListeners();

        Parcel parcel = parcels.get(position);
            holder.bindParcels(parcel);
            holder.bindNumber(position, parcels.size());


    }


    @Override
    public int getItemCount() {
        return parcels.size();
    }

    @Override
    public void onViewDetachedFromWindow(ParcelHolder holder) {
        super.onViewDetachedFromWindow(holder);

        //buffers.set(holder.getAdapterPosition(), holder.getText());
    }


    public void updateParcelList(List<Parcel> parcels) {
        this.parcels = parcels;
        notifyDataSetChanged();
    }
}