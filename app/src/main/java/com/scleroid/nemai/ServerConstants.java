package com.scleroid.nemai;

/**
 * Created by Prince on 10/18/2016.
 */

public class ServerConstants {
    public static final String URL = "url";
    public class serverUrl {

        public static final String POST_COURIER = "http://ec2-18-220-38-112.us-east-2.compute.amazonaws.com:3002/insert";
        public static final String POST_PINCODE = "http://ec2-18-221-108-81.us-east-2.compute.amazonaws.com:3008/insertQuery";
        public static final String POST_PINCODE_LIST = "http://ec2-18-221-108-81.us-east-2.compute.amazonaws.com:3008/customer";

        public static final String POST_REGISTER = "http://ec2-18-221-108-81.us-east-2.compute.amazonaws.com:3008/insertuser";
        public static final String POST_LOGIN = "abcd  ";
        public static final String POST_VALID_USER = "abcd  ";


    }

    public class ServiceCode {
        public static final int POST_COURIER = 101;
        public static final int POST_PINCODE = 102;
        public static final int POST_PINCODE_LIST = 103;
        public static final int POST_REGISTER = 104;
        public static final int POST_LOGIN = 105;
        public static final int POST_VALID_USER = 106;
    }


}
