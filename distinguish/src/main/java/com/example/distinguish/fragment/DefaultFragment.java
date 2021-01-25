package com.example.distinguish.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.distinguish.Adapter.DefaultAdapter;
import com.example.distinguish.R;
import com.example.distinguish.db.Waste;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import db.EventMessage;

public class DefaultFragment extends DialogFragment {

    private List<Waste> wasteList;
    private Button know;

    public DefaultFragment(List<Waste> wasteList) {
        this.wasteList = wasteList;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.default_fragment,container,false);
        know = view.findViewById(R.id.know);

        pushData();
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_item);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        DefaultAdapter adapter = new DefaultAdapter(wasteList);
        recyclerView.setAdapter(adapter);
        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        return view;
    }

    private void pushData() {
        SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        Integer rec = pref.getInt("recyclable",0);
        Integer non = pref.getInt("nonRecyclable",0);
        Integer oth = pref.getInt("other",0);
        Integer kit = pref.getInt("kitchen",0);
        for(Waste waste : wasteList){
            if(waste.getSort().equals("厨余垃圾")){
                kit++;
            }else if(waste.getSort().equals("其他垃圾")){
                oth++;
            }
            else if(waste.getSort().equals("有害垃圾")){
                non++;
            }
            else {
                rec++;
            }
        }
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("recyclable",rec);
        editor.putInt("nonRecyclable",non);
        editor.putInt("other",oth);
        editor.putInt("kitchen",kit);
        editor.apply();
        EventBus.getDefault().post(new EventMessage(999));
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("TAG", "onStart");
        //resizeDialogFragment();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((dm.widthPixels/9*8),1080);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

}
