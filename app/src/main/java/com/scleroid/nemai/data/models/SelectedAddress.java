package com.scleroid.nemai.data.models;

/**
 * Stores Selected Address with the position at which they are selected,
 * Needed to use this because, the viewholder reuses itself, making it impossible for keeping things selected
 *
 * @author Ganesh Kaple
 * @since 28-12-2017
 */

public class SelectedAddress {
    /**
     * the position of the Adapter at which the value selected
     */
    int position;
    /**
     * The address which is selected
     */
    Address address;

}
