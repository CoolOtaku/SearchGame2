package com.example.searchgame2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.TextView;
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

public class SetRating extends AsyncTask<String,Void,String> {

    Context context;
    CastomToast toast = new CastomToast();
    int postId;
    String userName;
    String rating;
    TextView aveR;
    TextView voteR;
    TextView sr1;
    TextView sr2;
    TextView sr3;
    TextView sr4;
    TextView sr5;
    TextView sr6;

    public SetRating(Context context, int postId, String rating, TextView aveR, TextView voteR, TextView sr1, TextView sr2, TextView sr3,
                     TextView sr4, TextView sr5, TextView sr6) {
        this.context = context;
        this.postId = postId;
        this.rating = rating;
        this.aveR = aveR;
        this.voteR = voteR;
        this.sr1 = sr1;
        this.sr2 = sr2;
        this.sr3 = sr3;
        this.sr4 = sr4;
        this.sr5 = sr5;
        this.sr6 = sr6;
        try {
            DataBase dataBase = new DataBase(context);
            SQLiteDatabase database2 = dataBase.getWritableDatabase();
            Cursor cursor = database2.rawQuery("SELECT " + dataBase.NAME + " FROM " + dataBase.TABLE_PROFILE, null);
            cursor.moveToFirst();
            this.userName = cursor.getString(cursor.getColumnIndex(dataBase.NAME));
        }catch (Exception e){
            this.userName = "";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String doInBackground(String... strings) {
        try {
            String link = DataBase.HOST+"SetRating.php";

            URL url = new URL(link);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            Map<String,String> arguments = new HashMap<>();
            arguments.put("postId", String.valueOf(postId));
            arguments.put("rating", rating);
            arguments.put("userName", userName);
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
            return reader.readLine();

        } catch (Exception ex) {
            return "false";
        }
    }
    @Override
    protected void onPostExecute(String result){
        if(result.equals("true")) {
            new GetRating(postId, aveR, voteR, sr1, sr2, sr3, sr4, sr5, sr6).execute();
        }else{
            toast.showToas(context,context.getString(R.string.golos),false);
        }
    }
}
