package com.example.searchgame2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class GetUser extends AsyncTask<String, Void, String> {

    Context context;
    int c;
    String UserID;
    CircleImageView img;
    ImageView backgroundProfile;
    TextView name;
    TextView countPublishing;
    TextView countComments;
    TextView info;

    public GetUser(Context context, String UserID, CircleImageView img, ImageView backgroundProfile, TextView name, TextView countPublishing,
                   TextView countComments, TextView info){
        this.context = context;
        this.UserID = UserID;
        this.img = img;
        this.backgroundProfile = backgroundProfile;
        this.name = name;
        this.countPublishing = countPublishing;
        this.countComments = countComments;
        this.info = info;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected String doInBackground(String... strings) {
        try {
            String link = DataBase.HOST+"GetUser.php";

            URL url = new URL(link);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            Map<String,String> arguments = new HashMap<>();
            arguments.put("user_id", UserID);
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

            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            return sb.toString();
        } catch (IOException ex) {
            return null;
        }
    }
    @Override
    protected void onPostExecute(String result){
        if (!result.isEmpty()) {
            StringTokenizer st = new StringTokenizer(result,"|");
            name.setText(context.getString(R.string.user) + ": "+st.nextToken()+"\n "
            +context.getString(R.string.grup) + ": " + UserGrupe(Integer.parseInt(st.nextToken())));
            Picasso.with(context).load("https:" + st.nextToken())
                    .placeholder(R.drawable.load).error(R.drawable.profileicon).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(img);
            countPublishing.setText(st.nextToken()+ "\n" + context.getString(R.string.count_publishing));
            countComments.setText(st.nextToken()+ "\n" + context.getString(R.string.setsComents));
            String linc_to_background = st.nextToken();
            if(!linc_to_background.equals("0")){
                Picasso.with(context).load(linc_to_background).fit().into(backgroundProfile);
            }
            info.setText(context.getString(R.string.last_date)+": "+st.nextToken()+"\n\n"
                    +context.getString(R.string.red_date)+": "+st.nextToken()+"\n\n"
                    +context.getString(R.string.full_name)+": "+st.nextToken()+"\n\n"
                    +context.getString(R.string.myself)+": "+st.nextToken()+"\n");
        }else{
            CastomToast toast = new CastomToast();
            toast.showToas(context,context.getString(R.string.error),false);
        }
    }
    public String UserGrupe(int i){
        String [] grup = context.getResources().getStringArray(R.array.arrGrupe);
        return grup[i-1];
    }

}

