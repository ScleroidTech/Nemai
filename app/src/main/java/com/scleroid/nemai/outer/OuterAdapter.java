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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ganesh on 15-11-2017.
 */

public class OuterAdapter extends TailAdapter<OuterItem> {

    private static final int EMPTY_VIEW = 10;
    private final int POOL_SIZE = 16;
    private final RecyclerView.RecycledViewPool mPool;
    ItemOuterBinding binding;
    private List<List<Address>> addressesList;
    private List<Address> addresses;
    private List<Parcel> parcels;


    public OuterAdapter(List<Address> addresses, List<Parcel> parcels) {
        this.addresses = addresses;
        this.parcels = parcels;
        addressesList = sortAddresses(parcels, addresses);
        mPool = new RecyclerView.RecycledViewPool();
        mPool.setMaxRecycledViews(0, POOL_SIZE);
    }

    @Override
    public OuterItem onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        Log.d("innerItem", "data " + addresses.size());
        //  binding.setDataset(addresses);

        return new OuterItem(view, mPool);
    }

    @Override
    public void onBindViewHolder(OuterItem holder, int position) {

        holder.itemView.setTag(parcels.get(position));
        if (addressesList.size() == 0)
            holder.setContent(parcels.get(position), position, parcels.size());
        else
            holder.setContent(addressesList.get(position), parcels.get(position), position, parcels.size());
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

    public List<Parcel> getParcels() {
        return parcels;
    }

    public void updateParcelList(List<Parcel> parcels) {
        this.parcels = parcels;
        //   updateAddressList(addresses);
        notifyDataSetChanged();

    }

    public void updateAddressList(List<Address> addresses) {

        this.addressesList = sortAddresses(this.parcels, addresses);
        notifyDataSetChanged();

    }

    public List<List<Address>> sortAddresses(List<Parcel> parcels, List<Address> innerData) {
        List<Address> tempList = new ArrayList<>();
        List<List<Address>> outerData = new ArrayList<>();
        for (Parcel parcel : parcels) {
            for (Address address : innerData) {
                //  if (parcel.getDestinationPin().equals(address.getPincode()))
                    tempList.add(address);

            }
            outerData.add(tempList);
        }
        return outerData;
    }

}


