package com.xiaobao.good.sign;

public class Visit {

    /**
     * client_id : 38
     * employee_id : 4
     * purpose : 展业
     * sign_address : 获取当前位置
     */

    private int client_id;
    private int employee_id;
    private String purpose;
    private String sign_address;
    private String wechat_content;

    public String getWechat_content() {
        return wechat_content;
    }

    public void setWechat_content(String wechat_content) {
        this.wechat_content = wechat_content;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getSign_address() {
        return sign_address;
    }

    public void setSign_address(String sign_address) {
        this.sign_address = sign_address;
    }
}
