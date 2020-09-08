package com.example.videotoimage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCodec;
import android.media.MediaPlayer;
import android.net.Uri;
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
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class VideoList_Adapter extends RecyclerView.Adapter<VideoList_Adapter.ViewHolder> {
    Context context ;
    List<File> listVideo ;
    VideoItem_OnClickListener onClickListerner ;
    public void Video_OnClick(VideoItem_OnClickListener onClickListerner){
        this.onClickListerner = onClickListerner;
    }

    public VideoList_Adapter(Context context, List<File> listVideo) {
        this.context = context;
        this.listVideo = listVideo;
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
//        Video video = list.get(position);
//        Glide.with(context).load(video.getPath()).into(holder.img_videoIcon);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
//        holder.tv_videoDuration.setText(simpleDateFormat.format(video.getDuration()));
//        holder.tv_titleVideo.setText(video.getTitle());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onClickListerner.OnClick(video);
//            }
//        });

        final Uri uri = Uri.fromFile(listVideo.get(position)) ;
        Glide.with(context).load(uri).into(holder.img_videoIcon);
        final String path = listVideo.get(position).getAbsolutePath() ;
        final Uri uri1 = uri.parse(path);
        holder.tv_titleVideo.setText(listVideo.get(position).getName());

        try {
            int duration = MediaPlayer.create(context, uri1).getDuration() /1000 ;
            holder.tv_videoDuration.setText(""+getTime(duration));
        }catch (Exception e){}

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onClickListerner.OnClick(position);
            }
        });
    }
    public String getTime(int second){
        int hr = second/3600 ;
        int rem = second % 3600 ;
        int mn = rem /60 ;
        int sec = rem % 60 ;
        if (hr < 1){
            return  String.format("%02d",mn) + ":"+ String .format("%02d",sec);
        }else {
            return String.format("%02d",hr) + ":" + String.format("%02d",mn) + ":" + String.format("%02d",sec);
        }

    }

    @Override
    public int getItemCount() {
        return listVideo.size();
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
