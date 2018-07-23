package com.scleroid.nemai.volley_support;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.scleroid.nemai.R;
import com.scleroid.nemai.network.LiveNetworkMonitor;
import com.scleroid.nemai.network.NetworkMonitor;


public class ShowNetworkErrorDialog {

    private MaterialStyledDialog dialog;
    private Context context;
    private boolean flag;
    private NetworkMonitor networkMonitor;

    public ShowNetworkErrorDialog(Context context) {
        this.context = context;
    }

    public boolean showDialog() {
        if (!isNetworkAvailable()) {
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

            //    dialog.setCancelable(false);
            dialog.show();
        } else return true;
        return false;
    }

    public boolean showDialog(boolean val) {
        if (val) {
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
        networkMonitor = new LiveNetworkMonitor(context);

        return networkMonitor.isConnected();

    }
}
