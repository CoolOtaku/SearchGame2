package com.example.searchgame2.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.example.searchgame2.Activity.ProfileActivity;
import com.example.searchgame2.CastomToast;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.GetMessages;
import com.example.searchgame2.R;
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
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ExampleViewHolder>{

    private Context context;
    private ArrayList<Integer> arrayList;
    private ScrollView containerMessage;
    private TextView name_profile;
    private CircleImageView img_profile;
    private TextView textMessage;
    private DataBase dataBase;
    private SQLiteDatabase database1;
    private Cursor cursor;
    CastomToast toast = new CastomToast();

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView statusMessage;
        TextView theme_message;
        TextView userName;
        TextView dateMessage;
        ImageView deleteMessage;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            statusMessage = itemView.findViewById(R.id.statusMessage);
            theme_message = itemView.findViewById(R.id.theme_message);
            userName = itemView.findViewById(R.id.userName);
            dateMessage = itemView.findViewById(R.id.dateMessage);
            deleteMessage = itemView.findViewById(R.id.deleteMessage);
        }
    }

    public MessageAdapter (Context context, ArrayList<Integer> arrayList, ScrollView containerMessage, TextView name_profile,
                           CircleImageView img_profile, TextView textMessage){
        this.context = context;
        this.arrayList = arrayList;
        this.containerMessage = containerMessage;
        this.name_profile = name_profile;
        this.img_profile = img_profile;
        this.textMessage = textMessage;
        this.dataBase = new DataBase(context);
        this.database1 = dataBase.getWritableDatabase();
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(final ExampleViewHolder holder, final int position) {
        cursor = database1.rawQuery("SELECT * FROM "+dataBase.TABLE_MESSAGES+" WHERE "+dataBase.ID+" = "+arrayList.get(position),null);
        cursor.moveToFirst();

        int isNew = cursor.getInt(cursor.getColumnIndex(dataBase.IS_READ));
        if(isNew == 0){
            holder.statusMessage.setImageResource(R.drawable.newicon);
        }else{
            holder.statusMessage.setImageResource(R.drawable.bgok);
        }
        holder.theme_message.setText(cursor.getString(cursor.getColumnIndex(dataBase.THEME)));
        holder.userName.setText(cursor.getString(cursor.getColumnIndex(dataBase.USER_FROM)));
        holder.dateMessage.setText(cursor.getString(cursor.getColumnIndex(dataBase.DATE)));

        View.OnClickListener l = new View.OnClickListener() {
            @SuppressLint("Recycle")
            @Override
            public void onClick(View v) {
                cursor = database1.rawQuery("SELECT * FROM "+dataBase.TABLE_MESSAGES+" WHERE "+dataBase.ID+" = "+arrayList.get(holder.getAdapterPosition()),null);
                cursor.moveToFirst();
                switch (v.getId()){
                    case R.id.theme_message:
                        containerMessage.setVisibility(View.VISIBLE);
                        name_profile.setText(holder.userName.getText()+"\n"+UserGrupe(cursor.getInt(cursor.getColumnIndex(dataBase.USER_GRUP))));
                        Picasso.with(context).load("https:" + cursor.getString(cursor.getColumnIndex(dataBase.PHOTO)))
                                .placeholder(R.drawable.load).error(R.drawable.profileicon).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                                .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(img_profile);
                        textMessage.setText(cursor.getString(cursor.getColumnIndex(dataBase.TEXT)));
                        if(cursor.getInt(cursor.getColumnIndex(dataBase.IS_READ)) != 1) {
                            database1.rawQuery("UPDATE " + dataBase.TABLE_MESSAGES + " SET " + dataBase.IS_READ + " = " + 1
                                    + " WHERE " + dataBase.ID + " = " + arrayList.get(position), null);
                        new Is_Read(cursor.getInt(cursor.getColumnIndex(dataBase.ID))).execute();
                        dataBase.RefreshMessages(database1);
                        new GetMessages().start(context);
                        }
                        break;
                    case R.id.deleteMessage:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle(context.getString(R.string.delete));
                        dialog.setMessage(context.getString(R.string.delete_message));
                        dialog.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int newPos = holder.getAdapterPosition();
                                if(!arrayList.isEmpty()) {
                                    arrayList.remove(newPos);
                                }
                                notifyItemRemoved(newPos);
                                new deleteMessage(cursor.getInt(cursor.getColumnIndex(dataBase.ID))).execute();
                                database1.delete(dataBase.TABLE_MESSAGES,dataBase.ID+" = "+cursor.getInt(cursor.getColumnIndex(dataBase.ID)),null);
                            }
                        });
                        dialog.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog newDialog = dialog.create();
                        newDialog.show();
                        break;
                    case R.id.userName:
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setClass(context, ProfileActivity.class);
                        intent.putExtra("USER_ID", holder.userName.getText().toString());
                        context.startActivity(intent);
                        break;
                }
            }
        };
        holder.theme_message.setOnClickListener(l);
        holder.deleteMessage.setOnClickListener(l);
        holder.userName.setOnClickListener(l);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public String UserGrupe(int i){
        String [] grup = context.getResources().getStringArray(R.array.arrGrupe);
        return grup[i-1];
    }

    private class Is_Read extends AsyncTask<String,Void, String> {
        int id;

        public Is_Read(int id){
            this.id = id;
        }
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... strings) {
            try {
                String link = DataBase.HOST + "Is_Read.php";

                URL url = new URL(link);
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection) con;
                http.setRequestMethod("POST");
                http.setDoOutput(true);

                Map<String, Integer> arguments = new HashMap<>();
                arguments.put("id", id);
                StringJoiner sj = new StringJoiner("&");
                for (Map.Entry<String, Integer> entry : arguments.entrySet())
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
                return reader.readLine();
            } catch (Exception e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result){
            System.out.println("Is_Read "+result);
        }
    }
    private class deleteMessage extends AsyncTask<String,Void, String> {
        int id;

        public deleteMessage(int id){
            this.id = id;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... strings) {
            try{
                String link = DataBase.HOST+"deleteMessage.php";

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

                BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
                return reader.readLine();
            }catch (Exception e){
                return "false";
            }
        }
        @Override
        protected void onPostExecute(String result){
            if(!result.equals("true")){
                toast.showToas(context,context.getString(R.string.error),false);
            }else{
                toast.showToas(context,context.getString(R.string.deletes),true);
            }
        }
    }
}