package com.example.videotoimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videotoimage.R;
import com.example.videotoimage.interface_.ImageChoice_OnClick;

import java.util.List;

public class ImageChoice_Adapter extends RecyclerView.Adapter<ImageChoice_Adapter.ViewHolder> {
    List<String> list ;
    Context context ;
    ImageChoice_OnClick onClick ;
    public void ImageChoi_OnClick (ImageChoice_OnClick onClick){
        this.onClick = onClick ;
    }

    public ImageChoice_Adapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imgslide,parent,false) ;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).trim()).into(holder.img_imageChoice) ;
        holder.img_unChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.OnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_imageChoice, img_unChoice ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_imageChoice = itemView.findViewById(R.id.img_imageChoice) ;
            img_unChoice = itemView.findViewById(R.id.img_unChoice) ;
        }
    }
}
