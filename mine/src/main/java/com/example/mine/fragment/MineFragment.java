package com.example.mine.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.View;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.mine.R;
import com.example.mine.db.PieData;
import com.example.mine.db.PieEntry;
import com.example.mine.db.PieView;
import com.leon.lib.settingview.LSettingItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import db.EventMessage;
import db.User;

import static com.google.android.material.color.MaterialColors.getColor;

@Route(path = "/mine/fragment")
public class MineFragment extends Fragment {

    ArrayList<PieData> list = new ArrayList<>();
    TextView name;                              //用户名
    PieView mPieView;
    TextView number;                            //手机号
    ImageView head_view;                        //头像
    LSettingItem safe;
    LSettingItem about;
    LSettingItem back;
    TextView isNo;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        head_view = view.findViewById(R.id.head_image);
        name = view.findViewById(R.id.name);
        number = view.findViewById(R.id.number);
        safe = view.findViewById(R.id.safety);
        about = view.findViewById(R.id.about);
        back = view.findViewById(R.id.back);
        isNo = view.findViewById(R.id.isNo);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeColors(R.color.design_default_color_secondary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPieView();
            }
        });

        mPieView = view.findViewById(R.id.pieView_item);
        initPieView();

        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        name.setText(pref.getString("name","未命名"));
        number.setText(pref.getString("telePhoneNumber","未绑定手机号码"));
        head_view.setImageResource(pref.getInt("imageId",R.drawable.headimage));

        safe.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                assert getFragmentManager() != null;
                EventBus.getDefault().post(new EventMessage(3));
                //getFragmentManager().beginTransaction().replace(R.id.mine_container, new SafetyManager()).addToBackStack(null).commit();
            }
        });
        about.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                Toast.makeText(getContext(), "about", Toast.LENGTH_SHORT).show();
            }
        });
        back.setmOnLSettingItemClick(new LSettingItem.OnLSettingItemClick() {
            @Override
            public void click() {
                showDialog();
            }
        });

        return view;
    }


    private void initPieView() {
        mPieView.setColors(createColors());
        mPieView.setData(createData());
        swipeRefreshLayout.setRefreshing(false);
    }

    private ArrayList<PieEntry> createData() {
        ArrayList<PieEntry> pieLists = new ArrayList<>();
        SharedPreferences pref = getActivity().getSharedPreferences("data",Context.MODE_PRIVATE);
        Integer recyclable = pref.getInt("recyclable",0);
        Integer nonRecyclable = pref.getInt("nonRecyclable",0);
        Integer other = pref.getInt("other",0);
        Integer kitchen = pref.getInt("kitchen",0);
        Integer num = recyclable+nonRecyclable+other+kitchen;
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        Log.d("........",recyclable+".."+nonRecyclable+".."+other+".."+kitchen);
        String rec = numberFormat.format((float)  recyclable/ (float)num* 100);
        String non = numberFormat.format((float)  nonRecyclable/ (float)num* 100);
        String oth = numberFormat.format((float)  other/ (float)num* 100);
        String kit = numberFormat.format((float)  kitchen/ (float)num* 100);
        Log.d("........",rec+".."+non+".."+oth+".."+kit);
        if(rec.equals("0") && rec.equals("0") && rec.equals("0") && rec.equals("0")){
            mPieView.setVisibility(View.INVISIBLE);
            isNo.setVisibility(View.VISIBLE);
        }else{
            mPieView.setVisibility(View.VISIBLE);
            isNo.setVisibility(View.INVISIBLE);
        }
        pieLists.add(new PieEntry(Float.parseFloat(rec), "可回收垃圾"));
        pieLists.add(new PieEntry(Float.parseFloat(non), "有害垃圾"));
        pieLists.add(new PieEntry(Float.parseFloat(oth), "其他垃圾"));
        pieLists.add(new PieEntry(Float.parseFloat(kit), "厨余垃圾"));
        return pieLists;
    }

    private ArrayList<Integer> createColors() {
        ArrayList<Integer> colorLists = new ArrayList<>();
        colorLists.add(Color.parseColor("#1981D5"));
        colorLists.add(Color.parseColor("#ff4d4d"));
        colorLists.add(Color.parseColor("#EBBF03"));
        colorLists.add(Color.parseColor("#41D230"));
        return colorLists;
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage("是否退出登录？");
        dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"已取消",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences pref = getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("isLogin",false);
                editor.apply();
                EventBus.getDefault().post(new EventMessage(100));
                Toast.makeText(getContext(),"已退出登录!",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    @SuppressLint("WrongConstant")
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void showEventMessage(EventMessage message){
        switch (message.getAccount()){
            case 999:
                /*SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                name.setText(pref.getString("name","未命名"));
                number.setText(pref.getString("telePhoneNumber","未绑定手机号码"));
                head_view.setImageResource(pref.getInt("imageId",R.drawable.ic_back));*/
                initPieView();
                break;
            default:break;
        }
        Log.d("***", String.valueOf(message.getAccount()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}