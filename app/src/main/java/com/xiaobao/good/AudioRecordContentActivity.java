package com.xiaobao.good;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.retrofit.result.VoiceContent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioRecordContentActivity extends BaseActivity2 {
    @BindView(R.id.tv_record_content)
    TextView tvRecordContent;

    private String voiceCont;
    private Context context;

    @OnClick(R.id.bt_back)
    public void back() {
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record_content);
        ButterKnife.bind(this);
        context = this;
        voiceCont = getIntent().getStringExtra("voiceCont");
        tvRecordContent.setText(voiceCont);
    }
}
