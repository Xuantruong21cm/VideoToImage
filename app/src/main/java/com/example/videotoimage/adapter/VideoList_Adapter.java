package com.example.videotoimage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCodec;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videotoimage.R;
import com.example.videotoimage.interface_.VideoItem_OnClickListener;
import com.example.videotoimage.interface_.Video_OnClickListerner;
import com.example.videotoimage.model.Video;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class VideoList_Adapter extends RecyclerView.Adapter<VideoList_Adapter.ViewHolder> {
    List<Video> list ;
    Context context ;
    VideoItem_OnClickListener onClickListerner ;
    public void Video_OnClick(VideoItem_OnClickListener onClickListerner){
        this.onClickListerner = onClickListerner;
    }

    public VideoList_Adapter(List<Video> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_video,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video video = list.get(position);
        Glide.with(context).load(video.getPath()).into(holder.img_videoIcon);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        holder.tv_videoDuration.setText(simpleDateFormat.format(video.getDuration()));
        holder.tv_titleVideo.setText(video.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListerner.OnClick(video);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_videoIcon ;
        TextView tv_titleVideo ;
        TextView tv_videoDuration ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_videoIcon = itemView.findViewById(R.id.img_videoIcon);
            tv_titleVideo = itemView.findViewById(R.id.tv_titleVideo);
            tv_videoDuration = itemView.findViewById(R.id.tv_videoDuration);
        }
    }
}
