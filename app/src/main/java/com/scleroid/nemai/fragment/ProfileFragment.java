package com.scleroid.nemai.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.scleroid.nemai.R;
import com.scleroid.nemai.activity.registration.PasswordChangeActivity;
import com.scleroid.nemai.activity.registration.SocialRegisterActivity;
import com.scleroid.nemai.utils.DateUtils;
import com.scleroid.nemai.utils.ProfileUtils;

import java.util.Date;
import java.util.regex.Pattern;

import static com.basgeekball.awesomevalidation.ValidationStyle.TEXT_INPUT_LAYOUT;
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
    private static final int REQUEST_DATE = 0;
    private static final String DIALOG_DATE = "DIALOG_DATE";
    private final DateUtils dateUtils = new DateUtils();
    private ProfileUtils profileUtils;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private ImageView avatar;
    private TextView nameTextView;
    private TextView emailTextView, mobileNumberTextView;
    private TextView genderTextView;
    private ImageView headerCover;
    private Button updateButton;
    private boolean toggle = false;
    private Button changePasswordButton;
    private AwesomeValidation mAwesomeValidation;
    private Button datePickerButton;
    private String updateProfile = "Update Profile";
    private String editProfile = "Edit Profile";
    private String saveProfile = "Save Profile";
    private TextInputEditText locationEditText;

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
    public void onActivityResult(int requestCode, int result, Intent data) {
        if (result != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            session.getUser().setUserDateOfBirth(date);
            updateDate(date);

        }
    }

    public void updateDate(Date date) {
        datePickerButton.setText(dateUtils.getFormattedDate(date));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileUtils = new ProfileUtils(getActivity());
        mAwesomeValidation = new AwesomeValidation(TEXT_INPUT_LAYOUT);
        // setupValidation();
        initializeViews(view);
        updateViews();
        updateButton.setOnClickListener((View l) -> {
            /*if (toggle) {*/
                /*if (mAwesomeValidation.validate()) {
                    session.getUser().setUserMobileAndEmail(mobileNumberTextView.getText().toString(), emailTextView.getText().toString());
                   // toggleEditing(false);
                    Toasty.success(ProfileFragment.this.getContext(), "Your Details Have been Updated", Toast.LENGTH_LONG).show();
                }
                //TODO post this updates to server
            } else //toggleEditing(true);*/
            if (updateButton.getText().equals(updateProfile)) {
                updateButton.setText(editProfile);
                toggleEditing(true);
            } else if (updateButton.getText().equals(editProfile)) {
                updateButton.setText(saveProfile);
                toggleEditing(false);
            } else if (updateButton.getText().equals(saveProfile)) {
                session.getUser().setUserLocation(locationEditText.getText().toString());
            }

        });
        changePasswordButton.setOnClickListener(l -> {
            Intent verification = new Intent(ProfileFragment.this.getContext(), PasswordChangeActivity.class);
            verification.putExtra(SocialRegisterActivity.INTENT_PHONENUMBER, mobileNumberTextView.getText().toString());
        });

        datePickerButton.setOnClickListener((View l) -> {
            FragmentManager fragmentManager = getFragmentManager();
            Date date = session.getUser().getUserDateOfBirth();
            DatePickerFragment dialogFragment = DatePickerFragment.newInstance(date);
            dialogFragment.setTargetFragment(ProfileFragment.this, REQUEST_DATE);
            dialogFragment.show(fragmentManager, DIALOG_DATE);
        });

        toggleEditing(false);
        return view;
    }

    private void updateViews() {
        //TO be set later      profileUtils.setHeaderBackgroundImage(headerCover);
        profileUtils.setUserProfilePicture(avatar);
        profileUtils.setProfileName(nameTextView);
        genderTextView.setText(session.getUser().getUserGender());
        emailTextView.setText(session.getUser().getUserEmail());
        mobileNumberTextView.setText(session.getUser().getUserPhone());
        Date date = session.getUser().getUserDateOfBirth();
        Date date1 = new Date(1970, 1, 1);
        if (date != null && date.compareTo(date1) != 0)
            updateDate(date);

        if (session.getUser().getUserLocation() != null)
            locationEditText.setText(session.getUser().getUserLocation());

    }

    private void setupValidation() {
        //adding validation to edit-texts
        mAwesomeValidation.addValidation(this.getActivity(), R.id.name_text_view, "[a-zA-Z\\s]+", R.string.fnameerror);

        Pattern regexEmail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

        mAwesomeValidation.addValidation(this.getActivity(), R.id.email_text_view, regexEmail, R.string.emailerror);
        mAwesomeValidation.addValidation(this.getActivity(), R.id.mobile_text_view, "^[789]\\d{9}$", R.string.mobileerror);


    }


    public void initializeViews(View view) {
        headerCover = view.findViewById(R.id.header_cover);
        avatar = view.findViewById(R.id.avatar);
        genderTextView = view.findViewById(R.id.agender_text_view);
        nameTextView = view.findViewById(R.id.name_text_view);

        emailTextView = view.findViewById(R.id.email_text_view);
        mobileNumberTextView = view.findViewById(R.id.mobile_text_view);
        updateButton = view.findViewById(R.id.update_profile_button);
        changePasswordButton = view.findViewById(R.id.change_password);

        datePickerButton = view.findViewById(R.id.date_of_birth_button);

        locationEditText = view.findViewById(R.id.location_edit_text);

    }

    /**
     * Toggle if the profile is editable or not
     *
     * @param toggle value to be set for editable
     */
    public void toggleEditing(boolean toggle) {
        this.toggle = toggle;

       /* emailTextView.setEnabled(toggle);
        mobileNumberTextView.setEnabled(toggle);*/
        datePickerButton.setEnabled(toggle);
        locationEditText.setEnabled(toggle);
       /* if (toggle) {

            updateButton.setText(updateProfile);
        }
        else {
            editProfile = "Edit Profile";
            updateButton.setText(editProfile);
        }*/
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
