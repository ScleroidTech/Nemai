package com.scleroid.nemai.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.scleroid.nemai.R;
import com.scleroid.nemai.models.Parcel;
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

    private static final String TAG = "DatePickerFragment";
    private static final String EXTRA_SOURCE_PIN = "source";
    private static final String EXTRA_PACKAGE = "packageType";
    private static final String EXTRA_DELIVERY_TYPE = "delivery";
    private static final String EXTRA_DEST_PIN = "destination";
    private static final String EXTRA_DESC = "description";
    private static final String EXTRA_INVOICE = "invoice";
    private static final String EXTRA_WEIGHT = "weight";
    private static final String EXTRA_HEIGHT = "height";
    private static final String EXTRA_WIDTH = "width";
    private static final String EXTRA_LENGTH = "length";

    Date tempDate = new Date();
    long serialNo;
    String source, destination, deliveryType, packageType, desc;
    int height, weight, width, length, invoice;
    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Parcel parcel) {
        Bundle bundle = createBundle(parcel);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(bundle);
        return fragment;


    }

    public static Bundle createBundle(Parcel parcel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DATE, parcel.getParcelDate());
        bundle.putLong(EXTRA_SERIAL, parcel.getSerialNo());
        bundle.putString(EXTRA_SOURCE_PIN, parcel.getSourcePin());
        bundle.putString(EXTRA_DELIVERY_TYPE, parcel.getDeliveryType());
        bundle.putString(EXTRA_DESC, parcel.getDescription());
        bundle.putString(EXTRA_DEST_PIN, parcel.getDestinationPin());
        bundle.putString(EXTRA_PACKAGE, parcel.getPackageType());
        bundle.putInt(EXTRA_INVOICE, parcel.getInvoice());
        bundle.putInt(EXTRA_WEIGHT, parcel.getWeight());
        bundle.putInt(EXTRA_WIDTH, parcel.getWidth());
        bundle.putInt(EXTRA_LENGTH, parcel.getLength());
        bundle.putInt(EXTRA_HEIGHT, parcel.getHeight());
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
            tempDate = (Date) bundle.getSerializable(EXTRA_DATE);
            serialNo = bundle.getLong(EXTRA_SERIAL);
            source = bundle.getString(EXTRA_SOURCE_PIN);
            destination = bundle.getString(EXTRA_DEST_PIN);
            deliveryType = bundle.getString(EXTRA_DELIVERY_TYPE);
            packageType = bundle.getString(EXTRA_PACKAGE);
            desc = bundle.getString(EXTRA_DESC);
            weight = bundle.getInt(EXTRA_WEIGHT);
            height = bundle.getInt(EXTRA_HEIGHT);
            width = bundle.getInt(EXTRA_WIDTH);
            length = bundle.getInt(EXTRA_LENGTH);
            invoice = bundle.getInt(EXTRA_INVOICE);

        }

        Calendar calendar = Calendar.getInstance();
        Log.d(TAG, calendar.toString());
        Log.d(TAG, tempDate + "THis should not be empty ");
        calendar.setTime(tempDate);


        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        mDatePicker = v.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);
        mDatePicker.setMinDate(System.currentTimeMillis() - 1000);

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
                Parcel parcel = new Parcel(source, destination, deliveryType, packageType, weight, invoice, length, width, height, desc, date, serialNo);
                Bundle bundle = createBundle(parcel);
                //sendResult(Activity.RESULT_OK,date);
                Events.DateMessage addressMessage = new Events.DateMessage(bundle);
                GlobalBus.getBus().post(addressMessage);
            }
        }).create();
    }
}