package com.example.mine.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.mine.R;

import org.greenrobot.eventbus.EventBus;

import db.EventMessage;

@Route(path = "/mine/teleChange")
public class TeleChange extends Fragment {

    TextView head;
    ImageView back;
    EditText oldTele;
    EditText newTele;
    Button changeTele;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_tele, container,false);
        head = view.findViewById(R.id.head_text);
        back = view.findViewById(R.id.goBack);
        oldTele = view.findViewById(R.id.oldTele);
        newTele = view.findViewById(R.id.newTele);
        changeTele = view.findViewById(R.id.change_tele);
        head.setText("修改手机号");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                //EventBus.getDefault().post(new EventMessage(-4));
            }
        });

        changeTele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                String tele = pref.getString("telePhoneNumber",null);
                if(!oldTele.getText().toString().equals(tele)){
                    Toast.makeText(getContext(),"原手机号码输入有误！",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(newTele.getText().toString().length() != 11){
                    Toast.makeText(getContext(),"请输入合法的手机号码！",Toast.LENGTH_SHORT).show();
                    return ;
                }
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("telePhoneNumber",newTele.getText().toString());
                editor.apply();
                Toast.makeText(getContext(),"修改成功！",Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
