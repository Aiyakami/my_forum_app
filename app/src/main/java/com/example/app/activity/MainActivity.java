package com.example.app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.example.app.CustomVideoView;
import com.example.app.R;
import com.example.app.api.ResData;

public class MainActivity extends AppCompatActivity {
    private CustomVideoView customVideoView;

    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //找VideoView控件
        customVideoView = findViewById(R.id.videoview);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.contains("json_data")) {
            String storedJsonData = preferences.getString("json_data", "");
            String token = (String) JSON.parseObject(storedJsonData, ResData.class).getBodyData();
            if(token!=null){
                Intent in = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(in);
            }
        }


        //加载视频文件
        customVideoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.shipin1));
        customVideoView.setOnPreparedListener(mp->mp.setLooping(true));
        customVideoView.start();
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this,LoginActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                in.setClass(MainActivity.this,LoginActivity.class);
                startActivity(in);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, RegisterActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                in.setClass(MainActivity.this,RegisterActivity.class);
                startActivity(in);
            }
        });
    }
}