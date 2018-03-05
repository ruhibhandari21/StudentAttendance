package com.quagnitia.studentattendance.teacher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.quagnitia.studentattendance.R;
import com.quagnitia.studentattendance.Services.AppConstants;
import com.quagnitia.studentattendance.Services.OnTaskCompleted;
import com.quagnitia.studentattendance.Services.WebService;
import com.quagnitia.studentattendance.utils.PreferencesManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

public class UploadTimeTableActivity extends AppCompatActivity implements View.OnClickListener, OnTaskCompleted, AdapterView.OnItemSelectedListener {

    private Button btn_gellery, btn_camera;
    private PreferencesManager preferencesManager;
    private Context mContext;
    private Spinner sp_class;
    private TextView tv_view_all;
    private Uri image_uri;
    private ImageView img_back;
    private String image_path = "";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_time_table);
        mContext = this;
        preferencesManager = PreferencesManager.getInstance(this);
        initUI();
        initListener();
    }

    public void initUI() {
        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_gellery = (Button) findViewById(R.id.btn_gallery);
        img_back = (ImageView) findViewById(R.id.img_back);
        tv_view_all=(TextView) findViewById(R.id.tv_view_all);
    }

    public void initListener() {
        btn_camera.setOnClickListener(this);
        btn_gellery.setOnClickListener(this);
        img_back.setOnClickListener(this);
        tv_view_all.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_view_all:
                Intent in=new Intent(mContext,ViewAllUploads.class);
                startActivity(in);
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_camera:
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent1, REQUEST_CAMERA);
                break;
            case R.id.btn_gallery:
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(
                        Intent.createChooser(intent, "Select File"),
                        SELECT_FILE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);

        }

    }

    private void onCaptureImageResult(Intent data) {
        image_uri = data.getData();
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        //   imageLoader.displayImage("file://" + destination.getAbsolutePath(), circularImageView, imageOptions2);
        image_path = "file://" + destination.getAbsolutePath();
        callUploadImage();
//        circularImageView.setImageBitmap(thumbnail);
    }

    public void callUploadImage() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("uploaded_file", image_path);
        new WebService(this, this, hashMap, "UploadFileWS").execute(AppConstants.BASE_URL + AppConstants.UPLOAD_FILE);
    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        image_uri = selectedImageUri;
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        String selectedImagePath = cursor.getString(column_index);
//        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
//        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        //imageLoader.displayImage("file://" + selectedImagePath, circularImageView, imageOptions2);
        image_path = "file://" + selectedImagePath;
        callUploadImage();
//        circularImageView.setImageBitmap(bm);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onTaskCompleted(JSONObject jsonObject, String result, String TAG) throws Exception {

        if(result.equals(""))
            return;

            Toast.makeText(mContext, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();

    }
}
