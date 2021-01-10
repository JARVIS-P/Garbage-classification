package com.example.wastesorting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.distinguish.fragment.SearchFragment;
import com.example.mine.fragment.LoginFragment;
import com.example.mine.fragment.PasswordChange;
import com.example.mine.fragment.SafetyManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import db.EventMessage;

@Route(path = "/app/MainActivity")
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Fragment distinguishFragment;
    private Fragment recoverFragment;
    private Fragment guideFragment;
    private Fragment mineFragment;

    private Fragment searchFragment;
    private FragmentTransaction beginTransaction;

    private Fragment passwordChange;
    private Fragment safetyManager;
    private Fragment teleChange;

    private Fragment loginFragment;

    private Fragment nowFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init(this.getApplication()); // 尽可能早，推荐在Application中初始化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置透明状态栏
        }
        initView();
    }

    private void initView() {
        File file = new File("/data/data/com.example.wastesorting/shared_prefs/data.xml");
        if(!file.exists()){
            SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
            editor.putBoolean("isLogin",false);
            editor.putString("admin","admin");
            editor.putString("password","12345678");
            editor.putString("name","青盲");
            editor.putString("telePhoneNumber","11111111111");
            editor.putInt("imageId",R.drawable.headimage);
            editor.putInt("recyclable",6);
            editor.putInt("nonRecyclable",5);
            editor.putInt("other",3);
            editor.putInt("kitchen",6);
            editor.apply();
        }

        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        boolean isLogin = pref.getBoolean("isLogin",true);

        searchFragment = (Fragment) ARouter.getInstance().build("/distinguish/searchFrag").navigation();
        passwordChange = (Fragment) ARouter.getInstance().build("/mine/passwordChange").navigation();
        safetyManager = (Fragment) ARouter.getInstance().build("/mine/safetyManager").navigation();
        teleChange = (Fragment) ARouter.getInstance().build("/mine/teleChange").navigation();
        loginFragment = (Fragment) ARouter.getInstance().build("/mine/login").navigation();

        distinguishFragment = (Fragment) ARouter.getInstance().build("/distinguish/fragment").navigation();
        recoverFragment = (Fragment) ARouter.getInstance().build("/recover/fragment").navigation();
        guideFragment = (Fragment) ARouter.getInstance().build("/guide/fragment").navigation();
        mineFragment = (Fragment) ARouter.getInstance().build("/mine/fragment").navigation();
        bottomNavigationView = findViewById(R.id.navigation_view);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frameLayout, distinguishFragment).add(R.id.frameLayout, recoverFragment).add(R.id.frameLayout, guideFragment).add(R.id.frameLayout, mineFragment).add(R.id.frameLayout,loginFragment);//.add(R.id.frameLayout, searchFragment);
        if(!isLogin){
            fragmentTransaction.hide(recoverFragment).hide(guideFragment).hide(mineFragment).hide(distinguishFragment);//.hide(searchFragment);
            bottomNavigationView.setVisibility(View.INVISIBLE);
            fragmentTransaction.show(loginFragment);
        }else{
            fragmentTransaction.hide(recoverFragment).hide(guideFragment).hide(mineFragment).hide(loginFragment);//.hide(searchFragment);
            fragmentTransaction.show(distinguishFragment);
        }
        fragmentTransaction.commit();




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                beginTransaction = getSupportFragmentManager().beginTransaction();
                switch (menuItem.getItemId()) {
                    case R.id.distinguish:
                        beginTransaction.hide(recoverFragment).hide(guideFragment).hide(mineFragment)/*.hide(searchFragment)*/.hide(loginFragment);
                        beginTransaction.show(distinguishFragment);
                        beginTransaction.commit();
                        break;
                    case R.id.recover:
                        beginTransaction.hide(distinguishFragment).hide(guideFragment).hide(mineFragment)/*.hide(searchFragment)*/.hide(loginFragment);
                        beginTransaction.show(recoverFragment);
                        beginTransaction.commit();
                        break;
                    case R.id.guide:
                        beginTransaction.hide(distinguishFragment).hide(recoverFragment).hide(mineFragment)/*.hide(searchFragment)*/.hide(loginFragment);
                        beginTransaction.show(guideFragment);
                        beginTransaction.commit();
                        break;
                    case R.id.mine:
                        beginTransaction.hide(distinguishFragment).hide(recoverFragment).hide(guideFragment)/*.hide(searchFragment)*/.hide(loginFragment);
                        beginTransaction.show(mineFragment);
                        beginTransaction.commit();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @SuppressLint("WrongConstant")
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void showEventMessage(EventMessage message){
        beginTransaction = getSupportFragmentManager().beginTransaction();
        switch (message.getAccount()){
            case 1:
                Log.d("999999","1111111");
                beginTransaction.replace(R.id.frameLayout,searchFragment).addToBackStack(null);
                nowFragment = searchFragment;
                beginTransaction.commit();
                break;
            case 3:
                beginTransaction.replace(R.id.frameLayout,safetyManager).addToBackStack(null);
                nowFragment = safetyManager;
                bottomNavigationView.setVisibility(View.INVISIBLE);
                beginTransaction.commit();
                break;
            case 4:
                beginTransaction.replace(R.id.frameLayout,teleChange).addToBackStack(null);
                nowFragment = teleChange;
                beginTransaction.commit();
                break;
            case 5:
                beginTransaction.replace(R.id.frameLayout,passwordChange).addToBackStack(null);
                nowFragment = passwordChange;
                beginTransaction.commit();
                break;
            case 9:
                beginTransaction.hide(recoverFragment).hide(guideFragment).hide(mineFragment).hide(loginFragment);
                beginTransaction.show(distinguishFragment);
                bottomNavigationView.setVisibility(View.VISIBLE);
                nowFragment = distinguishFragment;
                beginTransaction.commit();
                break;
            case -6:
                bottomNavigationView.setVisibility(View.VISIBLE);
                beginTransaction.commit();
                break;
            case 100:
                loginFragment = new LoginFragment();
                beginTransaction.add(R.id.frameLayout,loginFragment);
                beginTransaction.hide(recoverFragment).hide(guideFragment).hide(mineFragment).hide(distinguishFragment);//.hide(searchFragment);
                bottomNavigationView.setVisibility(View.INVISIBLE);
                beginTransaction.show(loginFragment);
                nowFragment = loginFragment;
                beginTransaction.commit();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("00000","00099");
        //nowFragment.equals(SafetyManager)
        if(nowFragment.equals(passwordChange) || nowFragment.equals(teleChange)){
            nowFragment = safetyManager;
        }else if(nowFragment.equals(safetyManager)){
            bottomNavigationView.setVisibility(View.VISIBLE);
            Log.d("111111","111111");
        }
    }
}