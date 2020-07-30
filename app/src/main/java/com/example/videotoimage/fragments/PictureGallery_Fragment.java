package com.example.videotoimage.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotoimage.R;
import com.example.videotoimage.activity.EditPictureActivity;
import com.example.videotoimage.adapter.ImageSlide_Adapter;
import com.example.videotoimage.interface_.Images_ClickListener;
import com.example.videotoimage.model.Images;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PictureGallery_Fragment extends Fragment {
    RecyclerView recyclerView_pictureGallary ;
    Cursor cursor ;
    List<Images> imagesList ;
    ImageSlide_Adapter adapter ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_picture_gallery_, container, false);
        imagesList = new ArrayList<>();
        recyclerView_pictureGallary = view.findViewById(R.id.recyclerView_pictureGallary);
        GetPictures();
        recyclerView_pictureGallary.setHasFixedSize(true);
        recyclerView_pictureGallary.setLayoutManager(new GridLayoutManager(getContext(),3));
        adapter = new ImageSlide_Adapter(imagesList,getContext());
        recyclerView_pictureGallary.setAdapter(adapter);
        adapter.ImageClickListener(new Images_ClickListener() {
            @Override
            public void OnClick(Images images) {
                Intent intent = new Intent(getContext(), EditPictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("path",images.getPath());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return  view ;
    }

    private void GetPictures() {
        File filepath = Environment.getExternalStorageDirectory() ;
        String pathFolder = filepath.getAbsoluteFile() + "/VideoToImage/Images" ;
        String selection = MediaStore.Images.Media.DATA + " like?" ;
        String[] selectionArgs = new String[]{"%" + pathFolder + "%"};
        cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection,
                selectionArgs, MediaStore.Images.Media.DATE_TAKEN + " DESC");
        Log.d("eee", "GetVideo: " + cursor.toString());
        cursor.moveToFirst();
        while ( cursor != null &&  cursor.moveToNext() ) {
            int title = cursor.getColumnIndex(MediaStore.Images.Media.TITLE);
            int path = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            String titImage = cursor.getString(title);
            String pathImage = cursor.getString(path);
            imagesList.add(new Images(titImage, pathImage));
        }
    }
}