package com.scleroid.nemai.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ramotion.garlandview.TailLayoutManager;
import com.ramotion.garlandview.TailRecyclerView;
import com.ramotion.garlandview.TailSnapHelper;
import com.ramotion.garlandview.header.HeaderTransformer;
import com.scleroid.nemai.GarlandApp;
import com.scleroid.nemai.R;
import com.scleroid.nemai.inner.InnerItem;
import com.scleroid.nemai.inner.InnerModel;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.outer.OuterAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.bloco.faker.Faker;


/**
 * Created by scleroid on 26/8/17.
 */

public class CheckoutActivity extends AppCompatActivity implements GarlandApp.FakerReadyListener {
    private final static int OUTER_COUNT = 5;
    private final static int INNER_COUNT = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ((GarlandApp) getApplication()).addListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onFakerReady(Faker faker) {
        final List<com.scleroid.nemai.models.Parcel> parcels = new ArrayList<>();
        List<List<InnerModel>> outerData = new ArrayList<>();
        for (int i = 0; i < OUTER_COUNT; i++) {
            final List<InnerModel> innerData = new ArrayList<>();
            for (int j = 0; j < INNER_COUNT; j++) {
                innerData.add(createInnerData(faker));

            }
            // outerData.add(innerData);
            parcels.add(createParcelData(faker));
        }
        // outerData = Collections.emptyList();
        initRecyclerView(outerData, parcels);
    }

    private void initRecyclerView(List<List<InnerModel>> data, List<Parcel> parcels) {
        findViewById(R.id.progressBar).setVisibility(View.GONE);

        final TailRecyclerView rv = findViewById(R.id.recycler_view);
        ((TailLayoutManager) rv.getLayoutManager()).setPageTransformer(new HeaderTransformer());
        rv.setAdapter(new OuterAdapter(data, parcels));

        new TailSnapHelper().attachToRecyclerView(rv);
    }

    private InnerModel createInnerData(Faker faker) {
        return new InnerModel(
                faker.name.name(),
                faker.address.streetAddress(),
                faker.address.streetName(),
                faker.address.state(),
                faker.address.city(),
                faker.number.between(111111, 999999),
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
        final InnerModel itemData = item.getItemData();
        if (itemData == null) {
            return;
        }

       /* DetailsActivity.start(this,
                item.getItemData().name, item.mAddress.getText().toString(),
                item.getItemData().avatarUrl, item.itemView, item.mAvatarBorder);*/
    }


}