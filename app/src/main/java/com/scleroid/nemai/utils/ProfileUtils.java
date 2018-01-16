package com.scleroid.nemai.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scleroid.nemai.R;
import com.scleroid.nemai.activity.MainActivity;
import com.scleroid.nemai.other.CircleTransform;

public class ProfileUtils {
    private final Context context;

    public ProfileUtils(Context context) {
        this.context = context;
    }

    /**
     * Sets profile picture of user if exists
     * if doesn't, uses the default profile picture of the user
     */
    public void setUserProfilePicture(ImageView profilePicture) {
        if (MainActivity.session.getUser().isUserImageExists()) {
            String profileURl = MainActivity.session.getUser().getUserImageUrl();
            // Loading profile image
            Glide.with(context).load(profileURl)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profilePicture);

        } else {
            Drawable d = context.getResources().getDrawable(R.drawable.ic_person);
            profilePicture.setImageDrawable(d);
        }
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