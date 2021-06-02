package com.example.searchgame2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.example.searchgame2.Activity.ProfileActivity;
import java.io.BufferedReader;
import java.io.IOException;
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

public class Login extends AsyncTask<String,Void, Boolean> {

    Context context;
    ForNetWorck netWorck = new ForNetWorck();
    DataBase dataBase;
    SQLiteDatabase database2;
    Intent in = new Intent();
    String email;
    String password;
    int c;
    int index;
    boolean isLogin = false;
    boolean firstLogin;

    public Login(Context context, String email, String password, boolean firstLogin){
        this.context = context;
        this.email = email;
        this.password = password;
        this.firstLogin = firstLogin;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            String link = DataBase.HOST+"Profile.php";

            URL url = new URL(link);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            Map<String,String> arguments = new HashMap<>();
            arguments.put("email", email);
            arguments.put("password", password);
            StringJoiner sj = new StringJoiner("&");
            for(Map.Entry<String,String> entry : arguments.entrySet())
                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
            byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));

            StringBuilder sb = new StringBuilder();
            dataBase = new DataBase(context);
            database2 = dataBase.getWritableDatabase();

            while ((c = reader.read()) != -1) {
                sb.append((char) c);
                if (sb.length() > 1) {
                    if (sb.charAt(sb.length() - 1) == '|' && sb.charAt(sb.length() - 2) != '|') {
                        index++;
                    }
                }
                if (index == 14) {
                    index = 0;
                    StringTokenizer st = new StringTokenizer(sb.toString(), "|");
                    sb.delete(0, sb.length());

                    ContentValues contentValues = new ContentValues();
                    try {
                        contentValues.put(dataBase.ID, st.nextToken());
                        contentValues.put(dataBase.USER_GRUP, st.nextToken());
                        contentValues.put(dataBase.EMAIL, st.nextToken());
                        contentValues.put(dataBase.NAME, st.nextToken());
                        contentValues.put(dataBase.COUNT_COMENTS,st.nextToken());
                        contentValues.put(dataBase.PASSWORD,password);
                        contentValues.put(dataBase.PHOTO,st.nextToken());
                        contentValues.put(dataBase.REG_DATE,st.nextToken());
                        contentValues.put(dataBase.LAST_DATE,st.nextToken());
                        contentValues.put(dataBase.INFO,st.nextToken());
                        contentValues.put(dataBase.IP,st.nextToken());
                        contentValues.put(dataBase.FAVORITES,st.nextToken());
                        contentValues.put(dataBase.FULL_NAME,st.nextToken());
                        contentValues.put(dataBase.COUNT_PUBLISHING,st.nextToken());
                        contentValues.put(dataBase.LINC_TO_BACKGROUND,st.nextToken());
                        if(firstLogin) {
                            database2.insert(dataBase.TABLE_PROFILE, null, contentValues);
                        }else{
                            dataBase.DesLogin(database2,context);
                            database2.insert(dataBase.TABLE_PROFILE, null, contentValues);
                        }
                        isLogin = true;
                    }catch (Exception e){
                        if (netWorck.isCon(context) && !firstLogin){
                            dataBase.DesLogin(database2,context);
                        }
                        isLogin = false;
                    }
                }
            }
            return isLogin;
        } catch (IOException ex) {
            return false;
        }
    }
    @Override
    protected void onPostExecute(Boolean result){
        if (result) {
            if(firstLogin) {
                context.startService(new Intent(context,MessageService.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                in.setClass(context, ProfileActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                in.putExtra("USER_ID","0");
                context.startActivity(in);
            }
        }else{
            CastomToast toast = new CastomToast();
            toast.showToas(context,context.getString(R.string.error),false);
        }
    }

}
