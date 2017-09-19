package info.androidhive.navigationdrawer.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.navigationdrawer.R;
import info.androidhive.navigationdrawer.activity.PartnerActivity;
import info.androidhive.navigationdrawer.models.PinCode;
import info.androidhive.navigationdrawer.network.PinAutoCompleteAdapter;
import info.androidhive.navigationdrawer.other.DelayedAutoCompleteTextView;


public class HomeFragment extends Fragment {
    public static final int THRESHOLD = 3;
    static String select;
    final CharSequence[] day_radio = {"Pune,MH,India", "Mumbai, MH,India", "Nagpur, MH, India"};
RadioButton radioParcel,radioDocument;
    RadioButton domestic, international;
    LinearLayout linearParcel,linearDocument;
    Button btn_save;
    TextView mWeightUnitTextView;
    ImageView img_address;
    DelayedAutoCompleteTextView pinSourceAutoCompleteTextView, pinDestinationAutoCompleteTextView;
    EditText editAddress,weightdocEditText;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        radioParcel = v.findViewById(R.id.rParcel);
        radioDocument = v.findViewById(R.id.rDocument);

        linearParcel=v.findViewById(R.id.linearExpandedData);
        linearDocument=v.findViewById(R.id.linearExpandedDataDoc);
        linearDocument.setVisibility(View.VISIBLE);

        //img_address =  v.findViewById(R.id.img_address);


        pinDestinationAutoCompleteTextView = v.findViewById(R.id.pin_dest_autocompletetextview);
        pinDestinationAutoCompleteTextView.setThreshold(THRESHOLD);
        pinDestinationAutoCompleteTextView.setAdapter(new PinAutoCompleteAdapter(getActivity()));
        pinDestinationAutoCompleteTextView.setLoadingIndicator(
                (android.widget.ProgressBar) v.findViewById(R.id.pb_loading_indicator2));
        pinDestinationAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PinCode pinCode = (PinCode) adapterView.getItemAtPosition(position);
                pinDestinationAutoCompleteTextView.setText(pinCode.getPincode());
            }
        });


        pinSourceAutoCompleteTextView = v.findViewById(R.id.pin_source_autocompletetextview);
        pinSourceAutoCompleteTextView.setThreshold(THRESHOLD);
        pinSourceAutoCompleteTextView.setAdapter(new PinAutoCompleteAdapter(getActivity())); // 'this' is Activity instance
        pinSourceAutoCompleteTextView.setLoadingIndicator(
                (android.widget.ProgressBar) v.findViewById(R.id.pb_loading_indicator));
        pinSourceAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                PinCode pinCode = (PinCode) adapterView.getItemAtPosition(position);
                pinSourceAutoCompleteTextView.setText(pinCode.getPincode());
            }
        });


        mWeightUnitTextView= v.findViewById(R.id.weight_unit_kg_textView);
        weightdocEditText = v.findViewById(R.id.editWeightDoc);
        weightdocEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) mWeightUnitTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                else mWeightUnitTextView.setTextColor(getResources().getColor(R.color.colorHint));
            }
        });
        //    editAddress = v.findViewById(R.id.editAddressDoc);

/*img_address.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity())
                .setTitle("Select an Address")
                .setSingleChoiceItems(day_radio, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getActivity(), "The selected Day is " + day_radio[which], Toast.LENGTH_LONG).show();
                        select= day_radio[which].toString();
                        dialog.dismiss();
                        editAddress.setText(select);
                    }
                });
        AlertDialog alertdialog2 = builder2.create();
        alertdialog2.show();

    }
});*/
        btn_save= v.findViewById(R.id.btn_saveDoc);

        btn_save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PartnerActivity.class);
                startActivity(i);
            }
        });
        radioDocument.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    linearDocument.setVisibility(View.VISIBLE);
                    linearParcel.setVisibility(View.GONE);
                    radioDocument.setTypeface(null, Typeface.BOLD);
                    radioParcel.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        radioParcel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                   linearParcel.setVisibility(View.VISIBLE);
                    linearDocument.setVisibility(View.GONE);
                    radioParcel.setTypeface(null, Typeface.BOLD);
                    radioDocument.setTypeface(null, Typeface.NORMAL);
                }
            }
        });


        domestic = (RadioButton) v.findViewById(R.id.rDomestic);
        international = (RadioButton) v.findViewById(R.id.rInternational);

        international.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    international.setTypeface(null, Typeface.BOLD);
                    domestic.setTypeface(null, Typeface.NORMAL);
                }
            }
        });

        domestic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    domestic.setTypeface(null, Typeface.BOLD);
                    international.setTypeface(null, Typeface.NORMAL);
                }
            }
        });
        return v;
    }

    private void showRadioButtonDialog() {

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radio_buttondiaglog);
        List<String> stringList=new ArrayList<>();  // here is list
        for(int i=0;i<5;i++) {
            stringList.add("RadioButton " + (i + 1));
        }
        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for(int i=0;i<stringList.size();i++){
            RadioButton rb=new RadioButton(getActivity()); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rg.addView(rb);
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        Toast.makeText(getActivity(),btn.getText(),Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

        dialog.show();

    }
}


