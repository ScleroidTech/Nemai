package com.scleroid.nemai.fragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.scleroid.nemai.R;
import com.scleroid.nemai.adapter.AppDatabase;
import com.scleroid.nemai.adapter.PagerAdapter;
import com.scleroid.nemai.adapter.ParcelLab;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.models.PinCode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import es.dmoral.toasty.Toasty;

import static com.scleroid.nemai.network.NetworkCalls.submitCouriers;

//TODO CHange most activities to fragment if performance becomes a bottleneck
//TODO implement ROOm
//TODO implement this http://droidmentor.com/credit-card-form/
public class HomeFragment extends Fragment {
    public static final int THRESHOLD = 3;
    private static final String ARG_PARCEL_ID = "parcel_id";
    private static final String TAG = HomeFragment.class.getSimpleName();
    public static PinCode mPinCodeDestination, mPinCodeSource;
    public static int parcelCount = 1;
    static String select;
    final CharSequence[] day_radio = {"Pune,MH,India", "Mumbai, MH,India", "Nagpur, MH, India"};
    /*RadioButton mParcelRadioButton, mDocumentRadioButton;
    RadioButton mDomesticRadioButton, mInternationalRadioButton;
    LinearLayout mParcelLinearLayout, mDocumentLinearLayout;
    */ Button mSubmitButton;
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
//TODO https://uk.linkedin.com/in/chrisbanes/
    FloatingActionButton fabNewCourier;/*
    TextView mWeightUnitTextView, mCurrencyUnitTextView;
    ImageView mAddressImageView;

    TextInputLayout mWeightTIL, mInvoiceTIL, mLengthTIL, mWidthTIL, mHeightTIL, mDescriptionTIL,
            mPinSourceTIL, mPinDestTIL;
    DelayedAutoCompleteTextView pinSourceAutoCompleteTextView, pinDestinationAutoCompleteTextView;
    EditText mWeightEditText,// mDescDocEditText,
            mInvoiceValueEditText, mPackageLengthParcelEditText, mPackageWidthParcelEditText, mHeightParcelEditText, mDescriptionEditText;*/
    Parcel parcel;
   /* boolean toggleDocParcel = false;//false == doc, true == parcel
    boolean toggleDomInternational = false;//Domestic false , International = true*/
    PagerAdapter recycleViewPagerAdapter;
   /* @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int serialNo;
        Bundle bundle = getArguments();
        if (bundle != null) {
            serialNo = (int) getArguments().getSerializable(ARG_PARCEL_ID);

            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (ParcelLab.getCount(AppDatabase.getAppDatabase(getApplicationContext())) > 0)

                        parcel = ParcelLab.getParcel(AppDatabase.getAppDatabase(getApplicationContext()), serialNo);

                }
            };
        } else parcel = new Parcel();

    }*/
   RecyclerViewPager recyclerViewPager;
    List<Parcel> crimes;
    private Context context;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(int parcel_id) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARCEL_ID, parcel_id);


        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        context = getActivity();
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        v.clearFocus();
        /*ShowLoader showNetworkErrorDialogDialog = new ShowLoader(context);
        showNetworkErrorDialogDialog.showDialog();*/
        Toasty.error(context, "stupid", Toast.LENGTH_LONG, true).show();

        //  showNetworkErrorDialogDialog.showDialog();
        /*try {
            View view = inflater.inflate(R.layout.no_internet_dialog_view,container, false);
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setView(view);
          //  alertDialog.setMessage("Internet not available, Cross check your internet connectivity and try again");
            alertDialog.setCancelable(false);
           // View view = inflater.inflate(R.layout.no_internet_dialog_view,container, false);

            *//*alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {


                }
            });*//*

            alertDialog.show();
        }
        catch(Exception e)
        {
            Log.d(TAG, "Show Dialog: "+e.getMessage());
        }*/

        setupRecyclerView(v);

        // updateUI(context);

       /* mWeightTIL = v.findViewById(R.id.textWeight);
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

        mDescriptionEditText = v.findViewById(R.id.editDescription);*/
       /* if (parcel != null) {
            mWeightEditText.setText(parcel.getWeight());
            mInvoiceValueEditText.setText(parcel.getInvoice());
            mPackageLengthParcelEditText.setText(parcel.getLength());
            mPackageWidthParcelEditText.setText(parcel.getWidth());
            mHeightParcelEditText.setText(parcel.getHeight());
            mDescriptionEditText.setText(parcel.getDescription());
        }*/
//        mDescDocEditText = v.findViewById(R.id.editPckDescDoc);
       /* mParcelRadioButton = v.findViewById(R.id.rParcel);
        mDocumentRadioButton = v.findViewById(R.id.rDocument);
*/
        fabNewCourier = v.findViewById(R.id.fab_new_data);
        fabNewCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//TODO Add A cartView, then refresh the layout(done), add data to database, & check existing data before sending it to server.& send all data to server at once
                //TODO IMP https://android.jlelse.eu/android-architecture-components-room-livedata-and-viewmodel-fca5da39e26b


                //validateFields(false);
                //submitRequest(null, false);
                submitData(true);


            }
        });
//        mParcelLinearLayout = v.findViewById(R.id.linearExpandedParcelView);
//        mDocumentLinearLayout = v.findViewById(R.id.linearExpandedDocumentView);
//        mDocumentLinearLayout.setVisibility(View.VISIBLE);


        //img_address =  v.findViewById(R.id.img_address);


      /*  pinDestinationAutoCompleteTextView = v.findViewById(R.id.pin_dest_autocompletetextview);
        pinDestinationAutoCompleteTextView.setText(parcel.getDestinationPin());
        pinDestinationAutoCompleteTextView.setThreshold(THRESHOLD);
        PinAutoCompleteAdapter pinAutoCompleteAdapter1 = new PinAutoCompleteAdapter(getApplicationContext());
        //pinAutoCompleteAdapter1.notifyDataSetChanged();
        pinDestinationAutoCompleteTextView.setAdapter(pinAutoCompleteAdapter1);*/

/*

        pinDestinationAutoCompleteTextView.setLoadingIndicator(
                (android.widget.ProgressBar) v.findViewById(R.id.pb_loading_indicator2));
        pinDestinationAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                mPinCodeDestination = (PinCode) adapterView.getItemAtPosition(position);
                pinDestinationAutoCompleteTextView.setText(String.format("%s, %s, %s", mPinCodeDestination.getLocation(), mPinCodeDestination.getPincode(), mPinCodeDestination.getState()));
            }
        });
*/


     /*   pinSourceAutoCompleteTextView = v.findViewById(R.id.pin_source_autocompletetextview);
        pinSourceAutoCompleteTextView.setText(parcel.getSourcePin());
        pinSourceAutoCompleteTextView.setThreshold(THRESHOLD);
        PinAutoCompleteAdapter pinAutoCompleteAdapter = new PinAutoCompleteAdapter(getApplicationContext());
        //pinAutoCompleteAdapter.notifyDataSetChanged();
        *//*PinAutoCompleteAdapter pinAutoCompleteAdapter2 = new PinAutoCompleteAdapter(getApplicationContext());
        pinAutoCompleteAdapter1.notifyDataSetChanged();*//*
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
*/

       /* mWeightUnitTextView = v.findViewById(R.id.weight_unit_kg_textView);

        mWeightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus)
                    mWeightUnitTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                else mWeightUnitTextView.setTextColor(getResources().getColor(R.color.colorHint));
            }
        });
*/
       /* mCurrencyUnitTextView = v.findViewById(R.id.currency_unit_text_view);*/
       /* mInvoiceValueEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    mCurrencyUnitTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                else mCurrencyUnitTextView.setTextColor(getResources().getColor(R.color.colorHint));
            }
        });*/

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
                submitData(false);


                // Intent i = new Intent(getActivity(), PartnerActivity.class);
                //startActivity(i);
            }
        });
     /*   mDocumentRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

*/
       /* mDomesticRadioButton = v.findViewById(R.id.rDomestic);
        mInternationalRadioButton = v.findViewById(R.id.rInternational);*/

        /*mInternationalRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        });*/

        return v;
    }

    private void setupRecyclerView(View v) {
        recyclerViewPager = v.findViewById(R.id.pager);
        recyclerViewPager.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        updateUI(context);
        recyclerViewPager.setTriggerOffset(0.15f);
        recyclerViewPager.setFlingFactor(0.25f);
        recyclerViewPager.setHasFixedSize(false);


        recyclerViewPager.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
//                mPositionText.setText("First: " + mRecyclerViewPager.getFirstVisiblePosition());
                int childCount = recyclerViewPager.getChildCount();
                int width = recyclerViewPager.getChildAt(0).getWidth();
                int padding = (recyclerViewPager.getWidth() - width) / 4;
                Log.d(TAG, "childCount " + childCount + " width " + width + " padding " + padding + " widthMain " + recyclerViewPager.getWidth());


                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                    }
                }
            }
        });
        recyclerViewPager.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition);
            }
        });

        recyclerViewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (recyclerViewPager.getChildCount() < 3) {
                    if (recyclerViewPager.getChildAt(1) != null) {
                        if (recyclerViewPager.getCurrentPosition() == 0) {
                            View v1 = recyclerViewPager.getChildAt(1);
                            v1.setScaleY(0.9f);
                            //    v1.setScaleX(0.9f);
                        } else {
                            View v1 = recyclerViewPager.getChildAt(0);
                            v1.setScaleY(0.9f);
                            //   v1.setScaleX(0.9f);
                        }
                    }
                } else {
                    if (recyclerViewPager.getChildAt(0) != null) {
                        View v0 = recyclerViewPager.getChildAt(0);
                        v0.setScaleY(0.9f);
                        // v0.setScaleX(0.9f);
                    }
                    if (recyclerViewPager.getChildAt(2) != null) {
                        View v2 = recyclerViewPager.getChildAt(2);
                        v2.setScaleY(0.9f);
                        //  v2.setScaleX(0.9f);
                    }
                }

            }
        });
    }

    private void submitData(boolean b) {
        Parcel parcel = recycleViewPagerAdapter.holder.validateFields();
        if (parcel == null) return;
        //  ParcelLab.addParcel(parcel, context);
        List<Parcel> parcels = null;
        ParcelLab.newParcel(context);
        parcels = updateParcelList(context);
        if (!b) {
            for (Parcel parcelTemp : parcels) {
                submitCouriers(context, parcelTemp);
            }
        } else {
            updateUI(context);
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

    public void updateUI(Context context) {
        List<Parcel> crimes = null;

        crimes = updateParcelList(context);
        Log.d(TAG, "crimes updateUI " + crimes);

        if (recycleViewPagerAdapter == null) {
            recycleViewPagerAdapter = new PagerAdapter(recyclerViewPager, getLayoutInflater(), getContext(), crimes);
            recyclerViewPager.setAdapter(recycleViewPagerAdapter);
        } else {
            int pos = RecyclerView.generateViewId();
            recycleViewPagerAdapter.setParcels(crimes);
            recycleViewPagerAdapter.notifyItemChanged(pos);
        }

        updateSubtitle();
    }

    private List<Parcel> updateParcelList(Context context) {

        ParcelLab.GetAllAsync task = new ParcelLab.GetAllAsync(AppDatabase.getAppDatabase(context), context);
//        task.setOnResultsListener(new ResultAsyncListener() {
//            @Override
//            public void onResultsReceived(List<Parcel> result) {
//                crimes = result;
//            }
//        });
        try {
            crimes = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "crimes" + crimes + crimes.size());
        if (crimes == null || crimes.size() < 1) {
            ParcelLab.newParcel(context);
            crimes = ParcelLab.getAllParcels(context);
            // task.execute();
            Log.d(TAG, "crimes2" + crimes);
        }
        return crimes;
    }

    private void updateSubtitle() {
    }


}


