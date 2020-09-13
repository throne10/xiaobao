package com.xiaobao.good;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.xiaobao.good.ui.ClientFragment;
import com.xiaobao.good.ui.MineFragment;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity_BA";

    @BindColor(R.color.color_text_white)
    int colorWhite;

    @BindColor(R.color.color_yellow)
    int colorYellow;

    @BindDrawable(R.mipmap.costum_unselected)
    Drawable costumUnselected;

    @BindDrawable(R.mipmap.costum_selected)
    Drawable costumSelected;

    @BindDrawable(R.mipmap.mine_unselected)
    Drawable mineUnselected;

    @BindDrawable(R.mipmap.mine_selected)
    Drawable mineSelected;

    @BindView(R.id.tv_client)
    TextView tvClient;

    @BindView(R.id.iv_client)
    ImageView ivClient;
    private static BaseActivity a;

    @OnClick({R.id.tv_client, R.id.iv_client})
    public void selectClient() {
        tvClient.setTextColor(colorYellow);
        ivClient.setImageDrawable(costumSelected);
        tvMine.setTextColor(colorWhite);
        ivMine.setImageDrawable(mineUnselected);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mClientFragment)
                .commitNow();
    }

    @BindView(R.id.tv_mine)
    TextView tvMine;

    @BindView(R.id.iv_mine)
    ImageView ivMine;

    @OnClick({R.id.tv_mine, R.id.iv_mine})
    public void selectMine() {
        tvClient.setTextColor(colorWhite);
        ivClient.setImageDrawable(costumUnselected);
        tvMine.setTextColor(colorYellow);
        ivMine.setImageDrawable(mineSelected);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, mMineFragment)
                .commitNow();
    }

    private ClientFragment mClientFragment;
    private MineFragment mMineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        ButterKnife.bind(this);
        mClientFragment = new ClientFragment();
        mMineFragment = new MineFragment();
        a = this;
        if (savedInstanceState == null) {
            selectClient();
        }
    }

    public static void finishBase() throws Exception {
        a.finish();
    }
}
