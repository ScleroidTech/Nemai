package com.scleroid.nemai.viewholders;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramotion.garlandview.inner.InnerRecyclerView;
import com.scleroid.nemai.R;
import com.scleroid.nemai.adapter.recyclerview.CourierAdapter;
import com.scleroid.nemai.data.models.Courier;
import com.scleroid.nemai.data.models.OrderedCourier;
import com.scleroid.nemai.data.models.Parcel;
import com.scleroid.nemai.utils.DateUtils;
import com.scleroid.nemai.utils.ImageUtils;

import java.util.List;

/**
 * @author Ganesh Kaple
 * @since 20-01-2018
 */

public class OrderHolder extends RecyclerView.ViewHolder {
    private final ImageView courierImageView;
    private final TextView courierDateTextView;
    private View mHeader;
    private View mHeaderAlpha;
    private InnerRecyclerView innerRecyclerView;
    private TextView shipmentTitle;
    private TextView mHeaderCaption2;
    private TextView sourceTextView;
    private TextView destinationTextView;
    private TextView costTextView;
    private TextView deliveryTime;
    private View mMiddle;
    private View mMiddleEdit;
    private View mFooter;
    private int m10dp;
    private int m120dp;
    private int mTitleSize1;
    private int mTitleSize2;
    private List<Courier> tail;
    private OrderedCourier thatOrderedCourier;
    //   private View mNewCourierButton;
    private boolean mIsScrolling;
    private View mEmptyView;
    private CourierAdapter adapter;
    private boolean isMultiSelect = false;
    private Parcel header;
    private OrderedCourier mOrderedCourier;
    private FloatingActionButton mNewCourierButton;
    private ImageUtils imageUtils;
    private DateUtils dateUtils = new DateUtils();

    public OrderHolder(View itemView) {
        super(itemView);
        Context context = itemView.getContext();

        sourceTextView = itemView.findViewById(R.id.tv_source);
        destinationTextView = itemView.findViewById(R.id.tv_destination);
        costTextView = itemView.findViewById(R.id.tv_cost);
        deliveryTime = itemView.findViewById(R.id.delivery_time);
        courierImageView = itemView.findViewById(R.id.delivery_partner);
        courierDateTextView = itemView.findViewById(R.id.courier_date);

    }

    public void setData(OrderedCourier orderedCourier) {
        sourceTextView.setText(String.format("From  %s", orderedCourier.getParcel().getSourcePinCode().getLocation()));
        destinationTextView.setText(String.format("From  %s", orderedCourier.getParcel().getDestinationPinCode().getLocation()));
        costTextView.setText(String.format("Rs. %s", orderedCourier.getCourier().getPrice()));
        deliveryTime.setText(String.format("Delivered by %s", orderedCourier.getCourier().getDeliveryTime()));
        courierDateTextView.setText(dateUtils.getFormattedDate(orderedCourier.getParcel().getParcelDate()));
        imageUtils.loadImageIntoImageView(courierImageView, orderedCourier.getCourier().getCourierImageUrl(), R.drawable.delivery_placeholder);

    }
}
