package com.example.app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.app.api.CrashHandler;
import com.example.app.api.Http;
import com.example.app.R;
import com.example.app.api.ResData;
import com.example.app.api.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private Button btnBigLogin;
    private EditText account;
    private EditText pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        setContentView(R.layout.activity_login);
        account=findViewById(R.id.et_account);
        pwd=findViewById(R.id.et_pwd);
        btnBigLogin = findViewById(R.id.btn_big_login);
        btnBigLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(LoginActivity.this,"检测输入",Toast.LENGTH_SHORT).show();
                String Account = account.getText().toString().trim();
                String Pwn = pwd.getText().toString().trim(); //trim方法去掉前后空格
                login(Account,Pwn);
            }
        });
    }

    private void login(String account, String pwd) {

        if (account == null || account.length() <= 0) {
            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();//android系统提示框，类似于windows的messagebox
            return;
        }
        if (pwd == null || account.length() <= 0) {
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();//android系统提示框，类似于windows的messagebox
            return;
        }
        String host = "http://47.120.45.216:8088/api/user/login";
        Http.doPost(host, "json", new User(account, pwd), new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                //Toast.makeText(LoginActivity.this, "网络请求错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res=response.body().string();
                //Toast.makeText(LoginActivity.this, "接收返回体", Toast.LENGTH_SHORT).show();
                System.out.println(res);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("json_data", res);
                editor.apply();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        String storedJsonData = preferences.getString("json_data", "");
                        String token = (String) JSON.parseObject(storedJsonData, ResData.class).getBodyData();

                        if(token!=null){

                            Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.setClass(LoginActivity.this,HomeActivity.class);
                            startActivity(in);
                            Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        });



    }
}