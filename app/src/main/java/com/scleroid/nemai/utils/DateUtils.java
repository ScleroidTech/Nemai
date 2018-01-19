package com.scleroid.nemai.utils;

import android.text.format.DateFormat;

import java.util.Date;

public class DateUtils {
    public DateUtils() {
    }

    public CharSequence getFormattedDate(Date parcelDate) {
        return DateFormat.format("EEE, MMM dd, yyyy ", parcelDate);
    }
}