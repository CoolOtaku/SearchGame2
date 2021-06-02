package com.example.searchgame2.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.searchgame2.Activity.ProfileActivity;
import com.example.searchgame2.CastomToast;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.ForNetWorck;
import com.example.searchgame2.GetComments;
import com.example.searchgame2.OBJComments;
import com.example.searchgame2.R;
import com.example.searchgame2.SetComments;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ExampleViewHolder> {
    private RecyclerView recyclerView;
    private int id;
    private int UserId;
    private Context context;
    private ArrayList<OBJComments> arrayList;
    private int apointent;
    private ConstraintLayout constraintLayout;
    private EditText editText;
    private Button button;
    private CastomToast toast = new CastomToast();
    int minutes = 0;
    int lm;
    private ForNetWorck netWorck = new ForNetWorck();
    private Calendar calendar = Calendar.getInstance();
    private DataBase dataBase;
    private SQLiteDatabase database2;
    private Cursor cursor;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView from;
        TextView Text;
        ImageView to_quote;
        ImageView report;
        ImageView delete;
        ImageView change;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_comments);
            from = itemView.findViewById(R.id.from_info);
            Text = itemView.findViewById(R.id.comments_text);
            to_quote = itemView.findViewById(R.id.to_quote);
            report = itemView.findViewById(R.id.report);
            delete = itemView.findViewById(R.id.deleteComments);
            change = itemView.findViewById(R.id.changeComments);
        }
    }

    public CommentsAdapter(Context context, int id, ArrayList<OBJComments> arrayList, RecyclerView recyclerView,
                           ConstraintLayout constraintLayout, EditText editText, Button button){
        this.context = context;
        this.id = id;
        this.arrayList = arrayList;
        this.recyclerView = recyclerView;
        this.constraintLayout = constraintLayout;
        this.editText = editText;
        this.button = button;
        dataBase = new DataBase(context);
        database2 = dataBase.getWritableDatabase();
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.coments, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }
    @Override
    public void onBindViewHolder(final ExampleViewHolder holder, final int position) {
        final OBJComments objComments = arrayList.get(position);
        Picasso.with(context).load("https:"+objComments.getFoto()).placeholder(R.drawable.profileicon)
                .error(R.drawable.profileicon).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imageView);
        holder.from.setText(Html.fromHtml("<p>"+context.getString(R.string.from)+" <span style=\"color:#DD163B;\">"+objComments.getAutor()+" </span>"+objComments.getDate()+"</p>"));
        holder.Text.setText(objComments.getText_Comments());
        try {
            cursor = database2.rawQuery("SELECT " + dataBase.ID + ", " + dataBase.USER_GRUP + " FROM " + dataBase.TABLE_PROFILE, null);
            cursor.moveToFirst();
            UserId = cursor.getInt(cursor.getColumnIndex(dataBase.ID));
            int grup = cursor.getInt(cursor.getColumnIndex(dataBase.USER_GRUP));
            if (UserId == objComments.getUser_id() || grup == 1 || grup == 6) {
                holder.delete.setVisibility(View.VISIBLE);
                holder.change.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            UserId = 0;
        }
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.deleteComments:
                        if (holder.delete.getVisibility() == View.VISIBLE) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                            dialog.setTitle(context.getString(R.string.delete));
                            dialog.setMessage(context.getString(R.string.delete_comments));
                            dialog.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new deleteComments(objComments.getId(), UserId, id).execute();
                                    if (!arrayList.isEmpty()){
                                        arrayList.clear();
                                    }
                                    new GetComments(context,id,arrayList,recyclerView, constraintLayout, editText, button).execute();
                                }
                            });
                            dialog.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog newDialog = dialog.create();
                            newDialog.show();
                        }
                        break;
                    case R.id.changeComments:
                        if(holder.change.getVisibility() == View.VISIBLE){
                            constraintLayout.setVisibility(View.VISIBLE);
                            apointent = 1;
                            editText.setText(objComments.getText_Comments());
                        }
                        break;
                    case R.id.to_quote:
                        lm = calendar.get(Calendar.MINUTE);
                        if(lm == minutes){
                            toast.showToas(context, context.getString(R.string.anti_spam), false);
                            break;
                        }
                        constraintLayout.setVisibility(View.VISIBLE);
                        apointent = 2;
                        break;
                    case R.id.report:
                        lm = calendar.get(Calendar.MINUTE);
                        if(lm == minutes){
                            toast.showToas(context, context.getString(R.string.anti_spam), false);
                            break;
                        }
                        constraintLayout.setVisibility(View.VISIBLE);
                        apointent = 3;
                        break;
                    case R.id.from_info:
                        if(netWorck.isCon(context)) {
                            Intent intent = new Intent();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setClass(context, ProfileActivity.class);
                            intent.putExtra("USER_ID", String.valueOf(objComments.getUser_id()));
                            context.startActivity(intent);
                        }
                        break;
                    case R.id.b_ok_rev:
                        switch (apointent) {
                            case 1:
                                new updateComments(objComments.getId(), editText.getText().toString()).execute();
                                if (!arrayList.isEmpty()){
                                    arrayList.clear();
                                }
                                new GetComments(context,id,arrayList,recyclerView, constraintLayout, editText, button).execute();
                                break;
                            case 2:
                                try {
                                    String text = "<div class=\"title_quote\">Цитата: "+objComments.getAutor()+"</div>\n<div class=\"quote\">"
                                        +objComments.getText_Comments()+"</div>\n"+editText.getText().toString();
                                    DataBase dataBase = new DataBase(context);
                                    SQLiteDatabase database2 = dataBase.getWritableDatabase();
                                    Cursor cursor = database2.rawQuery("SELECT " + dataBase.ID + ", " + dataBase.NAME + ", "
                                        + dataBase.EMAIL + " FROM " + dataBase.TABLE_PROFILE, null);
                                    cursor.moveToFirst();
                                    new SetComments(context, String.valueOf(id), cursor.getString(cursor.getColumnIndex(dataBase.ID)),
                                        cursor.getString(cursor.getColumnIndex(dataBase.NAME)),
                                        cursor.getString(cursor.getColumnIndex(dataBase.EMAIL)), text, "1").execute();
                                    cursor.close();
                                    database2.close();
                                    dataBase.close();
                                    if (!arrayList.isEmpty()) {
                                        arrayList.clear();
                                    }
                                    new GetComments(context,id,arrayList,recyclerView, constraintLayout, editText, button).execute();
                                }catch (Exception e){
                                    toast.showToas(context,context.getString(R.string.you_not_right),false);
                                }
                                break;
                            case 3:
                                new LogReport(objComments.getId(),editText.getText().toString()).execute();
                                if (!arrayList.isEmpty()){
                                    arrayList.clear();
                                }
                                new GetComments(context,id,arrayList,recyclerView, constraintLayout, editText, button).execute();
                                break;
                        }
                        editText.setText("");
                        constraintLayout.setVisibility(View.GONE);
                        break;
                }
            }
        };
        holder.delete.setOnClickListener(l);
        holder.change.setOnClickListener(l);
        holder.to_quote.setOnClickListener(l);
        holder.report.setOnClickListener(l);
        holder.from.setOnClickListener(l);
        button.setOnClickListener(l);
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    private class deleteComments extends AsyncTask<String,Void, String> {
        int idComments;
        int idUser;
        int postId;

        public deleteComments(int idComments, int idUser, int postId){
            this.idComments = idComments;
            this.idUser = idUser;
            this.postId = postId;
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... strings) {
            try{
                String link = DataBase.HOST+"deleteComments.php";

                URL url = new URL(link);
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection)con;
                http.setRequestMethod("POST");
                http.setDoOutput(true);

                Map<String,Integer> arguments = new HashMap<>();
                arguments.put("idComments", idComments);
                arguments.put("idUser",idUser);
                arguments.put("postId",postId);
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
                return reader.readLine();
            }catch (Exception e){
                return "false";
            }
        }
        @Override
        protected void onPostExecute(String result){
            if(result.equals("true")){
                toast.showToas(context,context.getString(R.string.deletes),true);
            }else {
                toast.showToas(context,context.getString(R.string.error),false);
            }
        }
    }
    private class updateComments extends AsyncTask<String,Void, String> {
        int idComments;
        String text;

        public updateComments(int idComments, String text){
            this.idComments = idComments;
            this.text = text;
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... strings) {
            try{
                String link = DataBase.HOST+"updateComments.php";

                URL url = new URL(link);
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection)con;
                http.setRequestMethod("POST");
                http.setDoOutput(true);

                Map<String,String> arguments = new HashMap<>();
                arguments.put("id", String.valueOf(idComments));
                arguments.put("text",text);
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
    private class LogReport extends AsyncTask<String,Void, String> {
        int idComments;
        String text;

        public LogReport(int idComments, String text){
            this.idComments = idComments;
            this.text = text;
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... strings) {
            try{
                String link = DataBase.HOST+"setReports.php";

                URL url = new URL(link);
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection)con;
                http.setRequestMethod("POST");
                http.setDoOutput(true);

                Map<String,String> arguments = new HashMap<>();
                arguments.put("id", String.valueOf(idComments));
                arguments.put("text",text);
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
}
