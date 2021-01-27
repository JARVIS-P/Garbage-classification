package com.example.mine.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mine.R;
import com.example.mine.fragment.MineFragment;
import com.example.mine.fragment.PasswordChange;
import com.example.mine.fragment.SafetyManager;
import com.example.mine.fragment.TeleChange;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import db.EventMessage;

public class MineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        getSupportFragmentManager().beginTransaction().replace(R.id.mine_container, new MineFragment()).commit();

        EventBus.getDefault().register(this);
    }

    @SuppressLint("WrongConstant")
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void showEventMessage(EventMessage message){
        switch (message.getAccount()){
            case 3:
                getSupportFragmentManager().beginTransaction().replace(R.id.mine_container, new SafetyManager()).addToBackStack(null).commit();
                break;
            case 4:
                getSupportFragmentManager().beginTransaction().replace(R.id.mine_container, new TeleChange()).addToBackStack(null).commit();
                break;
            case 5:
                getSupportFragmentManager().beginTransaction().replace(R.id.mine_container, new PasswordChange()).addToBackStack(null).commit();
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