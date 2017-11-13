package com.scleroid.nemai.volley_support;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.scleroid.nemai.R;


public class ShowLoader {

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    private ProgressDialog dialog;
    private Context context;

    public ShowLoader(Context context) {
        this.context = context;
    }


    public void showDialog() {

        if (dialogBuilder != null) dialogBuilder = null;
        dialogBuilder = new AlertDialog.Builder(context, R.style.CustomDialogTheme);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.progress, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(false);
        alertDialog = dialogBuilder
                .create();


        alertDialog.requestWindowFeature(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //     alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        /*if (dialog != null) {
            dialog = null;
        }

        new MaterialDialog.Builder(context)

                .customView(R.layout.progress, true)
                .cancelable(false)
                .show();*/
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


}
