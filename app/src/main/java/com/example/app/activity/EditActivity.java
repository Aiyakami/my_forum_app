package com.example.app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app.R;
import com.example.app.api.ApiUrl;
import com.example.app.api.Http;
import com.example.app.api.Vo.Article;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EditActivity extends AppCompatActivity {

    //控件变量

    private Button send;
    private EditText title;

    private EditText content;

    void finished(){
        finish();
        Toast.makeText(EditActivity.this, "上传成功，等待刷新", Toast.LENGTH_SHORT).show();
        return;
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        send = findViewById(R.id.send_button);
        title = findViewById(R.id.title_edittext);
        content = findViewById(R.id.content_edittext);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = title.getText().toString();
                String contentText = content.getText().toString();
                Article article = new Article(titleText, contentText);

                if(!titleText.isEmpty()&&!contentText.isEmpty()){
                    // 在这里实现发包  发送 文章标题 文章内容
                    Http.doPost(ApiUrl.Article_Add, "json", article, Http.getToken(getApplicationContext()), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {

                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            System.out.println();
                        }
                    });
                    finished();
                }else{
                    Toast.makeText(EditActivity.this, "标题和内容不能为空", Toast.LENGTH_SHORT).show();
                }

            }

        });




    }
}