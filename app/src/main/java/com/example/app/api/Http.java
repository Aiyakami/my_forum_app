package com.example.app.api;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import okhttp3.*;
import okio.ByteString;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;

public class Http {
    public static void doGET(String url,  Callback callback)  {
        OkHttpClient client=createClient();
        Request.Builder builder=defaultBuild();
        Request request=builder.url(url).build();
        response(client,request,callback);
    }

    public static void doGET(String url, HashMap<String,String> headers,Callback callback) {
        OkHttpClient client=createClient();
        Request.Builder builder=defaultBuild();
        builder=setHeaders(builder,headers);
        Request request=builder.url(url).build();
        response(client,request,callback);
     }


    public static void doPost(String url,String type,Object data,Callback callback)  {
        OkHttpClient client=createClient();
        Request.Builder builder=setReqBody(defaultBuild(),type,data);
        Request request=builder.url(url).build();
        response(client,request,callback);
    }

    public static void doPost(String url,String type,Object data,HashMap<String,String> headers,Callback callback)  {
        OkHttpClient client=createClient();
        Request.Builder builder=setReqBody(setHeaders(defaultBuild(), headers),type,data);
        Request request=builder.url(url).build();
        response(client,request,callback);
    }


    private static   Request.Builder setHeaders(Request.Builder builder, HashMap<String, String> headers){
        for (Map.Entry<String,String> header:headers.entrySet()) {
            builder=builder.addHeader(header.getKey(),header.getValue());
        }
        return builder;
    }

    private static OkHttpClient createClient(){
        return new OkHttpClient();
    }

    private static Request.Builder defaultBuild(){
        Request.Builder builder= new Request.Builder();
        builder=builder.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        return builder;
    }

    private static void response(OkHttpClient client,Request request,Callback callback)  {
        client.newCall(request).enqueue(callback);
        close(client);
    }



    private static Request.Builder setReqBody(Request.Builder builder, String type, Object body)  {
        RequestBody requestBody=null;
        if(type.equals("json")){
            requestBody = RequestBody.create(
                    MediaType.parse("application/json"), JSON.toJSONString(body));
        } else if (type.equals("form")) {
            //pass
            requestBody = RequestBody.create(
                    MediaType.parse("application/json"), JSON.toJSONString(body));
        } else if (type.equals("file")) {
            if(body instanceof File){
                File file=(File) body;
                requestBody =new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file",file.getName(),
                                RequestBody.create(MediaType.parse("application/octet-stream"),file))
                        .build();
            }
            if(body instanceof InputStream){
                InputStream in= (InputStream) body;
                try {
                    ByteString byteString = ByteString.read(in, in.available());
                    requestBody= new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("file", UUID.randomUUID()+".png",
                                    RequestBody.create(MediaType.parse("image/png"), byteString))
                            .build();
                } catch (IOException e) {
                   e.printStackTrace();
                }

            }else {
                //pass
            }
        }
        builder=builder.post(requestBody);
        return builder;
    }




    private static void close(OkHttpClient client){
        client.dispatcher().executorService().shutdown();
    }



    public static HashMap<String,String> getToken(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String storedJsonData = preferences.getString("json_data", "");
        String token = (String) JSON.parseObject(storedJsonData, ResData.class).getBodyData();
        HashMap<String, String> header = new HashMap<>();
        header.put("Token",token);
        return header;
    }
}

