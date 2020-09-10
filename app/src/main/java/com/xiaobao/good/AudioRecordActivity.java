package com.xiaobao.good;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jaygoo.library.converter.Mp3Converter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AudioRecordActivity extends Activity {

    private static String TAG = "AudioRecordActivity";

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

//        http://ineutech.com:60003/xiaobao/api/uploadVoice


        RequestBody requestFile = RequestBody.create(MediaType.parse("audio/*"), newFile);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("imageFile", newFile.getName(), requestFile);


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
        PLAYING, PAUSE_PLAYING, RECORDING, RECORDING_PAUSE, RECORD_STOP, UPLOADING;
    }


    private Status nowStus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        ButterKnife.bind(this);


        EventBus.getDefault().register(this);

        nowStus = Status.RECORDING;//默认进入后开始录音

        doNewRecord();


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

            newFile = new File(event.getRootFilePath(), event.getFileName() + ".amr");

            File mp3File = new File(event.getRootFilePath(), event.getFileName() + ".mp3");
            File root = new File(event.getRootFilePath());

            List<File> listv = new ArrayList<>();

            for (int i = 1; i <= event.getFileCount(); i++) {
                File child = new File(root, event.getFileName() + "_" + i + ".amr");

                listv.add(child);
            }


            CommonUtils.amrFileAppend(newFile, listv);


            CommonUtils.deleteFiles(listv);


            //please set your file
            Mp3Converter.init(44100, 1, 0, 44100, 96, 7);
            fileSize = newFile.length();
            new Thread(() -> Mp3Converter.convertMp3(newFile.getPath(), mp3File.getPath())).start();

            handler.postDelayed(runnable, 500);
        }


    }

    long fileSize;
    long bytes = 0;

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
                Log.i(TAG, "!!!");
            }
        }
    };

}
