package com.example.mine.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.mine.R;

import org.greenrobot.eventbus.EventBus;

import db.EventMessage;

@Route(path = "/mine/login")
public class LoginFragment extends Fragment {

    private EditText login_name;
    private EditText login_password;
    private Button login;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_mine1,container,false);
        login_name = view.findViewById(R.id.login_name);
        login_password = view.findViewById(R.id.login_password);
        login = view.findViewById(R.id.login);
        login_name.setText("");
        login_password.setText("");
        Log.d("11111111111111","11111111111");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                String name = login_name.getText().toString();
                String password = login_password.getText().toString();
                String localName = pref.getString("admin",null);
                String localPassword = pref.getString("password",null);
                String localTele = pref.getString("telePhoneNumber",null);
                if((name.equals(localName) && password.equals(localPassword)) || (name.equals(localTele) && password.equals(localPassword))){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("isLogin",true);
                    editor.apply();
                    Toast.makeText(getContext(),"登陆成功",Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new EventMessage(9));
                }else{
                    Toast.makeText(getContext(),"登陆失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
