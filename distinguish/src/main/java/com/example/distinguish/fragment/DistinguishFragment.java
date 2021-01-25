package com.example.distinguish.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.example.distinguish.ButtomDialog;
import com.example.distinguish.DownloadDialog;
import com.example.distinguish.Interface.ImageApi;
import com.example.distinguish.Interface.StringApi;
import com.example.distinguish.R;
import com.example.distinguish.RecordView;
import com.example.distinguish.db.ASRresponse;
import com.example.distinguish.db.HttpResult;
import com.example.distinguish.db.HttpResult1;
import com.example.distinguish.db.Rubbish;
import com.example.distinguish.db.Waste;
import com.google.gson.Gson;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import db.EventMessage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import static android.app.Activity.RESULT_OK;

@Route(path = "/distinguish/fragment")
public class DistinguishFragment extends Fragment implements EventListener{

    private String[] strings = new String[]{"水果","纸盒","水杯","作业本","塑料袋","电池","剩菜剩饭","废弃药品","家养绿植","中药材","水银温度计","金属刀"};
    View view;
    private ButtomDialog dialog;
    //final int[] index = {0};
    private EditText editText;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private TextView text6;
    private TextView text7;
    private TextView text8;
    private TextView text9;
    private TextView text10;
    private TextView search;
    private View camera;
    private View say;
    private EventManager asr;//语音识别核心库
    private Uri uri;
    private Bitmap bitmap;
    private Dialog downloadDialog;

    private RecordView recordView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_distinguish, container, false);

        editText = view.findViewById(R.id.search_edit);
        search = view.findViewById(R.id.search_item);
        camera = view.findViewById(R.id.search_camera);
        say = view.findViewById(R.id.search_say);
        //initData();
        recordView = view.findViewById(R.id.record_view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Random r = new Random();
                    double a = (r.nextInt(100) + 1) * 0.01;
                    recordView.setIndex(a);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


        /*String[] colors = new String[]{"#FF0000","#00FF00","#FF00FF","#0000FF","#C37F1B","#009688","#A3B308","#FF0000","#00FF00","#FF00FF","#0000FF","#C37F1B","#009688","#A3B308"};
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for(int i = 0;i < 3;i++){
            builder.append("    ");
            Random random = new Random();
            Spannable text = new SpannableString(strings[i]);
            text.setSpan(new ForegroundColorSpan(Color.parseColor(colors[i])) , 0 , text.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            text.setSpan(new AbsoluteSizeSpan(new Random().nextInt(50)+40),0,text.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            builder.append(text);
            int len = random.nextInt(7)+4;
            for(int j = 0;j < i+3;j++){
                builder.append("  ");           //12
            }
        }
        search_hot.setText(builder);
        SpannableStringBuilder builder1 = new SpannableStringBuilder();
        for(int i = 9;i < 11;i++){
            builder1.append("   ");
            Random random = new Random();
            Spannable text = new SpannableString(strings[i]);
            text.setSpan(new ForegroundColorSpan(Color.parseColor(colors[i])) , 0 , text.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            text.setSpan(new AbsoluteSizeSpan(new Random().nextInt(50)+40),0,text.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            builder1.append(text);
            int len = random.nextInt(7)+4;
            for(int j = 0;j < i-9+3;j++){
                builder1.append(" ");           //12
            }
        }
        search_hot1.setText(builder1);
        SpannableStringBuilder builder2 = new SpannableStringBuilder();
        for(int i = 3;i < 6;i++){
            builder2.append("   ");
            Random random = new Random();
            Spannable text = new SpannableString(strings[i]);
            text.setSpan(new ForegroundColorSpan(Color.parseColor(colors[i])) , 0 , text.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            text.setSpan(new AbsoluteSizeSpan(new Random().nextInt(50)+40),0,text.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            builder2.append(text);
            int len = random.nextInt(7)+4;
            for(int j = 0;j < i;j++){
                builder2.append(" ");           //12
            }
        }
        search_hot2.setText(builder2);
        SpannableStringBuilder builder3 = new SpannableStringBuilder();
        for(int i = 6;i < 9;i++){
            builder3.append(" ");
            Random random = new Random();
            Spannable text = new SpannableString(strings[i]);
            text.setSpan(new ForegroundColorSpan(Color.parseColor(colors[i])) , 0 , text.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            text.setSpan(new AbsoluteSizeSpan(new Random().nextInt(50)+40),0,text.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            builder3.append(text);
            int len = random.nextInt(7)+4;
            for(int j = 0;j < i-4+3;j++){
                builder3.append(" ");           //12
            }
        }
        search_hot3.setText(builder3);*/

        initData();

        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventMessage(1));
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtomDialog.Builder builder = new ButtomDialog.Builder(getContext());
                //添加条目，可多个
                builder.addMenu("相机", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        /*Intent intent = new Intent(getActivity(),TakePhotoActivity.class);
                        intent.putExtra("choose",1);
                        startActivity(intent);*/
                        TakePhotos();
                        //Toast.makeText(getContext(), "相机", Toast.LENGTH_SHORT).show();
                    }
                }).addMenu("相册", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        /*Intent intent = new Intent(getActivity(),TakePhotoActivity.class);
                        intent.putExtra("choose",2);
                        startActivity(intent);*/
                        ChoosePhoto();
                        //Toast.makeText(getContext(), "相册", Toast.LENGTH_SHORT).show();
                    }
                });
                //下面这些设置都可不写
                builder.setTitle("请选择方式");//添加标题
                builder.setCanCancel(true);//点击阴影时是否取消dialog，true为取消
                builder.setShadow(true);//是否设置阴影背景，true为有阴影
                builder.setCancelText("取消");//设置最下面取消的文本内容
                //设置点击取消时的事件
                builder.setCancelListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                        Toast.makeText(getContext(), "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog = builder.create();
                dialog.show();
                //showAlertDialog();
                /*Intent intent = new Intent(getActivity(),TakePhotoActivity.class);
                startActivity(intent);*/
            }
        });

        initView();
        initPermission();
        //初始化EventManager对象
        asr = EventManagerFactory.create(getContext(), "asr");
        //注册自己的输出事件类
        asr.registerListener(this); //  EventListener 中 onEvent方法
        say.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //Log.d("TAG","按下");
                        if(asr == null){
                            Log.d("---------","---------");
                        }
                        recordView.setVisibility(View.VISIBLE);
                        asr.send(SpeechConstant.ASR_START, null, null, 0, 0);
                        //startRecord();
                        //Toast.makeText(getContext(),"正在录音",Toast.LENGTH_SHORT).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        //Log.d("TAG","抬起");
                        recordView.setVisibility(View.INVISIBLE);
                        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
                        //stopRecord();
                        //Toast.makeText(getContext(),"录音结束",Toast.LENGTH_SHORT).show();
                        break;
                    default:break;
                }
                return true;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                sendHttp(name);
            }
        });
        return view;
    }


    private void initData() {
        text1 = view.findViewById(R.id.text1);
        text2 = view.findViewById(R.id.text2);
        text3 = view.findViewById(R.id.text3);
        text4 = view.findViewById(R.id.text4);
        text5 = view.findViewById(R.id.text5);
        text6 = view.findViewById(R.id.text6);
        text7 = view.findViewById(R.id.text7);
        text8 = view.findViewById(R.id.text8);
        /*StringBuilder builder = new StringBuilder();
        Spannable text = new SpannableString(strings[0]);
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#131212")) , 0 , text.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(100),0,text.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        builder.append(text);
        text1.setText(builder);
        text = new SpannableString(strings[1]);
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#131212")) , 0 , text.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(100),0,text.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text2.setText(text.toString());
        text = new SpannableString(strings[2]);
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#131212")) , 0 , text.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(100),0,text.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text3.setText(text.toString());
        text = new SpannableString(strings[3]);
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#131212")) , 0 , text.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(100),0,text.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text4.setText(text.toString());
        text = new SpannableString(strings[4]);
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#131212")) , 0 , text.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(100),0,text.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text5.setText(text.toString());
        text = new SpannableString(strings[5]);
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#131212")) , 0 , text.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(100),0,text.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text6.setText(text.toString());
        text = new SpannableString(strings[6]);
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#131212")) , 0 , text.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(100),0,text.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text7.setText(text.toString());
        text = new SpannableString(strings[7]);
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#131212")) , 0 , text.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(100),0,text.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text8.setText(text.toString());
        text = new SpannableString(strings[8]);
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#131212")) , 0 , text.length() , Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text.setSpan(new AbsoluteSizeSpan(100),0,text.length(),Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        text9.setText(text.toString());*/

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(text1.getText().toString().trim());
                sendHttp(editText.getText().toString());
            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(text2.getText().toString().trim());
                sendHttp(editText.getText().toString());
            }

        });
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(text3.getText().toString().trim());
                sendHttp(editText.getText().toString());
            }
        });
        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(text4.getText().toString().trim());
                sendHttp(editText.getText().toString());
            }
        });
        text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(text5.getText().toString().trim());
                sendHttp(editText.getText().toString());
            }
        });
        text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(text6.getText().toString().trim());
                sendHttp(editText.getText().toString());
            }
        });
        text7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(text7.getText().toString().trim());
                sendHttp(editText.getText().toString());
            }
        });
        text8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText(text8.getText().toString().trim());
                sendHttp(editText.getText().toString());
            }
        });
    }

    private void initView() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getContext(), perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
                //Toast.makeText(getContext(),"没有权限",Toast.LENGTH_SHORT).show();
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), toApplyList.toArray(tmpList), 123);
        }
    }

    private void ChoosePhoto() {
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
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
                    Toast.makeText(getContext(),"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void TakePhotos() {
        File file = new File(getActivity().getExternalCacheDir(),"distinguish.jpg");
        try {
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= 24){
            uri= FileProvider.getUriForFile(getContext(),getContext().getPackageName()+".fileprovider",file);
        }else{
            uri=Uri.fromFile(file);
        }

        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200) {
            checkPermission();
        }
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    try {
                        //步骤四
                        bitmap= BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));

                        sendHttp();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                if(resultCode==RESULT_OK){
                    //判断手机版本号再对图片进行不同的解析（步骤三）
                    if(Build.VERSION.SDK_INT>=19){
                        //4.4以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    }else {
                        //4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
        }
    }

    private void sendHttp(String name){
        if(name != null || !name.isEmpty() || !name.equals("")) {
            Log.d("++++",name);
            Log.d("++++","name");
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://recover2.market.alicloudapi.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            StringApi stringApi = retrofit.create(StringApi.class);
            String appcode = "775c3c9069f74bca84a54782f6e1c5a2";
            Call<HttpResult1> call = null;
            call = stringApi.getCall("西安",name,"APPCODE " + appcode);
            downloadDialog = DownloadDialog.createLoadingDialog(getContext(),"加载中...");
            call.enqueue(new Callback<HttpResult1>() {
                @Override
                public void onResponse(Call<HttpResult1> call, Response<HttpResult1> response) {
                    List<Waste> wasteList = new ArrayList<>();
                    if(response.body() == null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"未搜到该物品，请重新尝试。",Toast.LENGTH_SHORT).show();
                            }
                        });
                        DownloadDialog.closeDialog(downloadDialog);
                        return ;
                    }
                    if (response.body().getData() != null){
                        HttpResult1.DataBean beans = response.body().getData();
                        List<HttpResult1.DataBean.ListBean> list = beans.getList();
                        if(list == null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(),"未搜到该物品，请重新尝试。",Toast.LENGTH_SHORT).show();
                                }
                            });
                            DownloadDialog.closeDialog(downloadDialog);
                            return ;
                        }
                        Rubbish rubbish = Rubbish.getInstance();
                        if(!rubbish.getList().contains(name)){
                            rubbish.getList().add(name);
                        }
                        for(HttpResult1.DataBean.ListBean listBean : list){
                            Log.d("TAG",listBean.getName()+"属于"+listBean.getCategory());
                            wasteList.add(new Waste(listBean.getName(),listBean.getCategory()));
                        }
                        DefaultFragment defaultFragment = new DefaultFragment(wasteList);
                        defaultFragment.show(getFragmentManager(),"DefaultFragment");
                    }else {
                        Log.d("TAG","2222222222222222222");
                    }
                    Log.d("000000","000000");
                    DownloadDialog.closeDialog(downloadDialog);
                    editText.setText("");
                    Rubbish rubbish = Rubbish.getInstance();
                    rubbish.setName("");
                }

                @Override
                public void onFailure(Call<HttpResult1> call, Throwable t) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"未搜到该物品，请重新尝试。",Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.d("TAG","发生错误");
                    DownloadDialog.closeDialog(downloadDialog);
                    Rubbish rubbish = Rubbish.getInstance();
                    rubbish.setName("");
                }
            });
        }
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
        try {
            Log.d("TAG",URLEncoder.encode(body1,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Call<HttpResult> call = null;
        try {
            call = imageApi.getCall(URLEncoder.encode(body1,"UTF-8"),"APPCODE " + appcode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        downloadDialog = DownloadDialog.createLoadingDialog(getContext(),"加载中...");
        call.enqueue(new Callback<HttpResult>() {
            @Override
            public void onResponse(Call<HttpResult> call, Response<HttpResult> response) {
                if(response.body() == null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"未搜到该物品，请重新尝试。",Toast.LENGTH_SHORT).show();
                        }
                    });
                    DownloadDialog.closeDialog(downloadDialog);
                    return ;
                }
                Log.d("66666666666666666", String.valueOf(response.body().getRet()));
                List<Waste> wasteList = new ArrayList<>();
                Log.d("+++++",response.body().getData().toString());
                if (response.body().getData() != null){
                    List<HttpResult.DataBean> beans = response.body().getData();
                    List<HttpResult.DataBean.ListBean> list = beans.get(0).getList();
                    if(list == null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(),"未搜到该物品，请重新尝试。",Toast.LENGTH_SHORT).show();
                            }
                        });
                        DownloadDialog.closeDialog(downloadDialog);
                        return ;
                    }
                    Rubbish rubbish = Rubbish.getInstance();
                    if(!rubbish.getList().contains(list.get(0).getName())){
                        rubbish.getList().add(list.get(0).getName());
                    }
                    for(HttpResult.DataBean.ListBean listBean : list){
                        Log.d("TAG",listBean.getName()+"属于"+listBean.getCategory());
                        wasteList.add(new Waste(listBean.getName(),listBean.getCategory()));
                    }
                    DefaultFragment defaultFragment = new DefaultFragment(wasteList);
                    defaultFragment.show(getFragmentManager(),"DefaultFragment");
                }else {
                    Log.d("TAG","2222222222222222222");
                }
                DownloadDialog.closeDialog(downloadDialog);
                Rubbish rubbish = Rubbish.getInstance();
                rubbish.setName("");
                editText.setText("");
            }

            @Override
            public void onFailure(Call<HttpResult> call, Throwable t) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"未搜到该物品，请重新尝试。",Toast.LENGTH_SHORT).show();
                    }
                });
                DownloadDialog.closeDialog(downloadDialog);
                Rubbish rubbish = Rubbish.getInstance();
                rubbish.setName("");
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void handleImageOnKitKat(Intent data){//（不同类型的图片，要用不同形式的解析方法）
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(getContext(),uri)){
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
        Cursor cursor=getActivity().getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    public void displayImage(String imagePath){//步骤四
        if(imagePath!=null){
            bitmap=BitmapFactory.decodeFile(imagePath);
            Rubbish.getInstance().setName("水瓶");
            Rubbish.getInstance().setClassify("可回收垃圾");
            sendHttp();
        }else {
            Toast.makeText(getActivity(),"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {                            //每次交互时去观察Rubbish单例有没有值，将它直接附到EditText
        super.onResume();
        String name = Rubbish.getInstance().getName();
        if(name != null){
            editText.setText(name);
        }
    }

    private void initPermission() {
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        ArrayList<String> toApplyList = new ArrayList<String>();
        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getContext(), perm)) {
                toApplyList.add(perm);
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), toApplyList.toArray(tmpList), 123);
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), permissions, 200);
                    return ;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        // 基于SDK集成4.2 发送取消事件
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        // 基于SDK集成5.2 退出事件管理器
        // 必须与registerListener成对出现，否则可能造成内存泄露
        asr.unregisterListener(this);
    }

    @SuppressLint("WrongConstant")
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void showEventMessage(EventMessage message){
        switch (message.getAccount()){
            case -10:
                String str = Rubbish.getInstance().getName();
                Log.d("str",str);
                if(str != null || !str.isEmpty()){
                    editText.setText(str);
                    sendHttp(str);
                }
                break;
        }

    }

    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        Log.d("**********","*********");
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            if (params == null || params.isEmpty()) {
                return;
            }
            if (params.contains("\"final_result\"")) {
                // 一句话的最终识别结果
                Log.i("ars.event",params);
                Gson gson = new Gson();
                ASRresponse asRresponse = gson.fromJson(params, ASRresponse.class);//数据解析转实体bean
                if(asRresponse == null) {
                    return;
                }
                String s;//后面跟了一个中文输入法下的逗号，
                if(asRresponse.getBest_result().contains("，")){
                    s = asRresponse.getBest_result().replace('，', ' ').trim();
                    s = s.replace('。',' ').trim();
                    Log.d("TAG",s);
                }else {
                    s = asRresponse.getBest_result().trim();
                    s = s.replace('。',' ').trim();
                    Log.d("TAG",s);
                }
                editText.setText(s);
                Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
            }
        }
    }
}