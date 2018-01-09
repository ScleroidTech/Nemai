package com.scleroid.nemai.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.scleroid.nemai.R;
import com.scleroid.nemai.models.Address;
import com.scleroid.nemai.utils.Events;
import com.scleroid.nemai.utils.GlobalBus;

/**
 * Created by Ganesh on 20-11-2017.
 */

public class
AddressFragment extends DialogFragment {
    public static final String EXTRA_PIN = "pincode";
    public static final String EXTRA_CITY = "city";
    public static final String EXTRA_STATE = "state";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_MOBILE = "mobile";
    public static final String EXTRA_ADDRESS_LINE_1 = "address_line_1";
    public static final String EXTRA_ADDRESS_LINE_2 = "address_line_2";
    public static final String EXTRA_BUNDLE = "address_bundle";
    public static final String EXTRA_SERIAL_NO = "serial_no";
    public static final String EXTRA_NEW_ADDRESS = "new_address";

    public static final int REQUEST_ADDRESS = 10;
    public static final String EXTRA_ADDRESS = "address";
    private boolean isNewAddress;

    public static AddressFragment newInstance(Address address) {
        Bundle bundle = createBundle(address);
        AddressFragment fragment = new AddressFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public static AddressFragment newInstance(String city, String pin, String state) {
        Bundle bundle = createBundle(city, pin, state);
        AddressFragment fragment = new AddressFragment();
        fragment.setArguments(bundle);
        return fragment;


    }

    public static void show(AppCompatActivity context) {
        AddressFragment dialog = new AddressFragment();
    }

    @NonNull
    private static Bundle createBundle(Address address) {

        return createBundle(address, false);
    }

    @NonNull
    private static Bundle createBundle(Address address, boolean isNewAddress) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ADDRESS, address);
      /*  bundle.putString(EXTRA_PIN, address.getPincode());
        bundle.putString(EXTRA_CITY, address.getCity());
        bundle.putString(EXTRA_STATE, address.getState());
        bundle.putLong(EXTRA_SERIAL_NO, address.getSerialNo());
        bundle.putString(EXTRA_NAME, address.getName());
        bundle.putString(EXTRA_MOBILE, address.getMobileNo());
        bundle.putString(EXTRA_ADDRESS_LINE_1, address.getAddress_line_1());
        bundle.putString(EXTRA_ADDRESS_LINE_1, address.getAddress_line_1());*/
        bundle.putBoolean(EXTRA_NEW_ADDRESS, isNewAddress);
        return bundle;
    }

    @NonNull
    private static Bundle createBundle(String city, String pin, String state) {
        Address address = new Address(null, null, null, state, city, pin, null);
        return createBundle(address, true);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        long serialNo = 0;
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_address, null);
        final TextInputEditText pinEditText = v.findViewById(R.id.pincode_edit_text);
        final TextInputEditText cityEditText = v.findViewById(R.id.city_edit_text);
        final TextInputEditText stateEditText = v.findViewById(R.id.state_edit_text);
        final TextInputEditText nameEditText = v.findViewById(R.id.address_full_name_editText);
        final TextInputEditText mobileEditText = v.findViewById(R.id.mobile_edit_text);
        final TextInputEditText addressLine1EditText = v.findViewById(R.id.address_line_1_edit_text);
        final TextInputEditText addressLine2EditText = v.findViewById(R.id.address_line_2_edit_text);
        final Bundle bundle = getArguments();

        if (bundle != null) {

            Log.d("addressFragment", "Different Approch, bundle ");
            Address address = bundle.getParcelable(EXTRA_ADDRESS);
            pinEditText.setText(address.getPincode());
            cityEditText.setText(address.getCity());
            stateEditText.setText(address.getState());
            serialNo = address.getSerialNo();
            isNewAddress = bundle.getBoolean(EXTRA_NEW_ADDRESS);
            if (bundle.getBoolean(EXTRA_NEW_ADDRESS)) {
                nameEditText.setText(address.getName());
                mobileEditText.setText(address.getMobileNo());
                addressLine1EditText.setText(address.getAddress_line_1());
                addressLine2EditText.setText(address.getAddress_line_1());


            }
            pinEditText.setEnabled(false);
            cityEditText.setEnabled(false);
            stateEditText.setEnabled(false);
        }

        final long finalSerialNo = serialNo;
        MaterialStyledDialog dialog = new MaterialStyledDialog.Builder(getActivity()).setTitle("Add Address").setStyle(Style.HEADER_WITH_TITLE).setCustomView(v).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Bundle newBundle = createBundle(cityEditText, pinEditText, stateEditText, addressLine1EditText, addressLine2EditText, nameEditText, mobileEditText, finalSerialNo);
                Events.AddressMessage addressMessage = new Events.AddressMessage(newBundle);
                GlobalBus.getBus().post(addressMessage);
                //   sendResult(Activity.RESULT_OK, newBundle);
            }


        }).withDialogAnimation(true).onNegative((dialog1, which) -> getDialog().dismiss()).setScrollable(true).setNegativeText(android.R.string.cancel).setPositiveText(android.R.string.ok).build();
        return dialog;
    }

    private Bundle createBundle(TextInputEditText cityEditText, TextInputEditText pinEditText, TextInputEditText stateEditText, TextInputEditText addressLine1EditText, TextInputEditText addressLine2EditText, TextInputEditText nameEditText, TextInputEditText mobileEditText, long serialNo) {
        Address address = new Address(nameEditText.getText().toString(), addressLine1EditText.getText().toString(), addressLine2EditText.getText().toString(), stateEditText.getText().toString(), cityEditText.getText().toString(), pinEditText.getText().toString(), mobileEditText.getText().toString(), serialNo);

        return createBundle(address, isNewAddress);
    }



}
