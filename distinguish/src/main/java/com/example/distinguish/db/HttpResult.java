package com.example.distinguish.db;

import com.google.gson.Gson;

import java.util.List;

public class HttpResult {

    /**
     * ret : 200
     * data : [{"score":0.999989,"keyword":"西瓜","list":[{"name":"西瓜皮","category":"湿垃圾"},{"name":"西瓜籽","category":"湿垃圾"},{"name":"西瓜子","category":"湿垃圾"},{"name":"西瓜","category":"湿垃圾"},{"name":"包裹着西瓜籽的纸巾","category":"干垃圾"},{"name":"西瓜霜","category":"有害垃圾"}]},{"score":0.779957,"keyword":"西瓜皮","list":[{"name":"西瓜皮","category":"湿垃圾"}]},{"score":0.399997,"keyword":"火龙果","list":[{"name":"火龙果","category":"湿垃圾"},{"name":"火龙果皮","category":"湿垃圾"}]}]
     * qt : 0.769
     */

    private int ret;
    private double qt;
    private List<DataBean> data;
    public int getRet() {
        return ret;
    }
    public List<DataBean> getData() {
        return data;
    }
    public static class DataBean {
        /**
         * score : 0.999989
         * keyword : 西瓜
         * list : [{"name":"西瓜皮","category":"湿垃圾"},{"name":"西瓜籽","category":"湿垃圾"},{"name":"西瓜子","category":"湿垃圾"},{"name":"西瓜","category":"湿垃圾"},{"name":"包裹着西瓜籽的纸巾","category":"干垃圾"},{"name":"西瓜霜","category":"有害垃圾"}]
         */
        private double score;
        private String keyword;
        private List<ListBean> list;
        public String getKeyword() {
            return keyword;
        }
        public List<ListBean> getList() {
            return list;
        }
        public static class ListBean {
            /**
             * name : 西瓜皮
             * category : 湿垃圾
             */
            private String name;
            private String category;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCategory() {
                return category;
            }

        }
    }
}
