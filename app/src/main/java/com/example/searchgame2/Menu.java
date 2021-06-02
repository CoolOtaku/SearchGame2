package com.example.searchgame2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.MenuItem;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.searchgame2.Activity.ContactActivity;
import com.example.searchgame2.Activity.LoginActivity;
import com.example.searchgame2.Activity.MainActivity;
import com.example.searchgame2.Activity.MessageActivity;
import com.example.searchgame2.Activity.ProfileActivity;
import com.example.searchgame2.Activity.AboutAppActivity;

public class Menu {

    ForNetWorck netWorck = new ForNetWorck();
    Intent in = new Intent();
    DataBase dataBase;

    public void MenuSelect(MenuItem menuItem, DrawerLayout drawer, Context context, int ActivityId){
        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        switch (menuItem.getItemId()) {
            case R.id.nav_main:
                if(ActivityId != 1){
                    in.setClass(context,MainActivity.class);
                    context.startActivity(in);
                }
                break;
            case R.id.nav_update:
                if (netWorck.isCon(context)) {
                    new GetData(context, true).execute();
                }
                break;
            case R.id.nav_profile:
                startLoginOrActyvity(context,ProfileActivity.class);
                break;
            case R.id.nav_messages:
                if(ActivityId != 2) {
                    startLoginOrActyvity(context,MessageActivity.class);
                }
                break;
            case R.id.nav_aboutsApp:
                if(ActivityId != 3) {
                    in.setClass(context, AboutAppActivity.class);
                    context.startActivity(in);
                }
                break;
            case R.id.nav_contact:
                if(ActivityId != 4) {
                    in.setClass(context, ContactActivity.class);
                    context.startActivity(in);
                }
                break;
            case R.id.nav_appUpdate:
                Uri url = Uri.parse("http://app.search-games.online/");
                Intent i = new Intent(Intent.ACTION_VIEW,url);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
            case R.id.str1:
                Strimers(context,0);
                break;
            case R.id.str2:
                Strimers(context,1);
                break;
            case R.id.str3:
                Strimers(context,2);
                break;
            case R.id.str4:
                Strimers(context,3);
                break;
            case R.id.str5:
                Strimers(context,4);
                break;
            case R.id.str6:
                Strimers(context,5);
                break;
            case R.id.str7:
                Strimers(context,6);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
    }
    private void Strimers(Context context, int position){
        String[] urlStrimers = context.getResources().getStringArray(R.array.UrlStrimers);
        Uri uri = Uri.parse(urlStrimers[position]);
        Intent In = new Intent(Intent.ACTION_VIEW,uri);
        In.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(In);
    }
    private void startLoginOrActyvity(Context context, Class clas){
        boolean isLogin;
        int id;
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dataBase = new DataBase(context);
        final SQLiteDatabase database2 = dataBase.getWritableDatabase();
        try {
            Cursor cursor = database2.rawQuery("SELECT " + dataBase.ID + ", " + dataBase.EMAIL + ", "
                    + dataBase.PASSWORD + " FROM " + dataBase.TABLE_PROFILE, null);
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex(dataBase.ID));
            if (id != 0) {
                isLogin = true;
            } else {
                isLogin = false;
            }
        } catch (Exception e) {
            isLogin = false;
        }
        if (isLogin) {
            intent.setClass(context,clas);
        } else {
            intent.setClass(context, LoginActivity.class);
        }
        intent.putExtra("USER_ID","0");
        context.startActivity(intent);
    }

}
