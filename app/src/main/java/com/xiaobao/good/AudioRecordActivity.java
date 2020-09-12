package com.xiaobao.good;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xiaobao.good.record.RecordItem;
import com.xiaobao.good.record.RecordingService;
import com.xiaobao.good.ui.MyAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudioRecordActivity extends Activity {

    private static String TAG = "X_AudioRecordActivity";

    private RecordItem recordItem = new RecordItem();

    private MediaPlayer mMediaPlayer;
    private File newFile;

    @BindView(R.id.record_audio_chronometer_time)
    public Chronometer chronometer;


    @BindView(R.id.bt_pause_play)
    public Button pause_play;


    @BindView(R.id.bt_stop_record)
    public Button stop_record;


    @BindView(R.id.bt_play)
    public Button play;


    @BindView(R.id.bt_upload)
    public Button upload;

    @BindView(R.id.tv_location_data)
    TextView tvLocationData;

    @BindView(R.id.bt_name)
    public Button btName;

    @OnClick(R.id.iv_back)
    public void back() {
        if (nowStus == Status.RECORDING) {
            Toast.makeText(this, "请先处理完录音再退出！", Toast.LENGTH_LONG).show();
        } else if (nowStus == Status.RECORD_STOP) {
            Toast.makeText(this, "录音转码中请稍等！", Toast.LENGTH_LONG).show();
        } else {
            finish();
        }
    }

    private String visitId;

    @OnClick(R.id.bt_pause_play)
    public void pauseClick() {

        if (Status.PLAYING != nowStus) {
            toast("当前未在播放");
            return;
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.pause();

            nowStus = Status.PAUSE_PLAYING;

        }

    }

    @OnClick(R.id.bt_play)
    public void playClick() {

        if (Status.RECORDING == nowStus) {
            toast("录音未停止");
            return;

        }

        if (Status.RECORD_STOP == nowStus) {

            mMediaPlayer = new MediaPlayer();

            try {
                mMediaPlayer.setDataSource(newFile.getPath());
                mMediaPlayer.prepare();

                nowStus = Status.PLAYING;
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mMediaPlayer.start();
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, "prepare() failed");
            }
        } else if (Status.PAUSE_PLAYING == nowStus) {

            if (mMediaPlayer != null) {
                mMediaPlayer.start();
            }
        }

    }


    @OnClick(R.id.bt_stop_record)
    public void stopRecord() {

        if (nowStus != Status.RECORDING) {
            toast("未开始录音");
            return;
        }

        chronometer.stop();

        nowStus = Status.RECORD_STOP;

        doRecordStop();
    }


    @OnClick(R.id.bt_upload)
    public void uploadRecord() {

        if (nowStus == Status.RECORDING) {
            toast("未停止录音");
            return;
        }

        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
        }

        /**
         * 上传录音
         */
        MyAlertDialog myAlertDialog = new MyAlertDialog(this);
        myAlertDialog.setPositiveButton("保存到本地", view -> {

        });
        myAlertDialog.setNegativeButton("上传", view -> {

        });
//        http://ineutech.com:60003/xiaobao/api/uploadVoice


//        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/*"), newFile);
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("imageFile", newFile.getName(), requestFile);


//
//        String descriptionString = "This is a description";
//        RequestBody description =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"), descriptionString);
//
//        Call<ResponseBody> call = service.upload(description, body);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call,
//                                   Response<ResponseBody> response) {
//                System.out.println("success");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                t.printStackTrace();
//            }
    }


    @Deprecated
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

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        recordItem.setFileName(System.currentTimeMillis() + "");

        recordItem.setRootFilePath(Environment.getExternalStorageDirectory() + "/SoundRecorder/" + recordItem.getFileName());
        /**
         * 建立一个临时文件夹
         */
        File folder = new File(recordItem.getRootFilePath());

        Log.i(TAG, "root file :" + folder);
        if (folder.exists()) {
            folder.delete();
        }
        folder.mkdirs();

        Log.i(TAG, "root file :" + folder.exists());

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


    private void toast(String msg) {

        Toast.makeText(AudioRecordActivity.this, msg, Toast.LENGTH_SHORT).show();
    }


    public enum Status {
        PLAYING, PAUSE_PLAYING, RECORDING, RECORDING_PAUSE, RECORD_STOP, MP3DONE, UPLOADING;
    }


    private Status nowStus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        ButterKnife.bind(this);
        String addr = getIntent().getStringExtra("location");
        visitId = getIntent().getStringExtra("visitId");
        String name = getIntent().getStringExtra("name");
//        if (StringUtils.isNotEmpty(addr)) {
//            try {
//                addr = addr.substring(addr.indexOf("省") + 1);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        tvLocationData.setText(addr);
        Log.i("yxd123", name);
        btName.setText(name);
        EventBus.getDefault().

                register(this);

        nowStus = Status.RECORDING;//默认进入后开始录音

        doNewRecord();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doRecordStop();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RecordItem event) {


        if ("stop".equals(event.getStatus())) {
            Toast.makeText(this, "正在保存录音", Toast.LENGTH_LONG).show();
            nowStus = Status.RECORD_STOP;
            //文件合并
        }
        if ("finish".equals(event.getStatus())) {
            Toast.makeText(this, "文件保存完成>>>>" + recordItem.getRootFilePath() + "/" + recordItem.getFileName() + ".mp3", Toast.LENGTH_LONG).show();
            nowStus = Status.MP3DONE;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (nowStus == Status.RECORDING) {
                Toast.makeText(this, "请先处理完录音再退出！", Toast.LENGTH_LONG).show();
                return false;
            } else if (nowStus == Status.RECORD_STOP) {
                Toast.makeText(this, "录音转码中请稍等！", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
