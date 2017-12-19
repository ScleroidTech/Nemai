package com.scleroid.nemai.other;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
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
import com.scleroid.nemai.fragment.DatePickerFragment;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.models.PinCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.scleroid.nemai.activity.MainActivity.CURRENT_TAG;
import static com.scleroid.nemai.fragment.HomeFragment.THRESHOLD;
import static com.scleroid.nemai.fragment.HomeFragment.mPinCodeDestination;
import static com.scleroid.nemai.fragment.HomeFragment.mPinCodeSource;

/**
 * Created by Ganesh on 31-10-2017.
 */

public class PageHolder extends RecyclerView.ViewHolder {

    private static final String DIALOG_DATE = "DIALOG_DATE";
    private static final int REQUEST_DATE = 0;
    private static final String TAG = "PageHolder";
    TextView textClockDate;
    RadioButton mParcelRadioButton, mDocumentRadioButton;
    RadioButton mDomesticRadioButton, mInternationalRadioButton;
    LinearLayout mParcelLinearLayout, mDocumentLinearLayout;
    Context context;
    Parcel parcel;
    boolean toggleDocParcel = false;//false == doc, true == parcel
    boolean toggleDomInternational = false;//Domestic false , International = true
    TextView mWeightUnitTextView, mCurrencyUnitTextView, courierCount;
    ImageView mAddressImageView;
    TextInputLayout mWeightTIL, mInvoiceTIL, mLengthTIL, mWidthTIL, mHeightTIL, mDescriptionTIL, /*mDescDocTIL,*/
            mPinSourceTIL, mPinDestTIL, mDateTIL;
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
        mDateTIL = v.findViewById(R.id.date_TIL);
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

        courierCount = v.findViewById(R.id.courier_number_text_view);

        textClockDate = v.findViewById(R.id.parcel_date);

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

        mDescriptionEditText.setMinLines(6);
        mDocumentRadioButton.setTypeface(null, Typeface.BOLD);
        mParcelRadioButton.setTypeface(null, Typeface.NORMAL);

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

        textClockDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo textClockDate.setText(updateDate());
                FragmentManager fragmentManager = ((AppCompatActivity) context).getFragmentManager();
                DialogFragment dialogFragment = DatePickerFragment.newInstance(parcel);
                dialogFragment.setTargetFragment(fragmentManager.findFragmentByTag(CURRENT_TAG), REQUEST_DATE);
                dialogFragment.show(fragmentManager, DIALOG_DATE);
            }
        });
    }

    public Parcel validateFields(Parcel parcel) {
        boolean noSubmit = false;
        String delivery;
        if (isEmpty(textClockDate)) {
            mDateTIL.setErrorEnabled(true);
            mDateTIL.setError("You forgot the date ");
            noSubmit = true;
        } else mDateTIL.setErrorEnabled(false);

        if (isEmpty(pinSourceAutoCompleteTextView)) {
            mPinSourceTIL.setErrorEnabled(true);
            mPinSourceTIL.setError("Enter the Source first");
            noSubmit = true;/*else if (!isValidDate(textClockDate)) {
            mDateTIL.setErrorEnabled(true);
            mDateTIL.setError("Dude, That's in the past. Please Enter a Date after today");
            noSubmit = true;
        } */
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
                return nextScreenParcel(pinSourceAutoCompleteTextView.getText().toString(), pinDestinationAutoCompleteTextView.getText().toString(), Integer.parseInt(mWeightEditText.getText().toString()), Integer.parseInt(mInvoiceValueEditText.getText().toString()), Integer.parseInt(mPackageWidthParcelEditText.getText().toString()), Integer.parseInt(mHeightParcelEditText.getText().toString()), Integer.parseInt(mPackageLengthParcelEditText.getText().toString()), mDescriptionEditText.getText().toString(), delivery, parcel, parcel.getParcelDate());


        } else {

            if (!noSubmit) {
                return nextScreenDocument(pinSourceAutoCompleteTextView.getText().toString(), pinDestinationAutoCompleteTextView.getText().toString(), Integer.parseInt(mWeightEditText.getText().toString()), Integer.parseInt(mInvoiceValueEditText.getText().toString()), mDescriptionEditText.getText().toString(), delivery, parcel, parcel.getParcelDate());

            }
        }
        return null;
    }

    private boolean isValidDate(TextView textClockDate) {
        java.text.DateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy ");
        // DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(textClockDate.getText().toString());
            //     LocalDate date1 = LocalDate.of(D);
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date);
            cal2.setTime(new Date());
            if (cal1.before(cal2)) {
                return false;
            }

        } catch (ParseException e) {
            Log.e(TAG, "Date Parsing Exception " + e);
            return false;
        }
        return true;
    }

    public CharSequence getFormattedDate(Date parcelDate) {
        return DateFormat.format("EEE, MMM dd, yyyy ", parcelDate);
    }


    private boolean isEmpty(TextView text) {
        return TextUtils.isEmpty(text.getText());
    }

    private boolean isEmpty(DelayedAutoCompleteTextView text) {
        return TextUtils.isEmpty(text.getText());
    }

    private boolean isEmpty(EditText text) {
        return TextUtils.isEmpty(text.getText());
    }

    public Parcel nextScreenParcel(String source, String destination, int weight, int invoice, int width, int height, int length, String description, String deliveryType, Parcel parcel, Date parcelDate) {
        return parcel.updateInstance(source, destination, deliveryType, "Parcel", weight, invoice, width, height, length, description, parcelDate, parcel.getSerialNo(), mPinCodeSource, mPinCodeDestination);
    }


    public Parcel nextScreenDocument(String source, String destination, int weight, int invoice, String description, String deliveryType, Parcel parcel, Date parcelDate) {

        return parcel.updateInstance(source, destination, deliveryType, "Document", weight, invoice, 0, 0, 0, description, parcelDate, parcel.getSerialNo(), mPinCodeSource, mPinCodeDestination);

    }

    public void bindParcels(Parcel parcel) {
        this.parcel = parcel;
        if (parcel != null) {
            int invoice = parcel.getInvoice();
            int weight = parcel.getWeight();
            String source = parcel.getSourcePin();
            String destination = parcel.getDestinationPin();
            String deliveryType = parcel.getDeliveryType();
            String packageType = parcel.getPackageType();
            String description = parcel.getDescription();
            DateFormat dt = new DateFormat();
            Date parcelDate = parcel.getParcelDate();


            if (packageType.equals("Parcel")) {
                int width = parcel.getWidth(),
                        height = parcel.getHeight(),
                        length = parcel.getLength();
                mPackageLengthParcelEditText.setText(length == 0 ? null : Integer.toString(length));
                mPackageWidthParcelEditText.setText(width == 0 ? null : Integer.toString(width));
                mHeightParcelEditText.setText(height == 0 ? null : Integer.toString(height));
                mParcelRadioButton.setChecked(true);
            }
            if (deliveryType.equals("International")) {
                mInternationalRadioButton.setChecked(true);
            }
            mDescriptionEditText.setText(description.equals("null") ? null : description);
            mWeightEditText.setText(weight == 0 ? null : Integer.toString(weight));
            mInvoiceValueEditText.setText(invoice == 0 ? null : Integer.toString(invoice));

            //TODO use pincode class instead of this strings, to get more control over this.
            pinSourceAutoCompleteTextView.setText(source.equals("null") ? null : source);
            pinDestinationAutoCompleteTextView.setText(destination.equals("null") ? null : destination);
            String dm = "";
            if (parcelDate != null) {
                dm = getFormattedDate(parcelDate).toString();
            }
            textClockDate.setText(dm);


        }

    }


    public void bindNumber(int position, int size) {
        courierCount.setVisibility(View.VISIBLE);
        courierCount.setText(String.format("Shipment %d of %d", position + 1, size));
    }

        /*void setText(String text) {
            editor.setText(text);
            editor.setHint(editor.getContext().getString(R.string.hint,
                    getAdapterPosition()+1));
        }

        String getText() {
            return(editor.getText().toString());
        }*/

    private String updateDate() {

        //mCrimeDate.setText(dm);
        String dm = "avasfsd;";
        return dm;
        //mCrimeDate.setText(mCrime.getDate().toString());
    }

    public Parcel getParcel() {
        return parcel;
    }
}
