package com.scleroid.nemai.outer;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.garlandview.header.HeaderDecorator;
import com.ramotion.garlandview.header.HeaderItem;
import com.ramotion.garlandview.inner.InnerLayoutManager;
import com.ramotion.garlandview.inner.InnerRecyclerView;
import com.scleroid.nemai.R;
import com.scleroid.nemai.inner.InnerAdapter;
import com.scleroid.nemai.models.Address;
import com.scleroid.nemai.models.Order;
import com.scleroid.nemai.models.Parcel;

import java.util.ArrayList;
import java.util.List;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

/**
 * Created by Ganesh on 15-11-2017.
 */

public class OuterItem extends HeaderItem {

    private static final String TAG = "OuterItem";

    private final static float MIDDLE_RATIO_START = 0.7f;
    private final static float MIDDLE_RATIO_MAX = 0.1f;
    private final static float MIDDLE_RATIO_DIFF = MIDDLE_RATIO_START - MIDDLE_RATIO_MAX;

    private final static float FOOTER_RATIO_START = 1.1f;
    private final static float FOOTER_RATIO_MAX = 0.35f;
    private final static float FOOTER_RATIO_DIFF = FOOTER_RATIO_START - FOOTER_RATIO_MAX;

    private final static float ANSWER_RATIO_START = 0.75f;
    private final static float ANSWER_RATIO_MAX = 0.35f;
    private final static float ANSWER_RATIO_DIFF = ANSWER_RATIO_START - ANSWER_RATIO_MAX;

 /*   private final static float AVATAR_RATIO_START = 1f;
    private final static float AVATAR_RATIO_MAX = 0.25f;
    private final static float AVATAR_RATIO_DIFF = AVATAR_RATIO_START - AVATAR_RATIO_MAX;*/


    private final View mHeader;
    private final View mHeaderAlpha;

    private final InnerRecyclerView mRecyclerView;

    private final TextView mHeaderCaption1;
    private final TextView mHeaderCaption2;
    private final TextView source;
    private final TextView destination;
    private final TextView cost;
    private final TextView edit;

    private final View mMiddle;
    private final View mMiddleEdit;
    private final View mFooter;
    private final View mNewAddressButton;

    private final List<View> mMiddleCollapsible = new ArrayList<>(2);

    private final int m10dp;
    private final int m120dp;
    private final int mTitleSize1;
    private final int mTitleSize2;
    GestureDetectorCompat gestureDetector;
    android.view.ActionMode actionMode;
    List<Address> tail, multiSelectList;
    Order thatOrder;
    private boolean mIsScrolling;
    private View mEmptyView;
    private InnerAdapter adapter;
    private boolean isMultiSelect = false;
    private Parcel header;


    public OuterItem(View itemView, RecyclerView.RecycledViewPool pool) {
        super(itemView);

        // Init header
        m10dp = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp10);
        m120dp = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.dp120);
        mTitleSize1 = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.header_title2_text_size);
        mTitleSize2 = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.header_title2_name_text_size);

        mHeader = itemView.findViewById(R.id.header);
        mHeaderAlpha = itemView.findViewById(R.id.header_alpha);
        mNewAddressButton = itemView.findViewById(R.id.new_address_button);


        mHeaderCaption1 = itemView.findViewById(R.id.header_shipment_title_1);
        mHeaderCaption2 = itemView.findViewById(R.id.header_shipment_title_2);
        source = itemView.findViewById(R.id.tv_source);
        destination = itemView.findViewById(R.id.tv_destination);
        cost = itemView.findViewById(R.id.tv_cost);
        edit = itemView.findViewById(R.id.edit_image_button);


        mMiddle = itemView.findViewById(R.id.header_middle);
        mMiddleEdit = itemView.findViewById(R.id.header_middle_edit);
        mFooter = itemView.findViewById(R.id.header_footer);

        //  mMiddleCollapsible.add((View)mAvatar.getParent());
        mMiddleCollapsible.add((View) cost.getParent());

        // Init RecyclerView
        mRecyclerView = itemView.findViewById(R.id.recycler_view);
        mRecyclerView.setRecycledViewPool(pool);
        mRecyclerView.setAdapter(new InnerAdapter());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mIsScrolling = newState != RecyclerView.SCROLL_STATE_IDLE;
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // onItemScrolled(recyclerView, dx, dy);
            }
        });

        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRecyclerView.addItemDecoration(new HeaderDecorator(
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.inner_item_height_decoration),
                itemView.getContext().getResources().getDimensionPixelSize(R.dimen.inner_item_offset)));
        // Init fonts


        DataBindingUtil.bind(((FrameLayout) mHeader).getChildAt(0));
    }


    @Override
    public View getHeader() {
        return mHeader;
    }

    @Override
    public View getHeaderAlphaView() {
        return mHeaderAlpha;
    }

    @Override
    public boolean isScrolling() {
        return mIsScrolling;
    }

    @Override
    public InnerRecyclerView getViewGroup() {
        return mRecyclerView;
    }

    void setContent(@NonNull List<Address> innerDataList, final Parcel parcel, int position, int size) {
        final Context context = itemView.getContext();

        header = parcel;
        tail = innerDataList;
        multiSelectList = new ArrayList<>();
        //  Crashlytics.getInstance().crash(); // Force a crash



        mRecyclerView.setLayoutManager(new InnerLayoutManager());
        adapter = (InnerAdapter) mRecyclerView.getAdapter();
        ((InnerAdapter) mRecyclerView.getAdapter()).addData(tail, multiSelectList);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getHeader().getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                multi_select(position);

                Toast.makeText(getHeader().getContext(), "Details Page", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                if (!isMultiSelect) {
                    isMultiSelect = true;

                }

                //multi_select(position);

            }
        }));


        //gestureDetector = new GestureDetectorCompat(this.getHeader().getContext(), new RecyclerViewDemoOnGestureListener());

        final String title1 = bindNumber(position, size);

        final Spannable title2 = new SpannableString(title1 + " - Rs. " + parcel.getInvoice());
        title2.setSpan(new AbsoluteSizeSpan(mTitleSize1), 0, title1.length(), SPAN_INCLUSIVE_INCLUSIVE);
        title2.setSpan(new AbsoluteSizeSpan(mTitleSize2), title1.length(), title2.length(), SPAN_INCLUSIVE_INCLUSIVE);
        title2.setSpan(new ForegroundColorSpan(Color.argb(204, 255, 255, 255)), title1.length(), title2.length(), SPAN_INCLUSIVE_INCLUSIVE);

        mHeaderCaption1.setText(title1);
        mHeaderCaption2.setText(title2);

        source.setText(parcel.getSourcePin());//TODO COnvert Pincode to room , get source city instead of pincod,e & store selected object instead of text
        destination.setText(parcel.getDestinationPin());
        cost.setText("Rs. " + parcel.getInvoice());//TODO get delivery price, not invoice


    }

    private void multi_select(int position) {
        try {
            if (multiSelectList.isEmpty()) {
                Log.d(TAG, "list empty " + multiSelectList.isEmpty());
                thatOrder = new Order(header, tail.get(position));
                multiSelectList.add(tail.get(position));
            } else {
                Log.d(TAG, "list not  empty " + multiSelectList.isEmpty());

                if (multiSelectList.contains(tail.get(position))) {
                    multiSelectList.remove(tail.get(position));
                    thatOrder.setAddress(null);
                } else if (multiSelectList.size() > 0) {
                    multiSelectList.clear();
                    thatOrder.setAddress(tail.get(position));
                    multiSelectList.add(tail.get(position));
                }

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.e(TAG, "Array Out of Bound " + e);
        }
        refreshAdapter();

    }

    public void refreshAdapter() {
        adapter.addData(tail, multiSelectList);
    }

    public String bindNumber(int position, int size) {
        return (String.format("Shipment %d of %d", position + 1, size));
    }

    void clearContent() {
        // Glide.clear(mAvatar);
        ((InnerAdapter) mRecyclerView.getAdapter()).clearData();
    }

    private float computeRatio(RecyclerView recyclerView) {
        final View child0 = recyclerView.getChildAt(0);
        final int pos = recyclerView.getChildAdapterPosition(child0);
        if (pos != 0) {
            return 0;
        }

        final int height = child0.getHeight();
        final float y = Math.max(0, child0.getY());
        return y / height;
    }

    private void onItemScrolled(RecyclerView recyclerView, int dx, int dy) {
        final float ratio = computeRatio(recyclerView);

        final float footerRatio = Math.max(0, Math.min(FOOTER_RATIO_START, ratio) - FOOTER_RATIO_DIFF) / FOOTER_RATIO_MAX;
        //final float avatarRatio = Math.max(0, Math.min(AVATAR_RATIO_START, ratio) - AVATAR_RATIO_DIFF) / AVATAR_RATIO_MAX;
        final float answerRatio = Math.max(0, Math.min(ANSWER_RATIO_START, ratio) - ANSWER_RATIO_DIFF) / ANSWER_RATIO_MAX;
        final float middleRatio = Math.max(0, Math.min(MIDDLE_RATIO_START, ratio) - MIDDLE_RATIO_DIFF) / MIDDLE_RATIO_MAX;

        ViewCompat.setPivotY(mFooter, 0);
        ViewCompat.setScaleY(mFooter, footerRatio);
        ViewCompat.setAlpha(mFooter, footerRatio);

        ViewCompat.setPivotY(mMiddleEdit, mMiddleEdit.getHeight());
        ViewCompat.setScaleY(mMiddleEdit, 1f - answerRatio);
        ViewCompat.setAlpha(mMiddleEdit, 0.5f - answerRatio);

        ViewCompat.setAlpha(mHeaderCaption1, answerRatio);
        ViewCompat.setAlpha(mHeaderCaption2, 1f - answerRatio);

        final View mc2 = mMiddleCollapsible.get(0);
        ViewCompat.setPivotX(mc2, 0);
        ViewCompat.setPivotY(mc2, mc2.getHeight() / 2);

        for (final View view : mMiddleCollapsible) {
            ViewCompat.setScaleX(view, middleRatio);
            ViewCompat.setScaleY(view, middleRatio);
            ViewCompat.setAlpha(view, middleRatio);
        }
/*
        final ViewGroup.LayoutParams lp = mMiddle.getLayoutParams();
        lp.height = m120dp - (int) (m10dp * (1f - middleRatio));
        mMiddle.setLayoutParams(lp);*/
    }

    public void setContent(Parcel parcel, int position, int size) {
        setContent(new ArrayList<Address>(), parcel, position, size);
    }

   /* @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
      //  gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.menu_cab_recyclerviewdemoactivity, menu);

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

        this.actionMode = null;
  //      adapter.clearSelections();
    }
*/
   /* @Override
    public void onClick(View view) {
        // item click
        int idx = mRecyclerView.getChildPosition(view);

        if (actionMode != null) {
            return;
        }
        // Start the CAB using the ActionMode.Callback defined above
     //   actionMode = startActionMode(RecyclerViewDemoActivity.this);
            myToggleSelection(idx);
            adapter.activateButtons(true);
            return;



    }*/

    private void myToggleSelection(int idx) {
        // adapter.toggleSelection(idx);
        // adapter.setRadioSelected();
        // String title = getHeader().getContext().getString(R.string.selected_count, adapter.getSelectedItemCount() + "");
        //actionMode.setTitle(title);
    }


    private class RecyclerViewDemoOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
            int idx = mRecyclerView.getChildPosition(view);
            myToggleSelection(idx);
            return super.onSingleTapConfirmed(e);
        }

        public void onLongPress(MotionEvent e) {
            View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());

            // Start the CAB using the ActionMode.Callback defined above
            // actionMode = getHeader().startActionMode((android.view.ActionMode.Callback) getHeader().getContext()); */
            int idx = mRecyclerView.getChildPosition(view);
            myToggleSelection(idx);
            super.onLongPress(e);
        }
    }
}
