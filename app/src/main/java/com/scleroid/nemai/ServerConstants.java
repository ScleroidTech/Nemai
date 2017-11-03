package com.scleroid.nemai;

/**
 * Created by Prince on 10/18/2016.
 */

public class ServerConstants {

    public static final String URL = "url";
    public class serverUrl {
        public static final String BASE_URL = "http://ec2-34-238-40-184.compute-1.amazonaws.com:8089";
        public static final String POST_COURIER = BASE_URL + "/insert";
        public static final String POST_PINCODE = BASE_URL + "/insertQuery";
        public static final String POST_PINCODE_LIST = BASE_URL + "/customer";

        public static final String POST_REGISTER = BASE_URL + "/register";
        public static final String POST_SOCIAL_REGISTER = BASE_URL + "/register";
        public static final String POST_LOGIN = BASE_URL + "/login";
        public static final String POST_VALID_USER = BASE_URL + "/alreadyexisteduser";
        public static final String POST_RESET_PASS = "abcd  ";


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
