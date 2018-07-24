package com.scleroid.nemai.utils;

import android.arch.persistence.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.scleroid.nemai.data.models.PinCode;

import java.lang.reflect.Type;

/**
 * Created by Ganesh on 19-12-2017.
 */

public class PinConverter {
    @TypeConverter
    public static PinCode fromString(String value) {
        Type listType = new TypeToken<PinCode>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayLisr(PinCode list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }

}
