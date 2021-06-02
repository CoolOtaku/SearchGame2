package com.example.searchgame2;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.searchgame2.Adapters.CommentsAdapter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.StringTokenizer;

public class GetComments extends AsyncTask<String,Void, ArrayList<OBJComments>> {

    CastomToast toast = new CastomToast();
    Context context;
    ConstraintLayout constraintLayout;
    EditText editText;
    Button button;
    int id;
    ArrayList<OBJComments> arrayList;
    RecyclerView recyclerView;
    BufferedReader reader;
    StringBuilder sb = new StringBuilder();
    int c;
    int index;

    public GetComments(Context context, int id, ArrayList<OBJComments> arrayList, RecyclerView recyclerView,
                       ConstraintLayout constraintLayout, EditText editText, Button button){
        this.context = context;
        this.id = id;
        this.arrayList = arrayList;
        this.recyclerView = recyclerView;
        this.editText = editText;
        this.constraintLayout = constraintLayout;
        this.button = button;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected ArrayList<OBJComments> doInBackground(String... strings) {
        try{
            String link = DataBase.HOST+"getComments.php";

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

            reader = new BufferedReader(new InputStreamReader(http.getInputStream()));

            while ((c = reader.read()) != -1) {
                sb.append((char) c);
                if (sb.length() > 1) {
                    if (sb.charAt(sb.length() - 1) == '|' && sb.charAt(sb.length() - 2) != '|') {
                        index++;
                    }
                }
                if (index == 7) {
                    index = 0;
                    StringTokenizer st = new StringTokenizer(sb.toString(), "|");
                    sb.delete(0, sb.length());

                    int getId = Integer.parseInt(st.nextToken());
                    int getUserId = Integer.parseInt(st.nextToken());
                    String getDate = st.nextToken();
                    String getAutor = st.nextToken();
                    String getEmail = st.nextToken();
                    String getText = st.nextToken();
                    String getFoto = st.nextToken();

                    arrayList.add(new OBJComments(getId,getUserId,getDate,getAutor,getEmail,getText,getFoto));

                }
            }
            reader.close();
            http.disconnect();

            return arrayList;
        }catch (Exception e){
            return null;
        }
    }
    @Override
    protected void onPostExecute(ArrayList<OBJComments> result){
        if(arrayList.isEmpty()) {
            toast.showToas(context,context.getString(R.string.errorComments),false);
        }
        recyclerView.setAdapter(new CommentsAdapter(context, id, result, recyclerView, constraintLayout, editText, button));
    }
}
