package ru.kvisaz.yandextranslate.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityChecker {
    private Context mContext;

    public ConnectivityChecker(Context context){
        mContext = context;
    }

    public boolean isOnline() {
        boolean isConnected = false;
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo==null) {
            return isConnected;
        }
        isConnected = networkInfo.isConnected();
        return isConnected;
    }
}
