package com.sripadmanaban.googleplusphotos;

/**
 * Created by Sripadmanaban on 2/2/2015.
 */
public interface Constants {

    public static final int STATE_DEFAULT = 0;
    public static final int STATE_SIGN_IN = 1;
    public static final int STATE_IN_PROGRESS = 2;

    public static final int RC_SIGN_IN = 0;

    public static final int DIALOG_PLAY_SERVICES_ERROR = 0;

    public static final int PLUS_ONE_REQUEST_CODE = 0;

    public static final int SWIPE_MIN_DISTANCE = 6; // 120;
    public static final int SWIPE_MAX_OFF_PATH = 125; // 250;
    public static final int SWIPE_THRESHOLD_VELOCITY = 100; // 200;

    public static final String SAVED_PROGRESS = "sign_in_progress";
    public static final String TAG = "android-plus";
    public static final String PREFERENCES = "My_Preferences";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String IMAGE_POSITION = "Position";

}
