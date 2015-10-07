package com.softmobile.mialibrary.utility;

/**
 * Created by miahuang on 2015/10/6.
 */
public class CameraZoom {
    public static int getZoom(int meter) {

        if (meter<100) {
            return 18;
        }

        if (meter<200) {
            return 17;
        }

        if (meter<400) {
            return 16;
        }

        if (meter<800) {
            return 15;
        }

        if (meter<1600) {
            return 14;
        }

        if (meter<3200) {
            return 13;
        }

        if (meter<6400) {
            return 12;
        }

        if (meter<12800) {
            return 11;
        }

        if (meter<25600) {
            return 10;
        }

        if (meter<51200) {
            return 9;
        }
        return 8;
    }
}
