package com.scleroid.nemai.utils;

import android.os.Bundle;

import com.scleroid.nemai.data.models.Address;

/**
 * The class used to provide events that need to be handled by
 * @see org.greenrobot.eventbus.EventBus
 * The inner classes are static & needs it's methods to be implemented with
 * subscribe annotations
 * @author Ganesh
 * @since 22-11-2017
 *
 */

public class Events {

    /**
     * This class is used to display the selected & unselected Couriers from
     * the list of
     *
     * @see com.scleroid.nemai.data.models.OrderedCourier
     */
    public static class selectionMap {
        /**
         * The position in the list
         */
        private int position;
        /**
         * the variable showing is selected or not,
         * true if selected, otherwise false
         */
        private boolean isSelected;

        public selectionMap(int position, boolean isSelected) {
            this.position = position;
            this.isSelected = isSelected;
        }

        public int getPosition() {
            return position;
        }

        public boolean isSelected() {
            return isSelected;
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

    /**
     *
     */
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
    public static class DateMessage {
        private Bundle message;

        public DateMessage(Bundle bundle) {
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
