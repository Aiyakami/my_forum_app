package com.example.app.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.activity.ChangeActivity;
import com.example.app.activity.EditActivity;
import com.example.app.activity.MainActivity;
import com.example.app.api.Http;
import com.example.app.api.ResData;
import com.example.app.api.User;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {



    public static String name=null;
    public static String imgurl=null;

    private Button btn;
    private Button changebtn;
    private Button Editbtn;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_my, container, false);
        //为按钮绑定点击事件  退出登陆
        btn = v.findViewById(R.id.quit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除token

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                if (preferences.contains("json_data")) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("json_data");
                    editor.apply();
                }
                //跳转到登陆页面
                Intent in = new Intent(getActivity(), MainActivity.class);
                startActivity(in);
            }
        });

        changebtn = v.findViewById(R.id.change);
        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //跳转到登陆页面
                Intent in = new Intent(getActivity(), ChangeActivity.class);
                startActivity(in);
            }
        });

        Editbtn = v.findViewById(R.id.fatie);
        Editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //跳转到登陆页面
                Intent in = new Intent(getActivity(), EditActivity.class);
                startActivity(in);
            }
        });


        //需要发包获取头像和名字
        String url="http://47.120.45.216:8088/api/user/info";
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String storedJsonData = preferences.getString("json_data", "");
        String token = (String) JSON.parseObject(storedJsonData, ResData.class).getBodyData();
        HashMap<String, String> header = new HashMap<>();
        header.put("Token",token);



        if(MyFragment.name!=null){
            TextView tv=v.findViewById(R.id.myname);
            ImageView im=v.findViewById(R.id.img_header);
            if (MyFragment.name!=null){
                tv.setText(MyFragment.name);
            }
            if (MyFragment.imgurl!=null){
                Glide.with(getActivity()).load(MyFragment.imgurl).into(im);
            }
        }else{
            Http.doGET(url, header, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException  {

                    String res = response.body().string();
                    User user;
                    //User user= (User) JSON.parseObject(res, ResData.class).getBodyData();
                    ResData resData = JSON.parseObject(res, ResData.class);
                    if (resData.isFlag()) {
                        user = new User();
                        JSONObject bodyData = (JSONObject) JSONObject.parse(resData.getBodyData());
                        user.setAvatar((String) bodyData.get("avatar"));
                        user.setNickName((String) bodyData.get("nickName"));
                        MyFragment.name= user.getNickName();
                        MyFragment.imgurl= user.getAvatar();
                    } else {
                        // resData 为 null，处理解析失败的情况
                        System.out.println("Error: Failed to parse response data");
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //资源替换
                            TextView tv=v.findViewById(R.id.myname);
                            ImageView im=v.findViewById(R.id.img_header);
                            if (MyFragment.name!=null){
                                tv.setText(MyFragment.name);
                            }
                            if (MyFragment.imgurl!=null){
                                Glide.with(getActivity()).load(MyFragment.imgurl).into(im);
                            }


                        }
                    });

                }


            });

        }




        return v;
    }
}