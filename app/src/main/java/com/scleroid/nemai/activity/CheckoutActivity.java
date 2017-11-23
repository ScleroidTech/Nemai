package com.scleroid.nemai.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ramotion.garlandview.TailLayoutManager;
import com.ramotion.garlandview.TailRecyclerView;
import com.ramotion.garlandview.TailSnapHelper;
import com.ramotion.garlandview.header.HeaderTransformer;
import com.scleroid.nemai.GarlandApp;
import com.scleroid.nemai.R;
import com.scleroid.nemai.adapter.AddressLab;
import com.scleroid.nemai.adapter.AppDatabase;
import com.scleroid.nemai.adapter.ParcelLab;
import com.scleroid.nemai.inner.InnerItem;
import com.scleroid.nemai.models.Address;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.outer.OuterAdapter;
import com.scleroid.nemai.utils.Events;
import com.scleroid.nemai.utils.GlobalBus;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.bloco.faker.Faker;

import static com.scleroid.nemai.fragment.AddressFragment.EXTRA_ADDRESS_LINE_1;
import static com.scleroid.nemai.fragment.AddressFragment.EXTRA_ADDRESS_LINE_2;
import static com.scleroid.nemai.fragment.AddressFragment.EXTRA_CITY;
import static com.scleroid.nemai.fragment.AddressFragment.EXTRA_MOBILE;
import static com.scleroid.nemai.fragment.AddressFragment.EXTRA_NAME;
import static com.scleroid.nemai.fragment.AddressFragment.EXTRA_PIN;
import static com.scleroid.nemai.fragment.AddressFragment.EXTRA_SERIAL_NO;
import static com.scleroid.nemai.fragment.AddressFragment.EXTRA_STATE;


/**
 * Created by scleroid on 26/8/17.
 */

public class CheckoutActivity extends AppCompatActivity implements GarlandApp.FakerReadyListener {
    private final static int OUTER_COUNT = 5;
    private final static int INNER_COUNT = 5;


    Context context;
    List<List<Address>> outerData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        context = CheckoutActivity.this;
        ((GarlandApp) getApplication()).addListener(this);
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
        List<com.scleroid.nemai.models.Parcel> parcels = new ArrayList<>();

        populateData(faker);
        List<Address> innerData;
        innerData = AddressLab.getAllAddresss(context);
        parcels = ParcelLab.getAllParcels(context);

        outerData = sortAddresses(parcels, innerData);

        // outerData = Collections.emptyList();
        initRecyclerView(outerData, parcels);
    }

    private List<List<Address>> sortAddresses(List<Parcel> parcels, List<Address> innerData) {
        List<Address> tempList = new ArrayList<>();
        for (Parcel parcel : parcels) {
            for (Address address : innerData) {
                if (parcel.getDestinationPin().equals(address.getPincode())) {
                    tempList.add(address);
                }
            }
            outerData.add(innerData);
        }
        return outerData;
    }

    @NonNull
    private void populateData(Faker faker) {
        List<Address> innerData = new ArrayList<>();
        List<Address> tempList = new ArrayList<>();
        for (int i = 0; i < OUTER_COUNT; i++) {
            Parcel parcel = createParcelData(faker);
            ParcelLab.addParcel(parcel, AppDatabase.getAppDatabase(context));
        }

        for (int i = 0; i < OUTER_COUNT; i++) {

            for (int j = 0; j < INNER_COUNT - i; j++) {
                //innerData.add(createInnerData(faker));
                AddressLab.addAddress(createInnerData(faker), AppDatabase.getAppDatabase(context));

            }


            //  parcels.add(createParcelData(faker));

        }

    }

    private void initRecyclerView(List<List<Address>> data, List<Parcel> parcels) {
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        final TailRecyclerView rv = findViewById(R.id.recycler_view);
        ((TailLayoutManager) rv.getLayoutManager()).setPageTransformer(new HeaderTransformer());
        rv.setAdapter(new OuterAdapter(data, parcels));

        new TailSnapHelper().attachToRecyclerView(rv);
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
        return new com.scleroid.nemai.models.Parcel(
                faker.address.city(),
                faker.address.city(),
                "Domestic",
                "Parcel",
                faker.number.positive(0, 10),
                faker.number.positive(0, 1000),
                faker.number.positive(),
                faker.number.positive(),
                faker.number.positive(),
                faker.company.catchPhrase()
        );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnInnerItemClick(InnerItem item) {
        final Address itemData = item.getItemData();
        if (itemData == null) {
            return;
        }

       /* DetailsActivity.start(this,
                item.getItemData().name, item.mAddress.getText().toString(),
                item.getItemData().avatarUrl, item.itemView, item.mAvatarBorder);*/
    }

    @Subscribe
    public void onAddressMessage(Events.AddressMessage fragmentActivityMessage) {
        Bundle bundle = fragmentActivityMessage.getMessage();
        Address model = new Address(bundle.getString(EXTRA_NAME), bundle.getString(EXTRA_ADDRESS_LINE_1),
                bundle.getString(EXTRA_ADDRESS_LINE_2), bundle.getString(EXTRA_STATE), bundle.getString(EXTRA_CITY), bundle.getString(EXTRA_PIN), bundle.getString(EXTRA_MOBILE), bundle.getLong(EXTRA_SERIAL_NO));
        Log.d("CHeckout", "onAddress Eventbus");
        AddressLab.addAddress(model, AppDatabase.getAppDatabase(context));

        //   setContent(model);


    }



}