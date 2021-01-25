package com.example.distinguish.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Fragment;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.distinguish.Interface.ImageApi;
import com.example.distinguish.R;
import com.example.distinguish.db.HttpResult;
import com.example.distinguish.db.Rubbish;
import com.example.distinguish.fragment.DistinguishFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//无用
public class TakePhotoActivity extends AppCompatActivity {

    private ImageView photo;
    private ImageView agree;
    private ImageView disagree;
    private Uri uri;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        photo = findViewById(R.id.photo);
        agree = findViewById(R.id.yes);
        disagree = findViewById(R.id.no);

        Integer c = getIntent().getIntExtra("choose",1);
        if(c == 1){
            TakePhotos();
        }else if(c == 2){
            ChoosePhoto();
        }else{
            finish();
        }

        disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rubbish.getInstance().setName("水瓶");
                Rubbish.getInstance().setClassify("可回收垃圾");
                sendHttp();
                finish();
            }
        });
    }

    private void sendHttp() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://recover.market.alicloudapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ImageApi imageApi = retrofit.create(ImageApi.class);
        String body1 = bitmapToBase64(bitmap);
        String appcode = "775c3c9069f74bca84a54782f6e1c5a2";
        Log.d("TAG",bitmapToBase64(bitmap));
        Call<HttpResult> call = null;
        try {
            call = imageApi.getCall(URLEncoder.encode(body1,"UTF-8"),"APPCODE " + appcode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        call.enqueue(new Callback<HttpResult>() {
            @Override
            public void onResponse(Call<HttpResult> call, Response<HttpResult> response) {
                Log.d("66666666666666666", String.valueOf(response.body().getRet()));
                if (response.body().getData() != null){
                    List<HttpResult.DataBean> beans = response.body().getData();
                    List<HttpResult.DataBean.ListBean> list = beans.get(0).getList();
                    for(HttpResult.DataBean.ListBean listBean : list){
                        Log.d("TAG",listBean.getName()+"属于"+listBean.getCategory());
                    }
                }else {
                    Log.d("TAG","2222222222222222222");
                }

            }

            @Override
            public void onFailure(Call<HttpResult> call, Throwable t) {
                Log.d("TAG","发生错误");
            }
        });
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bos);//参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        //转换来的base64码不需要加前缀，必须是NO_WRAP参数，表示没有空格。
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
        //转换来的base64码需要需要加前缀，必须是NO_WRAP参数，表示没有空格。
        //return "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    private void ChoosePhoto() {
        if(ContextCompat.checkSelfPermission(TakePhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(TakePhotoActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
        }else {
            openAlbum();
        }
    }

    private void openAlbum() {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 2:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(TakePhotoActivity.this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void handleImageOnKitKat(Intent data){//（不同类型的图片，要用不同形式的解析方法）
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是document类型的Uri，则通过document id处理(html图片的格式)
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){//其它app下的html图片
                String id=docId.split(":")[1];//解析出数字格式的id
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.example.providers.downloads.documents".equals(uri.getAuthority())){//网络上下载的html图片
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){//其它app下的图片
            //如果是content类型的Uri，则通过普通方式处理
            imagePath=getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){//文件格式的图片
            //如果是file类行的Uri，直接获取图片路径即可
            imagePath=uri.getPath();
        }
        displayImage(imagePath);//根据图片路径显示图片
    }

    public void handleImageBeforeKitKat(Intent data){
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }

    public String getImagePath(Uri uri,String selection){
        String path=null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void displayImage(String imagePath){
        if(imagePath!=null){
            bitmap=BitmapFactory.decodeFile(imagePath);
            photo.setImageBitmap(bitmap);
        }else {
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    private void TakePhotos() {
        File file = new File(getExternalCacheDir(),"distinguish.jpg");
        try {
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= 24){
            uri= FileProvider.getUriForFile(TakePhotoActivity.this,"com.example.temp02.fileprovider",file);
        }else{
            uri=Uri.fromFile(file);
        }

        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    try {
                        bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        photo.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }else{
                    finish();
                }
                break;
            case 2:
                if(resultCode==RESULT_OK){
                    //判断手机版本号再对图片进行不同的解析
                    if(Build.VERSION.SDK_INT>=19){
                        //4.4以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }else {
                        //4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }else{
                    finish();
                }
                break;
            default:
        }
    }
}