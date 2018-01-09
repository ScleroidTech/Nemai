package com.scleroid.nemai.adapter.recyclerview;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ramotion.garlandview.TailAdapter;
import com.scleroid.nemai.R;
import com.scleroid.nemai.databinding.ItemOuterBinding;
import com.scleroid.nemai.models.Courier;
import com.scleroid.nemai.models.OrderedCourier;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.viewholders.ParcelHolderForCouriers;

import java.util.ArrayList;
import java.util.List;

import hugo.weaving.DebugLog;


/**
 * Adapter which holds the outer Recyclerview
 *
 * @author Ganesh Kaple
 * @see com.ramotion.garlandview.TailRecyclerView
 * which implements
 * @see TailAdapter
 * @since 09-01-2018
 */

public class ParcelAdapterForCouriers extends TailAdapter<ParcelHolderForCouriers> {

    private static final int EMPTY_VIEW = 10;
    private static final String TAG = "scleroid.nemai.outerAdapter";
    private final RecyclerView.RecycledViewPool mPool;
    ItemOuterBinding binding;
    private int poolSize = 1;
    private List<List<Courier>> courieresList;
    private List<Courier> courieres;
    private List<Parcel> parcels;
    private List<Courier> selectedCourier;
    private List<OrderedCourier> orderedCourierList;

    /**
     * Constructor for ParcelAdapterForCourier
     *
     * @param courieres list of courieres
     * @param parcels   list of parcels
     */
    @DebugLog
    public ParcelAdapterForCouriers(List<Courier> courieres, List<Parcel> parcels) {
        this.courieres = courieres;
        this.parcels = parcels;
        //  this.selectedCourier = selectedCourier;
        courieresList = sortCourieres(parcels, courieres);
        poolSize = courieres.size()/* + parcels.size()*/;
        mPool = new RecyclerView.RecycledViewPool();
        mPool.setMaxRecycledViews(0, poolSize);
    }

    /**
     * Getter for CourieresList
     *
     * @return List of List<Courier>
     */
    public List<List<Courier>> getCourieresList() {
        return courieresList;
    }

    /**
     * setter for CourieresList
     *
     * @param courieresList the list of courieres
     */
    public void setCourieresList(List<List<Courier>> courieresList) {
        this.courieresList = courieresList;
    }

    /**
     * getter for CourierList
     *
     * @return List of Courier
     */
    public List<Courier> getCourieres() {
        return courieres;
    }

    /**
     * setter for CourierList
     *
     * @param courieres list of courier
     */
    public void setCourieres(List<Courier> courieres) {
        this.courieres = courieres;
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
    public void updateCourierList(List<Courier> courieres) {

        setCourieresList(sortCourieres(this.parcels, courieres));
        Log.d(TAG, " Courier list updated");
        // notifyDataSetChanged();

    }

    /**
     * getter for selectedCourier
     *
     * @return List of Courier
     */
    public List<Courier> getSelectedCourier() {
        return selectedCourier;
    }

    /**
     * setter for selectedCourier
     *
     * @param selectedCourier list of selectedCourier
     */
    public void setSelectedCourier(List<Courier> selectedCourier) {
        this.selectedCourier = selectedCourier;
    }

    /**
     * getter for OrderedCouriersList
     *
     * @return List of OrderedCourier
     */
    public List<OrderedCourier> getOrderedCourierList() {
        return orderedCourierList;
    }

    /**
     * setter for OrderedCouriersList
     *
     * @param orderedCourierList List of OrderedCourierList
     */
    public void setOrderedCourierList(List<OrderedCourier> orderedCourierList) {
        this.orderedCourierList = orderedCourierList;
    }

    /**
     * creates the
     *
     * @param parent   viewgroup object
     * @param viewType type of view is passed, which can be set by calling
     * @return
     * @see com.ramotion.garlandview.header.HeaderItem ViewHolder for the
     * @see com.ramotion.garlandview.TailRecyclerView
     * @see #getItemViewType(int position)
     */

    @Override
    public ParcelHolderForCouriers onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parcel_view, parent, false);
        //   binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_parcel_view, parent, false);
        Log.d("innerItem", "data " + courieres.size());
        //  binding.setDataset(courieres);

        return new ParcelHolderForCouriers(view, mPool);
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onBindViewHolder(ParcelHolderForCouriers holder, int position) {
        holder.setIsRecyclable(true);
        //holder.itemView.setTag(parcels.get(position));
        //  holder.selectedCourierList = selectedCourier;
        OrderedCourier thatOrderedCourier = null;
        if (orderedCourierList != null && !orderedCourierList.isEmpty()) {
            thatOrderedCourier = orderedCourierList.get(position);
        }
        if (courieresList.size() == 0)
            holder.setContent(parcels.get(position), position, parcels.size(), thatOrderedCourier);
        else {
            holder.setContent(courieresList.get(position), parcels.get(position), position, parcels.size(), thatOrderedCourier);

            if (thatOrderedCourier != null && thatOrderedCourier.getCourier() != null) {
                Log.d(TAG, "I'm adding courier to selectedCourieres");
                holder.updateSelectedCourierList(thatOrderedCourier.getCourier());
            }
        }


    }


    @Override
    public void onViewRecycled(ParcelHolderForCouriers holder) {
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


    public List<List<Courier>> sortCourieres(List<Parcel> parcels, List<Courier> innerData) {
        List<Courier> tempList = new ArrayList<>();
        List<List<Courier>> outerData = new ArrayList<>();
        for (Parcel parcel : parcels) {
            for (Courier courier : innerData) {
                //  if (parcel.getDestinationPin().equals(courier.getPincode()))
                tempList.add(courier);

            }
            outerData.add(tempList);
        }
        return outerData;
    }

}



