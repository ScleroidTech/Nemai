package com.scleroid.nemai.fragment;
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
     //TODO IMP https://android.jlelse.eu/android-architecture-components-room-livedata-and-viewmodel-fca5da39e26b
     //TODO https://uk.linkedin.com/in/chrisbanes/
*/
//TODO https://uk.linkedin.com/in/chrisbanes/

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;
import com.scleroid.nemai.AppDatabase;
import com.scleroid.nemai.R;
import com.scleroid.nemai.adapter.PagerAdapter;
import com.scleroid.nemai.controller.ParcelLab;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.models.PinCode;
import com.scleroid.nemai.utils.Events;
import com.scleroid.nemai.utils.GlobalBus;
import com.scleroid.nemai.viewmodels.ParcelViewModel;
import com.scleroid.nemai.volley_support.ShowLoader;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.scleroid.nemai.fragment.DatePickerFragment.EXTRA_PARCEL;
import static com.scleroid.nemai.network.NetworkCalls.submitCouriers;


//TODO CHange most activities to fragment if performance becomes a bottleneck
//TODO implement ROOm
//TODO implement this http://droidmentor.com/credit-card-form/


public class HomeFragment extends Fragment {
    public static final int THRESHOLD = 3;
    // YourActivity.java
    public final static String LIST_STATE_KEY = "recycler_list_state";
    private static final String ARG_PARCEL_ID = "parcel_id";
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String TAG_COURIERS = "req_couriers";
    public static PinCode mPinCodeDestination, mPinCodeSource;
    public static int parcelCount = 1;
    static String select;
    final CharSequence[] day_radio = {"Pune,MH,India", "Mumbai, MH,India", "Nagpur, MH, India"};
    ParcelViewModel parcelViewModel;
    Button mSubmitButton;
    FloatingActionButton fabNewCourier;
    Parcel parcel;
    PagerAdapter recycleViewPagerAdapter;
   RecyclerViewPager recyclerViewPager;
    List<Parcel> crimes;
    Parcel parcelCurrent;
    Parcelable listState;
    private Context context;
    private ShowLoader loader;
    private LinearLayoutManager mLayoutManager;
    private FloatingActionButton fabDeleteCourier;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle state) {


        context = getActivity();
        loader = new ShowLoader(context);

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        v.clearFocus();

        setupRecyclerView(v, inflater, context);

        if (state != null)
            listState = state.getParcelable(LIST_STATE_KEY);
        if (listState != null) {
            recyclerViewPager.getLayoutManager().onRestoreInstanceState(listState);
        }


        parcelViewModel = ViewModelProviders.of(HomeFragment.this).get(ParcelViewModel.class);

        parcelViewModel.getParcelList().observe(HomeFragment.this, parcels -> {

            recycleViewPagerAdapter.updateParcelList(parcels);

            crimes = parcels;
            if ((parcels != null ? parcels.size() : 0) == 0)
                createDefaultParcel();


        });
        /*
        if (crimes == null || crimes.size() == 0){
            createDefaultParcel();
            Log.d(TAG,"Adding a parcel");
        }*/
        fabDeleteCourier = v.findViewById(R.id.fab_delete);
        fabDeleteCourier.setOnClickListener(v1 -> deleteParcel());
        fabNewCourier = v.findViewById(R.id.fab_new_data);
        fabNewCourier.setOnClickListener(v1 -> {
//TODO Add A cartView, then refresh the layout(done), add data to database, & check existing data before sending it to server.& send all data to server at once


            //validateFields(false);
            //submitRequest(null, false);
            submitData();


        });


        mSubmitButton = v.findViewById(R.id.btn_submit);

        mSubmitButton.setOnClickListener(view -> {
            if (submitData() != null)
                sendCouriers();


            // Intent i = new Intent(getActivity(), PartnerActivity.class);
            //startActivity(i);
        });
        setButtonsVisibility(recyclerViewPager.getCurrentPosition());
        return v;
    }

    private void deleteParcel() {
        int position = recyclerViewPager.getCurrentPosition();

        Parcel parcel = crimes.get(position);
        ParcelLab.deleteCurrentParcel(AppDatabase.getAppDatabase(context), parcel);
    }

    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        // Save list state
        listState = recyclerViewPager.getLayoutManager().onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, listState);
    }



    public void createDefaultParcel() {
        ParcelLab.newParcel(context);
    }

    private void setupRecyclerView(View v, LayoutInflater inflater, Context context) {
        recyclerViewPager = v.findViewById(R.id.pager);
        recyclerViewPager.setLayoutManager(new LinearLayoutManager(this.context,
                LinearLayoutManager.HORIZONTAL, false));
        recycleViewPagerAdapter = new PagerAdapter(recyclerViewPager, inflater, context, new ArrayList<Parcel>());
        recyclerViewPager.setAdapter(recycleViewPagerAdapter);
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
                int width = 0;
                if (recyclerViewPager.getChildAt(0) != null)
                    width = recyclerViewPager.getChildAt(0).getWidth();
                int padding = (recyclerViewPager.getWidth() - width) / 4;
                Log.d(TAG, "childCount " + childCount + " width " + width + " padding " + padding + " widthMain " + recyclerViewPager.getWidth());


                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);

                    float rate = 0;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                    } else {

                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                    }
                }
            }
        });
        //TODO implement this on empty address
        //
        //
        //
        //
        //
        //
        // recyclerViewPager.scrollToPosition();
        recyclerViewPager.addOnPageChangedListener((oldPosition, newPosition) -> {

            if (!crimes.get(oldPosition).equals(recycleViewPagerAdapter.holder.getParcel()))
                ParcelLab.addParcel(recycleViewPagerAdapter.holder.getParcel(), AppDatabase.getAppDatabase(context));
            setButtonsVisibility(newPosition);

            Log.d("test", "oldPosition:" + oldPosition + " newPosition:" + newPosition + " parcel at old " + crimes.get(oldPosition).toString() + " parcel at new " + crimes.get(newPosition).toString());
        });

        recyclerViewPager.addOnLayoutChangeListener((v12, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
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


        });
    }

    /**
     * sets the visibility of new & delete FAB
     *
     * @param newPosition the position where the view is just scrolled
     */
    private void setButtonsVisibility(int newPosition) {
        if (crimes != null) {

            if (crimes.size() <= 1) fabDeleteCourier.setVisibility(View.GONE);
            else fabDeleteCourier.setVisibility(View.VISIBLE);
            if (newPosition == crimes.size() - 1) fabNewCourier.setVisibility(View.VISIBLE);
            else fabNewCourier.setVisibility(View.GONE);
        } else {
            fabDeleteCourier.setVisibility(View.GONE);
            fabNewCourier.setVisibility(View.VISIBLE);
        }
    }


    private Parcel submitData() {
        if (parcelCurrent == null) parcelCurrent = recycleViewPagerAdapter.holder.getParcel();
        for (Parcel parcel :
             crimes) {
            //TODO check for all blank values
            Parcel parcelNew = recycleViewPagerAdapter.holder.validateFields(parcel);
            if (parcelNew == null) {
                recyclerViewPager.smoothScrollToPosition(crimes.indexOf(parcel));
                return null;
            }
        }


        //  ParcelLab.addParcel(parcel, context);
        parcelCurrent = ParcelLab.newParcel(context);
        return parcel;
        //    parcels = updateParcelList(context);

    }

    private void sendCouriers() {
        for (Parcel parcelTemp : crimes) {
            submitCouriers(context, parcelTemp, TAG_COURIERS, loader);
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

        if (recycleViewPagerAdapter == null) {
            recycleViewPagerAdapter = new PagerAdapter(recyclerViewPager, getLayoutInflater(), getContext(), new ArrayList<Parcel>());
            recyclerViewPager.setAdapter(recycleViewPagerAdapter);
        }/* else {
            int pos = RecyclerView.generateViewId();
            recycleViewPagerAdapter.setParcels(crimes);
            recycleViewPagerAdapter.notifyItemChanged(pos);
        }
*/
        // updateSubtitle();
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

    @Override
    public void onResume() {
        super.onResume();
        GlobalBus.getBus().register(this);
        loader.dismissDialog();
        /*if (listState != null) {
            recyclerViewPager.getLayoutManager().onRestoreInstanceState(listState);
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        // GlobalBus.getBus().unregister(this);
        if (loader != null) {
            loader.dismissDialog();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        GlobalBus.getBus().unregister(this);

    }

    @Subscribe
    public void onDateMessage(Events.DateMessage fragmentActivityMessage) {
        Bundle bundle = fragmentActivityMessage.getMessage();

        Parcel parcel = bundle.getParcelable(EXTRA_PARCEL);
       /* Date date = (Date) bundle.getSerializable(EXTRA_DATE);
        long lonely = bundle.getLong(EXTRA_SERIAL);
        Log.d("CHeckout", "onDate Eventbus");*/
        ParcelLab.updateParcel(context, parcel);
        recycleViewPagerAdapter.notifyDataSetChanged();
        // setContent(model);


    }


}


