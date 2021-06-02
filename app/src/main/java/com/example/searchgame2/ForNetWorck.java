package com.example.searchgame2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.example.searchgame2.Activity.FilledTheConnectionActivity;

public class ForNetWorck {
    public boolean isCon(Context context) {
        boolean isCon;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            isCon = netInfo.isConnected();
        }catch (Exception e){
            isCon = false;
        }
        if (!isCon){
            Intent i = new Intent();
            i.setClass(context, FilledTheConnectionActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
        return isCon;
    }
}
