package com.example.distinguish.db;

import com.google.gson.Gson;

import java.util.List;

public class HttpResult1 {

    /**
     * ret : 200
     * data : {"list":[{"name":"西瓜皮","category":"湿垃圾"},{"name":"西瓜籽","category":"湿垃圾"},{"name":"西瓜子","category":"湿垃圾"},{"name":"西瓜","category":"湿垃圾"},{"name":"包裹着西瓜籽的纸巾","category":"干垃圾"},{"name":"西瓜霜","category":"有害垃圾"}]}
     * qt : 0.011
     */

    private int ret;
    private DataBean data;
    private double qt;

    public static HttpResult1 objectFromData(String str) {

        return new Gson().fromJson(str, HttpResult1.class);
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public double getQt() {
        return qt;
    }

    public void setQt(double qt) {
        this.qt = qt;
    }

    public static class DataBean {
        private List<ListBean> list;

        public static DataBean objectFromData(String str) {

            return new Gson().fromJson(str, DataBean.class);
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * name : 西瓜皮
             * category : 湿垃圾
             */

            private String name;
            private String category;

            public static ListBean objectFromData(String str) {

                return new Gson().fromJson(str, ListBean.class);
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }
        }
    }
}

