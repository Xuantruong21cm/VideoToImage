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
import com.example.videotoimage.interface_.Images_ClickListener;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Image_TimeSnap_Adapter extends RecyclerView.Adapter<Image_TimeSnap_Adapter.ViewHolder>{
    List<String> list ;
    Context context ;
    Images_ClickListener clickListener ;

    public Image_TimeSnap_Adapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void OnClickImage (Images_ClickListener clickListener){
        this.clickListener = clickListener ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_timesnap,parent,false) ;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position)).into(holder.img_timesnap) ;
        holder.img_timesnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.OnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_timesnap ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_timesnap = itemView.findViewById(R.id.img_timesnap) ;
//            Collections.sort(list, new Comparator<String>() {
//                @Override
//                public int compare(String o1, String o2) {
//                    return Long.valueOf(o2.replaceAll("\\D","")).compareTo(Long.valueOf(o1.replaceAll("\\D","")));
//                }
//            });
        }
    }
}
