package com.example.searchgame2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringJoiner;

public class GetDescription extends AsyncTask<String,Void,String> {

    Context context;
    TextView text;
    int id;

    public GetDescription(Context context, TextView text, int id){
        this.context=context;
        this.text=text;
        this.id=id;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String doInBackground(String... strings) {
        try {
            String link = DataBase.HOST+"Description.php";

            URL url = new URL(link);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            Map<String,Integer> arguments = new HashMap<>();
            arguments.put("id", id);
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

            Scanner reader = new Scanner(new InputStreamReader(http.getInputStream()));

            StringBuilder sb = new StringBuilder();

            while (reader.hasNext()) {
                sb.append(reader.nextLine());
                break;
            }
            reader.close();
            return sb.toString();
        } catch (Exception ex) {
            return context.getString(R.string.error);
        }

    }
    @Override
    protected void onPostExecute(String result){
        this.text.setText(result);
    }
}
