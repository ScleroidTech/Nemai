package com.scleroid.nemai.inner;

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
    public final TextView address;
    public final View mLine;
    private final View innerLayout;
    private InnerModel mInnerData;

    public InnerItem(View itemView) {
        super(itemView);
        innerLayout = ((ViewGroup) itemView).getChildAt(0);
        mLine = itemView.findViewById(R.id.line);
        address = itemView.findViewById(R.id.tv_address);
        name = itemView.findViewById(R.id.tv_name);
        city = itemView.findViewById(R.id.tv_city);
        pincode = itemView.findViewById(R.id.tv_pincode);
        mobile = itemView.findViewById(R.id.tv_mobile);

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

    void setContent(InnerModel data) {
        mInnerData = data;

        name.setText(data.name);
        mobile.setText(data.mobileNo);
        address.setText(data.address);
        city.setText(data.city);
        pincode.setText(data.pincode + "");

     /*   Glide.with(itemView.getContext())
                .load(data.avatarUrl)
                .placeholder(R.drawable.avatar_placeholder)
                .bitmapTransform(new CropCircleTransformation(itemView.getContext()))
                .into(mAvatar);*/
    }
}
