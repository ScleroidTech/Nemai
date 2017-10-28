package com.scleroid.nemai.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import com.scleroid.nemai.activity.MainActivity;
import com.scleroid.nemai.activity.PartnerActivity;
import com.scleroid.nemai.adapter.PinAutoCompleteAdapter;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.models.PinCode;
import com.scleroid.nemai.other.DelayedAutoCompleteTextView;
import com.scleroid.nemai.volley_support.AppController;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
//TODO CHange most activities to fragment if performance becomes a bottleneck
//TODO implement ROOm
//TODO implement this http://droidmentor.com/credit-card-form/
public class HomeFragment extends Fragment {
    public static final int THRESHOLD = 3;
    private static final String TAG = HomeFragment.class.getSimpleName();
    public static PinCode mPinCodeDestination, mPinCodeSource;
    static String select;
    final CharSequence[] day_radio = {"Pune,MH,India", "Mumbai, MH,India", "Nagpur, MH, India"};
/*https://try.kotlinlang.org/
https://hackernoon.com/android-butterknife-vs-data-binding-fffceb77ed88
    //TODO read this https://medium.com/square-corner-blog/advocating-against-android-fragments-81fd0b462c97
    //TODo & this too http://smarterer.com/tests/android-developer https://www.buzzingandroid.com/ http://www.jbrugge.com/glean/index.html
    //TODO 7 this too https://www.infoq.com/presentations/Android-Design/ https://antonioleiva.com/free-guide/
    //TODO this too www.codacy.com https://possiblemobile.com/ http://www.andreamaglie.com/dont-waste-time-coding-2/
    //TODO https://androidbycode.wordpress.com/2015/02/13/static-code-analysis-automation-using-findbugs-android-studio/
    //TODO read this https://www.bignerdranch.com/blog/categories/android/ https://www.bignerdranch.com/blog/building-interfaces-with-constraintlayout/ https://www.bignerdranch.com/blog/the-rxjava-repository-pattern/ https://www.bignerdranch.com/blog/room-data-storage-for-everyone/ https://www.bignerdranch.com/blog/two-way-data-binding-on-android-observing-your-view-with-xml/ https://www.bignerdranch.com/blog/two-way-data-binding-on-android-observing-your-view-with-xml/ https://www.bignerdranch.com/blog/frame-animations-in-android/ https://www.bignerdranch.com/blog/building-animations-android-transition-framework-part-2/ https://www.bignerdranch.com/blog/testing-the-android-way/
    https://blog.mindorks.com/a-complete-guide-to-learn-kotlin-for-android-development-b1e5d23cc2d8
    https://developer.android.com/topic/libraries/architecture/room.html https://medium.com/google-developers/7-steps-to-room-27a5fe5f99b2 https://medium.com/@ajaysaini.official/building-database-with-room-persistence-library-ecf7d0b8f3e9 https://android.jlelse.eu/room-store-your-data-c6d49b4d53a3 http://www.vogella.com/tutorials/AndroidSQLite/article.html
    https://android.jlelse.eu/demystifying-the-jvmoverloads-in-kotlin-10dd098e6f72
*/

    RadioButton mParcelRadioButton, mDocumentRadioButton;
    RadioButton mDomesticRadioButton, mInternationalRadioButton;
    LinearLayout mParcelLinearLayout, mDocumentLinearLayout;
    Button mSubmitButton;
    FloatingActionButton fabNewCourier;
    TextView mWeightUnitTextView, mCurrencyUnitTextView;
    ImageView mAddressImageView;

    TextInputLayout mWeightTIL, mInvoiceTIL, mLengthTIL, mWidthTIL, mHeightTIL, mDescriptionTIL, /*mDescDocTIL,*/
            mPinSourceTIL, mPinDestTIL;
    DelayedAutoCompleteTextView pinSourceAutoCompleteTextView, pinDestinationAutoCompleteTextView;
    EditText mWeightEditText,/* mDescDocEditText,*/
            mInvoiceValueEditText, mPackageLengthParcelEditText, mPackageWidthParcelEditText, mHeightParcelEditText, mDescriptionEditText;
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
        v.clearFocus();

        mWeightTIL = v.findViewById(R.id.textWeight);
        mInvoiceTIL = v.findViewById(R.id.textInvoice);
        mPinSourceTIL = v.findViewById(R.id.pin_source_TIL);
        mPinDestTIL = v.findViewById(R.id.pin_dest_TIL);
        mLengthTIL = v.findViewById(R.id.textLength);
        mWidthTIL = v.findViewById(R.id.textWidth);
        mHeightTIL = v.findViewById(R.id.textHeight);
        mDescriptionTIL = v.findViewById(R.id.textDescription);
        //mDescDocTIL = v.findViewById(R.id.textPckDescDoc);
        mWeightEditText = v.findViewById(R.id.editWeight);
        mInvoiceValueEditText = v.findViewById(R.id.editInvoice);
        mPackageLengthParcelEditText = v.findViewById(R.id.editLength);
        mPackageWidthParcelEditText = v.findViewById(R.id.editWidth);
        mHeightParcelEditText = v.findViewById(R.id.editHeight);
        mDescriptionEditText = v.findViewById(R.id.editDescription);
//        mDescDocEditText = v.findViewById(R.id.editPckDescDoc);
        mParcelRadioButton = v.findViewById(R.id.rParcel);
        mDocumentRadioButton = v.findViewById(R.id.rDocument);

        fabNewCourier = v.findViewById(R.id.fab_new_data);
        fabNewCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//TODO Add A cartView, then refresh the layout(done), add data to database, & check existing data before sending it to server.& send all data to server at once
                //TODO IMP https://android.jlelse.eu/android-architecture-components-room-livedata-and-viewmodel-fca5da39e26b

                validateFields(false);



            }
        });
        mParcelLinearLayout = v.findViewById(R.id.linearExpandedParcelView);
//        mDocumentLinearLayout = v.findViewById(R.id.linearExpandedDocumentView);
//        mDocumentLinearLayout.setVisibility(View.VISIBLE);


        //img_address =  v.findViewById(R.id.img_address);


        pinDestinationAutoCompleteTextView = v.findViewById(R.id.pin_dest_autocompletetextview);
        pinDestinationAutoCompleteTextView.setThreshold(THRESHOLD);
        PinAutoCompleteAdapter pinAutoCompleteAdapter1 = new PinAutoCompleteAdapter(getApplicationContext());
        //pinAutoCompleteAdapter1.notifyDataSetChanged();
        pinDestinationAutoCompleteTextView.setAdapter(pinAutoCompleteAdapter1);


        pinDestinationAutoCompleteTextView.setLoadingIndicator(
                (android.widget.ProgressBar) v.findViewById(R.id.pb_loading_indicator2));
        pinDestinationAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mPinCodeDestination = (PinCode) adapterView.getItemAtPosition(position);
                pinDestinationAutoCompleteTextView.setText(String.format("%s, %s, %s", mPinCodeDestination.getLocation(), mPinCodeDestination.getPincode(), mPinCodeDestination.getState()));
            }
        });


        pinSourceAutoCompleteTextView = v.findViewById(R.id.pin_source_autocompletetextview);
        pinSourceAutoCompleteTextView.setThreshold(THRESHOLD);
        PinAutoCompleteAdapter pinAutoCompleteAdapter = new PinAutoCompleteAdapter(getApplicationContext());
        //pinAutoCompleteAdapter.notifyDataSetChanged();
        /*PinAutoCompleteAdapter pinAutoCompleteAdapter2 = new PinAutoCompleteAdapter(getApplicationContext());
        pinAutoCompleteAdapter1.notifyDataSetChanged();*/
        pinSourceAutoCompleteTextView.setAdapter(pinAutoCompleteAdapter); // 'this' is Activity instance
        pinSourceAutoCompleteTextView.setLoadingIndicator(
                (android.widget.ProgressBar) v.findViewById(R.id.pb_loading_indicator));
        pinSourceAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mPinCodeSource = (PinCode) adapterView.getItemAtPosition(position);
                pinSourceAutoCompleteTextView.setText(String.format("%s, %s, %s", mPinCodeSource.getLocation(), mPinCodeSource.getPincode(), mPinCodeSource.getState()));
            }
        });


        mWeightUnitTextView = v.findViewById(R.id.weight_unit_kg_textView);

        mWeightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                    mWeightUnitTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                else mWeightUnitTextView.setTextColor(getResources().getColor(R.color.colorHint));
            }
        });

        mCurrencyUnitTextView = v.findViewById(R.id.currency_unit_text_view);
        mInvoiceValueEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    mCurrencyUnitTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                else mCurrencyUnitTextView.setTextColor(getResources().getColor(R.color.colorHint));
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

//TODO https://uk.linkedin.com/in/chrisbanes/
        mSubmitButton = v.findViewById(R.id.btn_submit);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields(true);

                // Intent i = new Intent(getActivity(), PartnerActivity.class);
                //startActivity(i);
            }
        });
        mDocumentRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //mDocumentLinearLayout.setVisibility(View.VISIBLE);
                    toggleDocParcel = false;
                    mParcelLinearLayout.setVisibility(View.GONE);

                    mDescriptionEditText.setMinLines(6);
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
                    // mDocumentLinearLayout.setVisibility(View.GONE);
                    mDescriptionEditText.setMinLines(1);
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

    private void validateFields(boolean toggleMultiple) {
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

        if (isEmpty(mWeightEditText)) {
            mWeightTIL.setErrorEnabled(true);
            mWeightTIL.setError("Enter the Weight");
            noSubmit = true;
        } else mWeightTIL.setErrorEnabled(false);

        if (isEmpty(mInvoiceValueEditText)) {
            mInvoiceTIL.setErrorEnabled(true);
            mInvoiceTIL.setError("Enter the Invoice Value");
        } else mInvoiceTIL.setErrorEnabled(false);


        if (toggleDomInternational) delivery = "International";
        else delivery = "Domestic";
        if (toggleDocParcel) {


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


            if (!noSubmit)
                nextScreenParcel(pinSourceAutoCompleteTextView.getText().toString(), pinDestinationAutoCompleteTextView.getText().toString(), mWeightEditText.getText().toString(), mInvoiceValueEditText.getText().toString(), mPackageWidthParcelEditText.getText().toString(), mHeightParcelEditText.getText().toString(), mPackageLengthParcelEditText.getText().toString(), mDescriptionEditText.getText().toString(), delivery, toggleMultiple);


        } else {


            if (!noSubmit) {
                nextScreenDocument(pinSourceAutoCompleteTextView.getText().toString(), pinDestinationAutoCompleteTextView.getText().toString(), mWeightEditText.getText().toString(), mInvoiceValueEditText.getText().toString(), mDescriptionEditText.getText().toString(), delivery, toggleMultiple);

            }
        }
    }

    private boolean isEmpty(DelayedAutoCompleteTextView text) {
        return TextUtils.isEmpty(text.getText());
    }

    private boolean isEmpty(EditText text) {
        return TextUtils.isEmpty(text.getText());
    }

    private void nextScreenParcel(String source, String destination, String weight, String invoice, String width, String height, String length, String description, String deliveryType, boolean toggleMultiple) {
        Parcel parcel = new Parcel(source, destination, deliveryType, "Parcel", weight, invoice, length, width, height, description);
        submitRequest(parcel, toggleMultiple);

    }

    private void nextScreenDocument(String source, String destination, String weight, String invoice, String description, String deliveryType, boolean toggleMultiple) {

        Parcel parcel = new Parcel(source, destination, deliveryType, "Parcel", weight, invoice, null, null, null, description);
        submitRequest(parcel, toggleMultiple);
    }

    private void submitRequest(final Parcel parcel, boolean toggleMultiple) {

        if (toggleMultiple) {
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
                    params.put("source", parcel.getSourcePin());
                    params.put("destination", parcel.getDestinationPin());
                    params.put("weight", parcel.getWeight());
                    params.put("invoice", parcel.getInvoice());
                    params.put("width", parcel.getWidth());
                    params.put("length", parcel.getLength());
                    params.put("height", parcel.getHeight());
                    params.put("description", parcel.getDescription());
                    params.put("delivery_type", parcel.getDeliveryType());
                    params.put("package_type", parcel.getPackageType());

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        } else {
            MainActivity activity = (MainActivity) getActivity();
            activity.newParcel(parcel);

        }
    }

    private void showRadioButtonDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radio_buttondiaglog);
        List<String> stringList = new ArrayList<>();  // here is list
        for (int i = 0; i < 5; i++) {
            stringList.add("RadioButton " + (i + 1));
        }
        RadioGroup rg = dialog.findViewById(R.id.radio_group);

        for (int i = 0; i < stringList.size(); i++) {
            RadioButton rb = new RadioButton(getActivity()); // dynamically creating RadioButton and adding to RadioGroup.
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
                        Toast.makeText(getActivity(), btn.getText(), Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

        dialog.show();

    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    // Fragment for Center side
                    return new HomeFragment();
            }
            return null;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }
}


