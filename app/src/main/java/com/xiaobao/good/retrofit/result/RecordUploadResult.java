package com.xiaobao.good.retrofit.result;

import com.google.gson.annotations.SerializedName;

public class RecordUploadResult {
    @SerializedName("code")
    public String code;

    @SerializedName("data")
    public String data;

    @SerializedName("message")
    public String message;


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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
