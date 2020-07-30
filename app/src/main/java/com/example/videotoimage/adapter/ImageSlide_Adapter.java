package com.example.videotoimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videotoimage.R;
import com.example.videotoimage.interface_.Images_ClickListener;
import com.example.videotoimage.model.Images;

import java.util.List;

public class ImageSlide_Adapter extends RecyclerView.Adapter<ImageSlide_Adapter.ViewHolder> {
    List<Images> list ;
    Context context ;
    Images_ClickListener clickListener ;
    public void ImageClickListener(Images_ClickListener clickListener){
        this.clickListener = clickListener ;
    }

    public ImageSlide_Adapter(List<Images> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Images images =  list.get(position) ;
        Glide.with(context).load(images.getPath()).into(holder.img_imageIcon);
        holder.tv_titleImage.setText(images.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.OnClick(images);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_imageIcon ;
        TextView tv_titleImage ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_imageIcon = itemView.findViewById(R.id.img_imageIcon) ;
            tv_titleImage = itemView.findViewById(R.id.tv_titleImage) ;

        }
    }
}
