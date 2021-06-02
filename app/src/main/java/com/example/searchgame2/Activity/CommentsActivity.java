package com.example.searchgame2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.searchgame2.CastomToast;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.GetComments;
import com.example.searchgame2.Menu;
import com.example.searchgame2.OBJComments;
import com.example.searchgame2.R;
import com.example.searchgame2.SetComments;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.Calendar;

public class CommentsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout refresh;
    RecyclerView recyclerView;
    ConstraintLayout container_rev;
    EditText rev_name;
    EditText rev_mail;
    EditText text_rev;
    EditText editTextComments;
    Button addReviews;
    Button b_ok_rev;
    ArrayList<OBJComments> arrayList = new ArrayList<>();
    int id;
    DataBase dataBase;

    String postId;
    String userId;
    String autor;
    String email;
    String text;
    String isRegister;

    DrawerLayout drawer;
    Menu m = new Menu();
    CastomToast toast = new CastomToast();
    int minutes = 0;
    int getUserId;
    boolean isProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        refresh = (SwipeRefreshLayout)findViewById(R.id.refresh);
        refresh.setColorSchemeResources(R.color.colorRed);
        refresh.setProgressBackgroundColorSchemeResource(R.color.colorButton);
        refresh.setOnRefreshListener(this);
        recyclerView = (RecyclerView)findViewById(R.id.CommentsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editTextComments = (EditText)findViewById(R.id.editTextComments);
        b_ok_rev = (Button) findViewById(R.id.b_ok_rev);
        container_rev = (ConstraintLayout) findViewById(R.id.container_rev);
        rev_name = (EditText)findViewById(R.id.rev_name);
        rev_mail = (EditText)findViewById(R.id.rev_mail);
        text_rev = (EditText)findViewById(R.id.text_rev);
        addReviews = (Button)findViewById(R.id.addReviews);
        drawer = findViewById(R.id.drawer_layout);
        final NavigationView menu = (NavigationView) findViewById(R.id.nav_view);
        menu.setNavigationItemSelectedListener(this);

        Bundle b = getIntent().getExtras();
        id = b.getInt("ID_REVIEWS");

        new GetComments(CommentsActivity.this,id,arrayList,recyclerView,container_rev,editTextComments,b_ok_rev).execute();
        try {
            dataBase = new DataBase(getApplicationContext());
            SQLiteDatabase database2 = dataBase.getWritableDatabase();
            Cursor cursor = database2.rawQuery("SELECT " + dataBase.ID + ", " + dataBase.EMAIL + ", " + dataBase.NAME + " FROM " + dataBase.TABLE_PROFILE, null);
            cursor.moveToFirst();
            getUserId = cursor.getInt(cursor.getColumnIndex(dataBase.ID));
            autor = cursor.getString(cursor.getColumnIndex(dataBase.NAME));
            email = cursor.getString(cursor.getColumnIndex(dataBase.EMAIL));
            isProfile = true;
            rev_name.setVisibility(View.GONE);
            rev_mail.setVisibility(View.GONE);
        } catch (Exception e) {
            isProfile = false;
        }

        ImageView logo = (ImageView)findViewById(R.id.logo);
        addReviews.setOnClickListener(this);
        logo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addReviews:
                Calendar calendar = Calendar.getInstance();
                int lm = calendar.get(Calendar.MINUTE);
                if(lm == minutes){
                    toast.showToas(getApplicationContext(), getString(R.string.anti_spam), false);
                    break;
                }
                minutes = lm;
                text = String.valueOf(text_rev.getText());
                if(isProfile){
                    if(getUserId != 0 && !text.isEmpty()) {
                        postId = String.valueOf(id);
                        userId = String.valueOf(getUserId);
                        isRegister = "1";
                        new SetComments(getApplicationContext(), postId, userId, autor, email, text, isRegister).execute();
                        if (!arrayList.isEmpty()) {
                            arrayList.clear();
                        }
                        new GetComments(CommentsActivity.this, id, arrayList, recyclerView, container_rev, editTextComments, b_ok_rev).execute();
                    }else{
                        toast.showToas(getApplicationContext(), getString(R.string.error), false);
                    }
                }else{
                    autor = String.valueOf(rev_name.getText());
                    email = String.valueOf(rev_mail.getText());
                    if(!autor.isEmpty() && !email.isEmpty() && !text.isEmpty()){
                        postId = String.valueOf(id);
                        userId = String.valueOf(0);
                        isRegister = "0";
                        new SetComments(getApplicationContext(), postId, userId, autor, email, text, isRegister).execute();
                        if (!arrayList.isEmpty()) {
                            arrayList.clear();
                        }
                        new GetComments(CommentsActivity.this, id, arrayList, recyclerView, container_rev, editTextComments, b_ok_rev).execute();
                    }else{
                        toast.showToas(getApplicationContext(), getString(R.string.error), false);
                    }
                }
                break;
            case R.id.logo:
                drawer.openDrawer(GravityCompat.START);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        m.MenuSelect(menuItem,drawer,getApplicationContext(), 0);
        return true;
    }

    @Override
    public void onRefresh() {
        if (!arrayList.isEmpty()) {
            arrayList.clear();
        }
        new GetComments(CommentsActivity.this,id,arrayList,recyclerView,container_rev,editTextComments,b_ok_rev).execute();
        refresh.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        if(container_rev.getVisibility() == View.VISIBLE){
            container_rev.setVisibility(View.GONE);
        }else if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
        }
    }
}
