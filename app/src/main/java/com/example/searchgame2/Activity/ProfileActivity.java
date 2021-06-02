package com.example.searchgame2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.searchgame2.Adapters.SelectedAdapter;
import com.example.searchgame2.CastomToast;
import com.example.searchgame2.DataBase;
import com.example.searchgame2.ForNetWorck;
import com.example.searchgame2.GetUser;
import com.example.searchgame2.Login;
import com.example.searchgame2.Menu;
import com.example.searchgame2.R;
import com.example.searchgame2.EditProfile;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout refresh;
    DataBase dataBase;
    SQLiteDatabase database2;
    Cursor cursor;
    ForNetWorck netWorck = new ForNetWorck();
    CircleImageView img_profile;
    ConstraintLayout edit_profil_container;

    EditText edit_name;
    EditText edit_email;
    EditText edit_city;
    EditText edit_old_password;
    EditText edit_new_password;
    EditText edit_repeat_password;
    EditText edit_baned_to_ip;
    EditText edit_myself;
    EditText edit_singature;
    TextView name_profile;
    TextView countComents;
    TextView countPublishing;
    TextView user_info;
    TextView is_select_file;
    TextView banet_to_ip_text;
    Button b_Exsite;
    Button b_editProfile;
    Button b_file_select;
    Button save_editProfile;
    Button sendEmail;
    Button sendPM;
    CheckBox dont_get_letter;
    CheckBox unsubscribe_from_materials;
    CheckBox delete_avatar;
    ImageView viewSelected;
    ImageView backgroundProfile;

    RecyclerView SelectedList;
    ArrayList<Integer> arrayList = new ArrayList<>();
    Intent intent = new Intent();

    DrawerLayout drawer;
    Menu m = new Menu();
    CastomToast toast = new CastomToast();

    String userId;
    String email;
    String password;
    String selectedImagePath = "";
    String UserId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        refresh = (SwipeRefreshLayout)findViewById(R.id.refresh);
        refresh.setColorSchemeResources(R.color.colorRed);
        refresh.setProgressBackgroundColorSchemeResource(R.color.colorButton);
        refresh.setOnRefreshListener(this);
        edit_profil_container = (ConstraintLayout)findViewById(R.id.edit_profil_container);
        img_profile = (CircleImageView) findViewById(R.id.img_profile);
        backgroundProfile = (ImageView)findViewById(R.id.backgroundProfile);

        countComents = (TextView)findViewById(R.id.countComents);
        countPublishing = (TextView)findViewById(R.id.countPublishing);
        user_info = (TextView)findViewById(R.id.user_info);
        name_profile = (TextView)findViewById(R.id.name_profile);
        is_select_file = (TextView)findViewById(R.id.is_select_file);
        banet_to_ip_text = (TextView)findViewById(R.id.banet_to_ip_text);

        edit_name = (EditText)findViewById(R.id.edit_name);
        edit_email = (EditText)findViewById(R.id.edit_email);
        edit_city = (EditText)findViewById(R.id.edit_city);
        edit_old_password = (EditText)findViewById(R.id.edit_old_password);
        edit_new_password = (EditText)findViewById(R.id.edit_new_password);
        edit_repeat_password = (EditText)findViewById(R.id.edit_repeat_password);
        edit_baned_to_ip = (EditText)findViewById(R.id.edit_baned_to_ip);
        edit_myself = (EditText)findViewById(R.id.edit_myself);
        edit_singature = (EditText)findViewById(R.id.edit_singature);
        b_Exsite = (Button)findViewById(R.id.b_Exsite);
        b_editProfile = (Button)findViewById(R.id.b_editProfile);
        b_file_select = (Button)findViewById(R.id.b_file_select);
        save_editProfile = (Button)findViewById(R.id.save_editProfile);
        sendEmail = (Button)findViewById(R.id.sendEmail);
        sendPM = (Button)findViewById(R.id.sendPM);
        dont_get_letter = (CheckBox)findViewById(R.id.dont_get_letter);
        unsubscribe_from_materials = (CheckBox)findViewById(R.id.unsubscribe_from_materials);
        delete_avatar = (CheckBox)findViewById(R.id.delete_avatar);
        viewSelected = (ImageView)findViewById(R.id.viewSelected);

        SelectedList = (RecyclerView)findViewById(R.id.SelectedList);
        SelectedList.setLayoutManager(new LinearLayoutManager(this));

        drawer = findViewById(R.id.drawer_layout);
        final NavigationView menu = (NavigationView) findViewById(R.id.nav_view);
        menu.setNavigationItemSelectedListener(this);

        Bundle bundle = getIntent().getExtras();
        UserId = bundle.getString("USER_ID");

        if(UserId.equals("0")) {
            dataBase = new DataBase(getApplicationContext());
            database2 = dataBase.getWritableDatabase();
            insertData(false);
            syncProfile();
        }else{
            sendEmail.setVisibility(View.VISIBLE);
            sendPM.setVisibility(View.VISIBLE);
            viewSelected.setVisibility(View.GONE);
            b_editProfile.setVisibility(View.GONE);
            b_Exsite.setVisibility(View.GONE);
            new GetUser(getApplicationContext(),UserId,img_profile,backgroundProfile,name_profile,countPublishing,countComents,user_info).execute();
        }

        ImageView logo = (ImageView)findViewById(R.id.logo);

        b_Exsite.setOnClickListener(this);
        b_editProfile.setOnClickListener(this);
        b_file_select.setOnClickListener(this);
        save_editProfile.setOnClickListener(this);
        viewSelected.setOnClickListener(this);
        sendEmail.setOnClickListener(this);
        sendPM.setOnClickListener(this);
        logo.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_Exsite:
                dataBase.DesLogin(database2,getApplicationContext());
                finish();
                overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
                break;
            case R.id.b_editProfile:
                if(netWorck.isCon(getApplicationContext())){
                    if (edit_profil_container.getVisibility() == View.VISIBLE) {
                        edit_profil_container.setVisibility(View.GONE);
                    } else {
                        edit_profil_container.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.b_file_select:
                if (Build.VERSION.SDK_INT <= 19) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(i, 10);
                } else if (Build.VERSION.SDK_INT > 19) {
                    if(Build.VERSION.SDK_INT > 22) checkPermissions();
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 10);
                }
                break;
            case R.id.viewSelected:
                if(arrayList.get(0).equals(0) || arrayList.isEmpty()){
                    toast.showToas(getApplicationContext(),getString(R.string.not_found),false);
                    break;
                }
                if(SelectedList.getVisibility() == View.VISIBLE) {
                    SelectedList.setVisibility(View.GONE);
                }else{
                    SelectedList.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.sendEmail:
                intent.setClass(getApplicationContext(), MessageActivity.class);
                StringTokenizer stringTokenizer = new StringTokenizer(name_profile.getText().toString());
                String name = stringTokenizer.nextToken();
                name = stringTokenizer.nextToken();
                intent.putExtra("USER_ID",name);
                intent.putExtra("IS_PM",0);
                startActivity(intent);
                break;
            case R.id.sendPM:
                intent.setClass(getApplicationContext(), MessageActivity.class);
                StringTokenizer stringTokenizer1 = new StringTokenizer(name_profile.getText().toString());
                String name1 = stringTokenizer1.nextToken();
                name1 = stringTokenizer1.nextToken();
                intent.putExtra("USER_ID",name1);
                intent.putExtra("IS_PM",1);
                startActivity(intent);
                break;
            case R.id.save_editProfile:
                Pattern pattern = Pattern.compile("^(.+)@(.+)$");
                String postEmail = edit_email.getText().toString();
                Matcher matcher = pattern.matcher(postEmail);
                if(!postEmail.isEmpty() && !matcher.matches()){
                    toast.showToas(getApplicationContext(),getString(R.string.not_correct_email),false);
                    break;
                }
                String Isdont_get_letter = "0";
                if(dont_get_letter.isChecked()){
                    Isdont_get_letter = "1";
                }
                String Isunsubscribe_from_materials = "0";
                if(unsubscribe_from_materials.isChecked()){
                    Isunsubscribe_from_materials = "1";
                }
                String Isdelete_avatar = "0";
                if(delete_avatar.isChecked()){
                    Isdelete_avatar = "1";
                }
                String oldPassword = edit_old_password.getText().toString();
                String newPassword = edit_new_password.getText().toString();
                String repeat_password = edit_repeat_password.getText().toString();
                cursor = database2.rawQuery("SELECT "+dataBase.PASSWORD+" FROM " + dataBase.TABLE_PROFILE, null);
                cursor.moveToFirst();
                if(!oldPassword.isEmpty() && !oldPassword.equals(cursor.getString(cursor.getColumnIndex(dataBase.PASSWORD)))){
                    toast.showToas(getApplicationContext(),getString(R.string.print_old_password),false);
                    break;
                }
                cursor.close();
                if(!newPassword.isEmpty() && !newPassword.equals(repeat_password)){
                    toast.showToas(getApplicationContext(),getString(R.string.repeat_password),false);
                    break;
                }
                String codeImage = "";
                String type = "";
                if(!selectedImagePath.isEmpty()) {
                    File file = new File(selectedImagePath);
                    byte[] fileByte = new byte[(int) file.length()];
                    if (file.length() <= 1048576) {
                        try {
                            FileInputStream f = new FileInputStream(file);
                            f.read(fileByte);
                        } catch (Exception e) {
                            toast.showToas(getApplicationContext(),getString(R.string.not_found_file),false);
                            break;
                        }
                    } else {
                        toast.showToas(getApplicationContext(),getString(R.string.size_image_big),false);
                        break;
                    }
                    codeImage = Base64.encodeToString(fileByte, Base64.DEFAULT);
                    StringTokenizer st = new StringTokenizer(selectedImagePath, ".");
                    while (st.hasMoreTokens()){
                        type = st.nextToken();
                    }
                }
                new EditProfile(getApplicationContext(), userId, edit_name.getText().toString(), postEmail, edit_city.getText().toString(),
                        newPassword, edit_baned_to_ip.getText().toString(), codeImage, edit_myself.getText().toString(),
                        edit_singature.getText().toString(), type, Isdont_get_letter, Isunsubscribe_from_materials, Isdelete_avatar
                ).execute();
                edit_profil_container.setVisibility(View.GONE);
                syncProfile();
                break;
            case R.id.logo:
                drawer.openDrawer(GravityCompat.START);
                break;
        }
    }
    public String UserGrupe(int i){
        String [] grup = getResources().getStringArray(R.array.arrGrupe);
        return grup[i-1];
    }
    private void insertData(boolean isReset){
        try {
            cursor = database2.rawQuery("SELECT * FROM " + dataBase.TABLE_PROFILE, null);
            cursor.moveToFirst();
            if(isReset) {
                Picasso.with(getApplicationContext()).load("https:" + cursor.getString(cursor.getColumnIndex(dataBase.PHOTO)))
                    .placeholder(R.drawable.load).error(R.drawable.profileicon).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).into(img_profile);
            }else{
                Picasso.with(getApplicationContext()).load("https:" + cursor.getString(cursor.getColumnIndex(dataBase.PHOTO)))
                    .placeholder(R.drawable.profileicon).error(R.drawable.profileicon).into(img_profile);
            }
            String linc_to_background = cursor.getString(cursor.getColumnIndex(dataBase.LINC_TO_BACKGROUND));
            if(!linc_to_background.equals("0")){
                Picasso.with(getApplicationContext()).load(linc_to_background).fit().into(backgroundProfile);
            }
            name_profile.setText(getString(R.string.user) + ": " + cursor.getString(cursor.getColumnIndex(dataBase.NAME)) + "\n"
                    + getString(R.string.grup) + ": " + UserGrupe(cursor.getInt(cursor.getColumnIndex(dataBase.USER_GRUP))));
            countComents.setText(cursor.getString(cursor.getColumnIndex(dataBase.COUNT_COMENTS)) + "\n" + getString(R.string.setsComents));
            countPublishing.setText(cursor.getString(cursor.getColumnIndex(dataBase.COUNT_PUBLISHING)) + "\n" + getString(R.string.count_publishing));
            user_info.setText(getString(R.string.red_date)+": "+cursor.getString(cursor.getColumnIndex(dataBase.REG_DATE))+"\n\n"
            +getString(R.string.last_date)+": "+cursor.getString(cursor.getColumnIndex(dataBase.LAST_DATE))+"\n\n"
            +getString(R.string.your_email)+": "+cursor.getString(cursor.getColumnIndex(dataBase.EMAIL))+"\n\n"
            +getString(R.string.full_name)+": "+cursor.getString(cursor.getColumnIndex(dataBase.FULL_NAME))+"\n\n"
            +getString(R.string.myself)+": "+cursor.getString(cursor.getColumnIndex(dataBase.INFO))+"\n");
            userId = cursor.getString(cursor.getColumnIndex(dataBase.ID));
            email = cursor.getString(cursor.getColumnIndex(dataBase.EMAIL));
            password = cursor.getString(cursor.getColumnIndex(dataBase.PASSWORD));
            banet_to_ip_text.setText(getString(R.string.banes_to_ip)+cursor.getString(cursor.getColumnIndex(dataBase.IP))+" )");
            StringTokenizer st = new StringTokenizer(cursor.getString(cursor.getColumnIndex(dataBase.FAVORITES)));
            if(!arrayList.isEmpty()){
                arrayList.clear();
            }
            while (st.hasMoreTokens()){
                arrayList.add(Integer.parseInt(st.nextToken()));
            }
            if(!arrayList.get(0).equals(0)) {
                SelectedList.setAdapter(new SelectedAdapter(ProfileActivity.this, arrayList,Integer.parseInt(userId)));
            }
            cursor.close();
        }catch (Exception e){
            dataBase.DesLogin(database2,getApplicationContext());
            finish();
            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
        }
    }
    private void syncProfile(){
        if(netWorck.isCon(getApplicationContext())) {
            new Login(getApplicationContext(), email, password, false).execute();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    insertData(true);
                }
            }, 5 * 1000);
        }
    }
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1052);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            selectedImagePath = getRealPathFromURI(selectedImageUri);
            is_select_file.setText(selectedImagePath);
        }
    }
    public String getRealPathFromURI(Uri uri) {
        if (uri == null) return null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = ProfileActivity.this.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;
        }
        return uri.getPath();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        m.MenuSelect(menuItem,drawer,getApplicationContext(), 0);
        return true;
    }

    @Override
    public void onRefresh() {
        if(!UserId.equals("0")){
            new GetUser(getApplicationContext(),UserId,img_profile,backgroundProfile,name_profile,countPublishing,countComents,user_info).execute();
        }else {
            syncProfile();
        }
        refresh.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        if(SelectedList.getVisibility() == View.VISIBLE){
            SelectedList.setVisibility(View.GONE);
        }else if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_start, R.anim.activity_out);
        }
    }

}
