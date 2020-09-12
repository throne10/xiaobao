package com.xiaobao.good;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jaygoo.library.converter.Mp3Converter;
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
    String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.RECORD_AUDIO};
    final String aiffPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "test.aiff";
    final String wavPath = Environment.getExternalStorageDirectory().getPath() + File.separator + "test.wav";
    final String mp3Path = Environment.getExternalStorageDirectory().getPath() + File.separator + "test.mp3";
    long fileSize;
    long bytes = 0;

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

        init();

        TestBean mTestBean = new TestBean();
        mTestBean.setId(1);
        TestDao testDao = AbstractAppDatabase.getDbDateHelper().getTestDao();
        testDao.insert(mTestBean);
        String ss = testDao.getAll() + "";

//        t1.setText(ss);
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

    private void init() {
        putAssetsToSDCard("test.aiff", aiffPath);
        putAssetsToSDCard("test.wav", wavPath);
        startConvert(wavPath);
    }

    private void startConvert(final String sourcePath) {

        //please set your file
        Mp3Converter.init(44100, 1, 0, 44100, 96, 7);
        fileSize = new File(sourcePath).length();
        new Thread(() -> Mp3Converter.convertMp3(sourcePath, mp3Path)).start();

        handler.postDelayed(runnable, 500);
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            bytes = Mp3Converter.getConvertBytes();
            float progress = (100f * bytes / fileSize);
            if (bytes == -1) {
                progress = 100;
            }
            Log.i(TAG, "convert progress: " + progress);
            if (handler != null && progress < 100) {
                handler.postDelayed(this, 1000);
            } else {
                Log.i(TAG, mp3Path);
            }
        }
    };

    public void putAssetsToSDCard(String assetsPath,
                                  String sdCardPath) {
        try {
            String mString[] = getAssets().list(assetsPath);
            if (mString.length == 0) { // 说明assetsPath为空,或者assetsPath是一个文件
                InputStream mIs = getAssets().open(assetsPath); // 读取流
                byte[] mByte = new byte[1024];
                int bt = 0;
                File file = new File(sdCardPath);
                if (!file.exists()) {
                    file.createNewFile(); // 创建文件
                } else {
                    return;//已经存在直接退出
                }
                FileOutputStream fos = new FileOutputStream(file); // 写入流
                while ((bt = mIs.read(mByte)) != -1) { // assets为文件,从文件中读取流
                    fos.write(mByte, 0, bt);// 写入流到文件中
                }
                fos.flush();// 刷新缓冲区
                mIs.close();// 关闭读取流
                fos.close();// 关闭写入流

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
