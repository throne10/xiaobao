package com.xiaobao.good;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.xiaobao.good.db.AbstractAppDatabase;
import com.xiaobao.good.db.TestBean;
import com.xiaobao.good.db.dao.TestDao;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.retrofit.result.Clients;
import com.xiaobao.good.retrofit.result.WechatRecord;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "X_MainActivity";
    @BindView(R.id.t1)
    TextView t1;
    String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};

    /**
     * 部分框架例子写法
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        TestBean mTestBean = new TestBean();
        mTestBean.setId(1);
        TestDao testDao = AbstractAppDatabase.getDbDateHelper().getTestDao();
        testDao.insert(mTestBean);
        String ss = testDao.getAll() + "";

        t1.setText(ss);
        //日志
        LogUtil.i(TAG, ss);

        //权限请求操作
        if (EasyPermissions.hasPermissions(this, perms)) {
            //拥有权限进行操作
        } else {
            //没有权限进行提示且申请权限
            EasyPermissions.requestPermissions(this, "应用需要权限，请允许", 0, perms);
        }

        Call<Clients> mResponseBody = RetrofitUtils.getService().getClients("4");
        mResponseBody.enqueue(new Callback<Clients>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Clients> call, Response<Clients> response) {
                //请求处理,输出结果
                LogUtil.i(TAG, response.body().getData().getClients() + "");
                LogUtil.i(TAG, response.body().getData() + "");
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<Clients> call, Throwable throwable) {
                LogUtil.i(TAG, "连接失败");
            }
        });

        new Thread(() -> {
            try {
                Call<Clients> clients = RetrofitUtils.getService().getClients("4");
                Response<Clients> clientsResponse = clients.execute();
                LogUtil.i(TAG, clientsResponse.body().getData().getClients() + "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        File file = new File(Environment.getExternalStorageDirectory() + "/Download/2.png");
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("imgFile", file.getName(), requestBody);
        MultipartBody body = builder.build();//调用即可```
        Call<WechatRecord> wechatRecord = RetrofitUtils.getService().getWechatRecord(body);


        new Thread(() -> {
            try {
                Response<WechatRecord> clientsResponse = wechatRecord.execute();
                LogUtil.i(TAG, clientsResponse.body().getWords_result() + "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //把申请权限的回调交由EasyPermissions处理
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
