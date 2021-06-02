package com.example.searchgame2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.searchgame2.Adapters.SearchAdapter;
import com.example.searchgame2.CastomToast;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.ForNetWorck;
import com.example.searchgame2.Menu;
import com.example.searchgame2.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText searchT;
    ImageView searchB;
    TableRow fill;
    TextView textView;
    DrawerLayout drawer;
    Menu m = new Menu();
    RecyclerView GameList;

    ForNetWorck netWorck = new ForNetWorck();
    DataBase dataBase = new DataBase(this);

    Intent in = new Intent();
    ArrayList<Integer> arrayList = new ArrayList<>();

    StringBuilder Selection = new StringBuilder("SELECT "+dataBase.ID+" FROM "+dataBase.TABLE_GAMEALL+" WHERE "+dataBase.FOR_PLATFORM+" LIKE ");
    String Sort = " ORDER BY "+dataBase.ID+" DESC";
    String platform;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        try {
            searchT = (EditText) findViewById(R.id.searchT);
            searchB = (ImageView) findViewById(R.id.searchB);
            GameList = (RecyclerView) findViewById(R.id.GameList);
            GameList.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
            fill = (TableRow) findViewById(R.id.fill);
            textView = (TextView) findViewById(R.id.textView);
            drawer = findViewById(R.id.drawer_layout);
            final NavigationView menu = (NavigationView) findViewById(R.id.nav_view);
            menu.setNavigationItemSelectedListener(this);

            Bundle info = getIntent().getExtras();
            platform = "'%"+info.getString("CATEGORY")+"%'";
            Selection.append(platform);

            DisplayGame(Selection.toString());

        } catch (Exception e) {
            CastomToast toast = new CastomToast();
            toast.showToas(getApplicationContext(),getString(R.string.error),false);
        }

        View.OnClickListener click = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.searchB:
                        StringBuilder search = new StringBuilder(searchT.getText());
                        String Selection2 = Selection.toString()+" AND "+dataBase.TITLE+" LIKE '%"+search.toString()+"%'";
                        if(search.length() > 0) {
                            Selection2+=" OR " + dataBase.TITLE + " LIKE '" + search.charAt(0) + "%'";
                        }
                        DisplayGame(Selection2);
                        break;
                    case R.id.fill:
                        in.setClass(getApplicationContext(), FillterActivity.class);
                        startActivityForResult(in, 1);
                        overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
                        break;
                }
            }
        };
        searchB.setOnClickListener(click);
        fill.setOnClickListener(click);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) {
            return;
        }

        String res = data.getStringExtra("RES");
        Selection.delete(0,Selection.length());
        Selection.append("SELECT "+dataBase.ID+" FROM "+dataBase.TABLE_GAMEALL+" WHERE "+dataBase.FOR_PLATFORM+" LIKE "+platform);
        Selection.append(res);
        Sort = data.getStringExtra("RES_SORT");

        DisplayGame(Selection.toString());

    }

    private void DisplayGame (String selection){
        if (arrayList != null){
            arrayList.clear();
        }

        final  SQLiteDatabase database2 = dataBase.getWritableDatabase();
        Cursor cursor = database2.rawQuery(selection+Sort,null);
        if (cursor.moveToFirst()) {
            do {
                arrayList.add(cursor.getInt(cursor.getColumnIndex(dataBase.ID)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        database2.close();

        SearchAdapter adapter = new SearchAdapter(getApplicationContext(), arrayList);
        GameList.setAdapter(adapter);

        String found = getString(R.string.found);
        String materials = getString(R.string.materials);
        textView.setText(found + " " + arrayList.size() + " " + materials);
        //searchT.setText(Selection.toString());
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        m.MenuSelect(menuItem,drawer,getApplicationContext(),0);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
        }
    }
}
