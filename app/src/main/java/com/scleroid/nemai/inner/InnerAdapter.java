package com.scleroid.nemai.inner;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

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
    private static final String TAG = "innerAdapter";
    public static int lastSelectedPosition = -1;
    ItemInnerAddressCardBinding binding;
    private List<Address> mData = new ArrayList<>();
    private List<Address> mDataSelected = new ArrayList<>();

    private View mEmptyView;
    private View innerLayout;
    private RadioButton lastChecked = null;
    private Button deliverButton = null;
    // ...
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private boolean activate;
    private Button buttonDeliver;
    private Context context;


   /* public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }*/

    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }
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

        selectedItems = new SparseBooleanArray();
        //    Log.d("innerItem", "is it here? onCreateViewHolder" + mData.size());
        return new InnerItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(final InnerItem holder, final int position) {

        //  holder.setIsRecyclable(false);
        Address address = mData.get(position);
        Log.d("innerItem", "is it here? onBindViewHolder" + mData.size() + "  position " + position);
        // if (position < mData.size() && !mData.isEmpty())
        holder.setContent(address);
        context = holder.getInnerLayout().getContext();
        holder.itemView.setTag(address);
        //  setRadioSelected(holder, position);

        if (mDataSelected.contains(mData.get(position))) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
            holder.itemView.setActivated(true);
            holder.radioButton.setChecked(true);
            holder.deliverButtton.setEnabled(true);
            holder.deliverButtton.setVisibility(View.VISIBLE);

        } else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.list_item_normal_state));
            holder.itemView.setActivated(false);
            holder.radioButton.setChecked(false);

            holder.deliverButtton.setEnabled(false);
            holder.deliverButtton.setVisibility(View.INVISIBLE);
        }

        // holder.itemView.setActivated(selectedItems.get(position, false));
        //TODO disable alre
        // ady activated view, then activate second view, reduce response time
        // Use getSelectedItems for the purpose


    }

    // Swap itemA with itemB
    public void swapItems(int position) {
        //make sure to check if dataset is null and if itemA and itemB are valid indexes.
        //      List<Address> temp = new ArrayList<>();
        try {
            if (position != 0 && mDataSelected.contains(mData.get(position))) {
                Address itemA = mData.get(position);
                Address itemB = mData.get(0);
                mData.set(position, itemB);
                mData.set(0, itemA);
           /* mData.add(0, itemA);
            mData.remove(tail.lastIndexOf(itemA));*/

                //   notifyItemMoved(position, 0);
            }//This will trigger onBindViewHolder method from the adapter.
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "Array Out of Bound " + e);
        }
    }


    public void activateButtons(boolean activate) {
        this.activate = activate;
        notifyDataSetChanged(); //need to call it for the child views to be re-created with buttons.
    }


    public void setRadioSelected(InnerItem holder, int position) {
        lastSelectedPosition = position;
        RadioButton tempRadio = holder.radioButton;
        Button tempButton = holder.deliverButtton;

        lastChecked = tempRadio;
        buttonDeliver = tempButton;
        // lastChecked.setChecked(true);
        // deliverButton.setVisibility(View.VISIBLE);
        //holder.radioButton.setChecked(lastSelectedPosition == position);
        //holder.radioButton.setChecked(lastSelectedPosition == position);
        // notifyItemChanged(lastSelectedPosition);

    }


    @Override
    public void onViewRecycled(InnerItem holder) {
        // Log.d("innerItem", " onVIewRecycled" + mData.size());
        //  holder.clearContent();
    }

    @Override
    public int getItemCount() {

        //       Log.d("innerItem", "is it here? getItemCOunt" + mData.size());
        return mData.size();
    }


    @Override
    public int getItemViewType(int position) {
        //     Log.d("innerItem", "is it here? getItemViewType" + mData.size());
        return R.layout.item_inner_address_card;
    }

    public void addData(@Nullable List<Address> innerDataList, List<Address> selected) {
        final int size = mData.size();
        mData = innerDataList;
        mDataSelected = selected;
        if (!mDataSelected.isEmpty())
            swapItems(mData.indexOf(selected.get(0)));
        //      Log.d("innerItem", "is it here? addData" + mData.size());

        notifyDataSetChanged();
        //notifyItemRangeInserted(size, innerDataList.size());
    }

    public void updateSelectedData(int position, List<Address> selected) {


        mDataSelected = selected;
        // swapItems(position);
        //      Log.d("innerItem", "is it here? addData" + mData.size());

        notifyDataSetChanged();
        //notifyItemRangeInserted(size, innerDataList.size());
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


/*
    public void onLongPress(MotionEvent e) {
        View view =
                recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (actionMode != null) {
            return;
        }
        actionMode =
                startActionMode(RecyclerViewDemoActivity.this);
        int idx = recyclerView.getChildPosition(view);
        myToggleSelection(idx);
        super.onLongPress(e);
    }

    private void myToggleSelection(int idx) {
        adapter.toggleSelection(idx);
        String title = getString(
                R.string.selected_count,
                adapter.getSelectedItemCount());
        actionMode.setTitle(title);
    }

*/

}
