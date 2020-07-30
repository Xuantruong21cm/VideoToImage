package com.example.videotoimage.adapter;

import android.content.Context;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotoimage.R;
import com.example.videotoimage.interface_.Video_OnClickListerner;
import com.example.videotoimage.model.Folder;

import java.util.List;

public class SelectVideo_Adapter extends RecyclerView.Adapter<SelectVideo_Adapter.ViewHolder> {
    Context context;
    List<Folder> list ;
    Video_OnClickListerner onClickListerner ;

    public void Video_OnClick(Video_OnClickListerner onClickListerner){
        this.onClickListerner = onClickListerner;
    }

    public SelectVideo_Adapter(Context context, List<Folder> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
         View view =layoutInflater.inflate(R.layout.item_select_video,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Folder folder = list.get(position);
        holder.tv_nameFolder.setText(folder.getFolderName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListerner.OnClick(folder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_nameFolder ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_nameFolder = itemView.findViewById(R.id.tv_nameFolder);

        }
    }
}
