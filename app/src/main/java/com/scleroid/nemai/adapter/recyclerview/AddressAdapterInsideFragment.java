package com.scleroid.nemai.adapter.recyclerview;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
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
import com.scleroid.nemai.models.OrderedCourier;
import com.scleroid.nemai.viewholders.AddressHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ganesh on 15-11-2017.
 */

public class AddressAdapterInsideFragment extends com.ramotion.garlandview.inner.InnerAdapter<AddressHolder> {

    private static final int EMPTY_VIEW = 10;
    private static final String TAG = "innerAdapter";
    public static int lastSelectedPosition = -1;
    ItemInnerAddressCardBinding binding;
    /**
     * variable to hold selected Item position
     */

    int selectedItemPosition = -1;
    private List<Address> addresses = new ArrayList<>();
    private List<Address> selectedAddresses = new ArrayList<>();
    private View mEmptyView;
    private View innerLayout;
    private RadioButton lastChecked = null;
    private Button deliverButton = null;
    // ...
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private boolean activate;
    private Button buttonDeliver;
    private Context context;

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }


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
    /*public class EmptyViewHolder extends AddressHolder {
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
    public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* if (viewType == EMPTY_VIEW) {
            final ItemEmptyAddressViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_empty_address_view,parent,false);
            return new EmptyViewHolder(binding.getRoot());
        }*/
        //    addresses = new ArrayList<>();
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_inner_address_card, parent, false);
        //    Log.d("innerItem", "data " + addresses.size());

        //    Log.d("innerItem", "is it here? onCreateViewHolder" + addresses.size());
        return new AddressHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(final AddressHolder holder, final int position) {

        //   holder.setIsRecyclable(false);
        Address address = addresses.get(position);
        // Log.d("innerItem", "is it here? onBindViewHolder" + addresses.size() + "  position " + position);
        // if (position < addresses.size() && !addresses.isEmpty())
        holder.setContent(address);
        context = holder.getInnerLayout().getContext();
        holder.itemView.setTag(address);
        holder.setSelectedItemPosition(selectedItemPosition);
        holder.radioButton.setVisibility(View.GONE);
        holder.deliverButtton.setVisibility(View.INVISIBLE);
        //  setSelectedAddress();

      /*      if (selectedAddresses.contains(addresses.get(position))) {
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
*/
        // holder.itemView.setActivated(selectedItems.get(position, false));
        //TODO disable alre
        // ady activated view, then activate second view, reduce response time
        // Use getSelectedItems for the purpose


    }

    public void setSelection(OrderedCourier selection) {
    }

    // Swap itemA with itemB
    public void swapItems(int position) {
        //make sure to check if dataset is null and if itemA and itemB are valid indexes.
        //      List<Address> temp = new ArrayList<>();
        try {
            if (position != 0 && selectedAddresses.contains(addresses.get(position))) {
                Address itemA = addresses.get(position);
                Address itemB = addresses.get(0);
                addresses.set(position, itemB);
                addresses.set(0, itemA);
           /* addresses.add(0, itemA);
            addresses.remove(tail.lastIndexOf(itemA));*/

                //   notifyItemMoved(position, 0);
            }//This will trigger onBindViewHolder method from the adapter.
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "Array Out of Bound " + e);
        }
    }

    public void toggleSelection(int pos) {
        boolean check = !selectedItems.get(pos);
        selectedItems.put(pos, check);
        /*if (!selectedItems.get(pos))
        selectedItems.put(pos, true);
        else selectedItems.put(pos,false);*/

        notifyItemChanged(pos);
    }


    @Override
    public int getItemCount() {

        //       Log.d("innerItem", "is it here? getItemCOunt" + addresses.size());
        return addresses.size();
    }

    /**
     * TODO Need to override it for multiple view support
     * Returns the view Type to be displayed,
     *
     * @param position the position of adapter
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        //     Log.d("innerItem", "is it here? getItemViewType" + addresses.size());
        return position;
        //return R.layout.item_inner_address_card;
    }

    public void addData(@Nullable List<Address> innerDataList, List<Address> selected) {
        final int size = addresses.size();
        addresses = innerDataList;
        if (selected != null && !selected.isEmpty())
            selectedAddresses = selected;
        // swapItems(addresses.indexOf(selected.get(0)));
        Log.d("innerItem", "is it here? addData" + addresses.size());

        notifyDataSetChanged();
        //notifyItemRangeInserted(size, innerDataList.size());
    }

    public void updateSelectedData(int position, List<Address> selected) {

//        checkDifference(selectedAddresses, selected);
        selectedAddresses = selected;
        // swapItems(position);
        //      Log.d("innerItem", "is it here? addData" + addresses.size());

        notifyDataSetChanged();
        //notifyItemRangeInserted(size, innerDataList.size());
    }

    /**
     * TODO Future Implementation
     * Difference Util instead of notifyDataSetChanged,
     * to improve Performance
     *
     * @param oldItems
     * @param items
     */
    private void checkDifference(List<Address> oldItems, List<Address> items) {
        DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldItems.size();
            }

            @Override
            public int getNewListSize() {
                return items.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldItems.get(oldItemPosition).equals(items.get(newItemPosition));
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldItems.get(oldItemPosition).equals(items.get(newItemPosition));
            }
        }).dispatchUpdatesTo(this);

    }


    public void clearData() {
        addresses.clear();
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
