package com.example.searchgame2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.searchgame2.Activity.Full_StoriActivity;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.ForNetWorck;
import com.example.searchgame2.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ExampleViewHolder> {

    private Context context;
    private ForNetWorck netWorck = new ForNetWorck();
    private ArrayList<Integer> arrayList;
    private DataBase dataBase;
    private SQLiteDatabase database2;
    private Cursor cursor;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageGame;
        TextView View_Name;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            imageGame = itemView.findViewById(R.id.imageGame);
            View_Name = itemView.findViewById(R.id.View_Name);
        }
    }

    public SearchAdapter(Context context, ArrayList<Integer> arrayList){
        super();
        this.context = context;
        this.arrayList = arrayList;
        dataBase = new DataBase(context);
        database2 = dataBase.getWritableDatabase();
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(final ExampleViewHolder holder, final int position) {
        String [] s = {dataBase.TITLE,dataBase.POSTER};
        cursor = database2.query(dataBase.TABLE_GAMEALL,s,dataBase.ID+" = "+arrayList.get(position),null,null,null,null);
        cursor.moveToFirst();

        Picasso.with(context).load(DataBase.DIRECTORI+cursor.getString(cursor.getColumnIndex(dataBase.POSTER)))
                .resize(200,300)
                .placeholder(R.drawable.gameload)
                .error(R.drawable.bgerror)
                .into(holder.imageGame);

        StringBuilder viewName = new StringBuilder(cursor.getString(cursor.getColumnIndex(dataBase.TITLE)));
        if(viewName.length() > 19){
            viewName.delete(16,viewName.length());
            viewName.append("...");
        }
        holder.View_Name.setText(viewName.toString());

        holder.imageGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(netWorck.isCon(context)) {
                    Intent intent = new Intent(context, Full_StoriActivity.class);
                    intent.putExtra("KEY", arrayList.get(position));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
