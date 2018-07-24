package com.scleroid.nemai.viewholders;

import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.scleroid.nemai.R;
import com.scleroid.nemai.activity.MainActivity;
import com.scleroid.nemai.data.models.Address;
import com.scleroid.nemai.fragment.AddressFragment;

/**
 * Created by Ganesh on 15-11-2017.
 */

public class AddressHolder extends com.ramotion.garlandview.inner.InnerItem implements View.OnClickListener {
    private static DialogFragment dialog;
    public final TextView mobile;
    public final TextView name;
    public final TextView pincode;
    public final TextView city;
    public final ImageButton editTextView;
    public final TextView address_line_1;
    public final TextView address_line_2;
    public final TextView state;
    public final View mLine;
    public final View innerLayout;
    public final View innerItemView;
    public RadioButton radioButton;
    public Button deliverButtton;
    public CardView cardView;
    int selectedItemPosition = -1;
    private Address mInnerData;

    public AddressHolder(View itemView) {
        super(itemView);
        innerItemView = itemView;

        cardView = itemView.findViewById(R.id.frame_inner);
        //     Log.d("inneritem", "view " + itemView.toString());
        innerLayout = ((ViewGroup) itemView).getChildAt(0);
        mLine = itemView.findViewById(R.id.line);
        radioButton = itemView.findViewById(R.id.select_address_radio);
        address_line_1 = itemView.findViewById(R.id.tv_address_line_1);
        address_line_2 = itemView.findViewById(R.id.tv_address_line_2);
        name = itemView.findViewById(R.id.name_text_view);
        city = itemView.findViewById(R.id.tv_city);
        pincode = itemView.findViewById(R.id.tv_pincode);
        mobile = itemView.findViewById(R.id.tv_mobile);
        state = itemView.findViewById(R.id.tv_state);
        editTextView = itemView.findViewById(R.id.edit_image_button);
        deliverButtton = itemView.findViewById(R.id.deliver_button);
        deliverButtton.setVisibility(View.GONE);
       /* editTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((FragmentActivity) getInnerLayout().getContext()).getFragmentManager();
                dialog = AddressFragment.newInstance(mInnerData);

                dialog.show(fm, "adad");
            }
        });*/
//
        editTextView.setOnClickListener(this);
        itemView.setOnClickListener(this);


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

    public Address getItemData() {
        return mInnerData;
    }

    public void clearContent() {
        //   Glide.clear(mAvatar);
        mInnerData = null;
    }

    public void setContent(@NonNull Address data) {
        mInnerData = data;

        name.setText(data.getName());
        mobile.setText(data.getMobileNo());
        address_line_1.setText(data.getAddress_line_1());
        address_line_2.setText(data.getAddress_line_2());
        state.setText(data.getState());
        city.setText(data.getCity());
        pincode.setText(data.getPincode());


     /*   Glide.with(itemView.getContext())
                .load(data.avatarUrl)
                .placeholder(R.drawable.avatar_placeholder)
                .bitmapTransform(new CropCircleTransformation(itemView.getContext()))
                .into(mAvatar);*/
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == editTextView.getId()) {
            FragmentManager fm = ((MainActivity) getInnerLayout().getContext()).getSupportFragmentManager();
            dialog = AddressFragment.newInstance(mInnerData);

            dialog.show(fm, "adad");
        } else {
            //Handling for background selection state changed
            int previousSelectState = selectedItemPosition;
            selectedItemPosition = getAdapterPosition();
            //notify previous selected item
            // notifyItemChanged(previousSelectState);
            //notify new selected Item
            //   notifyItemChanged(selectedItemPosition);


        }

    }
}
