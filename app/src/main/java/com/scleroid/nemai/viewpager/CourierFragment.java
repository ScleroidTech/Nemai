package com.scleroid.nemai.viewpager;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scleroid.nemai.AppDatabase;
import com.scleroid.nemai.GarlandApp;
import com.scleroid.nemai.R;
import com.scleroid.nemai.adapter.recyclerview.CourierAdapter;
import com.scleroid.nemai.controller.CourierLab;
import com.scleroid.nemai.models.Courier;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.utils.DateUtils;
import com.scleroid.nemai.viewmodels.CourierViewModel;

import java.util.ArrayList;
import java.util.List;

import io.bloco.faker.Faker;

import static android.support.v7.widget.DividerItemDecoration.HORIZONTAL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourierFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourierFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourierFragment extends Fragment implements GarlandApp.FakerReadyListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARCEL = "parcel";
    private OnFragmentInteractionListener mListener;
    /**
     * List of all couriers,
     * Should be Retained over the app orientation
     */
    private List<Courier> courierList;
    private Parcel parcel;
    private CourierAdapter courierAdapter;

    public CourierFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 id of the parcel.
     * @return A new instance of fragment CourierFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourierFragment newInstance(Parcel param1) {
        CourierFragment fragment = new CourierFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARCEL, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            parcel = getArguments().getParcelable(ARG_PARCEL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        updateActionBar();

        View view = inflater.inflate(R.layout.fragment_courier, container, false);
        initializeRecyclerView(view);

        CourierViewModel courierViewModel = ViewModelProviders.of(CourierFragment.this).get(CourierViewModel.class);

        courierViewModel.getCourierList().observe(CourierFragment.this, couriers -> {
            courierAdapter.addData(couriers, null);
            courierAdapter.notifyDataSetChanged();
        });
        //TODO hide & un hide views according to data received
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * FakerListener, Listens to the fake data creation API,
     * & called when it's ready
     *
     * @param faker object of the Faker class
     */
    @Override
    public void onFakerReady(Faker faker) {
        populateCouriers(faker);
    }

    /**
     * Returns one fake courier at a time,
     *
     * @deprecated
     */
    private Courier createFakeCourierData(Faker faker) {
        return new Courier(
                faker.name.name(),
                faker.commerce.price().doubleValue(),
                faker.commerce.productName(),
                faker.team.sport(),
                faker.avatar.image(),
                faker.number.between() + "", faker.internet.macAddress() + "");
    }

    private void initializeRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_courier_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courierAdapter = new CourierAdapter();
        recyclerView.setAdapter(courierAdapter);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), HORIZONTAL);
        recyclerView.addItemDecoration(itemDecor);

    }

    /**
     * Used to populate the view with garbage data,
     *
     * @deprecated to be removed when actual data comes
     */
    private void populateCouriers(Faker faker) {
        List<Courier> couriers = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            Courier fakeCourierData = createFakeCourierData(faker);
            CourierLab.addCourier(fakeCourierData, AppDatabase.getAppDatabase(getContext()));
            couriers.add(fakeCourierData);
        }

        courierList = couriers;
    }

    private void updateActionBar() {

        String sub = parcel.getSourcePinCode().getLocation() + " - " + parcel.getDestinationPinCode().getLocation();
        DateUtils dateUtils = new DateUtils();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(sub);
        activity.getSupportActionBar().setSubtitle(dateUtils.getFormattedDate(parcel.getParcelDate()));
    }


    /**
     * TODO
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
