package com.example.app.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.app.api.Http;
import com.example.app.R;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangeActivity extends AppCompatActivity {


    private Uri selectedImageUri=null;
    private Button upload;
    private Button change;

    private ImageView imageViewAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);


        change = this.findViewById(R.id.change1);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发包修改
                if(selectedImageUri!=null){
                    sendimg(selectedImageUri);
                }else{
                    finish();
                    Toast.makeText(ChangeActivity.this, "上传成功，等待刷新", Toast.LENGTH_SHORT).show();

                }


            }
        });
        upload = this.findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //上传头像
                // 打开本地图片选择器
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //设置分隔符
        String boundary = "*****";


        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageViewAvatar.setImageURI(selectedImageUri);

        }
    }

    public void  sendimg(Uri img){
        ContentResolver contentResolver = getContentResolver();
        if(img==null){
            Toast.makeText(ChangeActivity.this, "尚未选择图片", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // 通过URI获取图像数据流
            InputStream inputStream = contentResolver.openInputStream(img);
            String url="http://47.120.45.216:8088/api/user/upload/avatar";

            Http.doPost(url,"file", inputStream, Http.getToken(this), new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String res = response.body().string();
                    System.out.println();

                }
            });

        } catch (IOException e) {
            // 发生异常时的处理
            e.printStackTrace();  // 打印异常信息
            // 进行其他错误处理，比如提示用户或记录日志
        }
    }

}