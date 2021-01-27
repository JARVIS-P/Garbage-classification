package com.example.mine.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.mine.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import db.EventMessage;

@Route(path = "/mine/safetyManager")
public class SafetyManager extends Fragment {

    ListView listView;
    private List<String> list;
    EditText editText;
    TextView head;
    ImageView back;
    boolean isShow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.safety_manager,container,false);
        head = view.findViewById(R.id.head_text);
        back = view.findViewById(R.id.goBack);
        head.setText("账号与安全");
        editText = new EditText(getContext());
        editText.setHint("不超过20个字符");
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        listView = view.findViewById(R.id.safety_list);
        initList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,list);
        listView.setAdapter(arrayAdapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventMessage(-6));
                getActivity().onBackPressed();
                //EventBus.getDefault().post(new EventMessage(-6));
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        editText = new EditText(getContext());
                        editText.setHint("不超过20个字符");
                        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                        showDialog();
                        break;
                    case 1:
                        assert getFragmentManager() != null;
                        EventBus.getDefault().post(new EventMessage(4));
                        //getFragmentManager().beginTransaction().replace(R.id.mine_container,new TeleChange()).addToBackStack(null).commit();
                        break;
                    case 2:
                        assert getFragmentManager() != null;
                        EventBus.getDefault().post(new EventMessage(5));
                        //getFragmentManager().beginTransaction().replace(R.id.mine_container,new PasswordChange()).addToBackStack(null).commit();
                        break;
                    default:break;
                }
            }
        });
        return view;
    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage("请输入新的用户名:");
        dialog.setView(editText);
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"已取消",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = editText.getText().toString();
                SharedPreferences.Editor edit = getContext().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                edit.putString("name",str);
                edit.apply();
                Toast.makeText(getContext(),"已修改!",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void initList() {
        list = new ArrayList<>();
        list.add("修改用户名");
        list.add("修改/绑定手机号");
        list.add("修改密码");
    }
}
