package com.scleroid.nemai.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Ganesh Kaple
 * @since 22-11-2017
 */

public class GlobalBus {
    private static EventBus sBus;

    public static EventBus getBus() {
        if (sBus == null)
            sBus = EventBus.getDefault();
        return sBus;
    }
}