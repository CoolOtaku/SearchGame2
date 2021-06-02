package com.example.searchgame2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.example.searchgame2.Menu;
import com.example.searchgame2.R;
import com.google.android.material.navigation.NavigationView;

public class AboutAppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    ImageView gifAnim;
    DrawerLayout drawer;
    Menu m = new Menu();
    MediaPlayer media;
    View openEaster;
    int counClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        drawer = findViewById(R.id.drawer_layout);
        final NavigationView menu = (NavigationView) findViewById(R.id.nav_view);
        menu.setNavigationItemSelectedListener(this);

        gifAnim = (ImageView) findViewById(R.id.gifAnim);
        openEaster = (View)findViewById(R.id.openEaster);
        openEaster.setOnClickListener(this);
        media = MediaPlayer.create(this,R.raw.u);

        ImageView logo = (ImageView)findViewById(R.id.logo);
        logo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logo:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.openEaster:
                counClick++;
                if(counClick==2) {
                    gifAnim.setVisibility(View.VISIBLE);
                    media.start();
                    counClick = 0;
                }
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        m.MenuSelect(menuItem,drawer,getApplicationContext(),3);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(gifAnim.getVisibility() == View.VISIBLE){
            media.pause();
            gifAnim.setVisibility(View.GONE);
        }else if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
        }
    }
}
