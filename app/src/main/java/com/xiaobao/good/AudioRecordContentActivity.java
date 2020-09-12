package com.xiaobao.good;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.retrofit.result.VoiceContent;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioRecordContentActivity extends Activity {
    @BindView(R.id.tv_record_content)
    TextView tvRecordContent;

    private int voiceId;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record_content);
        ButterKnife.bind(this);
        context = this;
        voiceId = getIntent().getIntExtra("voiceId", 32);
        Call<VoiceContent> voiceContentCall = RetrofitUtils.getService().getVoiceContent(voiceId);
        voiceContentCall.enqueue(new Callback<VoiceContent>() {
            @Override
            public void onResponse(Call<VoiceContent> call, Response<VoiceContent> response) {
                if (response.isSuccessful()) {
                    tvRecordContent.setText(response.body().getData().getVoice_content());
                } else {
                    Toast.makeText(context, "请求语音内容失败。", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<VoiceContent> call, Throwable t) {
                Toast.makeText(context, "请求语音内容失败。", Toast.LENGTH_LONG).show();
            }
        });
    }
}
