package com.example.searchgame2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.searchgame2.Adapters.CheckBoxAdapter;
import com.example.searchgame2.CastomToast;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.Menu;
import com.example.searchgame2.R;
import com.example.searchgame2.Translaytor;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import me.bendik.simplerangeview.SimpleRangeView;

public class FillterActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    SimpleRangeView year;
    SimpleRangeView price;

    TextView progresYearLeft;
    TextView progresYearRight;

    TextView progresPriceLeft;
    TextView progresPriceRight;

    String selectionYear = null;
    String selectionPrice = null;

    Button bGanre;
    Button bOk;
    Button bProcessor;
    Button bVideocard;
    Button bSort;
    Button bTypeSort;

    RecyclerView cores;
    RecyclerView corefre;
    RecyclerView ram;
    RecyclerView video_ram;
    RecyclerView hhd_ram;

    Intent intent = new Intent();

    DataBase dataBase = new DataBase(this);

    StringBuilder selection = new StringBuilder();

    ArrayList<String> arrJanre = new ArrayList<>();
    ArrayList<String> arrProcessor = new ArrayList<>();
    ArrayList<String> arrVideocard = new ArrayList<>();
    ArrayList<String> arrCores = new ArrayList<>();
    ArrayList<String> arrCorefre = new ArrayList<>();
    ArrayList<String> arrRam = new ArrayList<>();
    ArrayList<String> arrVideo_ram = new ArrayList<>();
    ArrayList<String> arrHhd_ram = new ArrayList<>();

    ArrayList<Integer> Janre = new ArrayList<>();
    ArrayList<Integer> Processor = new ArrayList<>();
    ArrayList<Integer> Videocard = new ArrayList<>();
    ArrayList<Integer> Cores = new ArrayList<>();
    ArrayList<Integer> Corefre = new ArrayList<>();
    ArrayList<Integer> Ram = new ArrayList<>();
    ArrayList<Integer> Video_ram = new ArrayList<>();
    ArrayList<Integer> Hhd_ram = new ArrayList<>();

    String key_word = "AND";
    String[] aarSort = new String[]{dataBase.TITLE,dataBase.YEAR};
    String[] aarTypeSort = new String[]{"DESC","ASC"};
    String Sort = null;
    String typeSort = null;

    boolean one_world = false;
    DrawerLayout drawer;
    Menu m = new Menu();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillter);
        try {
            year = (SimpleRangeView) findViewById(R.id.year);
            progresYearLeft = (TextView) findViewById(R.id.progresYearLeft);
            progresYearRight = (TextView) findViewById(R.id.progresYearRight);

            price = (SimpleRangeView) findViewById(R.id.price);
            progresPriceLeft = (TextView) findViewById(R.id.progresPriceLeft);
            progresPriceRight = (TextView) findViewById(R.id.progresPriceRight);

            drawer = findViewById(R.id.drawer_layout);
            final NavigationView menu = (NavigationView) findViewById(R.id.nav_view);
            menu.setNavigationItemSelectedListener(this);

            bGanre = (Button) findViewById(R.id.bGanre);
            bProcessor = (Button) findViewById(R.id.bProcessor);
            bVideocard = (Button) findViewById(R.id.bVideocard);
            bSort = (Button) findViewById(R.id.bSort);
            bTypeSort = (Button) findViewById(R.id.bTypeSort);
            bOk = (Button) findViewById(R.id.bOk);

            cores = (RecyclerView) findViewById(R.id.cores);
            corefre = (RecyclerView) findViewById(R.id.corefre);
            ram = (RecyclerView) findViewById(R.id.ram);
            video_ram = (RecyclerView) findViewById(R.id.video_ram);
            hhd_ram = (RecyclerView) findViewById(R.id.hhd_ram);

            getData();

            year.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
                @Override
                public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                    progresYearLeft.setText(String.valueOf(i + 1990));
                    progresYearRight.setText(String.valueOf(i1 + 1990));
                    selectionYear = " " + key_word + " " + dataBase.YEAR + " BETWEEN " + progresYearLeft.getText() + " " + key_word + " " + progresYearRight.getText();
                }
            });
            price.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
                @Override
                public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                    progresPriceLeft.setText(String.valueOf(i));
                    progresPriceRight.setText(String.valueOf(i1));
                    selectionPrice = " " + key_word + " " + dataBase.PRICE + " BETWEEN " + progresPriceLeft.getText() + " " + key_word + " " + progresPriceRight.getText();
                }
            });

            registerForContextMenu(bGanre);
            registerForContextMenu(bProcessor);
            registerForContextMenu(bVideocard);
            registerForContextMenu(bSort);
            registerForContextMenu(bTypeSort);
            bGanre.setOnClickListener(this);
            bProcessor.setOnClickListener(this);
            bVideocard.setOnClickListener(this);
            bSort.setOnClickListener(this);
            bTypeSort.setOnClickListener(this);

            cores.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
            cores.setAdapter(new CheckBoxAdapter(arrCores, Cores));
            corefre.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
            corefre.setAdapter(new CheckBoxAdapter(arrCorefre, Corefre, getString(R.string.between)));
            ram.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
            ram.setAdapter(new CheckBoxAdapter(arrRam, Ram));
            video_ram.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
            video_ram.setAdapter(new CheckBoxAdapter(arrVideo_ram, Video_ram));
            hhd_ram.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
            hhd_ram.setAdapter(new CheckBoxAdapter(arrHhd_ram, Hhd_ram));

            bOk.setOnClickListener(this);

            ImageView logo = (ImageView)findViewById(R.id.logo);
            logo.setOnClickListener(this);
        }catch (Exception e){
            CastomToast toast = new CastomToast();
            toast.showToas(getApplicationContext(),getString(R.string.error),false);
        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        switch (v.getId()){
            case R.id.bGanre:
                CreateContextMenu(menu,arrJanre,Janre,0,true);
                break;
            case R.id.bProcessor:
                CreateContextMenu(menu,arrProcessor,Processor,arrJanre.size(),false);
                break;
            case R.id.bVideocard:
                CreateContextMenu(menu,arrVideocard,Videocard,arrJanre.size()+arrProcessor.size(),false);
                break;
            case R.id.bSort:
                for (int i = 0; i < aarSort.length; i++) {
                    if (aarSort[i].equals(dataBase.TITLE)) {
                        menu.add(0, i+arrJanre.size()+arrProcessor.size()+arrVideocard.size(), 0, getString(R.string.sorts_name));
                    }else{
                        menu.add(0, i+arrJanre.size()+arrProcessor.size()+arrVideocard.size(), 0, getString(R.string.sorts_year));
                    }
                }
                break;
            case R.id.bTypeSort:
                for (int i = 0; i < aarTypeSort.length; i++) {
                    if (aarTypeSort[i].equals("DESC")) {
                        menu.add(0, i+arrJanre.size()+arrProcessor.size()+arrVideocard.size()+aarSort.length, 0, getString(R.string.sorts_type_desc));
                    }else{
                        menu.add(0, i+arrJanre.size()+arrProcessor.size()+arrVideocard.size()+aarSort.length, 0, getString(R.string.sorts_type_asc));
                    }
                }
                break;
        }
    }
    private void CreateContextMenu(ContextMenu menu,ArrayList<String> arrayList, ArrayList<Integer> arrIndex, int index, boolean tralayt){
        Translaytor t = new Translaytor();
        for (int i = 0; i < arrayList.size(); i++) {
            String s;
            if (tralayt) {
                s = t.Translayt(arrayList.get(i));
            }else{
                s = t.gup(arrayList.get(i));
            }
            String s1;
            if (arrIndex.contains(i)){
                s1="☑ "+s;
            }else{
                s1="□ "+s;
            }
            menu.add(0, i+index, 0, s1);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        int in = item.getItemId();
        SelectMenuIntem(in,arrJanre,Janre,0);
        SelectMenuIntem(in,arrProcessor,Processor,arrJanre.size());
        SelectMenuIntem(in,arrVideocard,Videocard,arrJanre.size()+arrProcessor.size());
        for (int i = 0; i < aarSort.length; i++){
            if (in == i+arrJanre.size()+arrProcessor.size()+arrVideocard.size()){
                int index = in-arrJanre.size()-arrProcessor.size()-arrVideocard.size();
                Sort=aarSort[index];
            }
        }
        for (int i = 0; i < aarTypeSort.length; i++){
            if (in == i+arrJanre.size()+arrProcessor.size()+arrVideocard.size()+aarSort.length){
                int index = in-arrJanre.size()-arrProcessor.size()-arrVideocard.size()-aarSort.length;
                typeSort=aarTypeSort[index];
            }
        }
        return super.onContextItemSelected(item);
    }
    private void SelectMenuIntem(int in, ArrayList<String> arrayList, ArrayList<Integer> arrIndex, int ind){
        for (int i = 0; i < arrayList.size();i++) {
            if (in == i+ind) {
                int  index = in-ind;
                if(arrIndex.contains((Object)index)){
                    arrIndex.remove((Object)index);
                }else{
                    arrIndex.add(index);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          case R.id.bOk:
            CheckBoxAdapter Acor = (CheckBoxAdapter) cores.getAdapter();
            Cores = Acor.Click(Cores);
            CheckBoxAdapter Acorfre = (CheckBoxAdapter) corefre.getAdapter();
            Corefre = Acorfre.Click(Corefre);
            CheckBoxAdapter Aram = (CheckBoxAdapter) ram.getAdapter();
            Ram = Aram.Click(Ram);
            CheckBoxAdapter AVran = (CheckBoxAdapter) video_ram.getAdapter();
            Video_ram = AVran.Click(Video_ram);
            CheckBoxAdapter AHhd_ram = (CheckBoxAdapter) hhd_ram.getAdapter();
            Hhd_ram = AHhd_ram.Click(Hhd_ram);
            if (selectionYear != null) {
                selection.append(selectionYear);
                one_world = true;
            }
            if (selectionPrice != null) {
                selection.append(selectionPrice);
                one_world = true;
            }
            setSelection(dataBase.JANRE,arrJanre,Janre,"%");
            setSelection(dataBase.PROCESSOR,arrProcessor,Processor,"%");
            setSelection(dataBase.VIDEO_CARD,arrVideocard,Videocard,"%");
            setSelection(dataBase.CORE,arrCores,Cores,"");
            setSelection(dataBase.COREFREQUENCY,arrCorefre,Corefre,"");
            setSelection(dataBase.RAM,arrRam,Ram,"");
            setSelection(dataBase.VIDEO_RAM,arrVideo_ram,Video_ram,"");
            setSelection(dataBase.HHD_RAM,arrHhd_ram,Hhd_ram,"");

            String ResSort = "";
            if (Sort != null) {
                ResSort += " ORDER BY " + Sort;
            } else {
                ResSort += " ORDER BY " + dataBase.ID;
            }
            if (typeSort != null) {
                ResSort += " " + typeSort;
            } else {
                ResSort += " DESC";
            }
            intent.putExtra("RES", selection.toString());
            intent.putExtra("RES_SORT", ResSort);
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
            break;
          case R.id.logo:
            drawer.closeDrawer(GravityCompat.START);
            break;
          default:
            Toast.makeText(getApplicationContext(),getString(R.string.clamp),Toast.LENGTH_LONG).show();
            break;
        }
    }
    private void setSelection(String from, ArrayList<String> arraySelectiom, ArrayList<Integer> arrayIndex, String seeker){
        for (int i = 0; i < arrayIndex.size(); i++) {
            if(one_world){
                key_word = "AND";
                one_world = false;
            }else {
                key_word = "OR";
            }
            if(selection.length() < 1){
                key_word = "AND";
                one_world = false;
            }
            selection.append(" "+key_word+" "+from+" LIKE '"+seeker+arraySelectiom.get(arrayIndex.get(i))+seeker+"'");
        }
    }

    private void getData(){
        String s = null;
        StringTokenizer st = null;
        Calendar calendar = Calendar.getInstance();
        int i = calendar.get(Calendar.YEAR);
        year.setCount(i-1990+1);
        progresYearRight.setText(String.valueOf(i));
        final SQLiteDatabase database2 = dataBase.getWritableDatabase();
        Cursor c = database2.rawQuery("SELECT MAX("+dataBase.PRICE+") FROM "+dataBase.TABLE_GAMEALL,null);
        c.moveToFirst();
        i = c.getInt(c.getPosition());
        price.setCount(i+1);
        progresPriceRight.setText(String.valueOf(i));
        c.close();

        Cursor cursor = database2.rawQuery("SELECT "+dataBase.JANRE+", "+dataBase.PROCESSOR+", "+dataBase.VIDEO_CARD
                +", "+dataBase.CORE+", "+dataBase.COREFREQUENCY+", "+dataBase.RAM+", "+dataBase.VIDEO_RAM+", "+dataBase.HHD_RAM+" FROM "
                +dataBase.TABLE_GAMEALL+" ORDER BY "+dataBase.ID+" DESC",null);

        getColumn(cursor,st,s,arrJanre,true,dataBase.JANRE);
        getColumn(cursor,st,s,arrProcessor,true,dataBase.PROCESSOR);
        getColumn(cursor,st,s,arrVideocard,true,dataBase.VIDEO_CARD);
        getColumn(cursor,st,s,arrCores,false,dataBase.CORE);
        getColumn(cursor,st,s,arrCorefre,false,dataBase.COREFREQUENCY);
        getColumn(cursor,st,s,arrRam,false,dataBase.RAM);
        getColumn(cursor,st,s,arrVideo_ram,false,dataBase.VIDEO_RAM);
        getColumn(cursor,st,s,arrHhd_ram,false,dataBase.HHD_RAM);

        cursor.close();
        database2.close();
        dataBase.close();
    }
    private void getColumn(Cursor cursor, StringTokenizer st, String s, ArrayList<String> arrayList, boolean moreOne, String column){
        cursor.moveToFirst();
        do {
            if (moreOne){
                s = cursor.getString(cursor.getColumnIndex(column));
                st = new StringTokenizer(s, ", ");
                while (st.hasMoreTokens()) {
                    s = st.nextToken();
                    if (!arrayList.contains(s)) {
                        arrayList.add(s);
                    }
                }
            }else {
                s = cursor.getString(cursor.getColumnIndex(column));
                if (!arrayList.contains(s)) {
                    arrayList.add(s);
                }
            }
        }while (cursor.moveToNext());
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        m.MenuSelect(menuItem,drawer,getApplicationContext(), 0);
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
