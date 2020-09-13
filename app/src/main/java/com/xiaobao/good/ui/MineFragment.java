package com.xiaobao.good.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xiaobao.good.R;
import com.xiaobao.good.SettingActivity;
import com.xiaobao.good.log.LogUtil;
import com.xiaobao.good.retrofit.result.UserInfoData;
import com.xiaobao.good.sp.UserSp;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends Fragment {

    private static final String TAG = "fragment_MF";

    private Unbinder unbinder;

    @BindView(R.id.tv_name)
    TextView tvName;

    @OnClick(R.id.iv_sys)
    public void settings() {
        Intent intent = new Intent(this.getContext(), SettingActivity.class);
        this.getContext().startActivity(intent);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragement_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LogUtil.d(TAG, "onViewCreated");
        UserInfoData.LoginUserData user = UserSp.getInstances().getUser();
        if (user != null) {
            tvName.setText(user.getEmployee_name());
        }
    }
}
