package com.xiaobao.good;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.xiaobao.good.common.CommonUtils;
import com.xiaobao.good.common.Constants;
import com.xiaobao.good.common.StringUtils;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.retrofit.result.UserInfoData;
import com.xiaobao.good.sp.UserSp;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "xiaobao_login";

    enum Result {

        SUCCESS(""),
        FAILVALID("用户名或者密码不可为空"),
        FAILNETWORKFAIL("网络未连接");

        private String msg;

        Result(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.tv_username);
        final EditText passwordEditText = findViewById(R.id.tv_pwd);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = usernameEditText.getText().toString().trim();
                String pwd = passwordEditText.getText().toString().trim();

                Result re = conditionCheck(name, pwd);

                if (!Result.SUCCESS.equals(re)) {
                    toast(re.getMsg());
                } else if (Result.SUCCESS.equals(re)) {

                    doLogin(name, pwd);

                }


            }
        });

    }

    private void doLogin(String name, String pwd) {

        try {
            Map map = new HashMap<String, String>(2);
            map.put("loginName", name);
            map.put("loginPwd", pwd);
            Call<UserInfoData> call = RetrofitUtils.getService().login(name, pwd);
            call.enqueue(new Callback<UserInfoData>() {
                @Override
                public void onResponse(Call<UserInfoData> call, Response<UserInfoData> response) {

                    LogUtil.i(TAG, "re:" + response.isSuccessful() + "," + response.message() + ">>" + response.body());

                    UserInfoData userInfoData = response.body();


                    if (userInfoData != null) {
                        if (Constants.LOGIN_SUCC.equals(userInfoData.getCode())) {

                            try {
                                /**
                                 * 登录成功。保存到本地
                                 */
                                UserSp.getInstances().saveUser(userInfoData.getData());

                                UserInfoData.LoginUserData loginUserData = UserSp.getInstances().getUser();
                                LogUtil.i(TAG, "read from sp:" + loginUserData.toString());




                            } catch (Exception e) {
                                LogUtil.e(TAG, "read from sp e:" + e);
                            }

                        }
                    }


                }

                @Override
                public void onFailure(Call<UserInfoData> call, Throwable t) {

                    LogUtil.i(TAG, "fail re:" + t.getMessage());

                }
            });
        } catch (Exception e) {
            LogUtil.i(TAG, "e:" + e);
        }


    }

    private Result conditionCheck(String name, String pwd) {

        if (StringUtils.isEmpty(name) || StringUtils.isEmpty(pwd)) {
            return Result.FAILVALID;

        }

        if (!CommonUtils.isNetWorkAvail(this)) {
            return Result.FAILNETWORKFAIL;
        }
        return Result.SUCCESS;
    }


    private void toast(String msg) {

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}