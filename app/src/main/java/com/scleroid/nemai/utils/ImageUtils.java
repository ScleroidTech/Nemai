package com.scleroid.nemai.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ImageUtils {

    private final Context context;

    public ImageUtils(Context context) {
        this.context = context;
    }

    /**
     * Sets profile picture of user if exists
     * if doesn't, uses the default profile picture of the user
     */
    public void loadImageIntoImageView(ImageView imageView, String imageUrl, int placeHolder) {
        /*if (MainActivity.session.getUser().isUserImageExists()) {*/

        Glide.with(context).load(imageUrl)
                .crossFade()
                .thumbnail(0.5f)
                .placeholder(placeHolder)
                .bitmapTransform(new CircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

       /* } else {
            Drawable d = context.getResources().getDrawable(R.drawable.ic_person);
            profilePicture.setImageDrawable(d);
        }*/
    }
}