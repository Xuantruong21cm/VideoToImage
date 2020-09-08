package com.example.videotoimage.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videotoimage.R;
import com.example.videotoimage.adapter.ImageChoice_Adapter;
import com.example.videotoimage.adapter.ImageSlide_Adapter;
import com.example.videotoimage.adapter.SlideFolder_Adapter;
import com.example.videotoimage.interface_.FolderSlide_OnClickListener;
import com.example.videotoimage.interface_.ImageChoice_OnClick;
import com.example.videotoimage.interface_.Images_ClickListener;
import com.example.videotoimage.model.FolderSlide;
import com.example.videotoimage.model.Images;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;


public class SlideShow_Fragment extends Fragment {
    static int count = 0;
    RecyclerView recyclerView_Folder_SlideShow, recyclerView_SlideImage, recyclerView_imageChoice;
    List<FolderSlide> myList;
    SlideFolder_Adapter adapter;
    LinearLayoutManager layoutManager;
    Button btn_clearAll;
    ImageView img_loadingSlide;
    Cursor cursor;
    List<Images> imagesList;
    ImageSlide_Adapter imageSlide_adapter;
    GridLayoutManager gridLayoutManager;
    View view_line, view_line2;
    ImageChoice_Adapter choiceAdapter;
    List<Bitmap> bitList ;
    public static List<String> list_imgChoice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide_show_, container, false);
        recyclerView_Folder_SlideShow = view.findViewById(R.id.recyclerView_Folder_SlideShow);
        recyclerView_SlideImage = view.findViewById(R.id.recyclerView_SlideChoice);
        recyclerView_imageChoice = view.findViewById(R.id.recyclerView_imageChoice);
        btn_clearAll = view.findViewById(R.id.btn_clearAll);
        img_loadingSlide = view.findViewById(R.id.img_loadingSlide);
        view_line = view.findViewById(R.id.view_lineSlide);
        view_line2 = view.findViewById(R.id.view_lineSlide2);
        Glide.with(getActivity().getApplicationContext()).load(R.drawable.loading).into(img_loadingSlide);
        btn_clearAll.setVisibility(View.INVISIBLE);
        imagesList = new ArrayList<>();
        myList = new ArrayList<>();
        list_imgChoice = new ArrayList<>();
        new Doing().execute();
        initListener();
        return view;
    }

    private void initListener() {
        btn_clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_imgChoice.clear();
                choiceAdapter.notifyDataSetChanged();
            }
        });
    }

    private void FilterList() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Set<FolderSlide> folderSet = myList.stream().collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(FolderSlide::getFolderName))));
            myList.clear();
            folderSet.forEach(d -> myList.add(d));
            Log.d("sss", "FilterList: " + myList.toString());
        }
    }

    private void getAllStuff(File file, String name) {
        File list[] = file.listFiles();
        boolean folderFound = false;
        File mfile = null;
        String directoryName = "";

        for (int i = 0; i < list.length; i++) {

            mfile = new File(file, list[i].getName());
            if (mfile.isDirectory()) {
                directoryName = list[i].getName();
                getAllStuff(mfile, directoryName);
            } else {
                if (list[i].getName().toLowerCase(Locale.getDefault()).endsWith(".jpg")) {
                    folderFound = true;
                    File fileVideo = new File(file.toString());
                    File[] listVideo = fileVideo.listFiles();
                    for (File f : listVideo) {
                        count = 0;
                        String nameVideo = f.getName();
                        if (nameVideo.endsWith(".jpg")) {
                            count++;
                            Log.d("xxx", "getAllStuff: " + name + " | " + file.toString());
                        }
                    }
                }
            }
        }

    }

    private void getAllFolder() {
        String root_sd = Environment.getExternalStorageDirectory().toString();
        File file = new File(root_sd);
        getAllStuff(file, "");
    }

    public void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                }
                requestPermission();
            }
        }
    }

    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            // new SelectVideo_Fragment.Doing().execute();
        }
    }

    private class Doing extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            initPermission();

            //     getAllFolder();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getAllFolder();
            FilterList();
            return null;
        }

        @Override
        protected void onPostExecute(Void folders) {
            super.onPostExecute(folders);
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView_Folder_SlideShow.setLayoutManager(layoutManager);
            recyclerView_Folder_SlideShow.setItemAnimator(new DefaultItemAnimator());
            adapter = new SlideFolder_Adapter(myList, getContext());
            recyclerView_Folder_SlideShow.setHasFixedSize(true);
            recyclerView_Folder_SlideShow.setAdapter(adapter);
            btn_clearAll.setVisibility(View.VISIBLE);
            img_loadingSlide.setVisibility(View.INVISIBLE);
            view_line.setVisibility(View.VISIBLE);
            view_line2.setVisibility(View.VISIBLE);
            adapter.FolderSlide_OnClick(new FolderSlide_OnClickListener() {
                @Override
                public void OnClick(FolderSlide folderSlide) {
                    GetImages(folderSlide);
                }
            });
        }
    }

    public void GetImages(FolderSlide folderSlide) {
        imagesList.clear();
        String selection = MediaStore.Images.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%" + folderSlide.getFolderPath() + "%"};
        cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, selection,
                selectionArgs, MediaStore.Images.Media.DATE_TAKEN + " DESC");
        Log.d("eee", "GetVideo: " + cursor.toString());
        cursor.moveToFirst();
        while (cursor != null && cursor.moveToNext()) {
            int title = cursor.getColumnIndex(MediaStore.Images.Media.TITLE);
            int path = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            String titImage = cursor.getString(title);
            String pathImage = cursor.getString(path);
            imagesList.add(new Images(titImage, pathImage));
        }
//        recyclerView_SlideImage.setHasFixedSize(true);
//        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 3);
      //  imageSlide_adapter = new ImageSlide_Adapter(imagesList, getActivity().getApplicationContext());
//        recyclerView_SlideImage.setLayoutManager(gridLayoutManager);
//        recyclerView_SlideImage.setAdapter(imageSlide_adapter);
//        imageSlide_adapter.ImageClickListener(new Images_ClickListener() {
//            @Override
//            public void OnClick(Images images) {
//                list_imgChoice.add(images.getPath());
//                choiceAdapter = new ImageChoice_Adapter(list_imgChoice, getContext());
//                recyclerView_imageChoice.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//                recyclerView_imageChoice.setHasFixedSize(true);
//                recyclerView_imageChoice.setAdapter(choiceAdapter);
//                adapter.notifyItemChanged(list_imgChoice.size());
//                choiceAdapter.ImageChoi_OnClick(new ImageChoice_OnClick() {
//                    @Override
//                    public void OnClick(int positon) {
//                        list_imgChoice.remove(positon);
//                        choiceAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_createslideshow, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_create:
                if (list_imgChoice.size() <= 0) {

                } else {

                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }
}