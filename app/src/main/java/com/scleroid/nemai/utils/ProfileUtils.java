package com.scleroid.nemai.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.scleroid.nemai.R;
import com.scleroid.nemai.activity.MainActivity;

public class ProfileUtils {
    private final Context context;
    private final ImageUtils imageUtils;

    public ProfileUtils(Context context) {
        this.context = context;
        this.imageUtils = new ImageUtils(context);
    }

    /**
     * Sets profile picture of user if exists
     * if doesn't, uses the default profile picture of the user
     */
    public void setUserProfilePicture(ImageView profilePicture) {
        /*if (MainActivity.session.getUser().isUserImageExists()) {*/
        // Loading profile image

       /* } else {
            Drawable d = context.getResources().getDrawable(R.drawable.ic_person);
            profilePicture.setImageDrawable(d);
        }*/
        String profileURl = MainActivity.session.getUser().getUserImageUrl();
        // Loading profile image
        int ic_person = R.drawable.ic_person;
        imageUtils.loadImageIntoImageView(profilePicture, profileURl, ic_person);
    }

    /**
     * sets the default image as navigation header
     * TODO add cover image from facebook or google as header background
     */
    public void setHeaderBackgroundImage(ImageView headerBackgroundImage) {
        Drawable d = context.getResources().getDrawable(R.drawable.nav_drawer_header);
        headerBackgroundImage.setImageDrawable(d);
    }

    /**
     * Sets first name & last name to the profile
     *
     * @param txtName name of the Textview to be updated
     */
    public void setProfileName(TextView txtName) {
        txtName.setText(String.format("%s %s", MainActivity.session.getUser().getUserFirstName(), MainActivity.session.getUser().getUserLastName()));
    }
}