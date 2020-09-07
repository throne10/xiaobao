package com.xiaobao.good.retrofit.result;

import java.util.List;

public class WechatRecord {

    /**
     * log_id : 5369227473586631000
     * words_result_num : 3
     * words_result : [{"location":{"width":18,"top":16,"left":85,"height":14},"words":" ok"},{"location":{"width":60,"top":61,"left":166,"height":13},"words":"昨天18:24"},{"location":{"width":42,"top":147,"left":85,"height":16},"words":"下雨了"}]
     */

    private long log_id;
    private int words_result_num;
    private List<WordsResultBean> words_result;

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public int getWords_result_num() {
        return words_result_num;
    }

    public void setWords_result_num(int words_result_num) {
        this.words_result_num = words_result_num;
    }

    public List<WordsResultBean> getWords_result() {
        return words_result;
    }

    public void setWords_result(List<WordsResultBean> words_result) {
        this.words_result = words_result;
    }

    public static class WordsResultBean {
        /**
         * location : {"width":18,"top":16,"left":85,"height":14}
         * words :  ok
         */

        private LocationBean location;
        private String words;

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        @Override
        public String toString() {
            return "WordsResultBean{" +
                    "location=" + location +
                    ", words='" + words + '\'' +
                    '}';
        }

        public static class LocationBean {
            /**
             * width : 18
             * top : 16
             * left : 85
             * height : 14
             */

            private int width;
            private int top;
            private int left;
            private int height;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            @Override
            public String toString() {
                return "LocationBean{" +
                        "width=" + width +
                        ", top=" + top +
                        ", left=" + left +
                        ", height=" + height +
                        '}';
            }
        }
    }
}
