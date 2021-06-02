package com.example.searchgame2.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.searchgame2.ForNetWorck;
import com.example.searchgame2.Login;
import com.example.searchgame2.R;



public class LoginActivity extends AppCompatActivity {

    ForNetWorck netWorck = new ForNetWorck();
    EditText InputEmail;
    EditText InputPassword;
    Button Login_bOk;
    Button b_forgot;
    Button b_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InputEmail = (EditText)findViewById(R.id.InputEmail);
        InputPassword = (EditText)findViewById(R.id.InputPassword);
        Login_bOk = (Button)findViewById(R.id.Login_bOk);
        b_forgot = (Button)findViewById(R.id.b_forgot);
        b_register = (Button)findViewById(R.id.b_register);

        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.Login_bOk:
                        if(netWorck.isCon(getApplicationContext())) {
                            new Login(getApplicationContext(), String.valueOf(InputEmail.getText()),
                                    String.valueOf(InputPassword.getText()), true).execute();
                        }
                        break;
                    case R.id.b_forgot:
                        if(netWorck.isCon(getApplicationContext())) {
                            Uri uri = Uri.parse("https://search-games.online/index.php?do=lostpassword");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                        break;
                    case R.id.b_register:
                        if(netWorck.isCon(getApplicationContext())) {
                            Uri uri1 = Uri.parse("https://search-games.online/index.php?do=register");
                            Intent intent1 = new Intent(Intent.ACTION_VIEW, uri1);
                            startActivity(intent1);
                        }
                        break;
                }
            }
        };
        Login_bOk.setOnClickListener(l);
        b_forgot.setOnClickListener(l);
        b_register.setOnClickListener(l);

    }

}
