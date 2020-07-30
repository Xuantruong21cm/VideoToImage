package com.example.videotoimage.fragments;

import android.app.Dialog;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.example.videotoimage.R;
import com.example.videotoimage.activity.GalleryActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import wseemann.media.FFmpegMediaMetadataRetriever;


public class VideoProcessing_Fragment extends Fragment {
    VideoView videoView_Play;
    ImageView img_controlVideo,img_closeTimeSnap;
    public static ImageView img_snaped;
    TextView tv_timeStart, tv_timeToltal, tv_videoName, tv_timeSnap;
    SeekBar skb_durationVideo;
    CheckedTextView checked_quickCapture, checked_timeCapture;
    LinearLayout layout_timeSnapControl;
    Bundle bundle;
    String title = "";
    String path = "";
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_video_processing_, container, false);
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
        img_snaped = view.findViewById(R.id.img_snaped);

        checked_quickCapture.setChecked(true);
        layout_timeSnapControl.setVisibility(View.INVISIBLE);
        checked_timeCapture.setChecked(false);
        bundle = getArguments();
        title = bundle.getString("titleVideo");
        path = bundle.getString("pathVideo");
        duration = bundle.getLong("durationVideo");
        Uri uri = Uri.parse(path);
        videoView_Play.setVideoURI(uri);
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        seekBar.setMax((int) duration);
        tv_timeStart.setText(simpleDateFormat.format(videoView.getCurrentPosition()));
        tv_timeToltal.setText(simpleDateFormat.format(duration));
        seekBar.setProgress(videoView.getCurrentPosition());
        tv_videoName.setText(title);

    }

    private void UpdateTimeVideo() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                tv_timeStart.setText(simpleDateFormat.format(videoView_Play.getCurrentPosition()));
                skb_durationVideo.setProgress(videoView_Play.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        }, 100);

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
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                tv_timeStart.setText(simpleDateFormat.format(skb_durationVideo.getProgress()));
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
            }
        });

        checked_quickCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked_timeCapture.setChecked(false);
                checked_quickCapture.setChecked(true);
                checked_quickCapture.setBackgroundResource(R.drawable.chk_select_option);
                layout_timeSnapControl.setVisibility(View.INVISIBLE);
            }
        });
        videoView_Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_controlVideo.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        img_controlVideo.setVisibility(View.GONE);
                    }
                }, 3000);
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

        }

        @Override
        protected Void doInBackground(Void... voids) {
            bitmap = fm.getFrameAtTime(videoView_Play.getCurrentPosition()*1000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
            File filePath = Environment.getExternalStorageDirectory();
            File dir = new File(filePath.getAbsolutePath() + "/VideoToImage/Images");
            dir.mkdirs();
            String name = System.currentTimeMillis() + ".jpg" ;
            File file = new File(dir, name);
            MediaScannerConnection.scanFile(getContext(), new String[]{name}, null, new MediaScannerConnection.OnScanCompletedListener() {
                @Override
                public void onScanCompleted(String path, Uri uri) {

                }
            });
            try {
                outputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
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
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           Glide.with(getContext()).load(bitmap).into(img_snaped);
            videoView_Play.start();
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
                frameList.add(retriever.getFrameAtTime(i*1000000));
            }
            Log.d("jjjj", "onOptionsItemSelected: "+frameList.size());
            for (int i = 0; i <frameList.size() ; i++) {
                bitmapSnap = frameList.get(i) ;
                File filePath = Environment.getExternalStorageDirectory();
                File dir = new File(filePath.getAbsolutePath() + "/VideoToImage/Images");
                dir.mkdirs();
                String name = System.currentTimeMillis() + ".jpg" ;
                File file = new File(dir, name);
                MediaScannerConnection.scanFile(getContext(), new String[]{name}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
                try {
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmapSnap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
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
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}