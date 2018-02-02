package com.scleroid.nemai.utils;

import android.content.Context;

public class EventBusUtils {
    public EventBusUtils() {
    }

    /**
     * Registers EventBus
     * To be called in onStart of the activity used to subscribe events
     */
    public void registerEventBus(Context context) {
        GlobalBus.getBus().register(context);
    }

    /**
     * De-Registers EventBus
     * To be called in onStop of the activity or fragment used to subscribe events
     */
    public void deRegisterEventBus(Context context) {
        GlobalBus.getBus().unregister(context);
    }
}