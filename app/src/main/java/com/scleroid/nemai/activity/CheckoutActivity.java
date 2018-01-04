package com.scleroid.nemai.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.ramotion.garlandview.TailLayoutManager;
import com.ramotion.garlandview.TailRecyclerView;
import com.ramotion.garlandview.TailSnapHelper;
import com.ramotion.garlandview.header.HeaderTransformer;
import com.scleroid.nemai.AppDatabase;
import com.scleroid.nemai.GarlandApp;
import com.scleroid.nemai.R;
import com.scleroid.nemai.controller.AddressLab;
import com.scleroid.nemai.controller.ParcelLab;
import com.scleroid.nemai.models.Address;
import com.scleroid.nemai.models.OrderedCourier;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.models.PinCode;
import com.scleroid.nemai.outer.OuterAdapter;
import com.scleroid.nemai.outer.OuterItem;
import com.scleroid.nemai.utils.Events;
import com.scleroid.nemai.utils.GlobalBus;
import com.scleroid.nemai.viewmodels.CheckoutViewModel;
import com.scleroid.nemai.viewmodels.OrderViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.bloco.faker.Faker;
import io.fabric.sdk.android.Fabric;

import static com.google.common.util.concurrent.Runnables.doNothing;
import static com.scleroid.nemai.fragment.AddressFragment.EXTRA_ADDRESS;
import static com.scleroid.nemai.fragment.AddressFragment.EXTRA_NEW_ADDRESS;


/**
 * @author Ganesh Kaple
 * @since 26/8/17
 */

public class CheckoutActivity extends AppCompatActivity implements GarlandApp.FakerReadyListener {
    private final static int OUTER_COUNT = 5;
    private final static int INNER_COUNT = 5;
    /**
     * Value used to generate Log
     */
    private static final String TAG = "com.scleroid.nemai.activity.checkoutActivity";
    Context context;
    List<List<Address>> outerData = new ArrayList<>();

    List<OrderedCourier> orderedCourierList = new ArrayList<>();
    /**
     * //TODO Future Implementation, remove all stuff here
     * Holds the positions value with true if address is already selected, false if not
     */
    SparseBooleanArray selectedPositions = new SparseBooleanArray();


    private CheckoutViewModel viewModel;
    private OuterAdapter outerAdapter;
    private TailRecyclerView outerRecyclerView;
    private ActionMode mActionMode;
    private Menu context_menu;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_checkout_activity, menu);
            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_next:
                    Toasty.error(context, "CLicked me");
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;

        }
    };
    private OrderViewModel orderViewModel;
    private boolean isFinalized = false;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private MenuItem nextItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
        setContentView(R.layout.activity_checkout);
        FacebookSdk.sdkInitialize(CheckoutActivity.this);
        context = CheckoutActivity.this;
        ((GarlandApp) getApplication()).addListener(this);
        initRecyclerView(new ArrayList<Address>(), new ArrayList<Parcel>());
        orderViewModel = ViewModelProviders.of(CheckoutActivity.this).get(OrderViewModel.class);

        orderViewModel.getOrderList().observe(CheckoutActivity.this, new Observer<List<OrderedCourier>>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onChanged(@Nullable List<OrderedCourier> orderedCouriers) {
                orderedCourierList = orderedCouriers;
                outerAdapter.setOrderedCourierList(orderedCouriers);
                Log.d(TAG, "Courier List updated, Yo" + orderedCouriers.size());
                isFinalized = orderedCouriers.size() == outerAdapter.getParcels().size();

            }
        });
        viewModel = ViewModelProviders.of(CheckoutActivity.this).get(CheckoutViewModel.class);

        viewModel.getParcelList().observe(CheckoutActivity.this, parcels -> {

            outerAdapter.setParcels(parcels);
            outerAdapter.notifyDataSetChanged();
            if (selectedPositions.size() != outerAdapter.getItemCount()) populateSelectionMap();

        });
        viewModel.getAddressList().observe(CheckoutActivity.this, addresses -> {
            outerAdapter.updateAddressList(addresses);
            outerAdapter.notifyDataSetChanged();
        });

        // populateSelectionMap();


    }

    /**
     * Populate the map with false values
     *
     * @see SparseBooleanArray
     * @deprecated no longer necessary, because we're now using
     */
    private void populateSelectionMap() {
        for (int i = 0; i < outerAdapter.getItemCount(); i++) {

            selectedPositions.put(i, false);
            if (!selectedPositions.valueAt(i)) selectedPositions.put(i, false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onFakerReady(Faker faker) {

        //  populateData(faker);

    }


    @NonNull
    private void populateData(Faker faker) {
        List<Address> innerData = new ArrayList<>();
        List<Address> tempList = new ArrayList<>();
        for (int i = 0; i < OUTER_COUNT; i++) {
            ParcelLab.addParcel(createParcelData(faker), AppDatabase.getAppDatabase(context));
        }

        for (int i = 0; i < OUTER_COUNT; i++) {

            for (int j = 0; j < INNER_COUNT - i; j++) {
                //innerData.add(createInnerData(faker));
                //AddressLab.addAddress(createInnerData(faker), AppDatabase.getAppDatabase(context));

            }


            //  parcels.add(createParcelData(faker));

        }

    }

    private void initRecyclerView(List<Address> data, List<Parcel> parcels) {
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        outerRecyclerView = findViewById(R.id.recycler_view);
        ((TailLayoutManager) outerRecyclerView.getLayoutManager()).setPageTransformer(new HeaderTransformer());
        outerAdapter = new OuterAdapter(data, parcels);
        outerRecyclerView.setAdapter(outerAdapter);
        outerRecyclerView.setOnFlingListener(null);
        new TailSnapHelper().attachToRecyclerView(outerRecyclerView);
    }

    private Address createInnerData(Faker faker) {
        return new Address(
                faker.name.name(),
                faker.address.streetAddress(),
                faker.address.streetName(),
                faker.address.state(),
                faker.address.city(),
                (faker.number.between(111111, 999999)) + "",
                faker.phoneNumber.phoneNumber()
        );
    }

    private com.scleroid.nemai.models.Parcel createParcelData(Faker faker) {
        String source = faker.address.city();
        String dest = faker.address.city();
        return new com.scleroid.nemai.models.Parcel(
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

    @SuppressLint("LongLogTag")
    @Subscribe
    public void onAddressMessage(Events.AddressMessage fragmentActivityMessage) {
        Bundle bundle = fragmentActivityMessage.getMessage();
        Address model = bundle.getParcelable(EXTRA_ADDRESS); /*new Address(bundle.getString(EXTRA_NAME), bundle.getString(EXTRA_ADDRESS_LINE_1),
                bundle.getString(EXTRA_ADDRESS_LINE_2), bundle.getString(EXTRA_STATE), bundle.getString(EXTRA_CITY), bundle.getString(EXTRA_PIN), bundle.getString(EXTRA_MOBILE), bundle.getLong(EXTRA_SERIAL_NO));
       */
        Log.d("CHeckout", "onAddress Eventbus");
        if (bundle.getBoolean(EXTRA_NEW_ADDRESS)) {
            AddressLab.addAddress(model, AppDatabase.getAppDatabase(context));
        } else {
            AddressLab.updateAddress(model, AppDatabase.getAppDatabase(context));
        }
        Log.d(TAG, " Is this address method  working");
        //   setContent(model);


    }

    /**
     * //TODO Future Implementation
     * The subscribe method of
     *
     * @param selectionMap
     * @see EventBus
     * Handles the message sent by An event sent at
     * @see OuterItem
     * which provides which items are selected & which aren't
     */
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
                if (orderedCourierList == null || orderedCourierList.isEmpty())
                // outerRecyclerView.smoothScrollToPosition(0);
                //This doesn't do anything
                {
                    Runnable runnable = doNothing();
                } else
                    isAllDone();

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

        if (orderedCourierList.size() != outerAdapter.getParcels().size()) {


            // Log.d(TAG, "What's the map value " + selectedPositions. + "=" + entry.getValue());

                //TODO The commented code doesn't work, keeping for future Implementation

            // outerRecyclerView.scrollToPosition(outerAdapter.getItemCount() - 5);*/
                    // TailLayoutManager layoutManager = (TailLayoutManager) outerRecyclerView.getLayoutManager();
                    //   layoutManager.scrollToPosition(5);
            for (int i = 0; i < selectedPositions.size(); i++) {
                int key = selectedPositions.keyAt(i);
                boolean value = selectedPositions.valueAt(i);
                // get the object by the key.
                if (!value && key != -1) {
                    Toasty.warning(context, "You haven't selected address for parcel No. " + key + 1).show();
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
                    Toasty.warning(context, "You haven't selected all addresses for parcels").show();
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