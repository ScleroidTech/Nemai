package com.scleroid.nemai.adapter.recyclerview;

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
import com.scleroid.nemai.databinding.ItemInnerCourierCardBinding;
import com.scleroid.nemai.models.Courier;
import com.scleroid.nemai.models.OrderedCourier;
import com.scleroid.nemai.viewholders.CourierHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ganesh Kaple
 * @since 10-01-2018
 */

public class CourierAdapter extends com.ramotion.garlandview.inner.InnerAdapter<CourierHolder> {

    private static final int EMPTY_VIEW = 10;
    private static final String TAG = "innerAdapter";
    public static int lastSelectedPosition = -1;
    ItemInnerCourierCardBinding binding;
    /**
     * variable to hold selected Item position
     */

    int selectedItemPosition = -1;
    private List<Courier> couriers = new ArrayList<>();
    private List<Courier> selectedCourierList = new ArrayList<>();
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
    /*public class EmptyViewHolder extends CourierHolder {
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
    public CourierHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       /* if (viewType == EMPTY_VIEW) {
            final ItemEmptyCourierViewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_empty_courier_view,parent,false);
            return new EmptyViewHolder(binding.getRoot());
        }*/
        //    couriers = new ArrayList<>();
        //TODO remove when reverting Changed this for testing
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_courier_card_view, parent, false);
        //    Log.d("innerItem", "data " + couriers.size());

        selectedItems = new SparseBooleanArray();
        //    Log.d("innerItem", "is it here? onCreateViewHolder" + couriers.size());
        return new CourierHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(final CourierHolder holder, final int position) {

        //   holder.setIsRecyclable(false);
        Courier courier = couriers.get(position);
        // Log.d("innerItem", "is it here? onBindViewHolder" + couriers.size() + "  position " + position);
        // if (position < couriers.size() && !couriers.isEmpty())

        context = holder.getInnerLayout().getContext();
        holder.setContent(courier, context);
        holder.itemView.setTag(courier);
        holder.setSelectedItemPosition(selectedItemPosition);

        //  setSelectedCourier();

        if (selectedCourierList.contains(couriers.get(position))) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
            holder.itemView.setActivated(true);
            holder.radioButton.setChecked(true);
            holder.selectButtton.setEnabled(true);
            holder.selectButtton.setVisibility(View.VISIBLE);

        } else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.list_item_normal_state));
            holder.itemView.setActivated(false);
            holder.radioButton.setChecked(false);

            holder.selectButtton.setEnabled(false);
            holder.selectButtton.setVisibility(View.INVISIBLE);
        }

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
        //      List<Courier> temp = new ArrayList<>();
        try {
            if (position != 0 && selectedCourierList.contains(couriers.get(position))) {
                Courier itemA = couriers.get(position);
                Courier itemB = couriers.get(0);
                couriers.set(position, itemB);
                couriers.set(0, itemA);
           /* couriers.add(0, itemA);
            couriers.remove(tail.lastIndexOf(itemA));*/

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

        //       Log.d("innerItem", "is it here? getItemCOunt" + couriers.size());
        return couriers.size();
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
        //     Log.d("innerItem", "is it here? getItemViewType" + couriers.size());
        return position;
        //return R.layout.item_inner_courier_card;
    }

    public void addData(@Nullable List<Courier> innerDataList, List<Courier> selected) {
        final int size = couriers.size();
        couriers = innerDataList;
        if (selected != null && !selected.isEmpty())
            selectedCourierList = selected;
        // swapItems(couriers.indexOf(selected.get(0)));
        Log.d("innerItem", "is it here? addData" + couriers.size());

        notifyDataSetChanged();
        //notifyItemRangeInserted(size, innerDataList.size());
    }

    public void updateSelectedData(int position, List<Courier> selected) {


        selectedCourierList = selected;
        // swapItems(position);
        //      Log.d("innerItem", "is it here? addData" + couriers.size());

        notifyDataSetChanged();
        //notifyItemRangeInserted(size, innerDataList.size());
    }


    public void clearData() {
        couriers.clear();
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
