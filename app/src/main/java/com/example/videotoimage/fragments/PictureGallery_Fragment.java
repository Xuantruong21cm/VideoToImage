package com.example.videotoimage.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotoimage.R;
import com.example.videotoimage.activity.EditPictureActivity;
import com.example.videotoimage.adapter.ImageSlide_Adapter;
import com.example.videotoimage.interface_.Images_ClickListener;
import com.example.videotoimage.model.Images;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class PictureGallery_Fragment extends Fragment {
    public RecyclerView recyclerView_pictureGallary;
    Cursor cursor;
    public static List<File> imagesList;
    public static ImageSlide_Adapter adapter;
    public  static FrameLayout layout_control_Picture_Gallery ;
    public static TextView tv_selectImage ;
    public ImageView img_reload_Choice ,img_share_ListChoice,
            img_delete_ListChoice,img_close_Coltrol_Picture_Gallery;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture_gallery_, container, false);
        imagesList = new ArrayList<>();
        img_close_Coltrol_Picture_Gallery = view.findViewById(R.id.img_close_Coltrol_Picture_Gallery) ;
        img_reload_Choice= view.findViewById(R.id.img_reload_Choice) ;
        img_delete_ListChoice = view.findViewById(R.id.img_delete_ListChoice) ;
        img_share_ListChoice = view.findViewById(R.id.img_share_ListChoice) ;
        tv_selectImage = view.findViewById(R.id.tv_selectImage);
        layout_control_Picture_Gallery = view.findViewById(R.id.layout_control_Picture_Gallery);
        recyclerView_pictureGallary = view.findViewById(R.id.recyclerView_pictureGallary);
        GetPictures();
        recyclerView_pictureGallary.setHasFixedSize(true);
        recyclerView_pictureGallary.setItemViewCacheSize(20);
        recyclerView_pictureGallary.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new ImageSlide_Adapter(imagesList, getContext());
        recyclerView_pictureGallary.setAdapter(adapter);
        adapter.ImageClickListener(new Images_ClickListener() {
            @Override
            public void OnClick(int position) {
                Intent intent = new Intent(getContext(), EditPictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("path", imagesList.get(position).getAbsolutePath());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        img_delete_ListChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesList.removeAll(ImageSlide_Adapter.getList);
                for (int i = 0; i <ImageSlide_Adapter.getList.size() ; i++) {
                    File file = new File(ImageSlide_Adapter.getList.get(i).getPath());
                    file.delete();
                    if (file.exists()){
                        try {
                            file.getCanonicalFile().delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (file.exists()){
                            getActivity().getApplicationContext().deleteFile(file.getAbsolutePath());
                        }
                    }
                    CallBroadcast(file);
                }
                ImageSlide_Adapter.getList.clear();
                adapter.notifyDataSetChanged();
                layout_control_Picture_Gallery.setVisibility(View.INVISIBLE);
            }
        });
        img_reload_Choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageSlide_Adapter.getList.clear();
                recyclerView_pictureGallary.setAdapter(null);
                recyclerView_pictureGallary.setLayoutManager(null);
                recyclerView_pictureGallary.setAdapter(adapter);
                recyclerView_pictureGallary.setLayoutManager(new GridLayoutManager(getContext(), 3));
               adapter.notifyDataSetChanged();
               tv_selectImage.setText("Select " + ImageSlide_Adapter.getList.size() + " image");
            }
        });

        img_close_Coltrol_Picture_Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               recyclerView_pictureGallary.setAdapter(null);
                recyclerView_pictureGallary.setLayoutManager(null);
                recyclerView_pictureGallary.setAdapter(adapter);
              recyclerView_pictureGallary.setLayoutManager(new GridLayoutManager(getContext(), 3));
                adapter.notifyDataSetChanged();
                layout_control_Picture_Gallery.setVisibility(View.INVISIBLE);
            }
        });

        img_share_ListChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                ArrayList<Uri> uris =new ArrayList<>();
                for (File file : ImageSlide_Adapter.getList){
                    uris.add(Uri.fromFile(file));
                }

                Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
                share.setType("image/*") ;
                share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                startActivity(Intent.createChooser(share,"Share via"));
            }
        });

        return view;
    }
    public void CallBroadcast(File file){
        if (Build.VERSION.SDK_INT <= 14){
            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(file.getAbsolutePath())));
        }else {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            getContext().sendBroadcast(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void GetPictures() {
//        File filepath = Environment.getExternalStorageDirectory();
//        String pathFolder = filepath.getAbsoluteFile() + "/VideoToImage/Images";
//        String selection = MediaStore.Images.Media.DATA + " like?";
//        String[] selectionArgs = new String[]{"%" + pathFolder + "%"};
//        cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection,
//                selectionArgs, MediaStore.Images.Media.DATE_TAKEN + " DESC");
//        Log.d("eee", "GetVideo: " + cursor.toString());
//        cursor.moveToFirst();
//        while (cursor != null && cursor.moveToNext()) {
//            int title = cursor.getColumnIndex(MediaStore.Images.Media.TITLE);
//            int path = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//
//            String titImage = cursor.getString(title);
//            String pathImage = cursor.getString(path);
//            imagesList.add(new Images(titImage, pathImage));
//        }

        try {
            imagesList = new ArrayList<>();
            File filepath = Environment.getExternalStorageDirectory();
            String pathFolder = filepath.getAbsoluteFile() + "/VideoToImage/Images";
            File file = new File(pathFolder);
            File[] files = file.listFiles();
            if (files != null && file.length() > 0) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().endsWith(".jpg") || files[i].getName().endsWith(".png")) {
                        this.imagesList.add(files[i]);
                    }
                }
            }
//            this.imagesList.sort((d1,d2) -> d2.compareTo(d1));
        } catch (Exception e) {
        }
    }
    public void Back(){
        getActivity().onBackPressed();
    }
}