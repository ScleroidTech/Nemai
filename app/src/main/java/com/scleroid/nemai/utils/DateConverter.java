package com.scleroid.nemai.utils;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Ganesh on 27-11-2017.
 */

public class DateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
