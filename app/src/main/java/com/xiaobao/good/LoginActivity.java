package com.xiaobao.good;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xiaobao.good.common.CommonUtils;
import com.xiaobao.good.common.Constants;
import com.xiaobao.good.common.StringUtils;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.retrofit.result.UserInfoData;
import com.xiaobao.good.sp.UserSp;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;
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

    String[] perms = {
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.RECORD_AUDIO
    };

    private boolean remember;

    @BindDrawable(R.mipmap.icon_selected)
    Drawable icSelected;

    @BindDrawable(R.mipmap.icon_unselected)
    Drawable icUnSelected;

    @BindView(R.id.iv_remember)
    ImageView ivRemember;

    @OnClick({R.id.iv_remember, R.id.tv_remember})
    public void selectRemember() {
        if (remember) {
            ivRemember.setImageDrawable(icUnSelected);
        } else {
            ivRemember.setImageDrawable(icSelected);
        }
        remember = !remember;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        // 权限请求操作
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拥有权限进行操作
        } else {
            // 没有权限进行提示且申请权限
            EasyPermissions.requestPermissions(this, "应用需要权限，请允许", 0, perms);
        }
        final EditText usernameEditText = findViewById(R.id.tv_username);
        final EditText passwordEditText = findViewById(R.id.tv_pwd);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        loginButton.setOnClickListener(
                v -> {
                    String name = usernameEditText.getText().toString().trim();
                    String pwd = passwordEditText.getText().toString().trim();

                    Result re = conditionCheck(name, pwd);

                    if (!Result.SUCCESS.equals(re)) {
                        toast(re.getMsg());
                    } else if (Result.SUCCESS.equals(re)) {

                        doLogin(name, pwd);
                    }
                });

        String spRemember = UserSp.getInstances().getString("remember");
        String loginName = UserSp.getInstances().getString("loginName");
        String loginPwd = UserSp.getInstances().getString("loginPwd");
        usernameEditText.setText(loginName);
        if ("true".equals(spRemember)) {
            remember = true;
            ivRemember.setImageDrawable(icSelected);
            passwordEditText.setText(loginPwd);
        } else {
            remember = false;
            ivRemember.setImageDrawable(icUnSelected);
        }
    }

    private void doLogin(String name, String pwd) {

        try {
            Map map = new HashMap<String, String>(2);
            map.put("loginName", name);
            map.put("loginPwd", pwd);
            Call<UserInfoData> call = RetrofitUtils.getService().login(name, pwd);
            call.enqueue(
                    new Callback<UserInfoData>() {
                        @Override
                        public void onResponse(
                                Call<UserInfoData> call, Response<UserInfoData> response) {

                            try {
                                LogUtil.i(
                                        TAG,
                                        "re:"
                                                + response.isSuccessful()
                                                + ","
                                                + response.message()
                                                + ">>"
                                                + response.body());
                            } catch (Exception e) {
                                LogUtil.e(TAG, "login e > " + e.toString());
                            }

                            if (response.isSuccessful()) {
                                UserInfoData userInfoData = response.body();

                                if (userInfoData != null) {
                                    if (Constants.LOGIN_SUCC.equals(userInfoData.getCode())) {

                                        try {
                                            UserSp.getInstances().saveString("loginName", name);
                                            UserSp.getInstances().saveString("loginPwd", pwd);
                                            if (remember) {
                                                UserSp.getInstances()
                                                        .saveString("remember", "true");
                                            } else {
                                                UserSp.getInstances()
                                                        .saveString("remember", "false");
                                            }
                                            /** 登录成功。保存到本地 */
                                            UserSp.getInstances().saveUser(userInfoData.getData());

                                            UserInfoData.LoginUserData loginUserData =
                                                    UserSp.getInstances().getUser();
                                            LogUtil.i(
                                                    TAG,
                                                    "read from sp:" + loginUserData.toString());

                                            Intent intent =
                                                    new Intent(
                                                            LoginActivity.this, BaseActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        } catch (Exception e) {
                                            LogUtil.e(TAG, "read from sp e:" + e);
                                        }
                                    } else {
                                        toast("登录失败:" + userInfoData.getCode());
                                    }
                                }
                            } else {
                                toast("登录失败:" + response.message());
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
