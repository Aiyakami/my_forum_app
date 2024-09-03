package com.example.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app.R;
import com.example.app.VideoEntity;
import com.example.app.activity.ContentActivity;
import com.example.app.fragment.MyFragment;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  static Context mContext;
    private List<VideoEntity> datas;
    public  VideoAdapter(Context context, List<VideoEntity> datas){
        this.mContext=context;
        this.datas=datas;

    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder vh = (ViewHolder) holder;
        VideoEntity videoEntity = datas.get(position);
        vh.tvTitle.setText(videoEntity.getTitle());
        vh.tvAuthor.setText(videoEntity.getName());
        vh.tvDz.setText(String.valueOf(videoEntity.getDzCount()));
        vh.tvComment.setText(String.valueOf(videoEntity.getCommentCount()));
        vh.tvCollect.setText(String.valueOf(videoEntity.getCollectCount()));
        if (videoEntity.getImgurl()!=null){
            //vh.img.setImageResource(videoEntity.getImgid());
            Glide.with(mContext).load(videoEntity.getImgurl()).into(vh.img);
        }else{
            vh.img.setVisibility(View.GONE);
            vh.img.getLayoutParams().height =0;
        }
        if (videoEntity.getTouxiang()!=null){
            Glide.with(mContext).load(videoEntity.getTouxiang()).into(vh.touxiang);
        }



    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvDz;
        private TextView tvComment;
        private TextView tvCollect;
        private ImageView img;

        private Button button;

        private ImageView touxiang;//@+id/img_header
        public ViewHolder(@NonNull View view) {
            super(view);
            tvTitle = view.findViewById(R.id.title);
            tvAuthor = view.findViewById(R.id.author);
            tvDz = view.findViewById(R.id.dz);
            tvComment = view.findViewById(R.id.comment);
            tvCollect = view.findViewById(R.id.collect);
            img=view.findViewById(R.id.prepare_view);
            touxiang=view.findViewById(R.id.img_header);
            button = itemView.findViewById(R.id.entry);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(mContext, ContentActivity.class);
                    mContext.startActivity(in);
                }
            });
        }
    }
}
