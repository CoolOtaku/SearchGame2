package com.example.searchgame2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;
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

public class EditProfile extends AsyncTask<String, Void, String> {

    CastomToast toast = new CastomToast();
    Context context;
    String userId = "";
    String name = "";
    String email = "";
    String city = "";
    String newPassword = "";
    String banedIp = "";
    String image = "";
    String myself = "";
    String singature = "";
    String imgType = "";
    String dont_get_letter = "";
    String unsubscribe_from_materials = "";
    String delete_avatar = "";

    public EditProfile(Context context, String userId, String name, String email, String city, String newPassword, String banedIp,
                       String image, String myself, String singature, String imgType, String dont_get_letter, String unsubscribe_from_materials, String delete_avatar) {
        this.context = context;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.city = city;
        this.newPassword = newPassword;
        this.banedIp = banedIp;
        this.image = image;
        this.myself = myself;
        this.singature = singature;
        this.imgType = imgType;
        this.dont_get_letter = dont_get_letter;
        this.unsubscribe_from_materials = unsubscribe_from_materials;
        this.delete_avatar = delete_avatar;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String doInBackground(String... strings) {
        try{
            String link = DataBase.HOST+"editProfile.php";

            URL url = new URL(link);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            Map<String,String> arguments = new HashMap<>();
            arguments.put("userId", userId);
            arguments.put("name", name);
            arguments.put("email", email);
            arguments.put("city", city);
            arguments.put("newPassword", newPassword);
            arguments.put("banedIp", banedIp);
            arguments.put("image", image);
            arguments.put("myself", myself);
            arguments.put("singature", singature);
            arguments.put("imgType", imgType);
            arguments.put("dont_get_letter", dont_get_letter);
            arguments.put("unsubscribe_from_materials", unsubscribe_from_materials);
            arguments.put("delete_avatar", delete_avatar);
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
        if (result.equals("true")){
            toast.showToas(context,context.getString(R.string.saved),true);
        }else{
            toast.showToas(context,context.getString(R.string.error),false);
        }
    }
}
