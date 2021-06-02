package com.example.searchgame2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.example.searchgame2.Activity.MessageActivity;
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
import java.util.concurrent.TimeUnit;

public class MessageService extends Service {

    int id_message = 0;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        final DataBase dataBase = new DataBase(MessageService.this);
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
                do {
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
                        boolean isLogs = false;
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
                                int id = Integer.parseInt(st.nextToken());
                                contentValues.put(dataBase.ID, id);
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

                                if (!user_from.equals(finalname) && is_read.equals("0") && sendid.equals("0") && id != id_message) {
                                    isLogs = true;
                                    id_message = id;
                                }

                                database2.insert(dataBase.TABLE_MESSAGES, null, contentValues);
                            }
                        }
                        if (isLogs) {
                            showNote(MessageService.this,getString(R.string.new_message));
                            System.out.println("Notification is show");
                        }
                        reader.close();

                    } catch (Exception e) {
                    }
                    try {
                        TimeUnit.MINUTES.sleep(1);
                        System.out.println("sleep");
                    } catch (InterruptedException e) {
                        System.out.println("no sleep");
                    }
                }while (true);
            }
        }).start();
        return START_STICKY;
    }

    private void showNote(Context context, String text){
        Intent resultIntent = new Intent(context, MessageActivity.class).putExtra("USER_ID","0");
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context,"1")
                        .setSmallIcon(R.drawable.favicon)
                        .setContentTitle(context.getString(R.string.message))
                        .setContentText(text)
                        .setContentIntent(resultPendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_VIBRATE|Notification.DEFAULT_SOUND)
                        .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        createChannelNotify(notificationManager);

        notificationManager.notify(1, builder.build());
    }
    private static void createChannelNotify(NotificationManager manager){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel("1","1",NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void onDestroy() {
        //showNote(MessageService.this, "служба вимкнулася по непонятній причині!");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
