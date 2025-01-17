package com.example.jainishadabhi.mysocialnetwork.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonUtils {
    public static String EXTRA_USER_ADMIN_DATA="extra_user_admin_data";

    public static String EXTRA_IS_TO_ADD="extra_is_to_add";

    public static String EXTRA_EDIT_MODE="extra_edit_mode";

    public static boolean checkInternetConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}


