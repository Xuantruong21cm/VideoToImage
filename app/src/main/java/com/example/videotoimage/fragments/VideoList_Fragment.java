package com.example.videotoimage.fragments;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotoimage.R;
import com.example.videotoimage.adapter.VideoList_Adapter;
import com.example.videotoimage.interface_.VideoItem_OnClickListener;
import com.example.videotoimage.model.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class VideoList_Fragment extends Fragment {
    String path = "";
    Cursor cursor;
    List<Video> videoList;
    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    VideoList_Adapter adapter;
    List<File> list ;
    Uri uri = null ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_list_, container, false);
        videoList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView_VideoList);
        new Doing().execute();

        return view;
    }

    public void GetVideo() {
//        String selection = MediaStore.Video.Media.DATA + " like?";
//        String[] selectionArgs = new String[]{"%" + path + "%"};
//        cursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, selection,
//                selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");
//        Log.d("eee", "GetVideo: " + cursor.toString());
//        cursor.moveToFirst();
//
//        while (cursor != null && cursor.moveToNext()) {
//            int title = cursor.getColumnIndex(MediaStore.Video.Media.TITLE);
//            int duration = cursor.getColumnIndex(MediaStore.Video.Media.DURATION);
//            int path = cursor.getColumnIndex(MediaStore.Video.Media.DATA);
//
//            String titVideo = cursor.getString(title);
//            long durationVideo = cursor.getLong(duration);
//            String pathVideo = cursor.getString(path);
//            videoList.add(new Video(pathVideo, titVideo, durationVideo));
//        }
        try {
            list = new ArrayList<>();
            File file = new File(path);
            File[] files = file.listFiles();
            if (files != null && file.length() > 0) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().endsWith(".mp4") || files[i].getName().endsWith(".avi")
                            || files[i].getName().endsWith(".flv") || files[i].getName().endsWith(".mov") || files[i].getName().endsWith(".wmv")) {
                        this.list.add(files[i]);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public class Doing extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Bundle bundle = getArguments();
            path = bundle.getString("path");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            GetVideo();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            layoutManager = new GridLayoutManager(getContext(), 2);
            adapter = new VideoList_Adapter(getContext(), list);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemViewCacheSize(20);
            recyclerView.setAdapter(adapter);
            adapter.Video_OnClick(new VideoItem_OnClickListener() {
                @Override
                public void OnClick(int position) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    Fragment fragment = new VideoProcessing_Fragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("titleVideo", list.get(position).getName());
                    bundle.putString("pathVideo", list.get(position).getAbsolutePath());
                    uri = Uri.parse(list.get(position).getAbsolutePath()) ;
                    try {
                        long duration = MediaPlayer.create(getContext(), uri).getDuration();
                        bundle.putLong("durationVideo",duration);
                    }catch (Exception e){}
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.layout_selectVideo, fragment).commit();
                    fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                }
            });
        }
    }
}