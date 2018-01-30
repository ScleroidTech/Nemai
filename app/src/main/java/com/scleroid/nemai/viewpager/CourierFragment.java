package com.scleroid.nemai.viewpager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scleroid.nemai.R;
import com.scleroid.nemai.models.Parcel;
import com.scleroid.nemai.utils.DateUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CourierFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CourierFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourierFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARCEL = "parcel";


    private Parcel parcel;

    private OnFragmentInteractionListener mListener;

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
        return inflater.inflate(R.layout.fragment_courier, container, false);
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void updateActionBar() {

        String sub = parcel.getSourcePinCode().getLocation() + " - " + parcel.getDestinationPinCode().getLocation();
        DateUtils dateUtils = new DateUtils();
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(sub);
        activity.getSupportActionBar().setSubtitle(dateUtils.getFormattedDate(parcel.getParcelDate()));
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
