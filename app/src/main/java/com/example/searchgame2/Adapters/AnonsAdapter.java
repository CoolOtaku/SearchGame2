package com.example.searchgame2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.OBJAnons;
import com.example.searchgame2.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class AnonsAdapter extends RecyclerView.Adapter<AnonsAdapter.ExampleViewHolder> {

    private Context context;
    private ArrayList<OBJAnons> arrayList;
    private ImageView img_fullscrean_news;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageNews;
        TextView NewsName;
        TextView NewsInfo;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            imageNews = itemView.findViewById(R.id.imageNews);
            NewsInfo = itemView.findViewById(R.id.NewsInfo);
            NewsName = itemView.findViewById(R.id.NewsName);
        }
    }

    public AnonsAdapter(Context context, ArrayList<OBJAnons> arrayList, ImageView img_fullscrean_news){
        this.context=context;
        this.arrayList=arrayList;
        this.img_fullscrean_news = img_fullscrean_news;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(final ExampleViewHolder holder, final int position) {
        final OBJAnons anons = arrayList.get(position);

        Picasso.with(context).load(DataBase.DIRECTORI+anons.getAnons_poster()).resize(200,300)
                .placeholder(R.drawable.gameload).error(R.drawable.bgerror).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imageNews);

        holder.NewsName.setText(anons.getAnons_title());
        holder.NewsInfo.setText("\uD83C\uDFAE "+anons.getAnons_platform()+"\n\n"+"\uD83D\uDCC5 "+anons.getAnons_reliz_date());

        holder.imageNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_fullscrean_news.setVisibility(View.VISIBLE);
                Picasso.with(context).load(DataBase.DIRECTORI+anons.getAnons_poster())
                        .placeholder(R.drawable.gameload).error(R.drawable.bgerror).networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(img_fullscrean_news);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

