package com.example.searchgame2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.example.searchgame2.Adapters.AnonsAdapter;
import com.example.searchgame2.CastomToast;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.Menu;
import com.example.searchgame2.OBJAnons;
import com.example.searchgame2.R;
import com.google.android.material.navigation.NavigationView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class AnonsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout refresh;
    RecyclerView list;
    ProgressBar loading_anons;
    DrawerLayout drawer;
    Menu m = new Menu();
    ImageView img_fullscrean_news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anons);
        refresh = (SwipeRefreshLayout)findViewById(R.id.refresh);
        refresh.setColorSchemeResources(R.color.colorRed);
        refresh.setProgressBackgroundColorSchemeResource(R.color.colorButton);
        refresh.setOnRefreshListener(this);
        img_fullscrean_news = (ImageView)findViewById(R.id.img_fullscrean_news);
        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        loading_anons = (ProgressBar)findViewById(R.id.loading_anons);
        drawer = findViewById(R.id.drawer_layout);
        final NavigationView menu = (NavigationView) findViewById(R.id.nav_view);
        menu.setNavigationItemSelectedListener(this);

        new GetAnons().execute();

        ImageView logo = (ImageView)findViewById(R.id.logo);
        logo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        drawer.openDrawer(GravityCompat.START);
    }

    private class GetAnons extends AsyncTask<String,Void, ArrayList<OBJAnons>> {

        ArrayList<OBJAnons> arrayList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader;
        int c;
        int index = 0;

        @Override
        protected ArrayList<OBJAnons> doInBackground(String... strings) {
            try {
                String link = DataBase.HOST + "Anons.php";

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

                        arrayList.add(new OBJAnons(st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken()));

                    }
                }
                reader.close();

                return arrayList;
            }catch (Exception e){
                return null;
            }
        }
        @Override
        protected void onPostExecute(ArrayList<OBJAnons> result){
            if(arrayList.isEmpty()){
                CastomToast toast = new CastomToast();
                toast.showToas(getApplicationContext(),getString(R.string.error),false);
            }else{
                list.setAdapter(new AnonsAdapter(getApplicationContext(), result,img_fullscrean_news));
            }
            loading_anons.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        m.MenuSelect(menuItem,drawer,getApplicationContext(), 0);
        return true;
    }

    @Override
    public void onRefresh() {
        new GetAnons().execute();
        refresh.setRefreshing(false);
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else if(img_fullscrean_news.getVisibility() == View.VISIBLE){
            img_fullscrean_news.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
        }
    }
}
