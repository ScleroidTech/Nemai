package com.scleroid.nemai.viewpager;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scleroid.nemai.R;

import java.util.List;

/**
 * Created by Scleroid on 10/27/2017.
 */

public class PackageInfoAdapter extends RecyclerView.Adapter<PackageInfoAdapter.ContactViewHolder> {

    private List<PackageInfo> contactList;

    public PackageInfoAdapter(List<PackageInfo> contactList) {
        this.contactList = contactList;
    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        PackageInfo ci = contactList.get(i);
        contactViewHolder.vPackage1.setText(ci.package1);
        //  contactViewHolder.vName.setText(ci.name);
        //   contactViewHolder.vSurname.setText(ci.surname);
        contactViewHolder.vEmail.setText(ci.email);
        contactViewHolder.vAddress.setText(ci.address);
        contactViewHolder.vPincode.setText(ci.pincode);
        contactViewHolder.vDistrict.setText(ci.district);
        contactViewHolder.vState.setText(ci.state);
        contactViewHolder.vTitle.setText(ci.name + "   ,   " + ci.surname);// contactViewHolder.vTitle.setText(ci.name + " " + ci.surname);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_package_card, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView vName;
        public TextView vSurname;
        public TextView vPackage1;
        public TextView vEmail;
        public TextView vTitle;
        public TextView vAddress;
        public TextView vPincode;
        public TextView vDistrict;
        public TextView vState;

        public ContactViewHolder(View v) {
            super(v);
            vPackage1 = v.findViewById(R.id.txtPackage1);
            //   vName =  (TextView) v.findViewById(R.id.txtName);
            //     vSurname = (TextView)  v.findViewById(R.id.txtSurname);
            vEmail = v.findViewById(R.id.txtEmail);
            vTitle = v.findViewById(R.id.title);
            vPincode = v.findViewById(R.id.txtPincode);
            vAddress = v.findViewById(R.id.txtAdd);
            vDistrict = v.findViewById(R.id.txtDistrict);
            vState = v.findViewById(R.id.txtState);
        }
    }
}