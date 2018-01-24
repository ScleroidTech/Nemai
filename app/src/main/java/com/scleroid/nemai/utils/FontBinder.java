package com.scleroid.nemai.utils;


import android.content.res.AssetManager;
import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.widget.TextView;

public class FontBinder {

    @BindingAdapter("bind:font")
    public static void setTypeface(TextView textView, String fontName) {
        final AssetManager assets = textView.getContext().getAssets();
        textView.setTypeface(Typeface.createFromAsset(assets, "fonts/" + fontName));
    }

}
