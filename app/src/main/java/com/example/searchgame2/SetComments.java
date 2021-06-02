package com.example.searchgame2;

import android.content.Context;
import android.os.AsyncTask;
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

public class SetComments extends AsyncTask<String,Void, String> {

    CastomToast toast = new CastomToast();
    Context context;
    String postId;
    String userId;
    String autor;
    String email;
    String text;
    String isRegister;

    public SetComments(Context context, String postId, String userId, String autor, String email, String text, String isRegister){
        this.context = context;
        this.postId = postId;
        this.userId = userId;
        this.autor = autor;
        this.email = email;
        this.text = text;
        this.isRegister = isRegister;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String doInBackground(String... strings) {
        try{
            String link = DataBase.HOST+"setComments.php";

            URL url = new URL(link);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            Map<String,String> arguments = new HashMap<>();
            arguments.put("postId", postId);
            arguments.put("userId", userId);
            arguments.put("autor", autor);
            arguments.put("email", email);
            arguments.put("text", text);
            arguments.put("isRegister", isRegister);
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
        }catch (Exception e){
            return "false";
        }
    }
    @Override
    protected void onPostExecute(String result){
        if(result.equals("true")){
            toast.showToas(context,context.getString(R.string.sends),true);
        }else {
            toast.showToas(context,context.getString(R.string.error),false);
        }
    }
}
