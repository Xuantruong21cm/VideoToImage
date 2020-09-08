package com.example.videotoimage.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.videotoimage.R;
import com.example.videotoimage.activity.EditPictureActivity;
import com.example.videotoimage.activity.GalleryActivity;
import com.example.videotoimage.adapter.ImageSnaped_Adapter;
import com.example.videotoimage.adapter.Image_TimeSnap_Adapter;
import com.example.videotoimage.interface_.Images_ClickListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import wseemann.media.FFmpegMediaMetadataRetriever;


public class VideoProcessing_Fragment extends Fragment {
    VideoView videoView_Play;
    ImageView img_controlVideo,img_closeTimeSnap;
    public RecyclerView recyclerView_img_snaped;
    TextView tv_timeStart, tv_timeToltal, tv_videoName, tv_timeSnap;
    SeekBar skb_durationVideo;
    CheckedTextView checked_quickCapture, checked_timeCapture;
    LinearLayout layout_timeSnapControl;
    Bundle bundle;
    String title = "";
    String path = "";
    String nameFile ;
    File file ;
    long duration = 0;
    Uri uri;
    FFmpegMediaMetadataRetriever fm;
    OutputStream outputStream;
    public static Bitmap bitmap, bitmapSnap;
    EditText edt_timeSnap;
    TextView tv_cancel_TimeSnap , tv_ok_TimeSnap ;
    public static int time, timesnap ;
    ArrayList<Bitmap> frameList ;
    MediaMetadataRetriever retriever ;
    SharedPreferences preferences ;
    RecyclerView recyclerView_TimeSnap ;
    List<String> list_TimeSnap ;
    Image_TimeSnap_Adapter adapter ;
    public static List<String> list_imgSnaped ;
    ImageSnaped_Adapter imageSnaped_adapter ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video_processing_, container, false);
        recyclerView_TimeSnap = view.findViewById(R.id.recyclerView_TimeSnap);
        videoView_Play = view.findViewById(R.id.videoView_Play);
        img_controlVideo = view.findViewById(R.id.img_controlVideo);
        tv_timeStart = view.findViewById(R.id.tv_timeStart);
        tv_timeToltal = view.findViewById(R.id.tv_timeToltal);
        tv_videoName = view.findViewById(R.id.tv_videoName);
        skb_durationVideo = view.findViewById(R.id.skb_durationVideo);
        tv_timeSnap = view.findViewById(R.id.tv_timeSnap);
        checked_quickCapture = view.findViewById(R.id.checked_quickCapture);
        checked_timeCapture = view.findViewById(R.id.checked_timeCapture);
        layout_timeSnapControl = view.findViewById(R.id.layout_timeSnapControl);
        recyclerView_img_snaped = view.findViewById(R.id.recycler_img_snaped);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext()) ;
        list_imgSnaped = new ArrayList<>();
        checked_quickCapture.setChecked(true);
        layout_timeSnapControl.setVisibility(View.INVISIBLE);
        checked_timeCapture.setChecked(false);
        bundle = getArguments();
        title = bundle.getString("titleVideo");
        path = bundle.getString("pathVideo");
        duration = bundle.getLong("durationVideo");

        videoView_Play.setVideoURI(Uri.parse(path));
        //videoView_Play.setVideoPath(path);
        videoView_Play.start();
        PlayStatus(img_controlVideo, videoView_Play);
        InfoVideo(skb_durationVideo, videoView_Play);
        UpdateTimeVideo();
        initListener();
        return view;
    }

    private void PlayStatus(ImageView img, VideoView videoView) {
        if (videoView.isPlaying()) {
            img.setImageResource(R.drawable.playvideo);
        } else if (!videoView.isPlaying()) {
            img.setImageResource(R.drawable.pausevideo);
        }
    }

    private void InfoVideo(SeekBar seekBar, VideoView videoView) {
        seekBar.setMax((int) duration);
        tv_timeStart.setText(getTimeString(videoView.getCurrentPosition()));
        tv_timeToltal.setText(getTimeString(duration));
        seekBar.setProgress(videoView.getCurrentPosition());
        tv_videoName.setText(title);

    }

    private void UpdateTimeVideo() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_timeStart.setText(getTimeString(videoView_Play.getCurrentPosition()));
                skb_durationVideo.setProgress(videoView_Play.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        }, 100);

    }
    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf
                .append(String.format("%02d", hours))
                .append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    private void initListener() {
        tv_timeSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_timesnap);
                edt_timeSnap = dialog.findViewById(R.id.edt_timeSnap);
                tv_cancel_TimeSnap = dialog.findViewById(R.id.tv_cancel_TimeSnap);
                tv_ok_TimeSnap = dialog.findViewById(R.id.tv_ok_TimeSnap);
                img_closeTimeSnap = dialog.findViewById(R.id.img_closeTimeSnap);
                dialog.setCanceledOnTouchOutside(false);
                tv_cancel_TimeSnap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                img_closeTimeSnap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tv_ok_TimeSnap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String timeSnap = edt_timeSnap.getText().toString().trim();
                        if (timeSnap.isEmpty()){
                            tv_timeSnap.setText("2");
                        }else {
                            long time = Long.parseLong(timeSnap);
                            if (time*1000 > duration){
                                Toast.makeText(getActivity().getApplicationContext(),getContext().getString(R.string.Video_Length),Toast.LENGTH_SHORT).show();
                            }else {
                                tv_timeSnap.setText(timeSnap);
                            }
                        }
                        dialog.dismiss();
                    }
                });
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });

        skb_durationVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_timeStart.setText(getTimeString(videoView_Play.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView_Play.seekTo(skb_durationVideo.getProgress());
                videoView_Play.start();
            }
        });

        checked_timeCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked_timeCapture.setChecked(true);
                checked_timeCapture.setBackgroundResource(R.drawable.chk_select_option);
                checked_quickCapture.setChecked(false);
                layout_timeSnapControl.setVisibility(View.VISIBLE);
                recyclerView_img_snaped.setVisibility(View.INVISIBLE);
                recyclerView_TimeSnap.setVisibility(View.VISIBLE);
            }
        });

        checked_quickCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView_TimeSnap.setVisibility(View.INVISIBLE);
                recyclerView_img_snaped.setVisibility(View.VISIBLE);
                checked_timeCapture.setChecked(false);
                checked_quickCapture.setChecked(true);
                checked_quickCapture.setBackgroundResource(R.drawable.chk_select_option);
                layout_timeSnapControl.setVisibility(View.INVISIBLE);
            }
        });
        videoView_Play.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                img_controlVideo.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        img_controlVideo.setVisibility(View.GONE);
                    }
                }, 3000);
                return true;
            }
        });
        img_controlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView_Play.isPlaying()) {
                    videoView_Play.pause();
                    img_controlVideo.setImageResource(R.drawable.playvideo);
                } else if (!videoView_Play.isPlaying()) {
                    img_controlVideo.setImageResource(R.drawable.pausevideo);
                    videoView_Play.start();
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_videoprocessing, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_videoFrame:
                if (checked_quickCapture.isChecked()){
                    videoView_Play.pause();
                    new Processing().execute();
                }else {
                    new TimeSnap().execute() ;
                }
                return true;
            case R.id.menu_done:
                Intent intent = new Intent(getContext(), GalleryActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public class Processing extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            uri = Uri.parse(path);
            fm = new FFmpegMediaMetadataRetriever();
            fm.setDataSource(getContext(), uri);
            retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bitmap = retriever.getFrameAtTime((long)videoView_Play.getCurrentPosition()*1000);
            File filePath = Environment.getExternalStorageDirectory();
            File dir = new File(filePath.getAbsolutePath() + "/VideoToImage/Images");
            dir.mkdirs();
            String value = preferences.getString("format","");
            if (value.equalsIgnoreCase("PNG")){
                Log.d("png", "doInBackground: "+preferences.getString(String.valueOf(R.string.File_Format),""));
                nameFile = System.currentTimeMillis() + ".png" ;
            }else {
                Log.d("jpg", "doInBackground: "+preferences.getString(String.valueOf(R.string.File_Format),""));
                nameFile = System.currentTimeMillis() + ".jpg" ;
            }

            file = new File(dir, nameFile);
            try {
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String size = preferences.getString("quality","") ;
            if (size.equalsIgnoreCase("Best")){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            }else if(size.equalsIgnoreCase("Very High")){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            }else if(size.equalsIgnoreCase("High")){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
            }else if (size.equalsIgnoreCase("Medium")){
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            }else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
            }
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            MediaScannerConnection.scanFile(getContext(), new String[]{file.getAbsolutePath(),nameFile}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    Log.d("scan", "onScanCompleted: "+path + " | " + uri.toString());
                }
            });
            list_imgSnaped.add(file.getAbsolutePath());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
//           Glide.with(getContext()).load(bitmap).into(img_snaped);
            imageSnaped_adapter = new ImageSnaped_Adapter(list_imgSnaped,getContext());
            recyclerView_img_snaped.setHasFixedSize(true);
            recyclerView_img_snaped.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
            recyclerView_img_snaped.setItemAnimator(new DefaultItemAnimator());
            recyclerView_img_snaped.setAdapter(imageSnaped_adapter);
            videoView_Play.start();
            imageSnaped_adapter.ClickImage(new Images_ClickListener() {
                @Override
                public void OnClick(int position) {
                    Intent intent = new Intent(getContext(), EditPictureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("path", list_imgSnaped.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
//            img_snaped.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getContext(), EditPictureActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("path", file.getAbsolutePath());
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//            });
        }
    }

    public class TimeSnap extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            retriever = new MediaMetadataRetriever() ;
            try {
                retriever.setDataSource(path) ;
            }catch (Exception e){e.printStackTrace();}

            frameList = new ArrayList<>() ;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int duration_milisec = Integer.parseInt(duration) ;
            int duration_second = duration_milisec/1000 ; //= 20s
            int frame_per_second = Integer.parseInt(tv_timeSnap.getText().toString().trim()) ;
            for (int i = frame_per_second; i <=duration_second ; i = i+frame_per_second) {
                frameList.add(retriever.getFrameAtTime((long)i*1000000));
            }
            Log.d("jjjj", "onOptionsItemSelected: "+frameList.size());
            list_TimeSnap = new ArrayList<>();
            for (int i = 0; i <frameList.size() ; i++) {
                bitmapSnap = frameList.get(i) ;
                File filePath = Environment.getExternalStorageDirectory();
                File dir = new File(filePath.getAbsolutePath() + "/VideoToImage/Images");
                dir.mkdirs();
                String value = preferences.getString("format","");
                if (value.equalsIgnoreCase("PNG")){
                    Log.d("png", "doInBackground: "+preferences.getString(String.valueOf(R.string.File_Format),""));
                    nameFile = System.currentTimeMillis() + ".png" ;
                }else {
                    Log.d("jpg", "doInBackground: "+preferences.getString(String.valueOf(R.string.File_Format),""));
                    nameFile = System.currentTimeMillis() + ".jpg" ;
                }
                File file = new File(dir, nameFile);
                try {
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String size = preferences.getString("quality","") ;
                if (size.equalsIgnoreCase("Best")){
                    bitmapSnap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                }else if(size.equalsIgnoreCase("Very High")){
                    bitmapSnap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                }else if(size.equalsIgnoreCase("High")){
                    bitmapSnap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
                }else if (size.equalsIgnoreCase("Medium")){
                    bitmapSnap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
                }else {
                    bitmapSnap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
                }
                try {
                    outputStream.flush();
                    MediaScannerConnection.scanFile(getContext(), new String[]{file.getAbsolutePath(),nameFile}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list_TimeSnap.add(file.getAbsolutePath());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("lll", "onPostExecute: "+list_TimeSnap.size());
            adapter = new Image_TimeSnap_Adapter(list_TimeSnap,getContext());
            recyclerView_TimeSnap.setHasFixedSize(true);
            recyclerView_TimeSnap.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
            recyclerView_TimeSnap.setItemAnimator(new DefaultItemAnimator());
            recyclerView_TimeSnap.setAdapter(adapter);
            adapter.OnClickImage(new Images_ClickListener() {
                @Override
                public void OnClick(int position) {
                    Intent intent = new Intent(getContext(), EditPictureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("path", list_TimeSnap.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }
}