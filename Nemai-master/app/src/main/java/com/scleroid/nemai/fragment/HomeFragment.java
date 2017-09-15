package com.scleroid.nemai.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.scleroid.nemai.R;
import com.scleroid.nemai.ServerConstants;
import com.scleroid.nemai.activity.PartnerActivity;
import com.scleroid.nemai.adapter.PinAutoCompleteAdapter;
import com.scleroid.nemai.models.PinCode;
import com.scleroid.nemai.other.DelayedAutoCompleteTextView;
import com.scleroid.nemai.volley_support.AppController;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;


public class HomeFragment extends Fragment {
    public static final int THRESHOLD = 3;
    private static final String TAG = HomeFragment.class.getSimpleName();
    public static PinCode mPinCodeDestination, mPinCodeSource;
    static String select;
    final CharSequence[] day_radio = {"Pune,MH,India", "Mumbai, MH,India", "Nagpur, MH, India"};

    RadioButton mParcelRadioButton, mDocumentRadioButton;
    RadioButton mDomesticRadioButton, mInternationalRadioButton;
    LinearLayout mParcelLinearLayout, mDocumentLinearLayout;
    Button mSubmitButton;
    TextView mWeightUnitTextView;
    ImageView mAddressImageView;
    TextInputLayout mWeightParcelTIL, mInvoiceTIL, mLengthTIL, mWidthTIL, mHeightTIL, mParcelDescTIL, mWeightDocTIL, mDescDocTIL, mPinSourceTIL, mPinDestTIL;
    DelayedAutoCompleteTextView pinSourceAutoCompleteTextView, pinDestinationAutoCompleteTextView;
    EditText mWeightdocEditText, mWeightParcelEditText, mDescDocEditText, mInvoiceValueParcelEditText, mPackageLengthParcelEditText, mPackageWidthParcelEditText, mHeightParcelEditText, mDescParcelEditText;
    boolean toggleDocParcel = false;//false == doc, true == parcel
    boolean toggleDomInternational = false;//Domestic false , International = true
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mWeightParcelTIL = v.findViewById(R.id.textWeight);
        mInvoiceTIL = v.findViewById(R.id.textInvoice);
        mPinSourceTIL = v.findViewById(R.id.pin_source_TIL);
        mPinDestTIL = v.findViewById(R.id.pin_dest_TIL);
        mLengthTIL = v.findViewById(R.id.textLength);
        mWidthTIL = v.findViewById(R.id.textWidth);
        mHeightTIL = v.findViewById(R.id.textHeight);
        mParcelDescTIL = v.findViewById(R.id.textPckDesc);
        mWeightDocTIL = v.findViewById(R.id.textWeightDoc);
        mDescDocTIL = v.findViewById(R.id.textPckDescDoc);
        mWeightParcelEditText = v.findViewById(R.id.editWeight);
        mInvoiceValueParcelEditText = v.findViewById(R.id.editInvoice);
        mPackageLengthParcelEditText = v.findViewById(R.id.editLength);
        mPackageWidthParcelEditText = v.findViewById(R.id.editWidth);
        mHeightParcelEditText = v.findViewById(R.id.editHeight);
        mDescParcelEditText = v.findViewById(R.id.editPckDesc);
        mDescDocEditText = v.findViewById(R.id.editPckDescDoc);
        mParcelRadioButton = v.findViewById(R.id.rParcel);
        mDocumentRadioButton = v.findViewById(R.id.rDocument);

        mParcelLinearLayout = v.findViewById(R.id.linearExpandedParcelView);
        mDocumentLinearLayout = v.findViewById(R.id.linearExpandedDocumentView);
        mDocumentLinearLayout.setVisibility(View.VISIBLE);


        //img_address =  v.findViewById(R.id.img_address);


        pinDestinationAutoCompleteTextView = v.findViewById(R.id.pin_dest_autocompletetextview);
        pinDestinationAutoCompleteTextView.setThreshold(THRESHOLD);
        PinAutoCompleteAdapter pinAutoCompleteAdapter1 = new PinAutoCompleteAdapter(getActivity());
        pinAutoCompleteAdapter1.notifyDataSetChanged();
        pinDestinationAutoCompleteTextView.setAdapter(pinAutoCompleteAdapter1);


        pinDestinationAutoCompleteTextView.setLoadingIndicator(
                (android.widget.ProgressBar) v.findViewById(R.id.pb_loading_indicator2));
        pinDestinationAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mPinCodeDestination = (PinCode) adapterView.getItemAtPosition(position);
                pinDestinationAutoCompleteTextView.setText(mPinCodeDestination.getLocation() + ", " + mPinCodeDestination.getPincode() + ", " + mPinCodeDestination.getState());
            }
        });


        pinSourceAutoCompleteTextView = v.findViewById(R.id.pin_source_autocompletetextview);
        pinSourceAutoCompleteTextView.setThreshold(THRESHOLD);
        PinAutoCompleteAdapter pinAutoCompleteAdapter = new PinAutoCompleteAdapter(getActivity());
        pinSourceAutoCompleteTextView.setAdapter(pinAutoCompleteAdapter); // 'this' is Activity instance
        pinSourceAutoCompleteTextView.setLoadingIndicator(
                (android.widget.ProgressBar) v.findViewById(R.id.pb_loading_indicator));
        pinSourceAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mPinCodeSource = (PinCode) adapterView.getItemAtPosition(position);
                pinSourceAutoCompleteTextView.setText(mPinCodeSource.getPincode() + ", " + mPinCodeSource.getPincode() + ", " + mPinCodeSource.getState());
            }
        });


        mWeightUnitTextView= v.findViewById(R.id.weight_unit_kg_textView);
        mWeightdocEditText = v.findViewById(R.id.editWeightDoc);
        mWeightdocEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) mWeightUnitTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                else mWeightUnitTextView.setTextColor(getResources().getColor(R.color.colorHint));
            }
        });

        //    editAddress = v.findViewById(R.id.editAddressDoc);

/*img_address.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity())
                .setTitle("Select an Address")
                .setSingleChoiceItems(day_radio, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getActivity(), "The selected Day is " + day_radio[which], Toast.LENGTH_LONG).show();
                        select= day_radio[which].toString();
                        dialog.dismiss();
                        editAddress.setText(select);
                    }
                });
        AlertDialog alertdialog2 = builder2.create();
        alertdialog2.show();

    }
});*/
        mSubmitButton = v.findViewById(R.id.btn_submit);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields();

                // Intent i = new Intent(getActivity(), PartnerActivity.class);
                //startActivity(i);
            }
        });
        mDocumentRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mDocumentLinearLayout.setVisibility(View.VISIBLE);
                    toggleDocParcel = false;
                    mParcelLinearLayout.setVisibility(View.GONE);
                    mDocumentRadioButton.setTypeface(null, Typeface.BOLD);
                    mParcelRadioButton.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        mParcelRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mParcelLinearLayout.setVisibility(View.VISIBLE);
                    mDocumentLinearLayout.setVisibility(View.GONE);
                    toggleDocParcel = true;
                    mParcelRadioButton.setTypeface(null, Typeface.BOLD);
                    mDocumentRadioButton.setTypeface(null, Typeface.NORMAL);
                }
            }
        });


        mDomesticRadioButton = v.findViewById(R.id.rDomestic);
        mInternationalRadioButton = v.findViewById(R.id.rInternational);

        mInternationalRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mInternationalRadioButton.setTypeface(null, Typeface.BOLD);
                    mDomesticRadioButton.setTypeface(null, Typeface.NORMAL);
                    toggleDomInternational = true;
                }
            }
        });

        mDomesticRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mDomesticRadioButton.setTypeface(null, Typeface.BOLD);
                    mDomesticRadioButton.setTypeface(null, Typeface.NORMAL);
                    toggleDomInternational = false;
                }
            }
        });
        return v;
    }

    private void validateFields() {
        boolean noSubmit = false;
        String delivery;

        if (isEmpty(pinSourceAutoCompleteTextView)) {
            mPinSourceTIL.setErrorEnabled(true);
            mPinSourceTIL.setError("Enter the Source first");
            noSubmit = true;
        } else mPinSourceTIL.setErrorEnabled(false);

        if (isEmpty(pinDestinationAutoCompleteTextView)) {
            mPinDestTIL.setErrorEnabled(true);
            mPinDestTIL.setError("Enter the Destination too");
            noSubmit = true;
        } else mPinDestTIL.setErrorEnabled(false);



        if (toggleDomInternational) delivery = "International";
        else delivery = "Domestic";
        if (toggleDocParcel) {

            if (isEmpty(mWeightParcelEditText)) {
                mWeightParcelTIL.setErrorEnabled(true);
                mWeightParcelTIL.setError("Enter the Weight");
                noSubmit = true;
            } else mWeightParcelTIL.setErrorEnabled(false);

            if (isEmpty(mPackageWidthParcelEditText)) {
                mWidthTIL.setErrorEnabled(true);
                mWidthTIL.setError("Enter the Width");
                noSubmit = true;
            } else mWidthTIL.setErrorEnabled(false);

            if (isEmpty(mHeightParcelEditText)) {
                mHeightTIL.setErrorEnabled(true);
                mHeightTIL.setError("Enter the Height");
                noSubmit = true;
            } else mHeightTIL.setErrorEnabled(false);

            if (isEmpty(mPackageLengthParcelEditText)) {
                mLengthTIL.setErrorEnabled(true);
                mLengthTIL.setError("Enter the Length");
                noSubmit = true;
            } else mLengthTIL.setErrorEnabled(false);

            if (isEmpty(mInvoiceValueParcelEditText)) {
                mInvoiceTIL.setErrorEnabled(true);
                mInvoiceTIL.setError("Enter the Invoice Value");
            } else mInvoiceTIL.setErrorEnabled(false);


            if (!noSubmit)
                nextScreenParcel(pinSourceAutoCompleteTextView.getText().toString(), pinDestinationAutoCompleteTextView.getText().toString(), mWeightParcelEditText.getText().toString(), mInvoiceValueParcelEditText.getText().toString(), mPackageWidthParcelEditText.getText().toString(), mHeightParcelEditText.getText().toString(), mPackageLengthParcelEditText.getText().toString(), mDescParcelEditText.getText().toString(), "Parcel", delivery);


        } else {
            if (isEmpty(mWeightdocEditText)) {
                mWeightDocTIL.setErrorEnabled(true);
                mWeightDocTIL.setError("Enter the Weight");
                noSubmit = true;

            } else mWeightDocTIL.setErrorEnabled(false);

            if (!noSubmit) {
                nextScreenDocument(pinSourceAutoCompleteTextView.getText().toString(), pinDestinationAutoCompleteTextView.getText().toString(), mWeightdocEditText.getText().toString(), mDescDocEditText.getText().toString(), "Documents", delivery);

            }
        }
    }

    private void nextScreenDocument(String source, String destination, String weight, String description, String packageType, String deliveryType) {
        submitRequest(source, destination, weight, null, null, null, null, description, packageType, deliveryType);
    }


    private void nextScreenParcel(String source, String destination, String weight, String invoice, String width, String height, String length, String description, String packageType, String deliveryType) {
        submitRequest(source, destination, weight, invoice, width, height, length, description, packageType, deliveryType);

    }

    private void submitRequest(final String source, final String destination, final String weight, final String invoice, final String width, final String height, final String length, final String description, final String packageType, final String deliveryType) {

        // Tag used to cancel the request
        String tag_string_req = "req_parcel";


        //showProgress(true);

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                ServerConstants.serverUrl.POST_COURIER, null, new Response.Listener<JSONObject>() {
            @SuppressLint("LongLogTag")

            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d(TAG, "Parcel Response: " + jsonObject.toString());
                //showProgress(false);
                if (true) {

                    Intent partner = new Intent(getApplicationContext(), PartnerActivity.class);
                    startActivity(partner);

                    // Launch login activity
                    /*Intent intent = new Intent(
                            RegisterActivity.this,
                            LoginActivity.class);
                    startActivity(intent); */
                    //finish();
                } else {

                    // Error occurred in registration. Get the error
                    // message

                    //String errorMsg = jsonObject.getString("error_msg");
                    Toast.makeText(getApplicationContext(),
                            "An Error occurred", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @SuppressLint("LongLogTag")
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server Error on Parcel: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("source", source);
                params.put("destination", destination);
                params.put("weight", weight);
                params.put("invoice", invoice);
                params.put("width", width);
                params.put("length", length);
                params.put("height", height);
                params.put("description", description);
                params.put("delivery_type", deliveryType);
                params.put("package_type", packageType);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showRadioButtonDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radio_buttondiaglog);
        List<String> stringList=new ArrayList<>();  // here is list
        for(int i=0;i<5;i++) {
            stringList.add("RadioButton " + (i + 1));
        }
        RadioGroup rg = dialog.findViewById(R.id.radio_group);

        for(int i=0;i<stringList.size();i++){
            RadioButton rb=new RadioButton(getActivity()); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rg.addView(rb);
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        Toast.makeText(getActivity(),btn.getText(),Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

        dialog.show();

    }

    private boolean isEmpty(EditText text) {
        return TextUtils.isEmpty(text.getText());
    }

    private boolean isEmpty(DelayedAutoCompleteTextView text) {
        return TextUtils.isEmpty(text.getText());
    }

}

