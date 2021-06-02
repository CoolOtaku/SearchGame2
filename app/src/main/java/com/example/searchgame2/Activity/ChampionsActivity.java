package com.example.searchgame2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.example.searchgame2.Adapters.ChampionsAdapter;
import com.example.searchgame2.CastomToast;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.Menu;
import com.example.searchgame2.MyWebViewClient;
import com.example.searchgame2.OBJChampions;
import com.example.searchgame2.R;
import com.google.android.material.navigation.NavigationView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ChampionsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout refresh;
    RecyclerView listView;
    ProgressBar loading_champions;
    WebView champions_video;
    DrawerLayout drawer;
    Menu m = new Menu();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champions);
        refresh = (SwipeRefreshLayout)findViewById(R.id.refresh);
        refresh.setColorSchemeResources(R.color.colorRed);
        refresh.setProgressBackgroundColorSchemeResource(R.color.colorButton);
        refresh.setOnRefreshListener(this);
        listView = (RecyclerView) findViewById(R.id.list_champions);
        listView.setLayoutManager(new LinearLayoutManager(this));
        loading_champions = (ProgressBar)findViewById(R.id.loading_champions);
        champions_video = (WebView)findViewById(R.id.champions_video);
        drawer = findViewById(R.id.drawer_layout);
        final NavigationView menu = (NavigationView) findViewById(R.id.nav_view);
        menu.setNavigationItemSelectedListener(this);

        champions_video.setBackgroundColor(0x00000000);
        champions_video.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = champions_video.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);

        new GetChampions().execute();

        ImageView logo = (ImageView)findViewById(R.id.logo);
        logo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        drawer.openDrawer(GravityCompat.START);
    }

    private class GetChampions extends AsyncTask<String,Void, ArrayList<OBJChampions>> {

        ArrayList<OBJChampions> arrayList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader;
        int c;
        int index = 0;

        @Override
        protected ArrayList<OBJChampions> doInBackground(String... strings) {
            try {
                String link = DataBase.HOST+"Champions.php";

                URL url = new URL(link);
                URLConnection con = url.openConnection();

                reader = new BufferedReader(new
                        InputStreamReader(con.getInputStream()));

                while ((c = reader.read()) != -1) {
                    sb.append((char) c);
                    if (sb.length() > 1) {
                        if (sb.charAt(sb.length() - 1) == '|' && sb.charAt(sb.length() - 2) != '|') {
                            index++;
                        }
                    }
                    if (index == 6) {
                        index = 0;
                        StringTokenizer st = new StringTokenizer(sb.toString(), "|");
                        sb.delete(0, sb.length());

                        arrayList.add(new OBJChampions(st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken()));

                    }
                }
                reader.close();

                return this.arrayList;
            } catch (Exception ex) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(ArrayList<OBJChampions> result) {
            if (arrayList.isEmpty()) {
                CastomToast toast = new CastomToast();
                toast.showToas(getApplicationContext(),getString(R.string.error),false);
            }else{
            listView.setAdapter(new ChampionsAdapter(getApplicationContext(), result, champions_video));
            }
            loading_champions.setVisibility(View.GONE);
        }

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        m.MenuSelect(menuItem,drawer,getApplicationContext(), 0);
        return true;
    }

    @Override
    public void onRefresh() {
        new GetChampions().execute();
        refresh.setRefreshing(false);
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else if (champions_video.getVisibility() == View.VISIBLE) {
            champions_video.loadData("<body></body>", "text/html", "utf-8");
            champions_video.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
        }
    }
}
