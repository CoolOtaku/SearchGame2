package com.example.searchgame2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.searchgame2.CastomToast;
import com.example.searchgame2.DataBase;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    Menu m = new Menu();
    DrawerLayout drawer;

    EditText contact_email;
    EditText contact_name;
    EditText contact_text;

    Button send_contact;
    Button go_to_site;

    ImageView insta1;
    ImageView insta2;
    ImageView telegram1;
    ImageView telegram2;
    ImageView faceboock1;
    ImageView faceboock2;

    String email;
    String name;
    String text;
    int hour = 0;

    CastomToast toast = new CastomToast();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        drawer = findViewById(R.id.drawer_layout);
        final NavigationView menu = (NavigationView) findViewById(R.id.nav_view);
        menu.setNavigationItemSelectedListener(this);

        contact_email = (EditText)findViewById(R.id.contact_email);
        contact_name = (EditText)findViewById(R.id.contact_name);
        contact_text = (EditText)findViewById(R.id.contact_text);

        send_contact = (Button)findViewById(R.id.send_contact);
        go_to_site = (Button)findViewById(R.id.go_to_site);

        insta1 = (ImageView)findViewById(R.id.insta1);
        insta2 = (ImageView)findViewById(R.id.insta2);
        telegram1 = (ImageView)findViewById(R.id.telegram1);
        telegram2 = (ImageView)findViewById(R.id.telegram2);
        faceboock1 = (ImageView)findViewById(R.id.faceboock1);
        faceboock2 = (ImageView)findViewById(R.id.faceboock2);

        send_contact.setOnClickListener(this);
        go_to_site.setOnClickListener(this);

        insta1.setOnClickListener(this);
        insta2.setOnClickListener(this);
        telegram1.setOnClickListener(this);
        telegram2.setOnClickListener(this);
        faceboock1.setOnClickListener(this);
        faceboock2.setOnClickListener(this);

        ImageView logo = (ImageView)findViewById(R.id.logo);
        logo.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        m.MenuSelect(menuItem,drawer,getApplicationContext(), 4);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_contact:
                Calendar calendar = Calendar.getInstance();
                int lm = calendar.get(Calendar.HOUR);
                if(lm == hour){
                    toast.showToas(getApplicationContext(), getString(R.string.anti_spam), false);
                    break;
                }
                email = contact_email.getText().toString();
                name = contact_name.getText().toString();
                text = contact_text.getText().toString();

                Pattern pattern = Pattern.compile("^(.+)@(.+)$");
                Matcher matcher = pattern.matcher(email);
                if(email.isEmpty() || !matcher.matches()){
                    toast.showToas(getApplicationContext(),getString(R.string.not_correct_email),false);
                    break;
                }
                if(name.isEmpty()){
                    toast.showToas(getApplicationContext(),getString(R.string.specify_name),false);
                    break;
                }
                if(text.isEmpty()){
                    toast.showToas(getApplicationContext(),getString(R.string.have_not_forgotten_text),false);
                    break;
                }

                new SendEmail().execute();
                break;
            case R.id.go_to_site:
                openUrl("https://search-games.online/");
                break;
            case R.id.insta1:
                openUrl("https://www.instagram.com/coll_otaku/");
                break;
            case R.id.insta2:
                openUrl("https://www.instagram.com/otaku_firstua/");
                break;
            case R.id.telegram1:
                openUrl("https://web.telegram.org/#/im?p=@Coll_Otaku");
                break;
            case R.id.telegram2:
                openUrl("https://web.telegram.org/#/im?p=@OtakuFirstUA");
                break;
            case R.id.faceboock1:
                openUrl("https://www.facebook.com/profile.php?id=100008579443704");
                break;
            case R.id.faceboock2:
                openUrl("https://www.facebook.com/profile.php?id=100014061362322");
                break;
            case R.id.logo:
                drawer.openDrawer(GravityCompat.START);
                break;
        }
    }
    private void openUrl(String s){
        Uri uri = Uri.parse(s);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
    private class SendEmail extends AsyncTask<String,Void,Boolean>{

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                String link = DataBase.HOST+"SendEmailApp.php";

                URL url = new URL(link);
                URLConnection con = url.openConnection();
                HttpURLConnection http = (HttpURLConnection)con;
                http.setRequestMethod("POST");
                http.setDoOutput(true);

                Map<String,String> arguments = new HashMap<>();
                arguments.put("email", email);
                arguments.put("name", name);
                arguments.put("message", text);
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
                if(reader.readLine().equals("true")){
                    return true;
                }else{
                    return false;
                }

            }catch (Exception e){
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                toast.showToas(getApplicationContext(),getString(R.string.sends),true);
            }else{
                toast.showToas(getApplicationContext(),getString(R.string.error),false);
            }
        }
    }
}
