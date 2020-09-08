package com.example.videotoimage.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.videotoimage.R;

import java.io.File;


public class ViewPicture_Edited extends AppCompatActivity {
    private ImageView img_picture_edited ;
    Bitmap bitmap = null ;
    String image ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_picture__edited);

        img_picture_edited = findViewById(R.id.img_picture_edited) ;
//        byte[] bytes = getIntent().getExtras().getByteArray("images") ;
//        if (bytes != null){
////            image = getIntent().getStringExtra("image") ;
//            bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//            img_picture_edited.setImageBitmap(bitmap);
//        }else {
           image = getIntent().getStringExtra("image");
            Glide.with(getApplicationContext()).load(image).into(img_picture_edited);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_edited, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_share_edited :
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*") ;
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(image)));
                startActivity(Intent.createChooser(share,"Share via"));
                break;
            case R.id.menu_home :
                Intent intent = new Intent(ViewPicture_Edited.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}