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
import io.reactivex.internal.operators.observable.ObservableReduceMaybe;

@Route(path = "/mine/passwordChange")
public class PasswordChange extends Fragment {

    TextView head;
    ImageView back;
    EditText zhanghao;
    EditText oldWord;
    EditText newWord;
    EditText newWord1;
    Button change;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_word,container,false);
        head = view.findViewById(R.id.head_text);
        back = view.findViewById(R.id.goBack);
        zhanghao = view.findViewById(R.id.zhanghao);
        oldWord = view.findViewById(R.id.oldWord);
        newWord = view.findViewById(R.id.newWord);
        newWord1 = view.findViewById(R.id.newWord1);
        change = view.findViewById(R.id.change);
        head.setText("修改密码");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
               //EventBus.getDefault().post(new EventMessage(-4));
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
                String mZhanghao = pref.getString("telePhoneNumber",null);
                String mOldWord = pref.getString("password",null);
                if(!zhanghao.getText().toString().equals(mZhanghao)){
                    Toast.makeText(getContext(),"账号输入错误请重新输入！",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(!oldWord.getText().toString().equals(mOldWord)){
                    Toast.makeText(getContext(),"密码输入错误，请重新输入！",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(newWord.getText().toString().equals("")){
                    Toast.makeText(getContext(),"新密码不能为空！",Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(!newWord.getText().toString().equals(newWord1.getText().toString())){
                    Toast.makeText(getContext(),"两次输入密码不一致，请重新输入！",Toast.LENGTH_SHORT).show();
                    return ;
                }
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("password",newWord.getText().toString());
                editor.apply();
                Toast.makeText(getContext(),"密码修改成功！",Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
        return view;
    }
}
