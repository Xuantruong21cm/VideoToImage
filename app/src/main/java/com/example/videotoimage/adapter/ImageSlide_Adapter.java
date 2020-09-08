package com.example.videotoimage.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videotoimage.R;
import com.example.videotoimage.fragments.PictureGallery_Fragment;
import com.example.videotoimage.interface_.Images_ClickListener;
import com.example.videotoimage.model.Images;

import java.io.File;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ImageSlide_Adapter extends RecyclerView.Adapter<ImageSlide_Adapter.ViewHolder> {
    List<File> list;
    Context context;
    public static List<File> getList;
    Images_ClickListener clickListener;

    public void ImageClickListener(Images_ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ImageSlide_Adapter(List<File> list, Context context) {
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
        getList = new ArrayList<>();
//        Images images = list.get(position);
        final Uri uri = Uri.fromFile(list.get(position));
        Glide.with(context).load(uri).into(holder.img_imageIcon);
        holder.tv_titleImage.setText(list.get(position).getName());
        holder.img_imageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.OnClick(position);
            }
        });

        holder.img_imageIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.img_multi_Picture.setVisibility(View.VISIBLE);
                holder.itemView.setClickable(false);
                getList.add(list.get(position));
                holder.itemView.setSelected(!holder.img_imageIcon.isSelected());
                PictureGallery_Fragment.tv_selectImage.setText("Select " + getList.size() + " image");
                PictureGallery_Fragment.layout_control_Picture_Gallery.setVisibility(View.VISIBLE);
                return true;
            }
        });


        holder.img_multi_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList.remove(list.get(position));
                PictureGallery_Fragment.tv_selectImage.setText("Select " + getList.size() + " image");
                holder.img_multi_Picture.setVisibility(View.INVISIBLE);
                if (getList.size() == 0) {
                    PictureGallery_Fragment.layout_control_Picture_Gallery.setVisibility(View.INVISIBLE);
                }
                holder.itemView.setEnabled(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_imageIcon, img_multi_Picture;
        TextView tv_titleImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_multi_Picture = itemView.findViewById(R.id.img_multi_Picture);
            img_imageIcon = itemView.findViewById(R.id.img_imageIcon);
            tv_titleImage = itemView.findViewById(R.id.tv_titleImage);
            Collections.sort(list, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return Long.valueOf(o2.lastModified()).compareTo(o1.lastModified());
                }
            });
        }
    }
}
