package com.example.distinguish.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.distinguish.R;
import com.example.distinguish.db.Rubbish;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import db.EventMessage;

import static androidx.core.content.ContextCompat.getSystemService;

@Route(path = "/distinguish/searchFrag")
public class SearchFragment extends Fragment {

    Button back1;
    Button back2;
    EditText editText;
    ListView listView;

    @Override
    public void onResume() {
        super.onResume();
        
        String name = Rubbish.getInstance().getName();
        if(name != null){
            editText.setText(name);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_distinguish,container,false);


        listView = view.findViewById(R.id.sear_list);
        editText = view.findViewById(R.id.edit_text);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        List<String> list = Rubbish.getInstance().getList();
        List<String> data = new ArrayList<>();
        int len = list.size();
        for(int i = len - 1;i >= 0;i--){
            data.add(list.get(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = list.get(list.size() -1 - position);
                editText.setText(str);
            }
        });

        back1 = view.findViewById(R.id.back_1);
        back2 = view.findViewById(R.id.back_2);
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rubbish rubbish = Rubbish.getInstance();
                rubbish.setName(editText.getText().toString());
                EventBus.getDefault().post(new EventMessage(-1));
                getActivity().onBackPressed();
            }
        });
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rubbish rubbish = Rubbish.getInstance();
                rubbish.setName(editText.getText().toString());
                EventBus.getDefault().post(new EventMessage(-10));
                getActivity().onBackPressed();
            }
        });
        return view;
    }

}
