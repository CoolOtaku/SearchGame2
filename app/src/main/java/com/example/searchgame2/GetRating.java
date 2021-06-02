package com.example.searchgame2;

import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
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
import java.util.StringTokenizer;

public class GetRating extends AsyncTask<String,Void,String> {

    int postId;
    TextView aveR;
    TextView voteR;
    TextView sr1;
    TextView sr2;
    TextView sr3;
    TextView sr4;
    TextView sr5;
    TextView sr6;

    public GetRating(int postId, TextView aveR, TextView voteR, TextView sr1, TextView sr2, TextView sr3, TextView sr4, TextView sr5, TextView sr6) {
        this.postId = postId;
        this.aveR = aveR;
        this.voteR = voteR;
        this.sr1 = sr1;
        this.sr2 = sr2;
        this.sr3 = sr3;
        this.sr4 = sr4;
        this.sr5 = sr5;
        this.sr6 = sr6;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String doInBackground(String... strings) {
        try {
            String link = DataBase.HOST+"GetRating.php";

            URL url = new URL(link);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            Map<String,Integer> arguments = new HashMap<>();
            arguments.put("postId", postId);
            StringJoiner sj = new StringJoiner("&");
            for(Map.Entry<String,Integer> entry : arguments.entrySet())
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

            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            reader.close();

            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }
    @Override
    protected void onPostExecute(String result){
        if(!result.isEmpty()) {
            StringTokenizer st = new StringTokenizer(result, "|");

            aveR.setText(st.nextToken());
            voteR.setText("Оцінок: "+st.nextToken());
            sr1.setText(st.nextToken());
            sr2.setText(st.nextToken());
            sr3.setText(st.nextToken());
            sr4.setText(st.nextToken());
            sr5.setText(st.nextToken());
            sr6.setText(st.nextToken());
        }
    }
}
