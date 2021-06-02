package com.example.searchgame2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import com.example.searchgame2.Activity.LoadingActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

public class GetData extends AsyncTask<String,Void,Void> {

    private Context context;
    private DataBase dataBase;
    private Intent intent = new Intent();
    private Boolean permis;
    StringBuilder sb = new StringBuilder();
    BufferedReader reader;
    int c;
    int index = 0;
    int OldVersion = 0;
    int NewVersion = 0;

    public GetData(Context context, Boolean permis){
        this.context=context;
        this.permis=permis;
    }

    @Override
    protected Void doInBackground(String... arg0){
        try {
            dataBase = new DataBase(context);
            final SQLiteDatabase database2 = dataBase.getWritableDatabase();

            if(!permis) {
                URL urlUpdate = new URL(DataBase.HOST + "Update.php");
                URLConnection conUpdate = urlUpdate.openConnection();

                reader = new BufferedReader(new InputStreamReader(conUpdate.getInputStream()));
                NewVersion = Integer.parseInt(reader.readLine());
                try {
                    Cursor cursor = database2.rawQuery("SELECT " + dataBase.ID + " FROM " + dataBase.TABLE_GAMEALL + " WHERE "
                            + dataBase.ID + " = (SELECT " + "MAX(" + dataBase.ID + ") FROM " + dataBase.TABLE_GAMEALL + " WHERE "
                            + dataBase.FOR_PLATFORM + " LIKE '%PC%' OR '%PS4%' OR '%XBOX%')", null);
                    cursor.moveToFirst();

                    OldVersion = cursor.getInt(cursor.getColumnIndex(dataBase.ID));
                    cursor.close();
                }catch (Exception e){
                    OldVersion = 1;
                }
                reader.close();
            }

            if (NewVersion != OldVersion || permis) {
                intent.setClass(context, LoadingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                URL url = new URL(DataBase.HOST+"Game.php");
                URLConnection con = url.openConnection();

                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                dataBase.onUpgrade(database2, OldVersion, NewVersion);

                while ((c = reader.read()) != -1) {
                    sb.append((char) c);
                    if (sb.length() > 1) {
                        if (sb.charAt(sb.length() - 1) == '|' && sb.charAt(sb.length() - 2) != '|') {
                            index++;
                        }
                    }
                    if (index == 22) {
                        index = 0;
                        StringTokenizer st = new StringTokenizer(sb.toString(), "|");
                        sb.delete(0, sb.length());

                        ContentValues contentValues = new ContentValues();

                        contentValues.put(dataBase.ID, st.nextToken());
                        contentValues.put(dataBase.TITLE, st.nextToken());
                        contentValues.put(dataBase.POSTER, st.nextToken());
                        contentValues.put(dataBase.SCREEN1, st.nextToken());
                        contentValues.put(dataBase.SCREEN2, st.nextToken());
                        contentValues.put(dataBase.SCREEN3, st.nextToken());
                        contentValues.put(dataBase.JANRE, st.nextToken());
                        contentValues.put(dataBase.YEAR, st.nextToken());
                        contentValues.put(dataBase.CORE, st.nextToken());
                        contentValues.put(dataBase.PROCESSOR, st.nextToken());
                        contentValues.put(dataBase.COREFREQUENCY, st.nextToken());
                        contentValues.put(dataBase.RAM, st.nextToken());
                        contentValues.put(dataBase.VIDEO_RAM, st.nextToken());
                        contentValues.put(dataBase.DEVELOPER, st.nextToken());
                        contentValues.put(dataBase.EDITION, st.nextToken());
                        contentValues.put(dataBase.VIDEO_CARD, st.nextToken());
                        contentValues.put(dataBase.FOR_PLATFORM, st.nextToken());
                        contentValues.put(dataBase.HHD_RAM, st.nextToken());
                        contentValues.put(dataBase.PRICE, st.nextToken());
                        contentValues.put(dataBase.LINK_ON_STEAM, st.nextToken());
                        contentValues.put(dataBase.LINK_ON_TEASET, st.nextToken());
                        contentValues.put(dataBase.LINK_ON_GAMEPLAY, st.nextToken());

                        database2.insert(dataBase.TABLE_GAMEALL, null, contentValues);
                    }
                }
                reader.close();

                sb.delete(0, sb.length());

            }
        }catch (IOException e){
            return null;
        }
        return null;
    }

}