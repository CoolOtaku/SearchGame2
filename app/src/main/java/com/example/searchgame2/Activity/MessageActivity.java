package com.example.searchgame2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.searchgame2.Adapters.MessageAdapter;
import com.example.searchgame2.CastomToast;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.GetMessages;
import com.example.searchgame2.Menu;
import com.example.searchgame2.R;
import com.google.android.material.navigation.NavigationView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    DrawerLayout drawer;
    SwipeRefreshLayout refresh;
    Menu m = new Menu();
    CastomToast toast = new CastomToast();
    DataBase dataBase;
    SQLiteDatabase database2;
    Cursor cursor;
    String name = "";
    ArrayList<Integer> arrayID = new ArrayList<>();
    RecyclerView list_message;

    ScrollView containerMessage;
    TextView name_profile;
    CircleImageView img_profile;
    TextView textMessage;
    Button clouseMessage;

    ConstraintLayout containerSendMessage;
    TextView name_send_message;
    TextView theme_send_message;
    TextView text_send_message;
    CheckBox saved_messages_for_send;
    Button send_message;

    Button b_sendPM;
    Button b_sendMessage;
    Button b_inputMessage;

    String query;
    int Is_Pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        refresh = (SwipeRefreshLayout)findViewById(R.id.refresh);
        refresh.setColorSchemeResources(R.color.colorRed);
        refresh.setProgressBackgroundColorSchemeResource(R.color.colorButton);
        refresh.setOnRefreshListener(this);
        drawer = findViewById(R.id.drawer_layout);
        final NavigationView menu = (NavigationView) findViewById(R.id.nav_view);
        menu.setNavigationItemSelectedListener(this);

        dataBase = new DataBase(getApplicationContext());
        database2 = dataBase.getWritableDatabase();

        list_message = (RecyclerView)findViewById(R.id.list_message);
        list_message.setLayoutManager(new LinearLayoutManager(this));
        containerMessage = (ScrollView)findViewById(R.id.containerMessage);
        name_profile = (TextView)findViewById(R.id.name_profile);
        img_profile = (CircleImageView)findViewById(R.id.img_profile);
        textMessage = (TextView)findViewById(R.id.textMessage);
        clouseMessage = (Button)findViewById(R.id.clouseMessage);

        containerSendMessage = (ConstraintLayout)findViewById(R.id.containerSendMessage);
        name_send_message = (TextView)findViewById(R.id.name_send_message);
        theme_send_message = (TextView)findViewById(R.id.theme_send_message);
        text_send_message = (TextView)findViewById(R.id.text_send_message);
        saved_messages_for_send = (CheckBox)findViewById(R.id.saved_messages_for_send);
        send_message = (Button)findViewById(R.id.send_message);

        b_sendPM = (Button)findViewById(R.id.b_sendPM);
        b_sendMessage = (Button)findViewById(R.id.b_sendMessage);
        b_inputMessage = (Button)findViewById(R.id.b_inputMessage);

        Bundle bundle = getIntent().getExtras();
        String info = bundle.getString("USER_ID");
        Is_Pm = bundle.getInt("IS_PM");
        if(!info.equals("0")){
            containerSendMessage.setVisibility(View.VISIBLE);
            name_send_message.setText(info);
        }

        try {
            cursor = database2.rawQuery("SELECT "+dataBase.NAME+" FROM "+dataBase.TABLE_PROFILE, null);
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(dataBase.NAME));
        }catch (Exception e){
            toast.showToas(getApplicationContext(),getString(R.string.please_go_profile),false);
        }
        query = "SELECT " + dataBase.ID + " FROM " + dataBase.TABLE_MESSAGES+" WHERE "+dataBase.USER_FROM+" != '"+name+"' AND "
                +dataBase.SENDID+" = 0";
        setContent(query,true);

        ImageView logo = (ImageView)findViewById(R.id.logo);
        logo.setOnClickListener(this);
        clouseMessage.setOnClickListener(this);
        send_message.setOnClickListener(this);
        b_sendPM.setOnClickListener(this);
        b_inputMessage.setOnClickListener(this);
        b_sendMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logo:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.clouseMessage:
                containerMessage.setVisibility(View.GONE);
                img_profile.setImageResource(R.drawable.profileicon);
                name_profile.setText("");
                textMessage.setText("");
                break;
            case R.id.send_message:
                String theme = theme_send_message.getText().toString();
                String text = text_send_message.getText().toString();
                String name_for = name_send_message.getText().toString();
                int is_send;
                if(theme.isEmpty()){
                    toast.showToas(getApplicationContext(),getString(R.string.input_theme),false);
                    break;
                }
                if(name_for.isEmpty()){
                    toast.showToas(getApplicationContext(),getString(R.string.specify_name),false);
                    break;
                }
                if(text.isEmpty()){
                    toast.showToas(getApplicationContext(),getString(R.string.have_not_forgotten_text),false);
                    break;
                }
                if(saved_messages_for_send.isChecked()){
                    is_send = 1;
                }else{
                    is_send = 0;
                }
                new SendMessage(theme,text,name,name_for,String.valueOf(is_send),Is_Pm).execute();
                dataBase.RefreshMessages(database2);
                containerSendMessage.setVisibility(View.GONE);
                new GetMessages().start(MessageActivity.this);
                break;
            case R.id.b_sendPM:
                containerSendMessage.setVisibility(View.VISIBLE);
                break;
            case R.id.b_inputMessage:
                query = "SELECT " + dataBase.ID + " FROM " + dataBase.TABLE_MESSAGES+" WHERE "+dataBase.USER_FROM+" != '"+name+"' AND "
                        +dataBase.SENDID+" = 0";
                setContent(query,false);
                break;
            case R.id.b_sendMessage:
                query = "SELECT " + dataBase.ID + " FROM " + dataBase.TABLE_MESSAGES+" WHERE "+dataBase.USER_FROM+" = '"+name+"' AND "
                        +dataBase.SENDID+" != 0";
                setContent(query,false);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        m.MenuSelect(menuItem,drawer,getApplicationContext(),2);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(containerMessage.getVisibility()==View.VISIBLE) {
            containerMessage.setVisibility(View.GONE);
        }else if(containerSendMessage.getVisibility()==View.VISIBLE){
            containerSendMessage.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
        }
    }

    @Override
    public void onRefresh() {
        dataBase.RefreshMessages(database2);
        new GetMessages().start(MessageActivity.this);
        setContent(query, false);
        refresh.setRefreshing(false);
    }

    private void setContent(String sql, boolean isFirst){
        if(!arrayID.isEmpty()){
            arrayID.clear();
        }
        try {
            cursor = database2.rawQuery(sql, null);
            cursor.moveToFirst();
            do {
                arrayID.add(cursor.getInt(cursor.getColumnIndex(dataBase.ID)));
            } while (cursor.moveToNext());
            cursor.close();
            list_message.setAdapter(new MessageAdapter(MessageActivity.this,arrayID,containerMessage,name_profile,img_profile,textMessage));
        } catch (Exception e) {
            if(!isFirst) {
                toast.showToas(getApplicationContext(), getString(R.string.error_messages), false);
            }
        }
    }
    public class SendMessage extends AsyncTask<String,Void,String> {

        String theme;
        String text;
        String user_from;
        String user_for;
        String is_send;
        int is_pm;

        public SendMessage(String theme, String text, String user_from, String user_for, String is_send, int is_pm){
            this.theme = theme;
            this.text = text;
            this.user_from = user_from;
            this.user_for = user_for;
            this.is_send = is_send;
            this.is_pm = is_pm;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected String doInBackground(String... strings) {
            try {
                String link = DataBase.HOST+"SendMessage.php";

                URL url = new URL(link);
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection)con;
                http.setRequestMethod("POST");
                http.setDoOutput(true);

                Map<String,String> arguments = new HashMap<>();
                arguments.put("theme", theme);
                arguments.put("text", text);
                arguments.put("user_from", user_from);
                arguments.put("user_for", user_for);
                arguments.put("is_send", is_send);
                arguments.put("is_pm", String.valueOf(is_pm));
                StringJoiner sj = new StringJoiner("&");
                for(Map.Entry<String,String> entry : arguments.entrySet())
                    sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                            + URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
                byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
                int length = out.length;

                http.setFixedLengthStreamingMode(length);
                http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                http.connect();
                try(OutputStream os = http.getOutputStream()) {
                    os.write(out);
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));

                return reader.readLine();
            } catch (Exception ex) {
                return "false";
            }

        }
        @Override
        protected void onPostExecute(String result){
            if(result.equals("true")){
                toast.showToas(getApplicationContext(),getString(R.string.sends),true);
            }else{
                toast.showToas(getApplicationContext(),getString(R.string.error),false);
            }
        }
    }

}
