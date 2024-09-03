package com.example.app.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.app.R;
import com.example.app.VideoEntity;
import com.example.app.adapter.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment {

    private String title;
    public VideoFragment() {
    }
    public static VideoFragment newInstance(String title) {
        VideoFragment fragment = new VideoFragment();
        fragment.title=title;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_video, container, false);
        RecyclerView recyclerView=v.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());//获得布局管理器 从activity借到
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //此处应道接收网络数据进行渲染
        List<VideoEntity> datas = new ArrayList<>();
        for(int i=0;i<8;i++){
            VideoEntity ve = new VideoEntity();
            ve.setTitle("震惊");
            ve.setName("大胃王");
            ve.setDzCount(i*2);
            ve.setCollectCount(i*4);
            ve.setCommentCount(i*6);
            datas.add(ve);
        }
        VideoAdapter videoAdapter=new VideoAdapter(getActivity(),datas);
        recyclerView.setAdapter(videoAdapter);
        return v;
    }
}