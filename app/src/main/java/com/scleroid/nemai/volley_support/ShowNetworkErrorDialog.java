package com.scleroid.nemai.volley_support;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.scleroid.nemai.R;


public class ShowNetworkErrorDialog {

    private MaterialStyledDialog dialog;
    private Context context;
    private boolean flag;

    public ShowNetworkErrorDialog(Context context) {
        this.context = context;
    }

    public boolean showDialog() {
        if (true) {
            if (dialog != null) {
                dialog = null;
            }

            dialog = new MaterialStyledDialog.Builder(context)
                    .setStyle(Style.HEADER_WITH_ICON)
                    .setTitle("No Internet Connection")
                    .setIcon(R.drawable.ic_no_network)
                    .withDarkerOverlay(true)
                    .withDialogAnimation(true)
                    .setCancelable(false)
                    .withIconAnimation(true).setPositiveText("Try Again")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (isNetworkAvailable()) {
                                dismissDialog();
                                flag = true;
                            } else dialog.show();
                        }
                    })
                    .setDescription("Looks like you aren't connected to internet!")
                    .build();

            dialog.setCancelable(false);
            dialog.show();
        } else return true;
        return false;
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }


        return false;
    }
}
