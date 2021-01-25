package com.example.distinguish.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.distinguish.R;
import com.example.distinguish.fragment.DistinguishFragment;
import com.example.distinguish.fragment.SearchFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import db.EventMessage;

public class DistinguishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distinguish);
        EventBus.getDefault().register(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.distinguish_container, new DistinguishFragment()).commit();
    }

    @SuppressLint("WrongConstant")
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void showEventMessage(EventMessage message){
        switch (message.getAccount()){
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.distinguish_container, new SearchFragment()).addToBackStack(null).commit();
                break;
            case -1:
                getSupportFragmentManager().beginTransaction().replace(R.id.distinguish_container, new DistinguishFragment()).commit();
            case -10:
                getSupportFragmentManager().beginTransaction().replace(R.id.distinguish_container, new DistinguishFragment()).commit();
                break;
            default:break;
        }
        Log.d("***", String.valueOf(message.getAccount()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}