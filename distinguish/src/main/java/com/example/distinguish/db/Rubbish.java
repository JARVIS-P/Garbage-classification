package com.example.distinguish.db;

import java.util.ArrayList;
import java.util.List;

public class Rubbish {
    String name = null;
    String classify = null;
    List<String> list = new ArrayList<>();
    private static Rubbish rubbish;

    public static synchronized Rubbish getInstance(){               //同步控制,避免多线程的状况多创建了实例对象
        if (rubbish == null){
            rubbish = new Rubbish();
        }
        return rubbish;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
