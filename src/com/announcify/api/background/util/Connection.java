package com.announcify.api.background.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;

public class Connection {

    public static boolean isConnected(final Context context) {
        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == PackageManager.PERMISSION_DENIED || context.checkCallingOrSelfPermission("android.permission.INTERNET") == PackageManager.PERMISSION_DENIED) {
            return true;
        }

        final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return manager.getActiveNetworkInfo().isConnected();
        }
    }

}
