package com.xiaobao.good.retrofit.result;


import com.google.gson.annotations.SerializedName;

public class UserInfoData {

    @SerializedName("code")
    private String code;

    @SerializedName("data")
    private LoginUserData data;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LoginUserData getData() {
        return data;
    }

    public void setData(LoginUserData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserInfoData{" +
                "code='" + code + '\'' +
                ", data=" + data +
                '}';
    }

    public class LoginUserData {

        @SerializedName("employee_id")
        private int employee_id;

        @SerializedName("employee_login")
        private String employee_login;

        @SerializedName("employee_name")
        private String employee_name;

        @SerializedName("employee_phone")
        private String employee_phone;
        @SerializedName("employee_pwd")
        private String employee_pwd;

        @SerializedName("leader_id")
        private String leader_id;

        @SerializedName("role")
        private String role;

        public int getEmployee_id() {
            return employee_id;
        }

        public void setEmployee_id(int employee_id) {
            this.employee_id = employee_id;
        }

        public String getEmployee_login() {
            return employee_login;
        }

        public void setEmployee_login(String employee_login) {
            this.employee_login = employee_login;
        }

        public String getEmployee_name() {
            return employee_name;
        }

        public void setEmployee_name(String employee_name) {
            this.employee_name = employee_name;
        }

        public String getEmployee_phone() {
            return employee_phone;
        }

        public void setEmployee_phone(String employee_phone) {
            this.employee_phone = employee_phone;
        }

        public String getEmployee_pwd() {
            return employee_pwd;
        }

        public void setEmployee_pwd(String employee_pwd) {
            this.employee_pwd = employee_pwd;
        }

        public String getLeader_id() {
            return leader_id;
        }

        public void setLeader_id(String leader_id) {
            this.leader_id = leader_id;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        @Override
        public String toString() {
            return "LoginUserData{" +
                    "employee_id='" + employee_id + '\'' +
                    ", employee_login='" + employee_login + '\'' +
                    ", employee_name='" + employee_name + '\'' +
                    ", employee_phone='" + employee_phone + '\'' +
                    ", employee_pwd='" + employee_pwd + '\'' +
                    ", leader_id='" + leader_id + '\'' +
                    ", role='" + role + '\'' +
                    '}';
        }
    }
}
