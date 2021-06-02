package com.example.searchgame2.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.GetDescription;
import com.example.searchgame2.GetRating;
import com.example.searchgame2.Menu;
import com.example.searchgame2.R;
import com.example.searchgame2.SetRating;
import com.example.searchgame2.SetSelected;
import com.example.searchgame2.Translaytor;
import com.google.android.material.navigation.NavigationView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class Full_StoriActivity extends YouTubeBaseActivity implements NavigationView.OnNavigationItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    TextView name_view;
    TextView haracteristic_view;
    TextView haracteristic_view2;
    TextView TextPrice;

    ImageView img_view;
    ImageView image1;
    ImageView image2;
    ImageView image3;
    ImageView img_1;
    ImageView img_2;
    ImageView img_3;
    ImageView img_4;

    TextView aveR;
    TextView voteR;
    TextView sr1;
    TextView sr2;
    TextView sr3;
    TextView sr4;
    TextView sr5;
    TextView sr6;
    TextView progresRating;

    SeekBar sbr1;
    SeekBar sbr2;
    SeekBar sbr3;
    SeekBar sbr4;
    SeekBar sbr5;
    SeekBar sbr6;

    Animation anim;
    Cursor cursor;
    SQLiteDatabase database2;

    int id;
    int Index;
    boolean isTouch;
    boolean openFullScrean;
    float startX;
    float stopX;
    String url_img_1;
    String url_img_2;
    String url_img_3;
    String url_img_4;

    DataBase dataBase;
    DrawerLayout drawer;
    Menu m = new Menu();
    Intent In = new Intent();

    YouTubePlayerView VideoPlayer;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    public static final String key="[YOUR_YouTube_API_KEY]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_stori);
        drawer = findViewById(R.id.drawer_layout);
        final NavigationView menu = (NavigationView) findViewById(R.id.nav_view);
        menu.setNavigationItemSelectedListener(this);

        Bundle info = getIntent().getExtras();
        id = info.getInt("KEY");

        dataBase = new DataBase(this);
        database2 = dataBase.getWritableDatabase();

        cursor = database2.rawQuery("SELECT * FROM "+dataBase.TABLE_GAMEALL+" WHERE "+dataBase.ID+" = "+id,null);
        cursor.moveToFirst();

        img_view = (ImageView)findViewById(R.id.img_view);
        image1 = (ImageView)findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.image2);
        image3 = (ImageView)findViewById(R.id.image3);
        img_1 = (ImageView)findViewById(R.id.img_1);
        img_2 = (ImageView)findViewById(R.id.img_2);
        img_3 = (ImageView)findViewById(R.id.img_3);
        img_4 = (ImageView)findViewById(R.id.img_4);

        url_img_1 = cursor.getString(cursor.getColumnIndex(dataBase.POSTER));
        url_img_2 = cursor.getString(cursor.getColumnIndex(dataBase.SCREEN1));
        url_img_3 = cursor.getString(cursor.getColumnIndex(dataBase.SCREEN2));
        url_img_4 = cursor.getString(cursor.getColumnIndex(dataBase.SCREEN3));

        LoadImage(img_view,image1,image2,image3);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoadImage(img_1,img_2,img_3,img_4);
            }
        }, 2 * 1000);

        name_view = (TextView) findViewById(R.id.name_view);
        haracteristic_view = (TextView) findViewById(R.id.haracteristic_view);
        haracteristic_view2 = (TextView) findViewById(R.id.haracteristic_view2);
        TextPrice = (TextView)findViewById(R.id.TextPrice);
        progresRating = (TextView)findViewById(R.id.progresRating);

        TextProcesed();
        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.appearence);

        VideoPlayer = (YouTubePlayerView)findViewById(R.id.VideoPlayer);

        onInitializedListener = new  YouTubePlayer.OnInitializedListener(){

            Button bTlayer = (Button)findViewById(R.id.bTlayer);
            Button bGameplay = (Button)findViewById(R.id.bGameplay);
            Button bSteam = (Button)findViewById(R.id.bSteam);
            Button bReviews = (Button)findViewById(R.id.bReviews);
            Button bSelected = (Button)findViewById(R.id.bSelected);

            String Video1 = cursor.getString(cursor.getColumnIndex(dataBase.LINK_ON_TEASET));
            String Video2 = cursor.getString(cursor.getColumnIndex(dataBase.LINK_ON_GAMEPLAY));
            String Steam = cursor.getString(cursor.getColumnIndex(dataBase.LINK_ON_STEAM));

            ImageView logo = (ImageView)findViewById(R.id.logo);

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(Video1);
                View.OnClickListener l = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()){
                            case R.id.bTlayer:
                                youTubePlayer.cueVideo(Video1);
                                break;
                            case R.id.bGameplay:
                                youTubePlayer.cueVideo(Video2);
                                break;
                            case R.id.bSteam:
                                Uri urlSteam = Uri.parse(Steam);
                                Intent in = new Intent(Intent.ACTION_VIEW,urlSteam);
                                startActivity(in);
                                break;
                            case R.id.bReviews:
                                In.putExtra("ID_REVIEWS",id);
                                In.setClass(getApplicationContext(), CommentsActivity.class);
                                startActivity(In);
                                overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
                                break;
                            case R.id.bSelected:
                                SQLiteDatabase d = dataBase.getWritableDatabase();
                                Cursor cursor1 = d.rawQuery("SELECT "+dataBase.ID+" FROM "+dataBase.TABLE_PROFILE,null);
                                cursor1.moveToFirst();
                                int userId = cursor1.getInt(cursor1.getColumnIndex(dataBase.ID));
                                new SetSelected(getApplicationContext(),userId,id).execute();
                                cursor1.close();
                                d.close();
                                break;
                            case R.id.img_view:
                                openFullScrean = true;
                                Index = 1;
                                ShowIndexImg(Index);
                                break;
                            case R.id.image1:
                                openFullScrean = true;
                                Index = 2;
                                ShowIndexImg(Index);
                                break;
                            case R.id.image2:
                                openFullScrean = true;
                                Index = 3;
                                ShowIndexImg(Index);
                                break;
                            case R.id.image3:
                                openFullScrean = true;
                                Index = 4;
                                ShowIndexImg(Index);
                                break;
                            case R.id.logo:
                                drawer.openDrawer(GravityCompat.START);
                                break;
                        }
                    }
                };
                bTlayer.setOnClickListener(l);
                bGameplay.setOnClickListener(l);
                bSteam.setOnClickListener(l);
                bReviews.setOnClickListener(l);
                bSelected.setOnClickListener(l);
                img_view.setOnClickListener(l);
                image1.setOnClickListener(l);
                image2.setOnClickListener(l);
                image3.setOnClickListener(l);
                logo.setOnClickListener(l);
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult){
                }
            };
            VideoPlayer.initialize(key,onInitializedListener);

        cursor.close();
        database2.close();

            View.OnTouchListener toch = new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            isTouch = true;
                            startX = event.getX();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (isTouch) {
                                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.img_out);
                                ShowIndexImg(Index);
                                isTouch = false;
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            isTouch = true;
                            stopX = event.getX();
                            if (stopX > startX) {
                                Index--;
                                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left);
                                if (Index < 1) {
                                    Index = 4;
                                }
                            } else {
                                Index++;
                                anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
                                if (Index > 4) {
                                    Index = 1;
                                }
                            }
                            ShowIndexImg(Index);
                            break;
                    }
                    return true;
                }
            };
            img_1.setOnTouchListener(toch);
            img_2.setOnTouchListener(toch);
            img_3.setOnTouchListener(toch);
            img_4.setOnTouchListener(toch);

        sbr1 = (SeekBar)findViewById(R.id.sbr1);
        sbr2 = (SeekBar)findViewById(R.id.sbr2);
        sbr3 = (SeekBar)findViewById(R.id.sbr3);
        sbr4 = (SeekBar)findViewById(R.id.sbr4);
        sbr5 = (SeekBar)findViewById(R.id.sbr5);
        sbr6 = (SeekBar)findViewById(R.id.sbr6);
        sbr1.setOnSeekBarChangeListener(this);
        sbr2.setOnSeekBarChangeListener(this);
        sbr3.setOnSeekBarChangeListener(this);
        sbr4.setOnSeekBarChangeListener(this);
        sbr5.setOnSeekBarChangeListener(this);
        sbr6.setOnSeekBarChangeListener(this);
    }
    private void LoadImage(ImageView m1, ImageView m2, ImageView m3, ImageView m4){
        Picasso.with(getApplicationContext()).load(DataBase.DIRECTORI + url_img_1)
                .resize(360,520).placeholder(R.drawable.load).error(R.drawable.bgerror).into(m1);
        Picasso.with(getApplicationContext()).load(DataBase.DIRECTORI + url_img_2)
                .resize(768,432).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.drawable.bgerror).placeholder(R.drawable.load).into(m2);
        Picasso.with(getApplicationContext()).load(DataBase.DIRECTORI + url_img_3)
                .resize(768,432).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.drawable.bgerror).placeholder(R.drawable.load).into(m3);
        Picasso.with(getApplicationContext()).load(DataBase.DIRECTORI + url_img_4)
                .resize(768,432).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE)
                .error(R.drawable.bgerror).placeholder(R.drawable.load).into(m4);
    }
    private void TextProcesed(){
       Translaytor t = new Translaytor();
       String translaytJanre = t.Translayt(cursor.getString(cursor.getColumnIndex(dataBase.JANRE)));

        String info_processor= t.gup(cursor.getString(cursor.getColumnIndex(dataBase.PROCESSOR)));
        String info_pfree = t.Breaker(cursor.getString(cursor.getColumnIndex(dataBase.COREFREQUENCY)),getString(R.string.between));
        String info_videocard = t.gup(cursor.getString(cursor.getColumnIndex(dataBase.VIDEO_CARD)));
        String info_videoram = t.gup(cursor.getString(cursor.getColumnIndex(dataBase.VIDEO_RAM)));
        String info_ram = t.gup(cursor.getString(cursor.getColumnIndex(dataBase.RAM)));
        String info_developer = t.gup(cursor.getString(cursor.getColumnIndex(dataBase.DEVELOPER)));
        String info_edition = t.gup(cursor.getString(cursor.getColumnIndex(dataBase.EDITION)));
        String info_hhd_ram = t.gup(cursor.getString(cursor.getColumnIndex(dataBase.HHD_RAM)));

        name_view.setText(cursor.getString(cursor.getColumnIndex(dataBase.TITLE)));

        String title_developer = getString(R.string.developer);
        String title_publishing = getString(R.string.publishing);
        String system_req = getString(R.string.system_req);
        String processor = getString(R.string.processor);
        String ram = getString(R.string.ram);
        String videocard = getString(R.string.videocard);
        String hhd_ssd = getString(R.string.hhd_ssd);
        String ganre = getString(R.string.ganre);
        String year = getString(R.string.year);

        int indexYear = cursor.getColumnIndex(dataBase.YEAR);
        int indexPrice = cursor.getColumnIndex(dataBase.PRICE);

        haracteristic_view.setText(Html.fromHtml(
                "<p style=\"margin:0px;padding:0px;\">"+year+": <span style=\"color:#DD163B;\">" + cursor.getString(indexYear)+"</span></p>"
                +"<p style=\"margin:0px;padding:0px;\">"+ganre+": <span style=\"color:#DD163B;\">"+translaytJanre+"</span></p>"
                +"<p style=\"margin:0px;padding:0px;\">"+title_developer+": <span style=\"color:#DD163B;\">"+info_developer+"</span></p>"
                +"<p style=\"margin:0px;padding:0px;\">"+title_publishing+": <span style=\"color:#DD163B;\">"+info_edition+"</span></p><br>"
                +"<p style=\"margin:0px;padding:0px;\">"+system_req+":</p>"
                +"<p style=\"margin:0px;padding:0px;\">"+processor+":<span style=\"color:#DD163B;\">"+info_processor+", "+info_pfree+"</span></p>"
                +"<p style=\"margin:0px;padding:0px;\">"+videocard+": <span style=\"color:#DD163B;\">"+info_videocard+", "+info_videoram+"</span></p>"
                +"<p style=\"margin:0px;padding:0px;\">"+ram+": <span style=\"color:#DD163B;\">"+info_ram+"</span></p>"
                +"<p style=\"margin:0px;padding:0px;\">"+hhd_ssd+": <span style=\"color:#DD163B;\">"+info_hhd_ram+"</span></p>"
        ));

        TextPrice.setText(cursor.getString(indexPrice)+" ₴");

        new GetDescription(getApplicationContext(),haracteristic_view2,id).execute();

        aveR = (TextView)findViewById(R.id.aveR);
        voteR = (TextView)findViewById(R.id.voteR);
        sr1 = (TextView)findViewById(R.id.sr1);
        sr2 = (TextView)findViewById(R.id.sr2);
        sr3 = (TextView)findViewById(R.id.sr3);
        sr4 = (TextView)findViewById(R.id.sr4);
        sr5 = (TextView)findViewById(R.id.sr5);
        sr6 = (TextView)findViewById(R.id.sr6);

        new GetRating(id, aveR, voteR, sr1, sr2, sr3, sr4, sr5, sr6).execute();
    }

    private void ShowIndexImg(int i){
        img_1.clearAnimation();
        img_2.clearAnimation();
        img_3.clearAnimation();
        img_4.clearAnimation();
        switch (i){
            case 1:
                img_1.setVisibility(View.VISIBLE);
                img_2.setVisibility(View.INVISIBLE);
                img_3.setVisibility(View.INVISIBLE);
                img_4.setVisibility(View.INVISIBLE);
                img_1.startAnimation(anim);
                break;
            case 2:
                img_1.setVisibility(View.INVISIBLE);
                img_2.setVisibility(View.VISIBLE);
                img_3.setVisibility(View.INVISIBLE);
                img_4.setVisibility(View.INVISIBLE);
                img_2.startAnimation(anim);
                break;
            case 3:
                img_1.setVisibility(View.INVISIBLE);
                img_2.setVisibility(View.INVISIBLE);
                img_3.setVisibility(View.VISIBLE);
                img_4.setVisibility(View.INVISIBLE);
                img_3.startAnimation(anim);
                break;
            case 4:
                img_1.setVisibility(View.INVISIBLE);
                img_2.setVisibility(View.INVISIBLE);
                img_3.setVisibility(View.INVISIBLE);
                img_4.setVisibility(View.VISIBLE);
                img_4.startAnimation(anim);
                break;
        }
        anim.cancel();
    }
    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else if (openFullScrean){
            img_1.setVisibility(View.GONE);
            img_2.setVisibility(View.GONE);
            img_3.setVisibility(View.GONE);
            img_4.setVisibility(View.GONE);
            openFullScrean = false;
        }else{
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        m.MenuSelect(menuItem,drawer,getApplicationContext(),0);
        return true;
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        String result = "";
        int progres = seekBar.getProgress()+1;
        switch (seekBar.getId()){
            case R.id.sbr6:
                result = "optimization="+progres+"|||||";
                break;
            case R.id.sbr3:
                result = "|"+"sound="+progres+"||||";
                break;
            case R.id.sbr5:
                result = "||"+"plot="+progres+"|||";
                break;
            case R.id.sbr1:
                result = "|||"+"video="+progres+"||";
                break;
            case R.id.sbr2:
                result = "||||"+"gameplay="+progres+"|";
                break;
            case R.id.sbr4:
                result = "|||||"+"atm="+progres+"";
                break;
        }
        new SetRating(getApplicationContext(),id,result,aveR, voteR, sr1, sr2, sr3, sr4, sr5, sr6).execute();
        progresRating.setVisibility(View.GONE);
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progresRating.setText(String.valueOf(progress+1));
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        progresRating.setVisibility(View.VISIBLE);
    }
}
