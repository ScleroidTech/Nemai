package com.scleroid.nemai.adapter.recyclerview;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ramotion.garlandview.TailAdapter;
import com.scleroid.nemai.R;
import com.scleroid.nemai.data.models.Address;
import com.scleroid.nemai.data.models.OrderedCourier;
import com.scleroid.nemai.data.models.Parcel;
import com.scleroid.nemai.viewholders.ParcelHolderForAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter which holds the outer Recyclerview
 * @see com.ramotion.garlandview.TailRecyclerView
 * which implements
 * @see  TailAdapter
 * @author Ganesh Kaple
 * @since 15-11-2017
 */

public class ParcelAdapterForAddress extends TailAdapter<ParcelHolderForAddress> {

    private static final int EMPTY_VIEW = 10;
    private static final String TAG = "scleroid.nemai.outerAdapter";
    private final RecyclerView.RecycledViewPool mPool;

    private int poolSize = 1;
    private List<List<Address>> addressesList;
    private List<Address> addresses;
    private List<Parcel> parcels;
    private List<Address> selectedAddress;
    private List<OrderedCourier> orderedCourierList;

    /**
     * Constructor for ParcelAdapterForAddress
     *
     * @param addresses list of addresses
     * @param parcels   list of parcels
     */

    public ParcelAdapterForAddress(List<Address> addresses, List<Parcel> parcels) {
        this.addresses = addresses;
        this.parcels = parcels;
        //  this.selectedAddress = selectedAddress;
        addressesList = sortAddresses(parcels, addresses);
        poolSize = addresses.size()/* + parcels.size()*/;
        mPool = new RecyclerView.RecycledViewPool();
        mPool.setMaxRecycledViews(0, poolSize);
    }

    /**
     * Getter for AddressesList
     *
     * @return List of List<Address>
     */
    public List<List<Address>> getAddressesList() {
        return addressesList;
    }

    /**
     * setter for AddressesList
     *
     * @param addressesList the list of addresses
     */
    public void setAddressesList(List<List<Address>> addressesList) {
        this.addressesList = addressesList;
    }

    /**
     * getter for AddressList
     *
     * @return List of Address
     */
    public List<Address> getAddresses() {
        return addresses;
    }

    /**
     * setter for AddressList
     *
     * @param addresses list of address
     */
    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    /**
     * getter for ParcelList
     *
     * @return List of parcels
     */
    public List<Parcel> getParcels() {
        return parcels;
    }

    /**
     * setter for parcelList
     *
     * @param parcels list of parcels
     */
    public void setParcels(List<Parcel> parcels) {
        this.parcels = parcels;
    }

    @SuppressLint("LongLogTag")
    public void updateAddressList(List<Address> addresses) {

        setAddressesList(sortAddresses(this.parcels, addresses));
        Log.d(TAG, " Address list updated");
        // notifyDataSetChanged();

    }

    /**
     * getter for selectedAddress
     *
     * @return List of Address
     */
    public List<Address> getSelectedAddress() {
        return selectedAddress;
    }

    /**
     * setter for selectedAddress
     *
     * @param selectedAddress list of selectedAddress
     */
    public void setSelectedAddress(List<Address> selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    /**
     * getter for OrderedCouriersList
     * @return List of OrderedCourier
     */
    public List<OrderedCourier> getOrderedCourierList() {
        return orderedCourierList;
    }

    /**
     * setter for OrderedCouriersList
     * @param orderedCourierList  List of OrderedCourierList
     */
    public void setOrderedCourierList(List<OrderedCourier> orderedCourierList) {
        this.orderedCourierList = orderedCourierList;
    }

    /**
     * creates the
     * @see com.ramotion.garlandview.header.HeaderItem ViewHolder for the
     * @see com.ramotion.garlandview.TailRecyclerView
     * @param parent viewgroup object
     * @param viewType type of view is passed, which can be set by calling
     * @see #getItemViewType(int position)
     * @return
     */

    @Override
    public ParcelHolderForAddress onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parcel_view, parent, false);
        //   binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_parcel_view, parent, false);
        Log.d("innerItem", "data " + addresses.size());
        //  binding.setDataset(addresses);

        return new ParcelHolderForAddress(view, mPool);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(ParcelHolderForAddress holder, int position) {
        holder.setIsRecyclable(true);
        //holder.itemView.setTag(parcels.get(position));
        //  holder.selectedAddressList = selectedAddress;
        OrderedCourier thatOrderedCourier = null;
        if (orderedCourierList != null && !orderedCourierList.isEmpty()) {
            //TODO there's an issue here
            //  thatOrderedCourier = orderedCourierList.get(position);
        }
            if (addressesList.size() == 0)
                holder.setContent(parcels.get(position), position, parcels.size(), thatOrderedCourier);
            else {
                holder.setContent(addressesList.get(position), parcels.get(position), position, parcels.size(), thatOrderedCourier);

                if (thatOrderedCourier != null &&  thatOrderedCourier.getAddress() != null) {
                    Log.d(TAG, "I'm adding address to selectedAddresses");
                    holder.updateSelectedAddressList(thatOrderedCourier.getAddress());
                }
            }




    }


    @Override
    public void onViewRecycled(ParcelHolderForAddress holder) {
        //  holder.clearContent();
    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }


    public List<List<Address>> sortAddresses(List<Parcel> parcels, List<Address> innerData) {
        List<Address> tempList = new ArrayList<>();
        List<List<Address>> outerData = new ArrayList<>();
        for (Parcel parcel : parcels) {
            for (Address address : innerData) {
                // if (parcel.getDestinationPin().equals(address.getPincode()))
                    tempList.add(address);

            }
            outerData.add(tempList);
        }
        return outerData;
    }

}


