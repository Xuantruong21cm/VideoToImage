package com.example.videotoimage.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotoimage.R;
import com.example.videotoimage.adapter.SelectVideo_Adapter;
import com.example.videotoimage.interface_.Video_OnClickListerner;
import com.example.videotoimage.model.Folder;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static android.os.Environment.DIRECTORY_MOVIES;


public class SelectVideo_Fragment extends Fragment {
    static int count = 0;
    List<Folder> myList ;
    RecyclerView recyclerView ;
    LinearLayoutManager layoutManager ;
    SelectVideo_Adapter adapter ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment__select_video, container, false);
        myList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView_SelectVideo);
        new Doing().execute();
        return view;
    }
    private void FilterList(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Set<Folder> folderSet = myList.stream().collect(Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Folder::getFolderName))));
            myList.clear();
            folderSet.forEach(d -> myList.add(d));
        }
    }
    public void initPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED){
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){}else { }
                requestPermission();
            }
        }


    }
    public void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
//            getAllFolder();z
            new Doing().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1: {
                if (grantResults.length > 0 ){
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                            && grantResults[2] == PackageManager.PERMISSION_GRANTED){
                        new Doing().execute();
                    }
                }else {
                    Toast.makeText(getActivity(),getActivity().getString(R.string.Toast_Permission),Toast.LENGTH_SHORT).show();
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(2000);
                            }catch (Exception e){}
                            finally {
                                getActivity().finish();
                            }
                        }
                    };
                    thread.start();
                }
            }
        }
        return;
    }

    private void getAllStuff(File file, String name){
        File list[] = file.listFiles();
        boolean folderFound = false;
        File mfile = null;
        String directoryName = "" ;

        for (int i = 0; i < list.length ; i++) {

            mfile = new File(file,list[i].getName());
            if (mfile.isDirectory()){
                directoryName = list[i].getName();
                getAllStuff(mfile,directoryName);
            } else {
                if (list[i].getName().endsWith(".mp4") || list[i].getName().endsWith(".avi")
                || list[i].getName().endsWith(".flv") || list[i].getName().endsWith(".mov") || list[i].getName().endsWith(".wmv")){
                    folderFound = true;
                }
            }
        }
        if (folderFound){
            myList.add(new Folder(name,file.getAbsolutePath(),count));
        }

    }
    private void getAllFolder(){
        if (Build.VERSION.SDK_INT <29){
            File file = Environment.getExternalStorageDirectory();
            getAllStuff(file,"");
        }else if (Build.VERSION.SDK_INT >= 29){
            File file = getContext().getExternalFilesDir(DIRECTORY_MOVIES);
            getAllStuff(file,"");
        }
//        String root_sd = Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES).toString();
//        File file = new File(root_sd);
//        getAllStuff(file,"");
    }

    private class Doing extends AsyncTask<Void, Void, Void>{
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
            layoutManager = new LinearLayoutManager(getContext());
            adapter = new SelectVideo_Adapter(getContext(),myList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
//            recyclerView.setDrawingCacheEnabled(true);
//            recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.Video_OnClick(new Video_OnClickListerner() {
                @Override
                public void OnClick(Folder folder) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Fragment fragment = new VideoList_Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("path",folder.getFolderPath());
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.layout_selectVideo,fragment).commit();
                    fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                }
            });
        }
    }
}