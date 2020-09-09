package com.xiaobao.good;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xiaobao.good.common.CommonUtils;
import com.xiaobao.good.record.RecordItem;
import com.xiaobao.good.record.RecordingService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudioRecordActivity extends Activity {

    private RecordItem recordItem = new RecordItem();

    @BindView(R.id.record_audio_chronometer_time)
    public Chronometer chronometer;


    @BindView(R.id.bt_begin_or_pause)
    public Button begin_pause;


    @BindView(R.id.bt_stop)
    public Button stop;


    @BindView(R.id.bt_play)
    public Button play;


    @BindView(R.id.bt_upload)
    public Button upload;

    @OnClick(R.id.bt_begin_or_pause)
    public void beginOrPauseClick() {

        if (Status.IDEL == nowStus) {
            /**
             * 开始录音
             */
            nowStus = Status.RECORDING;
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            begin_pause.setText("暂停");
            doNewRecord();

        } else if (Status.RECORDING == nowStus) {
            chronometer.stop();
            nowStus = Status.PAUSE;

            doRecordPause();


        } else if (Status.PAUSE == nowStus) {
            chronometer.start();
            begin_pause.setText("录音");
            nowStus = Status.RECORDING;

            doAddRecord();
        }

    }

    private void doRecordPause() {

        Intent intent = new Intent(this, RecordingService.class);

        intent.putExtra("pause", true);

        startService(intent);

    }

    private void doRecordStop() {

        Intent intent = new Intent(this, RecordingService.class);

        intent.putExtra("stop", true);

        startService(intent);

    }

    private void doNewRecord() {

        recordItem.setFileName(System.currentTimeMillis() + "");

        recordItem.setRootFilePath(Environment.getExternalStorageDirectory() + "/SoundRecorder/" + recordItem.getFileName());
        /**
         * 建立一个临时文件夹
         */
        File folder = new File(recordItem.getRootFilePath());
        if (folder.exists()) {
            folder.delete();
        }
        folder.mkdir();

        Intent intent = new Intent(this, RecordingService.class);

        intent.putExtra("start", true);
        intent.putExtra("item", recordItem);

        startService(intent);

    }

    private void doAddRecord() {

        recordItem.setFileCount(recordItem.getFileCount() + 1);

        Intent intent = new Intent(this, RecordingService.class);

        intent.putExtra("start", true);
        intent.putExtra("item", recordItem);

        startService(intent);

    }


    @OnClick(R.id.bt_stop)
    public void stopRecord() {

        if (nowStus == Status.IDEL) {
            toast("未开始录音");
            return;
        }

        chronometer.stop();

        nowStus = Status.IDEL;
        begin_pause.setText("录音");

        doRecordStop();
    }

    @OnClick(R.id.bt_play)
    public void playRecord() {

        if (nowStus != Status.IDEL) {
            toast("未停止录音");
            return;
        }

    }


    @OnClick(R.id.bt_upload)
    public void uploadRecord() {

        if (nowStus != Status.IDEL) {
            toast("未停止录音");
            return;
        }
    }

    private void toast(String msg) {

        Toast.makeText(AudioRecordActivity.this, msg, Toast.LENGTH_SHORT).show();
    }


    public enum Status {
        IDEL, RECORDING, PAUSE;
    }


    private Status nowStus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        ButterKnife.bind(this);


        EventBus.getDefault().register(this);

        nowStus = Status.IDEL;


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RecordItem event) {


        if ("stop".equals(event.getStatus())) {
            Toast.makeText(this, "正在保存录音", Toast.LENGTH_LONG).show();
            //文件合并


//            CommonUtils.amrFileAppend();

        }
    }

}
