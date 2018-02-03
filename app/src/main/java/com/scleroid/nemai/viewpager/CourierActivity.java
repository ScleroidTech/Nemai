package com.scleroid.nemai.viewpager;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.scleroid.nemai.AppDatabase;
import com.scleroid.nemai.GarlandApp;
import com.scleroid.nemai.R;
import com.scleroid.nemai.activity.CheckoutActivity;
import com.scleroid.nemai.adapter.recyclerview.ParcelAdapterForAddress;
import com.scleroid.nemai.controller.CourierLab;
import com.scleroid.nemai.controller.ParcelLab;
import com.scleroid.nemai.models.Courier;
import com.scleroid.nemai.models.OrderedCourier;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.models.PinCode;
import com.scleroid.nemai.utils.EventBusUtils;
import com.scleroid.nemai.utils.Events;
import com.scleroid.nemai.viewmodels.OrderViewModel;
import com.scleroid.nemai.viewmodels.ParcelViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import hugo.weaving.DebugLog;
import io.bloco.faker.Faker;

public class CourierActivity extends AppCompatActivity implements GarlandApp.FakerReadyListener {


    /**
     * Value used to generate Log
     */
    private static final String TAG = CourierActivity.class.getSimpleName();
    private static final int OUTER_COUNT = 5;
    private final EventBusUtils eventBusUtils = new EventBusUtils();
    Context context;

    List<OrderedCourier> orderedCourierList = new ArrayList<>();
    /**
     * //TODO Future Implementation, remove all stuff here
     * Holds the positions value with true if courier is already selected, false if not
     */
    SparseBooleanArray selectedPositions = new SparseBooleanArray();

    private ParcelViewModel parcelViewModel;


    private ActionMode mActionMode;
    private Menu context_menu;
    private OrderViewModel orderViewModel;
    private boolean isFinalized = false;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private MenuItem nextItem;
    private List<Parcel> parcels = new ArrayList<>();
    private ViewPager viewPager;


    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_courier_viewpager);
        viewPager = findViewById(R.id.viewpager);


        // ((GarlandApp) getApplication()).addListener(this);
        setupViewModels();
        setupViewPager();
    }

    private void setupViewPager() {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Parcel parcel = parcels.get(position);
                return CourierFragment.newInstance(parcel);
            }

            @Override
            public int getCount() {
                return parcels.size();
            }
        });
        viewPager.setCurrentItem(0);
    }

    /**
     * Setting Up ViewModels,
     * Which will provide the lists of data whenever needed from the database on demand
     * in realtime(with milliseconds late, I mean't)
     */
    private void setupViewModels() {
        setupOrderViewModel();
        parcelViewModel = ViewModelProviders.of(CourierActivity.this).get(ParcelViewModel.class);


        parcelViewModel.getParcelList().observe(CourierActivity.this, parcels -> {
            this.parcels = parcels;
            viewPager.getAdapter().notifyDataSetChanged();
            /*parcelAdapter.setParcels(parcels);
            parcelAdapter.notifyDataSetChanged();
            if (selectedPositions.size() != parcelAdapter.getItemCount()) populateSelectionMap();*/

        });

       /* CourierViewModel courierViewModel = ViewModelProviders.of(CourierActivity.this).get(CourierViewModel.class);

        courierViewModel.getCourierList().observe(CourierActivity.this, couriers -> {
           *//* parcelAdapter.updateCourierList(couriers);
            parcelAdapter.notifyDataSetChanged();*//*
        });*/
    }

    private void setupOrderViewModel() {
        orderViewModel = ViewModelProviders.of(CourierActivity.this).get(OrderViewModel.class);

        orderViewModel.getOrderList().observe(CourierActivity.this, orderedCouriers -> {
            orderedCourierList = orderedCouriers;

            Log.d(TAG, "Courier List updated, Yo" + orderedCouriers.size());
            //TODO what to do?
            // isFinalized = orderedCouriers.size() == parcels.getParcels().size();

        });
    }


    /**
     * Populate the map with false values
     *
     * @see SparseBooleanArray
     * @deprecated no longer necessary, because we're now using
     */
    @DebugLog
    private void populateSelectionMap() {
        CourierListAdapter parcelAdapter = null;
        for (int i = 0; i < parcelAdapter.getItemCount(); i++) {

            selectedPositions.put(i, false);
            if (!selectedPositions.valueAt(i)) selectedPositions.put(i, false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //  eventBusUtils.registerEventBus(CourierActivity.this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        //eventBusUtils.deRegisterEventBus(CourierActivity.this);
    }


    @Override
    public void onFakerReady(Faker faker) {

        //  populateData(faker);

    }


    private void populateData(Faker faker) {
        List<Courier> innerData = new ArrayList<>();
        List<Courier> tempList = new ArrayList<>();
        for (int i = 0; i < OUTER_COUNT; i++) {
            ParcelLab.addParcel(createParcelData(faker), AppDatabase.getAppDatabase(context));
            CourierLab.addCourier(createInnerData(faker), AppDatabase.getAppDatabase(context));
        }


    }



    private Courier createInnerData(Faker faker) {
        return new Courier(
                faker.name.name(),
                faker.commerce.price().doubleValue(),
                faker.commerce.productName(),
                faker.team.sport(),
                faker.avatar.image(),
                faker.number.between() + "", faker.internet.macAddress() + "");
    }

    private Parcel createParcelData(Faker faker) {
        String source = faker.address.city();
        String dest = faker.address.city();
        return new Parcel(
                source,
                dest,
                "Domestic",
                "Parcel",
                faker.number.positive(0, 10),
                faker.number.positive(0, 1000),
                faker.number.positive(),
                faker.number.positive(),
                faker.number.positive(),
                faker.company.catchPhrase(),
                faker.date.forward(),
                new PinCode(source, (faker.number.between(111111, 999999)) + "", faker.address.state(), faker.address.streetName()),
                new PinCode(dest, (faker.number.between(111111, 999999)) + "", faker.address.state(), faker.address.streetName())
        );
    }

    /**
     * //TODO Future Implementation
     * The subscribe method of
     *
     * @param selectionMap
     * @see EventBus
     * Handles the message sent by An event sent at
     * @see com.scleroid.nemai.viewholders.ParcelHolderForCouriers
     * .
     * which provides which items are selected & which aren't
     */
    @DebugLog
    @SuppressLint("LongLogTag")
    @Subscribe
    public void onSelection(Events.selectionMap selectionMap) {
        int position = selectionMap.getPosition();
        // saves if the position is selected or not
        boolean isSelected = selectionMap.isSelected();
        Log.d(TAG, "is this called?" + isSelected + " " + position);

        selectedPositions.put(position, isSelected);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checkout_activity, menu);
        context_menu = menu;
        nextItem = menu.findItem(R.id.action_next);
        // nextItem.setEnabled(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_next:
                //TODO temp code, Uncomment below code
                startActivity(new Intent(CourierActivity.this, CheckoutActivity.class));
             /*   if (orderedCourierList == null || orderedCourierList.isEmpty())
                // outerRecyclerView.smoothScrollToPosition(0);
                //This doesn't do anything
                {
                    Runnable runnable = doNothing();
                } else
                    isAllDone();
*/
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * if already all items have been set, It'll go to next screen, otherwise, it'll throw the Courier number which is not ready
     */

    @SuppressLint("LongLogTag")
    private void isAllDone() {

        invalidateOptionsMenu();
        if (nextItem == null) return;

        ParcelAdapterForAddress parcelAdapter
                = null;
        if (orderedCourierList.size() != parcelAdapter.getParcels().size()) {


            // Log.d(TAG, "What's the map value " + selectedPositions. + "=" + entry.getValue());

            //TODO The commented code doesn't work, keeping for future Implementation

            // outerRecyclerView.scrollToPosition(parcelAdapter.getItemCount() - 5);*/
            // TailLayoutManager layoutManager = (TailLayoutManager) outerRecyclerView.getLayoutManager();
            //   layoutManager.scrollToPosition(5);
            for (int i = 0; i < selectedPositions.size(); i++) {
                int key = selectedPositions.keyAt(i);
                boolean value = selectedPositions.valueAt(i);
                // get the object by the key.
                if (!value && key != -1) {
                    Toasty.warning(context, "You haven't selected courier for parcel No. " + key + 1).show();
                }
            }
            //Another way to do same thing
            // int index = selectedPositions.indexOfValue(false);
            isFinalized = false;




           /*  Just another way for the same approach
           Iterator<Map.Entry<Integer, Boolean>> entryIterator= selectedPositions.entrySet().iterator();
            while (entryIterator.hasNext()){
                Map.Entry entry = entryIterator.next();
                if (!(Boolean)entry.getValue()){
                    outerRecyclerView.scrollToPosition((Integer) entry.getKey());
                    Toasty.warning(context, "You haven't selected all courieres for parcels").show();
                    break;
                }
            }*/

        } else isFinalized = true;

        if (isFinalized) {
            nextItem.setEnabled(true);
        } else nextItem.setEnabled(true);
        getSupportActionBar().setSubtitle("Hey Ya");
    }


}