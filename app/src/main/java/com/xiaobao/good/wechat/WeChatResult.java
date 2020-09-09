package com.xiaobao.good.wechat;

public class WeChatResult {

    /**
     * code : success
     * data : 我买的第三的味道确实可以就没再<br>换过<br>价格确实差不多<br>2020年8月23日上午11:29<br>评分也差不多<br>星期六下午2:39<br>
     * message : null
     */

    private String code;
    private String data;
    private Object message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
