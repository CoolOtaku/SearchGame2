package com.example.searchgame2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class getUpdateApp extends AsyncTask<String,Void,Boolean> {

    Context context;
    public getUpdateApp(Context context){
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            URL urlUpdate = new URL(DataBase.HOST + "getUpdateApp.php");
            URLConnection conUpdate = urlUpdate.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conUpdate.getInputStream()));
            String VersionServer = reader.readLine();
            reader.close();

            String VersionApp = BuildConfig.VERSION_NAME;

            if(!VersionApp.equals(VersionServer)){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }
    @Override
    protected void onPostExecute(Boolean result) {
        if(result){
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle(context.getString(R.string.updatesApp));
            dialog.setMessage(context.getString(R.string.is_new_update_app));
            dialog.setIcon(R.drawable.favicon);
            dialog.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Uri url = Uri.parse("http://app.search-games.online/");
                    Intent i = new Intent(Intent.ACTION_VIEW,url);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
            dialog.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog newDialog = dialog.create();
            newDialog.show();
        }
    }

}
