package com.scleroid.nemai.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.scleroid.nemai.R;

/**
 * @author Ganesh Kaple
 * @since 19-01-2018
 */

public class PartnerViewHolder extends RecyclerView.ViewHolder {
    private ImageView partnerLogo;

    public PartnerViewHolder(View itemView) {
        super(itemView);
        partnerLogo = itemView.findViewById(R.id.logo);
        partnerLogo.setScaleType(ImageView.ScaleType.CENTER_CROP);

    }

    public void setPartnerLogo(int resId) {
        partnerLogo.setImageResource(resId);
        partnerLogo.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }
}
