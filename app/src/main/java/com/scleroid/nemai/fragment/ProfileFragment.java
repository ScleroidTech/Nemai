package com.scleroid.nemai.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.scleroid.nemai.R;
import com.scleroid.nemai.utils.ProfileUtils;

import es.dmoral.toasty.Toasty;

import static com.scleroid.nemai.activity.MainActivity.session;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final ProfileUtils profileUtils = new ProfileUtils(getContext());
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private ImageView avatar;
    private TextView nameTextView;
    private TextInputEditText emailEditText;
    private TextInputEditText mobileNumber;
    private TextView genderTextView;
    private ImageView headerCover;
    private Button updateButton;
    private boolean toggle = false;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initializeViews(view);
        updateViews();
        return view;
    }

    private void updateViews() {
        profileUtils.setHeaderBackgroundImage(headerCover);
        profileUtils.setUserProfilePicture(avatar);
        profileUtils.setProfileName(nameTextView);

    }

    public void initializeViews(View view) {
        headerCover = view.findViewById(R.id.header_cover);
        avatar = view.findViewById(R.id.avatar);
        genderTextView = view.findViewById(R.id.agender_text_view);
        nameTextView = view.findViewById(R.id.name_text_view);

        emailEditText = view.findViewById(R.id.email_text_input_edit_text);
        toggleEditing(false);
        mobileNumber = view.findViewById(R.id.mobile_text_input_edit_text);
        updateButton = view.findViewById(R.id.update_profile_button);
        updateButton.setOnClickListener(l -> {
            if (toggle) {
                session.getUser().setUserMobileAndEmail(mobileNumber.getText().toString(), emailEditText.getText().toString());
                toggleEditing(false);
                Toasty.success(getContext(), "Your Details Have been Updated");
                //TODO post this updates to server
            } else toggleEditing(true);
        });
    }

    /**
     * Toggle if the profile is editable or not
     *
     * @param toggle value to be set for editable
     */
    public void toggleEditing(boolean toggle) {
        this.toggle = toggle;

        emailEditText.setEnabled(toggle);
        mobileNumber.setEnabled(toggle);
        if (toggle) updateButton.setText("Update Profile");
        else updateButton.setText("Edit Profile");
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
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
