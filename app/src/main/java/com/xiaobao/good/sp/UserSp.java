package com.xiaobao.good.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.xiaobao.good.InitApplication;
import com.xiaobao.good.common.Constants;
import com.xiaobao.good.common.StringUtils;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.retrofit.result.UserInfoData;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSp {


    private SharedPreferences userP;
    private SharedPreferences.Editor editor;

    private Gson gson = new Gson();
    private static UserSp userSp = new UserSp();

    private UserSp() {
        userP = InitApplication.getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = userP.edit();
    }

    public static UserSp getInstances() {
        return userSp;
    }


    public void saveUser(String userData) {
        editor.putString(Constants.USERINFO, userData).commit();
    }

    public void saveUser(UserInfoData.LoginUserData loginUserData) {
        editor.putString(Constants.USERINFO, gson.toJson(loginUserData)).commit();
    }

    public UserInfoData.LoginUserData getUser() {

        String userData = userP.getString(Constants.USERINFO, null);
        if (StringUtils.isNotEmpty(userData)) {


            try {
                JSONObject jo = new JSONObject(userData);

                LogUtil.i("xiaobao_login", "read from sp:" + userData);


                return gson.fromJson(jo.toString(), UserInfoData.LoginUserData.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;


    }

}
