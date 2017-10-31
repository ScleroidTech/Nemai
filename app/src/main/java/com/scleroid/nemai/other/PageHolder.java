package com.scleroid.nemai.other;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.scleroid.nemai.R;
import com.scleroid.nemai.adapter.PinAutoCompleteAdapter;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.models.PinCode;

import static com.scleroid.nemai.fragment.HomeFragment.THRESHOLD;
import static com.scleroid.nemai.fragment.HomeFragment.mPinCodeDestination;
import static com.scleroid.nemai.fragment.HomeFragment.mPinCodeSource;
import static com.scleroid.nemai.network.NetworkCalls.submitCouriers;

/**
 * Created by Ganesh on 31-10-2017.
 */

public class PageHolder extends RecyclerView.ViewHolder {
    RadioButton mParcelRadioButton, mDocumentRadioButton;
    RadioButton mDomesticRadioButton, mInternationalRadioButton;
    LinearLayout mParcelLinearLayout, mDocumentLinearLayout;
    Context context;

    boolean toggleDocParcel = false;//false == doc, true == parcel
    boolean toggleDomInternational = false;//Domestic false , International = true

    TextView mWeightUnitTextView, mCurrencyUnitTextView;
    ImageView mAddressImageView;

    TextInputLayout mWeightTIL, mInvoiceTIL, mLengthTIL, mWidthTIL, mHeightTIL, mDescriptionTIL, /*mDescDocTIL,*/
            mPinSourceTIL, mPinDestTIL;
    DelayedAutoCompleteTextView pinSourceAutoCompleteTextView, pinDestinationAutoCompleteTextView;
    EditText mWeightEditText,/* mDescDocEditText,*/
            mInvoiceValueEditText, mPackageLengthParcelEditText, mPackageWidthParcelEditText, mHeightParcelEditText, mDescriptionEditText;

    public PageHolder(View v, Context context) {
        super(v);

        this.context = context;
        attachViews(v);

    }


    private void attachViews(View v) {
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

        mParcelRadioButton = v.findViewById(R.id.rParcel);
        mDocumentRadioButton = v.findViewById(R.id.rDocument);

        mParcelLinearLayout = v.findViewById(R.id.linearExpandedParcelView);

        pinDestinationAutoCompleteTextView = v.findViewById(R.id.pin_dest_autocompletetextview);
        pinSourceAutoCompleteTextView = v.findViewById(R.id.pin_source_autocompletetextview);

        mWeightUnitTextView = v.findViewById(R.id.weight_unit_kg_textView);
        mCurrencyUnitTextView = v.findViewById(R.id.currency_unit_text_view);

        mDomesticRadioButton = v.findViewById(R.id.rDomestic);
        mInternationalRadioButton = v.findViewById(R.id.rInternational);
        pinDestinationAutoCompleteTextView.setLoadingIndicator(
                (android.widget.ProgressBar) v.findViewById(R.id.pb_loading_indicator2));

        pinSourceAutoCompleteTextView.setLoadingIndicator(
                (android.widget.ProgressBar) v.findViewById(R.id.pb_loading_indicator));

    }

    public void setListeners() {


        pinDestinationAutoCompleteTextView.setThreshold(THRESHOLD);
        PinAutoCompleteAdapter pinAutoCompleteAdapter1 = new PinAutoCompleteAdapter(context);
        pinAutoCompleteAdapter1.notifyDataSetChanged();
        pinDestinationAutoCompleteTextView.setAdapter(pinAutoCompleteAdapter1);


        pinDestinationAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mPinCodeDestination = (PinCode) adapterView.getItemAtPosition(position);
                pinDestinationAutoCompleteTextView.setText(String.format("%s, %s, %s", mPinCodeDestination.getLocation(), mPinCodeDestination.getPincode(), mPinCodeDestination.getState()));
            }
        });


        pinSourceAutoCompleteTextView.setThreshold(THRESHOLD);
        PinAutoCompleteAdapter pinAutoCompleteAdapter = new PinAutoCompleteAdapter(context);
        pinAutoCompleteAdapter.notifyDataSetChanged();

        pinSourceAutoCompleteTextView.setAdapter(pinAutoCompleteAdapter); // 'this' is Activity instance

        pinSourceAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mPinCodeSource = (PinCode) adapterView.getItemAtPosition(position);

                pinSourceAutoCompleteTextView.setText(String.format("%s, %s, %s", mPinCodeSource.getLocation(), mPinCodeSource.getPincode(), mPinCodeSource.getState()));
            }
        });


        mWeightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                    mWeightUnitTextView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                else
                    mWeightUnitTextView.setTextColor(context.getResources().getColor(R.color.colorHint));
            }
        });
        mInvoiceValueEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    mCurrencyUnitTextView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                else
                    mCurrencyUnitTextView.setTextColor(context.getResources().getColor(R.color.colorHint));
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

    public void nextScreenParcel(String source, String destination, String weight, String invoice, String width, String height, String length, String description, String deliveryType, boolean toggleMultiple) {
        Parcel parcel = new Parcel(source, destination, deliveryType, "Parcel", weight, invoice, length, width, height, description);
        submitCouriers(context, parcel, toggleMultiple);

    }

    public void nextScreenDocument(String source, String destination, String weight, String invoice, String description, String deliveryType, boolean toggleMultiple) {

        Parcel parcel = new Parcel(source, destination, deliveryType, "Parcel", weight, invoice, null, null, null, description);
        submitCouriers(context, parcel, toggleMultiple);
    }

        /*void setText(String text) {
            editor.setText(text);
            editor.setHint(editor.getContext().getString(R.string.hint,
                    getAdapterPosition()+1));
        }

        String getText() {
            return(editor.getText().toString());
        }*/
}
