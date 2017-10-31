package com.scleroid.nemai.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.scleroid.nemai.R;
import com.scleroid.nemai.other.PageHolder;

import java.util.ArrayList;

/**
 * Created by Ganesh on 31-10-2017.
 */

public class PagerAdapter extends RecyclerView.Adapter<PageHolder> {
    private static final String STATE_BUFFERS = "buffers";
    private static final int PAGE_COUNT = 10;
    private final RecyclerViewPager pager;
    private final LayoutInflater inflater;
    private final Context context;
    private ArrayList<String> buffers = new ArrayList<>();

    public PagerAdapter(RecyclerViewPager pager, LayoutInflater inflater, Context context) {
        this.pager = pager;
        this.inflater = inflater;


        this.context = context;
    }

    @Override
    public PageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return (new PageHolder(inflater.inflate(R.layout.list_parcel_recyclerview, parent, false), context));
    }

    @Override
    public void onBindViewHolder(PageHolder holder, int position) {
        holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return (PAGE_COUNT);
    }

    @Override
    public void onViewDetachedFromWindow(PageHolder holder) {
        super.onViewDetachedFromWindow(holder);

        //buffers.set(holder.getAdapterPosition(), holder.getText());
    }


}