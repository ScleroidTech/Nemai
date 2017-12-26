package com.scleroid.nemai.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import io.bloco.faker.Faker;

import static com.scleroid.nemai.fragment.AddressFragment.EXTRA_ADDRESS;
import static com.scleroid.nemai.fragment.AddressFragment.EXTRA_NEW_ADDRESS;


/**
 * Created by scleroid on 26/8/17.
 */

public class CheckoutActivity extends AppCompatActivity implements GarlandApp.FakerReadyListener {
    private final static int OUTER_COUNT = 5;
    private final static int INNER_COUNT = 5;
    Context context;
    List<List<Address>> outerData = new ArrayList<>();

    List<OrderedCourier> orderedCourierList = new ArrayList<>();
    /**
     * //TODO Future Implementation, remove all stuff here
     * Holds the positions value with true if address is already selected, false if not
     */
    Map<Integer, Boolean> selectedPositions = new HashMap<>();
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
      /* toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Select Address");
        }

*/
        /*final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);*/
        setContentView(R.layout.activity_checkout);
        FacebookSdk.sdkInitialize(CheckoutActivity.this);
        context = CheckoutActivity.this;
        ((GarlandApp) getApplication()).addListener(this);
        initRecyclerView(new ArrayList<Address>(), new ArrayList<Parcel>());

        orderViewModel = ViewModelProviders.of(CheckoutActivity.this).get(OrderViewModel.class);

        orderViewModel.getOrderList().observe(CheckoutActivity.this, new Observer<List<OrderedCourier>>() {
            @Override
            public void onChanged(@Nullable List<OrderedCourier> orderedCouriers) {
                orderedCourierList = orderedCouriers;
                isFinalized = orderedCouriers.size() == outerAdapter.getParcels().size();
                updateSubtitle();
            }
        });
        viewModel = ViewModelProviders.of(CheckoutActivity.this).get(CheckoutViewModel.class);

        viewModel.getParcelList().observe(CheckoutActivity.this, new Observer<List<Parcel>>() {
            @Override
            public void onChanged(@Nullable List<Parcel> parcels) {

                outerAdapter.updateParcelList(parcels);

            }
        });
        viewModel.getAddressList().observe(CheckoutActivity.this, new Observer<List<Address>>() {
            @Override
            public void onChanged(@Nullable List<Address> addresses) {
                outerAdapter.updateAddressList(addresses);
            }
        });

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
        //  List<com.scleroid.nemai.models.Parcel> parcels = new ArrayList<>();

        populateData(faker);
        // List<Address> innerData;
        // innerData = AddressLab.getAllAddresss(context);
        //  parcels = ParcelLab.getAllParcels(context);

        // outerData = outerAdapter.sortAddresses(parcels, innerData);

        // outerData = Collections.emptyList();
        //  initRecyclerView(outerData, parcels);
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
                AddressLab.addAddress(createInnerData(faker), AppDatabase.getAppDatabase(context));

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
        //   setContent(model);


    }

    /**
     * The subscribe method of
     *
     * @param selectionMap
     * @see EventBus
     * Handles the message sent by An event sent at
     * @see OuterItem
     * which provides which items are selected & which aren't
     */
    @Subscribe
    public void onSelection(Events.selectionMap selectionMap) {
        /**
         * Saves the position of the Courier
         */
        int position = selectionMap.getPosition();
        /**
         * saves if the position is selected or not
         */
        boolean isSelected = selectionMap.isSelected();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            selectedPositions.putIfAbsent(position, isSelected);
        } else if (!selectedPositions.containsKey(position))
            selectedPositions.put(position, isSelected);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checkout_activity, menu);
        context_menu = menu;
        nextItem = menu.findItem(R.id.action_next);
        nextItem.setEnabled(false);
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
                if (orderedCourierList == null || orderedCourierList.isEmpty()) {
                    outerRecyclerView.scrollToPosition(0);
                } else {
                    if (orderedCourierList.size() == outerAdapter.getParcels().size()) {
                        Toast.makeText(getApplicationContext(), "You did it hero", Toast.LENGTH_SHORT).show();
                    } else {
                        //TODO Add logic to get unselected addresses view


                    }
                }
                Toast.makeText(getApplicationContext(), "Settings Click", Toast.LENGTH_SHORT).show();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This will enable or disable the Next Menu Button from the actionBar, if already all items have been set, the button
     */
    private void updateSubtitle() {

        invalidateOptionsMenu();
        if (nextItem == null) return;
        if (orderedCourierList.size() != outerAdapter.getParcels().size()) {

        }

        if (isFinalized) {
            //TODO
            nextItem.setEnabled(true);
        }
        nextItem.setEnabled(false);
        getSupportActionBar().setSubtitle("Hey Ya");
    }


}