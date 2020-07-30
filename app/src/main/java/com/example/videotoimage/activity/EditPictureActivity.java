package com.example.videotoimage.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.videotoimage.R;
import com.example.videotoimage.adapter.Emoji_Adapter;
import com.example.videotoimage.interface_.Emoji_OnClickListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import yuku.ambilwarna.AmbilWarnaDialog;

public class EditPictureActivity extends AppCompatActivity {
    PhotoEditorView photoEditorView;
    CropImageView cropImageView;
    String path = "";
    Bundle bundle;
    LinearLayout layout_shareEditPicture, layout_editPicture, layout_deletePicture;
    Typeface mTypeface;
    Typeface mEmojiTypeFace;
    PhotoEditor mPhotoEditor;
    FrameLayout frame_editPicture, layout_addText_Picture, frame_PhotoEditorView, frame_cropImageView, layout_cutControl_Picture;
    ImageView img_closeEdit_Picture, img_colorPick_Paint, img_back_Paint,
            img_colorPick_addText, img_ADD_addText, img_back_addText,
            img_redoEdit_Picture, img_undoEdit_Picture, img_rotate_left, img_flipImageHorizontally, img_flipVertical,
            img_rotate_right, img_back_cutPicture;
    LinearLayout item_cutPicture, item_addtext_Picture,
            item_emoji_Picture, item_paint_Picture, layout_controlPicture,
            layout_controlPaint, item_sticker_Picture;
    SeekBar seekbar_paintSize;
    CheckedTextView img_eraserPick_Paint;
    EditText edt_addtext_Picture;
    BottomSheetDialog bottomSheetDialog;
    RecyclerView recyclerView_emoji;
    List<String> emojisList;
    Emoji_Adapter emoji_adapter;
    Bitmap bitmap = null;
    Bitmap cropped = null;
    OutputStream outputStream;

    int mDefaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_picture);
        initUI();
        getBitmap();
        initListener();
        loadFont();
    }

    private void initUI() {
        img_back_cutPicture = findViewById(R.id.img_back_cutPicture);
        img_rotate_right = findViewById(R.id.img_rotate_right);
        img_flipImageHorizontally = findViewById(R.id.img_flipImageHorizontally);
        img_rotate_left = findViewById(R.id.img_rotate_left);
        img_flipVertical = findViewById(R.id.img_flipVertical);
        layout_cutControl_Picture = findViewById(R.id.layout_cutControl_Picture);
        frame_PhotoEditorView = findViewById(R.id.frame_PhotoEditorView);
        frame_cropImageView = findViewById(R.id.frame_cropImageView);
        edt_addtext_Picture = findViewById(R.id.edt_addtext_Picture);
        layout_addText_Picture = findViewById(R.id.layout_addText_Picture);
        img_back_addText = findViewById(R.id.img_back_addText);
        img_ADD_addText = findViewById(R.id.img_ADD_addText);
        img_colorPick_addText = findViewById(R.id.img_colorPick_addText);
        img_colorPick_Paint = findViewById(R.id.img_colorPick_Paint);
        img_eraserPick_Paint = findViewById(R.id.img_eraserPick_Paint);
        img_back_Paint = findViewById(R.id.img_back_Paint);
        layout_controlPaint = findViewById(R.id.layout_controlPaint);
        seekbar_paintSize = findViewById(R.id.seekbar_paintSize);
        img_redoEdit_Picture = findViewById(R.id.img_redoEdit_Picture);
        img_undoEdit_Picture = findViewById(R.id.img_undoEdit_Picture);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            path = bundle.getString("path");
        }
        photoEditorView = findViewById(R.id.PhotoEditorView);
        photoEditorView.getSource().setImageURI(Uri.parse(path));
        cropImageView = findViewById(R.id.cropImageView);

        layout_shareEditPicture = findViewById(R.id.layout_shareEditPicture);
        layout_editPicture = findViewById(R.id.layout_editPicture);
        layout_deletePicture = findViewById(R.id.layout_deletePicture);

        frame_editPicture = findViewById(R.id.frame_editPicture);
        img_closeEdit_Picture = findViewById(R.id.img_closeEdit_Picture);
        item_cutPicture = findViewById(R.id.item_cutPicture);
        item_addtext_Picture = findViewById(R.id.item_addtext_Picture);
        item_emoji_Picture = findViewById(R.id.item_emoji_Picture);
        item_paint_Picture = findViewById(R.id.item_paint_Picture);
        item_sticker_Picture = findViewById(R.id.item_sticker_Picture);
        layout_controlPicture = findViewById(R.id.layout_controlPicture);

        mDefaultColor = ContextCompat.getColor(EditPictureActivity.this, R.color.design_default_color_primary_dark);

    }

    public Bitmap getBitmap() {
        try {
            File f = new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);

            cropImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
        }
        return bitmap;
    }

    private void loadFont() {
        mTypeface = ResourcesCompat.getFont(getApplicationContext(), R.font.roboto_medium);
        mEmojiTypeFace = ResourcesCompat.getFont(getApplicationContext(), R.font.emojione_android);
        mPhotoEditor = new PhotoEditor.Builder(getApplicationContext(), photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultTextTypeface(mTypeface)
                .setDefaultEmojiTypeface(mEmojiTypeFace).build();
    }

    private void initListener() {
        //Nút Share trong control Picture
        layout_shareEditPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Nút Edit trong control Picture
        layout_editPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_controlPicture.setVisibility(View.INVISIBLE);
                frame_editPicture.setVisibility(View.VISIBLE);
                img_closeEdit_Picture.setVisibility(View.VISIBLE);
                img_undoEdit_Picture.setVisibility(View.VISIBLE);
                img_redoEdit_Picture.setVisibility(View.VISIBLE);
            }
        });

        //Nút Delete trong control Picture
        layout_deletePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Nút Close trong control Picture
        img_closeEdit_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_controlPicture.setVisibility(View.VISIBLE);
                frame_editPicture.setVisibility(View.INVISIBLE);
                img_closeEdit_Picture.setVisibility(View.INVISIBLE);
            }
        });

        //redo edit Picture
        img_undoEdit_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoEditor.undo();
            }
        });

        //undo edit Picture
        img_redoEdit_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoEditor.redo();
            }
        });

        //Nút pain trong edit control
        Pain_Control();

        AddText_Control();

        AddEmoji_Control();

        AddSticker_Control();

        AddCut_Control();

    }

    private void AddCut_Control() {
        item_cutPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frame_PhotoEditorView.setVisibility(View.INVISIBLE);
                frame_cropImageView.setVisibility(View.VISIBLE);
                layout_cutControl_Picture.setVisibility(View.VISIBLE);
                frame_editPicture.setVisibility(View.INVISIBLE);
                img_undoEdit_Picture.setVisibility(View.INVISIBLE);
                img_redoEdit_Picture.setVisibility(View.INVISIBLE);
                img_closeEdit_Picture.setVisibility(View.INVISIBLE);
            }
        });

        img_back_cutPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frame_PhotoEditorView.setVisibility(View.VISIBLE);
                frame_cropImageView.setVisibility(View.INVISIBLE);
                layout_cutControl_Picture.setVisibility(View.INVISIBLE);
                frame_editPicture.setVisibility(View.VISIBLE);
                img_undoEdit_Picture.setVisibility(View.VISIBLE);
                img_redoEdit_Picture.setVisibility(View.VISIBLE);
                img_closeEdit_Picture.setVisibility(View.VISIBLE);
            }
        });

        img_flipImageHorizontally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.flipImageHorizontally();
            }
        });
        img_flipVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.flipImageVertically();
            }
        });

        img_rotate_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(-90);
            }
        });
        img_rotate_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(90);
            }
        });


    }

    private void AddSticker_Control() {
        item_sticker_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void AddEmoji_Control() {
        item_emoji_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EmojiShow().execute();
            }
        });
    }

    public class EmojiShow extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            bottomSheetDialog = new BottomSheetDialog(EditPictureActivity.this);
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_emoji);
            bottomSheetDialog.setCanceledOnTouchOutside(false);
            recyclerView_emoji = bottomSheetDialog.findViewById(R.id.recyclerView_emoji);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            emojisList = PhotoEditor.getEmojis(getApplicationContext());
            emoji_adapter = new Emoji_Adapter(emojisList, getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView_emoji.setVisibility(View.VISIBLE);
            recyclerView_emoji.setLayoutManager(new GridLayoutManager(getApplicationContext(), 7));
            recyclerView_emoji.setAdapter(emoji_adapter);
            bottomSheetDialog.show();
            emoji_adapter.Emoji_OnClick(new Emoji_OnClickListener() {
                @Override
                public void OnClick(int positon) {
                    mPhotoEditor.addEmoji(emojisList.get(positon));
                    bottomSheetDialog.dismiss();
                }
            });
        }
    }

    private void AddText_Control() {
        item_addtext_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_addText_Picture.setVisibility(View.VISIBLE);
                img_closeEdit_Picture.setVisibility(View.INVISIBLE);
                img_redoEdit_Picture.setVisibility(View.INVISIBLE);
                img_undoEdit_Picture.setVisibility(View.INVISIBLE);
                frame_editPicture.setVisibility(View.INVISIBLE);
            }
        });

        img_back_addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_addText_Picture.setVisibility(View.INVISIBLE);
                frame_editPicture.setVisibility(View.VISIBLE);
                img_closeEdit_Picture.setVisibility(View.VISIBLE);
                img_undoEdit_Picture.setVisibility(View.VISIBLE);
                img_redoEdit_Picture.setVisibility(View.VISIBLE);
            }
        });

        img_colorPick_addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultColor = Color.rgb(255, 255, 255);
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(EditPictureActivity.this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mDefaultColor = color;
                    }
                });
                colorPicker.show();
            }
        });
        img_ADD_addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edt_addtext_Picture.getText().toString().trim();
                if (text.equals("")) {

                } else {
                    mPhotoEditor.addText(mTypeface, text, mDefaultColor);
                }
            }
        });
    }

    private void Pain_Control() {
        seekbar_paintSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mPhotoEditor.setBrushSize(seekbar_paintSize.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //Paint Button
        item_paint_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_controlPaint.setVisibility(View.VISIBLE);
                frame_editPicture.setVisibility(View.INVISIBLE);
                img_closeEdit_Picture.setVisibility(View.INVISIBLE);
                img_redoEdit_Picture.setVisibility(View.INVISIBLE);
                img_undoEdit_Picture.setVisibility(View.INVISIBLE);


                mPhotoEditor.setBrushDrawingMode(true);
                mPhotoEditor.setBrushColor(mDefaultColor);
            }
        });

        //Button back in Paint control
        img_back_Paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_controlPaint.setVisibility(View.INVISIBLE);
                frame_editPicture.setVisibility(View.VISIBLE);
                img_closeEdit_Picture.setVisibility(View.VISIBLE);
                img_undoEdit_Picture.setVisibility(View.VISIBLE);
                img_redoEdit_Picture.setVisibility(View.VISIBLE);
            }
        });
        //Button back in Paint control

        img_eraserPick_Paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (img_eraserPick_Paint.isChecked()) {
                    mPhotoEditor.brushEraser();
                } else {
                    mPhotoEditor.setBrushDrawingMode(true);
                    img_eraserPick_Paint.setChecked(true);
                    img_eraserPick_Paint.setBackgroundResource(R.drawable.custom_clickview);
                }
            }
        });

        //Pick Color for Draw Brush Picture
        img_colorPick_Paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(EditPictureActivity.this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        mDefaultColor = color;
                        mPhotoEditor.setBrushColor(mDefaultColor);
                    }
                });
                colorPicker.show();
            }
        });
        //Pick Color for Draw Brush Picture
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                if (frame_PhotoEditorView.getVisibility() == View.INVISIBLE) {
                    cropped = cropImageView.getCroppedImage();
                    File filePath = Environment.getExternalStorageDirectory();
                    File dir = new File(filePath.getAbsolutePath() + "/VideoToImage/Images");
                    dir.mkdirs();
                    String name = System.currentTimeMillis() + ".jpg";
                    File file = new File(dir, name);
                    MediaScannerConnection.scanFile(getApplicationContext(), new String[]{name}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {

                        }
                    });
                    try {
                        outputStream = new FileOutputStream(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    cropped.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
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
                    Toast.makeText(getApplicationContext(), R.string.Save_Image_Complete, Toast.LENGTH_SHORT).show();

                } else {
                    File filePath = Environment.getExternalStorageDirectory();
                    File dir = new File(filePath.getAbsolutePath() + "/VideoToImage/Images/");
                    dir.mkdirs();
                    String name = dir + String.valueOf(System.currentTimeMillis()) + ".jpg";
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermission();
                    } else {
                        mPhotoEditor.saveAsFile(name, new PhotoEditor.OnSaveListener() {
                            @Override
                            public void onSuccess(@NonNull String imagePath) {
                                Log.e("Success", "onSuccess: " + imagePath);
                            }

                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                Log.e("erroFaild", "onFailure: " + exception.getMessage());
                            }
                        });
                        return true;
                    }
                    Toast.makeText(getApplicationContext(), R.string.Save_Image_Complete, Toast.LENGTH_SHORT).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(EditPictureActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            //getAllFolder();
        }
    }

    public void NoClick(View view) {
    }

}