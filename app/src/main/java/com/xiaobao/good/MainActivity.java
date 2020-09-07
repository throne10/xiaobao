package com.xiaobao.good;

import android.Manifest;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.xiaobao.good.db.AbstractAppDatabase;
import com.xiaobao.good.db.TestBean;
import com.xiaobao.good.db.dao.TestDao;
import com.xiaobao.good.log.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.t1)
    TextView t1;
    private static final String TAG = "X_MainActivity";
    String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

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

        LogUtil.i(TAG, ss);

        if (EasyPermissions.hasPermissions(this, perms)) {
            //拥有权限进行操作
        } else {
            //没有权限进行提示且申请权限
            EasyPermissions.requestPermissions(this, "应用需要权限，请允许", 0, perms);
        }
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
