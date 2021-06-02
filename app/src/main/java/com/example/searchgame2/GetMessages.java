package com.example.searchgame2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.StringTokenizer;

public class GetMessages {

    public void start(final Context context) {
        final DataBase dataBase = new DataBase(context);
        final SQLiteDatabase database2 = dataBase.getWritableDatabase();
        Cursor cursor;
        int id;
        String name = "";
        int is_empty;
        try {
            cursor = database2.rawQuery("SELECT " + dataBase.ID + ", " + dataBase.NAME + " FROM " + dataBase.TABLE_PROFILE, null);
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex(dataBase.ID));
            name = cursor.getString(cursor.getColumnIndex(dataBase.NAME));
        } catch (Exception e) {
            id = 0;
        }
        try {
            cursor = database2.rawQuery("SELECT " + dataBase.ID + " FROM " + dataBase.TABLE_MESSAGES, null);
            cursor.moveToFirst();
            is_empty = 0;
        } catch (Exception e) {
            is_empty = 1;
        }

        final int finalId = id;
        final int finalIs_empty = is_empty;
        final String finalname = name;
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                    try {
                        String link = DataBase.HOST + "GetMessages.php";

                        URL url = new URL(link);
                        URLConnection con = url.openConnection();
                        HttpURLConnection http = (HttpURLConnection) con;
                        http.setRequestMethod("POST");
                        http.setDoOutput(true);

                        Map<String, String> arguments = new HashMap<>();
                        arguments.put("id", String.valueOf(finalId));
                        arguments.put("name", finalname);
                        arguments.put("is_empty", String.valueOf(finalIs_empty));
                        StringJoiner sj = new StringJoiner("&");
                        for (Map.Entry<String, String> entry : arguments.entrySet())
                            sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                                    + URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
                        byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
                        int length = out.length;

                        http.setFixedLengthStreamingMode(length);
                        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                        http.connect();
                        try (OutputStream os = http.getOutputStream()) {
                            os.write(out);
                        }

                        BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        int index = 0;
                        int c;
                        boolean refresh = false;
                        while ((c = reader.read()) != -1) {
                            sb.append((char) c);
                            if (sb.length() > 1) {
                                if (sb.charAt(sb.length() - 1) == '|' && sb.charAt(sb.length() - 2) != '|') {
                                    index++;
                                }
                            }
                            if (index == 9) {
                                if (!refresh) {
                                    dataBase.RefreshMessages(database2);
                                    refresh = true;
                                }
                                index = 0;
                                StringTokenizer st = new StringTokenizer(sb.toString(), "|");
                                sb.delete(0, sb.length());

                                ContentValues contentValues = new ContentValues();
                                contentValues.put(dataBase.ID, st.nextToken());
                                contentValues.put(dataBase.THEME, st.nextToken());
                                contentValues.put(dataBase.TEXT, st.nextToken());
                                String user_from = st.nextToken();
                                contentValues.put(dataBase.USER_FROM, user_from);
                                contentValues.put(dataBase.DATE, st.nextToken());
                                String sendid = st.nextToken();
                                contentValues.put(dataBase.SENDID, sendid);
                                String is_read = st.nextToken();
                                contentValues.put(dataBase.IS_READ, is_read);
                                contentValues.put(dataBase.USER_GRUP, st.nextToken());
                                contentValues.put(dataBase.PHOTO, st.nextToken());

                                database2.insert(dataBase.TABLE_MESSAGES, null, contentValues);
                            }
                        }
                        reader.close();

                    } catch (Exception e) {

                    }
            }
        }).start();
    }
}
