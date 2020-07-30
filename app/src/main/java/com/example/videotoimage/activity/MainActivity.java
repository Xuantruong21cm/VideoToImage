package com.example.videotoimage.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.videotoimage.R;
import com.example.videotoimage.fragments.SelectVideo_Fragment;
import com.example.videotoimage.fragments.Settings_Fragment;
import com.example.videotoimage.fragments.SlideShow_Fragment;
import com.example.videotoimage.model.Folder;

public class MainActivity extends AppCompatActivity {
    private LinearLayout item_selectVideo, item_gallery,
            item_sildeShowMarker, item_settings, item_share,
            item_rateUs, item_aboutUs;
    // public static List<Folder> myList;

    Folder folder ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
        initUI();
        initListener();

    }

    private void initListener() {
        item_selectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new SelectVideo_Fragment();
                fragmentTransaction.replace(R.id.layout_main,fragment).commit();
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            }
        });

        item_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GalleryActivity.class);
                startActivity(intent);
            }
        });

        item_sildeShowMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new SlideShow_Fragment();
                fragmentTransaction.replace(R.id.layout_main,fragment).commit();
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            }
        });

        item_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new Settings_Fragment();
                fragmentTransaction.replace(R.id.layout_main,fragment).commit();
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
            }
        });

        item_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
         item_rateUs.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         });

         item_aboutUs.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this) ;
                dialog.setContentView(R.layout.dialog_aboutus);
                dialog.setCanceledOnTouchOutside(false);
//                ImageView img_aboutUs = dialog.findViewById(R.id.img_aboutUs) ;
//                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.me);
//                RoundedBitmapDrawable img = RoundedBitmapDrawableFactory.create(getResources(),bitmap) ;
//                img.setCornerRadius(15.0f);
//                img.setAntiAlias(true);
//                img_aboutUs.setImageDrawable(img);

                Button btn_ok_aboutUs = dialog.findViewById(R.id.btn_ok_aboutUs);
                btn_ok_aboutUs.setOnClickListener(view ->{dialog.dismiss();});



                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
             }
         });
    }

    private void initUI() {
        item_selectVideo = findViewById(R.id.item_selectVideo);
        item_gallery = findViewById(R.id.item_gallery);
        item_sildeShowMarker = findViewById(R.id.item_slideShowMarker);
        item_settings = findViewById(R.id.item_setting);
        item_share = findViewById(R.id.item_share);
        item_rateUs = findViewById(R.id.item_rateUS);
        item_aboutUs = findViewById(R.id.item_aboutUs);
//        if (myList != null){}
//        else {
//            myList = new ArrayList<>();
//        }
    }

    public void initPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED){
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){ }else { }
                requestPermission();
            }
        }


    }
    public void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            //getAllFolder();
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
                    }
                }else {
                    Toast.makeText(MainActivity.this,MainActivity.this.getString(R.string.Toast_Permission),Toast.LENGTH_SHORT).show();
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            super.run();
                        try {
                            Thread.sleep(2000);
                        }catch (Exception e){}
                        finally {
                            finish();
                        }
                        }
                    };
                    thread.start();
                }
            }
        }
        return;
    }


    public void NoClick(View view) {
    }
}