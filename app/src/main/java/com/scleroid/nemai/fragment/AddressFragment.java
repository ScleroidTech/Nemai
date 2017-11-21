package com.scleroid.nemai.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.scleroid.nemai.R;

/**
 * Created by Ganesh on 20-11-2017.
 */

public class AddressFragment extends DialogFragment {
    private static final String EXTRA_DATE = "date";

    public static AddressFragment newInstance() {
        Bundle bundle = new Bundle();
        //bundle.putSerializable(ARG_DATE, date);

        AddressFragment fragment = new AddressFragment();
        // fragment.setArguments(bundle);
        return fragment;


    }

    public static void show(AppCompatActivity context) {
        AddressFragment dialog = new AddressFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_address, null);
        MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(getActivity()).setTitle("Add Address").setStyle(Style.HEADER_WITH_TITLE).setCustomView(v).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                sendResult(Activity.RESULT_OK, "bcds");
            }


        }).withDialogAnimation(true).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            }
        }).setScrollable(true).setNegativeText(android.R.string.cancel).setPositiveText(android.R.string.ok).build();
        return dialog;
    }

    private void sendResult(int ResultCode, String date) {
        if (getTargetFragment() == null) return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), ResultCode, intent);
    }


}
