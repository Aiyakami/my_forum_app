package com.example.app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.app.R;
import com.example.app.VideoEntity;
import com.example.app.adapter.VideoAdapter;
import com.example.app.api.ApiUrl;
import com.example.app.api.Http;
import com.example.app.api.ResData;
import com.example.app.api.Vo.ArticleListVo;
import com.flyco.tablayout.SlidingTabLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class HomeFragment extends Fragment {


    private SwipeRefreshLayout swipeRefreshLayout;


    private RecyclerView recyclerView;
    List<VideoEntity> datas = new ArrayList<>();

    private VideoAdapter videoAdapter;
    public Boolean flag = Boolean.FALSE;
    private ViewPager viewPager;
    private SlidingTabLayout slidingTabLayout;

    public HomeFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//获得布局管理器 从activity借到
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //此处应道接收网络数据进行渲染

        if (!datas.isEmpty()) {
            videoAdapter = new VideoAdapter(getActivity(), datas);
            recyclerView.setAdapter(videoAdapter);
            return v;
        } else {
            Toast.makeText(getContext(), "正在努力获取数据", Toast.LENGTH_SHORT).show();
        }
        //发送网络请求，假设返回res  注意此处返回最新的5条
        Http.doGET(ApiUrl.Article_list + "?pageNum=1&pageSize=5", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                ResData resData = JSON.parseObject(res, ResData.class);
                List<ArticleListVo> articleListVos = JSON.parseArray((String) resData.getBodyData(), ArticleListVo.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //资源替换
                        for (ArticleListVo articleListVo : articleListVos) {

                            VideoEntity ve = new VideoEntity();
                            ve.setTitle(articleListVo.getTitle());
                            ve.setName(articleListVo.getNickName());
                            ve.setTouxiang(articleListVo.getAvatar());
                            ve.setDzCount(0);
                            ve.setCollectCount(0);
                            ve.setCommentCount(0);
                            ve.setImg(R.drawable.temp1);
                            datas.add(ve);

                        }
                        videoAdapter = new VideoAdapter(getActivity(), datas);
                        recyclerView.setAdapter(videoAdapter);
                        Toast.makeText(getContext(), "获取成功", Toast.LENGTH_SHORT).show();
//                        for(int i=0;i<1;i++){
//                            VideoEntity ve = new VideoEntity();
//                            ve.setTitle("震惊，大学生半夜两点不睡觉，是道德的沦丧...");
//                            ve.setName("明月网络安全官方");
//                            ve.setDzCount(i*2);
//                            ve.setCollectCount(i*4);
//                            ve.setCommentCount(i*6);
//                            ve.setImg(R.drawable.temp1);
//                            datas.add(ve);
//                            VideoEntity ve1 = new VideoEntity();
//                            ve1.setTitle("疑似胡宇轩深夜EMO文案爆出...");
//                            ve1.setName("明月网络安全官方");
//                            ve1.setDzCount(i*2);
//                            ve1.setCollectCount(i*4);
//                            ve1.setCommentCount(i*6);
//                            ve1.setImg(R.drawable.temp5);
//                            datas.add(ve1);
//                            VideoEntity ve2 = new VideoEntity();
//                            ve2.setTitle("网络安全就业形式大好，就业多就业广");
//                            ve2.setName("明月网络安全官方");
//                            ve2.setDzCount(i*2);
//                            ve2.setCollectCount(i*4);
//                            ve2.setCommentCount(i*6);
//                            ve2.setImg(R.drawable.temp3);
//                            datas.add(ve2);
//                            VideoEntity ve3 = new VideoEntity();
//                            ve3.setTitle("EDG 5:26不敌LOUD，胡宇轩半夜流泪");
//                            ve3.setName("明月网络安全官方");
//                            ve3.setDzCount(i*2);
//                            ve3.setCollectCount(i*4);
//                            ve3.setCommentCount(i*6);
//                            ve3.setImg(R.drawable.temp2);
//                            datas.add(ve3);
//                            VideoEntity ve4 = new VideoEntity();
//                            ve4.setTitle("新版本大耳朵图图即将上线");
//                            ve4.setName("明月网络安全官方");
//                            ve4.setDzCount(i*2);
//                            ve4.setCollectCount(i*4);
//                            ve4.setCommentCount(i*6);
//                            ve4.setImg(R.drawable.temp4);
//                            datas.add(ve4);
//                        }

                    }
                });

                System.out.println();
            }
        });

        swipeRefreshLayout = v.findViewById(R.id.swipe_refresh_layout);
        // 设置上拉刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 在这里执行上拉刷新时的操作，比如从网络获取最新数据
                send();


            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    void send() {
        Http.doGET(ApiUrl.Article_list + "?pageNum=1&pageSize=5", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "获取失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String res = response.body().string();
                ResData resData = JSON.parseObject(res, ResData.class);
                List<ArticleListVo> articleListVos = JSON.parseArray((String) resData.getBodyData(), ArticleListVo.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        datas.clear();
                        //资源替换
                        for (ArticleListVo articleListVo : articleListVos) {
                            VideoEntity ve = new VideoEntity();
                            ve.setTitle(articleListVo.getTitle());
                            ve.setName(articleListVo.getNickName());
                            ve.setTouxiang(articleListVo.getAvatar());
                            ve.setDzCount(0);
                            ve.setCollectCount(0);
                            ve.setCommentCount(0);
                            ve.setImg(R.drawable.temp1);
                            datas.add(ve);
                        }
                        videoAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getContext(), "获取成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return;
    }
}

