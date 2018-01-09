package com.scleroid.nemai.viewholders;

import android.app.DialogFragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scleroid.nemai.R;
import com.scleroid.nemai.models.Courier;

/**
 * @author Ganesh Kaple
 * @since 09-01-2018
 */

public class CourierHolder extends com.ramotion.garlandview.inner.InnerItem {
    private static DialogFragment dialog;
    public final ImageView courierImageView;
    public final TextView serviceType;
    public final TextView deliveryTimeTextView;
    public final TextView priceTextView;

    public final View mLine;
    public final View innerLayout;
    public final View innerItemView;
    public RadioButton radioButton;
    public Button selectButtton;
    public CardView cardView;
    int selectedItemPosition = -1;
    private Courier mInnerData;

    public CourierHolder(View itemView) {
        super(itemView);
        innerItemView = itemView;

        cardView = itemView.findViewById(R.id.frame_inner_courier);
        //     Log.d("inneritem", "view " + itemView.toString());
        innerLayout = ((ViewGroup) itemView).getChildAt(0);
        mLine = itemView.findViewById(R.id.line);

        radioButton = itemView.findViewById(R.id.select_courier_radio);
        serviceType = itemView.findViewById(R.id.service_type_text_view);
        priceTextView = itemView.findViewById(R.id.price_text_view);
        deliveryTimeTextView = itemView.findViewById(R.id.delivery_time_text_view);
        courierImageView = itemView.findViewById(R.id.courier_image);
        selectButtton = itemView.findViewById(R.id.select_button);
        selectButtton.setVisibility(View.GONE);


    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
    }

    @Override
    public View getInnerLayout() {
        return innerLayout;
    }

    public Courier getItemData() {
        return mInnerData;
    }

    public void clearContent() {
        //   Glide.clear(mAvatar);
        mInnerData = null;
    }

    public void setContent(@NonNull Courier data, Context context) {
        mInnerData = data;

        serviceType.setText(data.getServiceType());

        priceTextView.setText(data.getPrice() + "");
        deliveryTimeTextView.setText(data.getDeliveryTime());


        String img = data.getCourierImageUrl();
        Glide.with(context).load(img)
                .crossFade()
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(courierImageView);

    }
    //TODO More Info button  VIew, future Implementation

}
