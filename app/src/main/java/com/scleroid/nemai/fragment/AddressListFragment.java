package com.scleroid.nemai.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.nemai.R;
import com.scleroid.nemai.adapter.recyclerview.AddressAdapter;
import com.scleroid.nemai.models.Address;
import com.scleroid.nemai.viewmodels.AddressViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddressListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddressListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private List<Address> addresses;
    private TextView noAddressTitleTextView;
    private TextView noAddressSubtitleTextView;
    private AddressAdapter addressAdapter;

    public AddressListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressListFragment newInstance(String param1, String param2) {
        AddressListFragment fragment = new AddressListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_address_list, container, false);
        noAddressTitleTextView = v.findViewById(R.id.no_address_title);
        noAddressSubtitleTextView = v.findViewById(R.id.no_address_subtitle);


        RecyclerView addressRecyclerView = v.findViewById(R.id.addressRecyclerView);


        AddressViewModel addressViewModel = ViewModelProviders.of(AddressListFragment.this).get(AddressViewModel.class);
        addressViewModel.getAddressList().observe(AddressListFragment.this, addresses -> {
         /*   parcelAdapterForAddress.updateAddressList(addresses);
            parcelAdapterForAddress.notifyDataSetChanged();*/
            addressAdapter.setAddresses(addresses);
            addressAdapter.notifyDataSetChanged();
        });
        if (addresses != null && !addresses.isEmpty()) {
            noAddressTitleTextView.setVisibility(View.GONE);
            noAddressSubtitleTextView.setVisibility(View.GONE);
            addressRecyclerView.setVisibility(View.VISIBLE);
            setupRecyclerView(addressRecyclerView);
        } else {
            noAddressTitleTextView.setVisibility(View.VISIBLE);
            noAddressSubtitleTextView.setVisibility(View.VISIBLE);
            addressRecyclerView.setVisibility(View.GONE);

        }


        //list package


        return v;
    }

    public void setupRecyclerView(RecyclerView addressRecyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        addressRecyclerView.setLayoutManager(linearLayoutManager);
        addressAdapter = new AddressAdapter();
        addressRecyclerView.setAdapter(addressAdapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
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
