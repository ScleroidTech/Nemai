package com.scleroid.nemai.inner;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.nemai.R;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Ganesh on 15-11-2017.
 */

public class InnerItem extends com.ramotion.garlandview.inner.InnerItem {
    public final TextView mobile;
    public final TextView name;
    public final TextView pincode;
    public final TextView city;
    public final TextView address_line_1;
    public final TextView address_line_2;
    public final TextView state;
    public final View mLine;
    private final View innerLayout;
    private InnerModel mInnerData;

    public InnerItem(View itemView) {
        super(itemView);
        Log.d("inneritem", "view " + itemView.toString());
        innerLayout = ((ViewGroup) itemView).getChildAt(0);
        mLine = itemView.findViewById(R.id.line);
        address_line_1 = itemView.findViewById(R.id.tv_address_line_1);
        address_line_2 = itemView.findViewById(R.id.tv_address_line_2);
        name = itemView.findViewById(R.id.tv_name);
        city = itemView.findViewById(R.id.tv_city);
        pincode = itemView.findViewById(R.id.tv_pincode);
        mobile = itemView.findViewById(R.id.tv_mobile);
        state = itemView.findViewById(R.id.tv_state);

        innerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(InnerItem.this);
            }
        });
    }

    @Override
    protected View getInnerLayout() {
        return innerLayout;
    }

    public InnerModel getItemData() {
        return mInnerData;
    }

    public void clearContent() {
        //   Glide.clear(mAvatar);
        mInnerData = null;
    }

    void setContent(@NonNull InnerModel data) {
        mInnerData = data;

        name.setText(data.getName());
        mobile.setText(data.getMobileNo());
        address_line_1.setText(data.getAddress_line_1());
        address_line_2.setText(data.getAddress_line_2());
        state.setText(data.getState());
        city.setText(data.getCity());
        pincode.setText(Integer.toString(data.getPincode()));

     /*   Glide.with(itemView.getContext())
                .load(data.avatarUrl)
                .placeholder(R.drawable.avatar_placeholder)
                .bitmapTransform(new CropCircleTransformation(itemView.getContext()))
                .into(mAvatar);*/
    }
}
