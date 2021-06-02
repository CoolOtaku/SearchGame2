package com.example.searchgame2.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.MyWebViewClient;
import com.example.searchgame2.OBJChampions;
import com.example.searchgame2.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ChampionsAdapter extends RecyclerView.Adapter<ChampionsAdapter.ExampleViewHolder> {

    private WebView webView;
    private Context context;
    private ArrayList<OBJChampions> arrayList;

    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout constraintLayout;
        public ImageView img_1;
        public ImageView img_2;
        public WebView vieoView;
        public TextView textView1;
        public TextView textView2;
        public TextView textView3;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.champions_layout);
            img_1 = itemView.findViewById(R.id.img_team_1);
            img_2 = itemView.findViewById(R.id.img_team_2);
            vieoView = itemView.findViewById(R.id.view_champions);
            textView1 = itemView.findViewById(R.id.name_team_1);
            textView2 = itemView.findViewById(R.id.name_team_2);
            textView3 = itemView.findViewById(R.id.champions_game);
        }
    }

    public ChampionsAdapter(Context context, ArrayList<OBJChampions> arrayList, WebView webView) {
        this.context = context;
        this.arrayList = arrayList;
        this.webView = webView;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.champions, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindViewHolder(final ExampleViewHolder holder, final int position) {
        final OBJChampions objChampions = arrayList.get(position);

        Picasso.with(context).load(DataBase.DIRECTORI+objChampions.getTeam_logo_1()).placeholder(R.drawable.load).error(R.drawable.bgerror)
                .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.img_1);
        Picasso.with(context).load(DataBase.DIRECTORI+objChampions.getTeam_logo_2()).placeholder(R.drawable.load).error(R.drawable.bgerror)
                .networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.img_2);
        holder.vieoView.setBackgroundColor(0x00000000);
        WebSettings webSettings = holder.vieoView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        holder.vieoView.setWebChromeClient(new ChromeClient(holder.vieoView,objChampions));
        holder.vieoView.setWebViewClient(new MyWebViewClient());

        holder.vieoView.loadUrl(objChampions.getStream_link()+"&autoplay=false");
        holder.textView1.setText(objChampions.getName_team_1());
        holder.textView2.setText(objChampions.getName_team_2());
        holder.textView3.setText(objChampions.getGame_name());

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.vieoView.reload();
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(objChampions.getStream_link()+"&autoplay=true");
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private class ChromeClient extends WebChromeClient {

        WebView locVideo;
        OBJChampions locObj;

        ChromeClient(WebView locVideo, OBJChampions locObj){
            this.locVideo = locVideo;
            this.locObj = locObj;
        }

        public void onHideCustomView(){
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback){
            locVideo.reload();
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(locObj.getStream_link()+"&autoplay=true");
        }
    }

}
