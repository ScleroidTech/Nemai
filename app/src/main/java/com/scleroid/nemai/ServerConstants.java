package com.scleroid.nemai;

/**
 * Created by Prince on 10/18/2016.
 */

public class ServerConstants {
    public static final String URL = "url";
    public class serverUrl {

        public static final String POST_COURIER = "http://ec2-18-220-38-112.us-east-2.compute.amazonaws.com:3002/insert";
        public static final String POST_PINCODE = "http://ec2-18-221-108-81.us-east-2.compute.amazonaws.com:3008/insertquery";



    }

    public class ServiceCode {
        public static final int POST_COURIER = 101;
        public static final int POST_PINCODE = 102;
    }


}
