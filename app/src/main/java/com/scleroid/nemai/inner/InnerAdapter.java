package com.scleroid.nemai.inner;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
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

        // holder.itemView.setActivated(selectedItems.get(position, false));
        //TODO disable already activated view, then activate second view, reduce response time
        // Use getSelectedItems for the purpose

        // binding.setDiff((position >= mData.size() || mData.isEmpty()) ? 1 : 0);
        // bindViewHolder(holder,position);

       /* holder.innerItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRadioSelected(holder, position);
            }
        });
*/

    }

    public void activateButtons(boolean activate) {
        this.activate = activate;
        notifyDataSetChanged(); //need to call it for the child views to be re-created with buttons.
    }

/*
    public void toggleSelection(int pos) {
        boolean bool = false;

        if (selectedItems.get(pos, false)) {

         //   clearSelections();
            selectedItems.delete(pos);
            bool = true;

            buttonDeliver.setVisibility(View.VISIBLE);
        } else {
          */
/*  if (getSelectedItemCount() > 1) {
               *//*
*/
/*for (int item = 0; item < selectedItems.size(); item++) {
                   Log.d("InnerAdapter", selectedItems.get(item) + "");
                   if (item != pos) selectedItems.delete(item);

               }
           }*//*
*/
/*
                clearSelections();*//*

            }
            bool = false;
            selectedItems.put(pos, true);
            buttonDeliver.setVisibility(View.INVISIBLE);
        }

        this.activate = bool;
        lastChecked.setChecked(bool);
        //activateButtons(bool);
        notifyItemChanged(pos);
    }

*/

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
        holder.clearContent();
    }

    @Override
    public int getItemCount() {

        //       Log.d("innerItem", "is it here? getItemCOunt" + mData.size());
        return mData.size();
    }

    //TODO getItemCOunt, onBindViewHolder, & onCreateViewHolder doesn'tget called at all.
    @Override
    public int getItemViewType(int position) {
        //     Log.d("innerItem", "is it here? getItemViewType" + mData.size());
        return R.layout.item_inner_address_card;
    }

    public void addData(@Nullable List<Address> innerDataList) {
        final int size = mData.size();
        mData = innerDataList;
        //      Log.d("innerItem", "is it here? addData" + mData.size());

        //notifyDataSetChanged();
        notifyItemRangeInserted(size, innerDataList.size());
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
