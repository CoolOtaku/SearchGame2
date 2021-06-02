package com.example.searchgame2.Adapters;

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
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.searchgame2.Activity.Full_StoriActivity;
import com.example.searchgame2.CastomToast;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.R;
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

public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.ExampleViewHolder> {

    private Context context;
    private ArrayList<Integer> arrayList;
    private int userId;
    private CastomToast toast = new CastomToast();
    private DataBase dataBase;
    private SQLiteDatabase database1;
    private Cursor cursor;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout selectedContainer;
        public ImageView imageSelected;
        public TextView infoSelected;
        public ImageView b_delete_selected;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            selectedContainer = itemView.findViewById(R.id.selectedContainer);
            imageSelected = itemView.findViewById(R.id.imageSelected);
            infoSelected = itemView.findViewById(R.id.infoSelected);
            b_delete_selected = itemView.findViewById(R.id.b_delete_selected);
        }
    }

    public SelectedAdapter(Context context, ArrayList<Integer> arrayList, int userId){
        this.context = context;
        this.arrayList = arrayList;
        this.userId = userId;
        dataBase = new DataBase(context);
        database1 = dataBase.getWritableDatabase();
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }
    @Override
    public void onBindViewHolder(final ExampleViewHolder holder, final int position) {
        cursor = database1.rawQuery("SELECT " + dataBase.POSTER + ", " + dataBase.TITLE + ", " + dataBase.PRICE + " FROM "
                + dataBase.TABLE_GAMEALL + " WHERE " + dataBase.ID + " = " + arrayList.get(position), null);
        cursor.moveToFirst();
        Picasso.with(context).load(dataBase.DIRECTORI + cursor.getString(cursor.getColumnIndex(dataBase.POSTER)))
            .resize(200,300).placeholder(R.drawable.gameload).error(R.drawable.bgerror).into(holder.imageSelected);
        holder.infoSelected.setText(cursor.getString(cursor.getColumnIndex(dataBase.TITLE)) + "\n\n"
                + cursor.getString(cursor.getColumnIndex(dataBase.PRICE)) + " ₴");

        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.b_delete_selected:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                        dialog.setTitle(context.getString(R.string.delete));
                        dialog.setMessage(context.getString(R.string.delete_selected));
                        dialog.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int newPos = holder.getAdapterPosition();
                                new deleteSelected(userId, arrayList.get(newPos), holder.selectedContainer).execute();
                                if (!arrayList.isEmpty()) {
                                    arrayList.remove(newPos);
                                }
                                notifyItemRemoved(newPos);
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
                    case R.id.imageSelected:
                        Intent intent = new Intent(context, Full_StoriActivity.class).putExtra("KEY",arrayList.get(position));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                }
            }
        };
        holder.b_delete_selected.setOnClickListener(l);
        holder.imageSelected.setOnClickListener(l);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private class deleteSelected extends AsyncTask<String,Void, String> {

        int userId;
        int gameId;
        ConstraintLayout constraintLayout;

        public deleteSelected(int userId, int gameId, ConstraintLayout constraintLayout){
            this.userId = userId;
            this.gameId = gameId;
            this.constraintLayout = constraintLayout;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... strings) {
            try{
                String link = DataBase.HOST+"deleteSelected.php";

                URL url = new URL(link);
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection)con;
                http.setRequestMethod("POST");
                http.setDoOutput(true);

                Map<String,Integer> arguments = new HashMap<>();
                arguments.put("userId", userId);
                arguments.put("gameId",gameId);
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
}
