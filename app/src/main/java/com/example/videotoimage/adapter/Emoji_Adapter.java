package com.example.videotoimage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotoimage.R;
import com.example.videotoimage.interface_.Emoji_OnClickListener;

import java.util.List;

public class Emoji_Adapter extends RecyclerView.Adapter<Emoji_Adapter.ViewHolder>{
    List<String> list ;
    Context context ;
    Emoji_OnClickListener onClickListener ;
    public void Emoji_OnClick (Emoji_OnClickListener onClickListener){
        this.onClickListener = onClickListener ;
    }

    public Emoji_Adapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_emoji.setText(list.get(position));
        holder.tv_emoji.setOnClickListener(new View.OnClickListener() {
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
        TextView tv_emoji ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_emoji = itemView.findViewById(R.id.tv_emoji) ;
        }
    }
}
