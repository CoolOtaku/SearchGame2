package com.example.searchgame2.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.searchgame2.CastomToast;
import com.example.searchgame2.ForNetWorck;
import com.example.searchgame2.GetData;
import com.example.searchgame2.Menu;
import com.example.searchgame2.MessageService;
import com.example.searchgame2.R;
import com.example.searchgame2.getUpdateApp;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Menu m = new Menu();
    DrawerLayout drawer;
    Intent in = new Intent();
    ForNetWorck netWorck = new ForNetWorck();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);
        final NavigationView menu = (NavigationView) findViewById(R.id.nav_view);
        TableRow Intem = (TableRow)findViewById(R.id.Intem);
        TableRow Intem_1 = (TableRow)findViewById(R.id.Intem_1);
        TableRow Intem_2 = (TableRow)findViewById(R.id.Intem_2);
        TableRow Intem_3 = (TableRow)findViewById(R.id.Intem_3);
        TableRow Intem_4 = (TableRow)findViewById(R.id.Intem_4);
        TableRow Intem_5 = (TableRow)findViewById(R.id.Intem_5);
        ImageView logo = (ImageView)findViewById(R.id.logo);

        new GetData(getApplicationContext(),false).execute();

        new getUpdateApp(MainActivity.this).execute();

        Intent intent = new Intent(getApplicationContext(), MessageService.class);
        startService(intent);

        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.Intem:
                        in.putExtra("CATEGORY","PC");
                        in.setClass(getApplicationContext(), SearchActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
                        break;
                    case R.id.Intem_1:
                        in.putExtra("CATEGORY","PS4");
                        in.setClass(getApplicationContext(),SearchActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
                        break;
                    case R.id.Intem_2:
                        in.putExtra("CATEGORY","XBOX");
                        in.setClass(getApplicationContext(),SearchActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
                        break;
                    case R.id.Intem_3:
                        if (netWorck.isCon(getApplicationContext())) {
                            in.setClass(getApplicationContext(), AnonsActivity.class);
                            startActivity(in);
                            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
                        }
                        break;
                    case R.id.Intem_4:
                        if (netWorck.isCon(getApplicationContext())) {
                            in.setClass(getApplicationContext(), ChampionsActivity.class);
                            startActivity(in);
                            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
                        }
                        break;
                    case R.id.Intem_5:
                        if (netWorck.isCon(getApplicationContext())) {
                            in.setClass(getApplicationContext(), StrimersActivity.class);
                            startActivity(in);
                            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
                        }
                        break;
                    case R.id.logo:
                        drawer.openDrawer(GravityCompat.START);
                        break;
                }
            }
        };
        Intem.setOnClickListener(l);
        Intem_1.setOnClickListener(l);
        Intem_2.setOnClickListener(l);
        Intem_3.setOnClickListener(l);
        Intem_4.setOnClickListener(l);
        Intem_5.setOnClickListener(l);
        logo.setOnClickListener(l);
        menu.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        m.MenuSelect(menuItem,drawer,getApplicationContext(), 1);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}
