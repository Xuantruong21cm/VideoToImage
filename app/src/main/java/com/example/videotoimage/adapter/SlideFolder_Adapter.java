package com.example.videotoimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotoimage.R;
import com.example.videotoimage.interface_.FolderSlide_OnClickListener;
import com.example.videotoimage.model.FolderSlide;

import java.util.List;

public class SlideFolder_Adapter extends RecyclerView.Adapter<SlideFolder_Adapter.ViewHolder> {
    List<FolderSlide> list ;
    Context context ;
    FolderSlide_OnClickListener onClickListener ;

    public void FolderSlide_OnClick(FolderSlide_OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public SlideFolder_Adapter(List<FolderSlide> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder,parent,false) ;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FolderSlide  folderSlide = list.get(position) ;
        holder.tv_folderSlide.setText(folderSlide.getFolderName());
        holder.itemView.setBackgroundResource(R.drawable.custom_clickview);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    onClickListener.OnClick(folderSlide);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_folderSlide ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_folderSlide = itemView.findViewById(R.id.tv_folderSlide) ;
        }
    }
}
