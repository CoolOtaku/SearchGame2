package com.example.searchgame2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.searchgame2.OBJStrimers;
import com.example.searchgame2.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StrimersAdapter extends RecyclerView.Adapter<StrimersAdapter.ExampleViewHolder>{

    private WebView webView;
    private Context context;
    private ArrayList<OBJStrimers> arrayList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageStrimers;
        public ImageView language;
        public TextView CountViews;
        public TextView NameStrimers;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            imageStrimers = itemView.findViewById(R.id.imageStrimers);
            language = itemView.findViewById(R.id.language);
            CountViews = itemView.findViewById(R.id.CountViews);
            NameStrimers = itemView.findViewById(R.id.NameStrimers);
        }
    }

    public StrimersAdapter(Context context, ArrayList<OBJStrimers> arrayList, WebView webView) {
        this.context = context;
        this.arrayList = arrayList;
        this.webView = webView;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.strimers, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }
    @Override
    public void onBindViewHolder(final ExampleViewHolder holder, final int position) {
        final OBJStrimers objStrimers = arrayList.get(position);

        Picasso.with(context).load(objStrimers.getImage()).placeholder(R.drawable.strimersload).error(R.drawable.bgerror)
                .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().into(holder.imageStrimers);

        if(objStrimers.getLanguage().equals("ru")){
            holder.language.setImageResource(R.drawable.ru);
        }

        holder.CountViews.setText(String.valueOf(objStrimers.getCountViews()));
        holder.NameStrimers.setText(objStrimers.getName());

        holder.imageStrimers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl("https://player.twitch.tv/?channel="+objStrimers.getName()+"&parent=search-games.online&autoplay=false");
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
