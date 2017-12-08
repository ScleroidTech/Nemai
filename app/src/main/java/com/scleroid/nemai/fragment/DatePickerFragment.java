package com.scleroid.nemai.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.scleroid.nemai.R;
import com.scleroid.nemai.utils.Events;
import com.scleroid.nemai.utils.GlobalBus;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//import android.support.v7.app.AlertDialog;

/**
 * Created by Ganesh on 09-01-2016.
 */
public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "com.example.ganesh.criminalintent.date";
    public static final String EXTRA_SERIAL = "serial_no";
    private static final String ARG_DATE = "crime_date";
    private static final String ARG_SERIAL_NO = "SERIAL_NO";
    Date tempDate = new Date();
    long serialNo;
    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Date parcel, long id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE, parcel);
        bundle.putLong(ARG_SERIAL_NO, id);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(bundle);
        return fragment;


    }

    @NonNull
    private static Bundle createBundle(Date date, long serialNo) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DATE, date);
        bundle.putLong(EXTRA_SERIAL, serialNo);
        return bundle;
    }

    private void sendResult(int ResultCode, Date date) {
        if (getTargetFragment() == null) return;
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), ResultCode, intent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Bundle bundle = getArguments();
        if (bundle != null) {
            tempDate = (Date) getArguments().getSerializable(ARG_DATE);
            serialNo = getArguments().getLong(ARG_SERIAL_NO);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tempDate);


        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        mDatePicker = v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);

     /*   MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(getActivity()).setTitle("Add Address").setStyle(Style.HEADER_WITH_TITLE).setCustomView(v).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Bundle newBundle = createBundle(parcelDate);
                Events.AddressMessage addressMessage = new Events.AddressMessage(newBundle);
                GlobalBus.getBus().post(addressMessage);
                //   sendResult(Activity.RESULT_OK, newBundle);
            }


        }).withDialogAnimation(true).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                getDialog().dismiss();
            }
        }).setScrollable(true).setNegativeText(android.R.string.cancel).setPositiveText(android.R.string.ok).build();
        return dialog;
*/

        return new AlertDialog.Builder(getActivity()).setTitle(R.string.date_picker_title).setView(v).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                Date date = new GregorianCalendar(year, month, day).getTime();
                Bundle bundle = createBundle(date, serialNo);
                //sendResult(Activity.RESULT_OK,date);
                Events.DateMessage addressMessage = new Events.DateMessage(bundle);
                GlobalBus.getBus().post(addressMessage);
            }
        }).create();
    }
}
