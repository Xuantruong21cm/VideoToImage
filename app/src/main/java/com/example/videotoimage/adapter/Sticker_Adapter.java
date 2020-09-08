package com.example.videotoimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotoimage.R;
import com.example.videotoimage.interface_.Sticker_OnClickListener;

import java.util.List;

public class Sticker_Adapter extends RecyclerView.Adapter<Sticker_Adapter.ViewHolder> {
    List<Integer> list ;
    Context context ;
    Sticker_OnClickListener onClickListener ;

    public Sticker_Adapter(List<Integer> list, Context context) {
        this.list = list;
        this.context = context;
    }
    public void Sticker_OnClick (Sticker_OnClickListener onClickListener){
        this.onClickListener = onClickListener ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.img_sticker.setImageResource(list.get(position));
        holder.img_sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.OnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_sticker ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_sticker =  itemView.findViewById(R.id.img_sticker);
        }
    }
}
