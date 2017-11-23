package com.scleroid.nemai.utils;

import android.os.Bundle;

import com.scleroid.nemai.models.Address;

/**
 * Created by Ganesh on 22-11-2017.
 */

public class Events {
    // Event used to send message from fragment to activity.
    public static class FragmentActivityMessage {
        private String message;

        public FragmentActivityMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    // Event used to send message from activity to fragment.
    public static class ActivityFragmentMessage {
        private String message;

        public ActivityFragmentMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    // Event used to send message from activity to activity.
    public static class ActivityActivityMessage {
        private String message;

        public ActivityActivityMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    // Event used to send message from activity to activity.
    public static class AddressMessage {
        private Bundle message;

        public AddressMessage(Bundle bundle) {
            this.message = bundle;
        }

        public Bundle getMessage() {
            return message;
        }
    }

    // Event used to send message from activity to activity.
    public static class DataSetChanged {
        private Address message;

        public DataSetChanged(Address bundle) {
            this.message = bundle;
        }

        public Address getMessage() {
            return message;
        }
    }

    // Event used to send message from activity to activity.
    public static class populateData {
        private boolean message;

        public populateData(boolean bundle) {
            this.message = bundle;
        }

        public boolean getMessage() {
            return message;
        }
    }

}
