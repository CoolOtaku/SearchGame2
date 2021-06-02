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
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.example.searchgame2.Adapters.StrimersAdapter;
import com.example.searchgame2.CastomToast;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.Menu;
import com.example.searchgame2.MyWebViewClient;
import com.example.searchgame2.OBJStrimers;
import com.example.searchgame2.R;
import com.google.android.material.navigation.NavigationView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class StrimersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout refresh;
    RecyclerView listView;
    ProgressBar loading_strimers;
    WebView strimers_video;
    DrawerLayout drawer;
    Menu m = new Menu();

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strimers);
        refresh = (SwipeRefreshLayout)findViewById(R.id.refresh);
        refresh.setColorSchemeResources(R.color.colorRed);
        refresh.setProgressBackgroundColorSchemeResource(R.color.colorButton);
        refresh.setOnRefreshListener(this);
        listView = (RecyclerView)findViewById(R.id.list_strimers);
        listView.setLayoutManager(new LinearLayoutManager(this));
        loading_strimers = (ProgressBar)findViewById(R.id.loading_strimers);
        strimers_video = (WebView)findViewById(R.id.strimers_video);

        drawer = findViewById(R.id.drawer_layout);
        final NavigationView menu = (NavigationView) findViewById(R.id.nav_view);
        menu.setNavigationItemSelectedListener(this);

        strimers_video.setBackgroundColor(0x00000000);
        strimers_video.setWebViewClient(new MyWebViewClient());
        strimers_video.setWebChromeClient(new ChromeClient());
        WebSettings webSettings = strimers_video.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);

        new GetStrimers().execute();

        ImageView logo = (ImageView)findViewById(R.id.logo);
        logo.setOnClickListener(this);
    }
    private class ChromeClient extends WebChromeClient {

        public void onHideCustomView(){
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback){
            strimers_video.loadData("<body></body>", "text/html", "utf-8");
            strimers_video.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        drawer.openDrawer(GravityCompat.START);
    }

    private class GetStrimers extends AsyncTask<String,Void, ArrayList<OBJStrimers>> {

        ArrayList<OBJStrimers> arrayList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader;
        int c;
        int index = 0;

        @Override
        protected ArrayList<OBJStrimers> doInBackground(String... strings) {
            try {
                String link = DataBase.HOST+"Strimers.php";

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
                    if (index == 4) {
                        index = 0;
                        StringTokenizer st = new StringTokenizer(sb.toString(), "|");
                        sb.delete(0, sb.length());

                        arrayList.add(new OBJStrimers(st.nextToken(),st.nextToken(),Integer.parseInt(st.nextToken()),st.nextToken()));

                    }
                }
                reader.close();

                return this.arrayList;
            } catch (Exception ex) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(ArrayList<OBJStrimers> result) {
            if (arrayList.isEmpty()) {
                CastomToast toast = new CastomToast();
                toast.showToas(getApplicationContext(),getString(R.string.not_strimers),false);
            }else{
                listView.setAdapter(new StrimersAdapter(getApplicationContext(), result, strimers_video));
            }
            loading_strimers.setVisibility(View.GONE);
        }

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        m.MenuSelect(menuItem,drawer,getApplicationContext(), 0);
        return true;
    }

    @Override
    public void onRefresh() {
        new GetStrimers().execute();
        refresh.setRefreshing(false);
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else if (strimers_video.getVisibility() == View.VISIBLE) {
            strimers_video.loadData("<body></body>", "text/html", "utf-8");
            strimers_video.setVisibility(View.GONE);
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
        }
    }
}