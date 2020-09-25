package com.xiaobao.good;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xiaobao.good.common.CommonUtils;
import com.xiaobao.good.db.AbstractAppDatabase;
import com.xiaobao.good.db.RecordHistoryBean;
import com.xiaobao.good.db.dao.RecordHistoryDao;
import com.xiaobao.good.record.FileUtil;
import com.xiaobao.good.record.RecordItem;
import com.xiaobao.good.record.RecordingService;
import com.xiaobao.good.retrofit.RetrofitUtils;
import com.xiaobao.good.retrofit.result.RecordUploadResult;
import com.xiaobao.good.sp.UserSp;
import com.xiaobao.good.ui.MyAlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AudioRecordActivity extends BaseActivity2 {

    private static String TAG = "audio_record";

    private RecordItem recordItem = new RecordItem();

    private MediaPlayer mMediaPlayer;
    private File newFile;

    ProgressDialog dialog;
    ProgressDialog uploadDialog;


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
    private AudioRecordActivity context;
    private long base;

    @OnClick({R.id.tv_location_data, R.id.bt_name})
    public void tos(View view) {
        switch (view.getId()) {
            case R.id.tv_location_data:
                Toast.makeText(context, tvLocationData.getText(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_name:
                Toast.makeText(context, btName.getText(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @BindView(R.id.bt_name)
    public Button btName;

    @OnClick(R.id.iv_back)
    public void back() {
        if (!nowStus.can_be_close) {
            Toast.makeText(this, "请先处理完录音再退出！", Toast.LENGTH_LONG).show();
        } else {
            finish();
        }
    }

    private int visitId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @OnClick(R.id.bt_pause_play)
    public void pauseClick() {
        if (nowStus == Status.RECORDING) {
            pause_play.setText("录音");
            nowStus = Status.PAUSE_RECORDING;
            base = SystemClock.elapsedRealtime();
            chronometer.stop();
            doRecordPause();
        } else if (nowStus == Status.PAUSE_RECORDING) {
            pause_play.setText("暂停");
            nowStus = Status.RECORDING;
            chronometer.setBase(chronometer.getBase() + (SystemClock.elapsedRealtime() - base));
            chronometer.start();
            reStartRecord();
        } else {
            toast("当前不在录音状态");
        }

    }

    @OnClick(R.id.bt_play)
    public void playClick() {
        if (StatusPlay.PLAYING == nowStusPlay) {
            play.setText("播放");
            if (mMediaPlayer != null) {
                mMediaPlayer.pause();
                nowStusPlay = StatusPlay.PAUSE_PLAYING;
            }
        } else if (StatusPlay.PAUSE_PLAYING == nowStusPlay) {
            if (Status.MP3DONE == nowStus) {
                mMediaPlayer = new MediaPlayer();
                try {
                    Log.i(TAG, "play :" + newFile.getPath());
                    mMediaPlayer.setDataSource(newFile.getPath());
                    mMediaPlayer.prepare();

                    nowStusPlay = StatusPlay.PLAYING;
                    mMediaPlayer.setOnPreparedListener(mp -> {
                        mMediaPlayer.start();
                        play.setText("停止");
                    });

                    mMediaPlayer.setOnCompletionListener(mp -> {
                        mMediaPlayer.release();
                        nowStusPlay = StatusPlay.PAUSE_PLAYING;
                        play.setText("播放");
                    });
                } catch (IOException e) {
                    Log.e(TAG, "prepare() failed");
                }
            } else {
                toast("正在录音，当前操作无法完成");
            }
        }
    }


    @OnClick(R.id.bt_stop_record)
    public void stopRecord() {

        if (Status.RECORD_STOP == nowStus) {
            toast("已经停止录音");
            return;
        }
        if (nowStus == Status.RECORDING || nowStus == Status.PAUSE_RECORDING) {

            chronometer.stop();

            nowStus = Status.RECORD_STOP;

            doRecordStop();
        } else {
            toast("未开始录音");
            return;
        }
    }


    @OnClick(R.id.bt_upload)
    public void uploadRecord() {

        if (Status.UPLOADING == nowStus) {
            toast("正在提交");
            return;
        }

        if (nowStus == Status.RECORDING) {
            toast("未停止录音");
            return;
        }

        boolean re = false;
        if (nowStus == Status.MP3DONE) {
            re = true;
        }

        if (!re) {
            toast("当前状态不能提交");
            return;
        }


        if (mMediaPlayer != null) {
            try {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }


        /**
         * 上传录音
         */

        /**
         * 上传录音
         */

        nowStus = Status.UPLOADING;


        MyAlertDialog myAlertDialog = new MyAlertDialog(this);
        myAlertDialog.setTitle("是否上传到云端");


        myAlertDialog.setCancelable(false);
        myAlertDialog.setPositiveButton("否", view -> {

            myAlertDialog.dismiss();
            nowStus = Status.MP3DONE;
        });
        myAlertDialog.setNegativeButton("上传", view -> {
            if (myAlertDialog != null) {
                myAlertDialog.dismiss();
            }

            dialog = ProgressDialog.show(this, "提示", "正在提交中", false, false);


            toast("正在提交");
            File file = new File(recordItem.getRootFilePath() + "/" + recordItem.getFileName() + ".mp3");

            RequestBody fileRQ = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("picture", file.getName(), fileRQ);

            RequestBody body = new MultipartBody.Builder()
                    .addFormDataPart("visit_id", visitId + "")
                    .addFormDataPart("voiceFile", file.getName(), fileRQ)
                    .build();
            Call<RecordUploadResult> uploadCall = RetrofitUtils.getService().uploadVoice(body);
            uploadCall.enqueue(new Callback<RecordUploadResult>() {
                @Override
                public void onResponse(Call<RecordUploadResult> call, Response<RecordUploadResult> response) {

                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Log.i(TAG, "recordupload :" + response.body().getCode());
                    String code = response.body().getCode();

                    if ("success".equals(code)) {
                        /**
                         * 更新录音详情
                         */

                        RecordHistoryDao testDao = AbstractAppDatabase.getDbDateHelper().getRecordHistoryDao();
                        testDao.updateCloudStatus(0, file.getPath());


                        toast("提交成功");

                        finish();

                    } else {
                        toast("上传失败");
                    }

                    nowStus = Status.MP3DONE;

                }

                @Override
                public void onFailure(Call<RecordUploadResult> call, Throwable t) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    toast("上传失败");
                    nowStus = Status.MP3DONE;


                }
            });
        });


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

        startRecord();

    }

    private void startRecord() {
        Intent i = new Intent(this, RecordingService.class);

        i.putExtra("item", recordItem);
        i.putExtra("start", true);
        Log.i(TAG, "recordItem>>>" + recordItem);
        startService(i);
    }

    private void reStartRecord() {
        Intent intent = new Intent(this, RecordingService.class);

        intent.putExtra("restart", true);

        startService(intent);
    }

    private void toast(String msg) {

        Toast.makeText(AudioRecordActivity.this, msg, Toast.LENGTH_SHORT).show();
    }


    public enum Status {
        RECORDING(false),
        PAUSE_RECORDING(false),
        RECORD_STOP(false),
        MP3DONE(true),
        UPLOADING(false);


        boolean can_be_close;

        Status(boolean can_be_close) {
            this.can_be_close = can_be_close;
        }
    }

    public enum StatusPlay {
        NULL(true),
        PLAYING(true),
        PAUSE_PLAYING(true);
        boolean can_be_close;

        StatusPlay(boolean can_be_close) {
            this.can_be_close = can_be_close;
        }
    }


    private Status nowStus;
    private StatusPlay nowStusPlay = StatusPlay.PAUSE_PLAYING;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + FileUtil.AUDIO_PCM_BASEPATH);
        String[] fs = f.list();
        new Thread(() -> CommonUtils.clearFiles(fs, Environment.getExternalStorageDirectory().getAbsolutePath() + FileUtil.AUDIO_PCM_BASEPATH)).start();

        ButterKnife.bind(this);
        context = this;
        try {
            String addr = "";
            if (getIntent().hasExtra("location")) {

                addr = getIntent().getStringExtra("location");
            }

            visitId = getIntent().getIntExtra("visitId", -1);

            String name = getIntent().getStringExtra("name");

            tvLocationData.setText(addr);
            Log.i("yxd123", name);
            btName.setText(name);
        } catch (Exception e) {
            Log.i(TAG, "oncreate e:" + e);
        }
        EventBus.getDefault().

                register(this);

        nowStus = Status.RECORDING;//默认进入后开始录音

        doNewRecord();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doRecordStop();
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().unregister(this);//反注册EventBus
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RecordItem event) {

        if ("process".equals(event.getStatus())) {
            Log.i(TAG, "getProcess :" + event.getProcess());
            if (uploadDialog != null) {
                try {
                    uploadDialog.setProgress(event.getProcess());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if ("stop".equals(event.getStatus())) {


            uploadDialog = new ProgressDialog(this);
            uploadDialog.setTitle("请稍等");
            //设置对话进度条样式为水平
            uploadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //设置提示信息
            uploadDialog.setMessage("正在正在转码中......");
            uploadDialog.setCancelable(false);

            uploadDialog.show();
            uploadDialog.setMax(100);

            Toast.makeText(this, "正在保存录音", Toast.LENGTH_LONG).show();
            nowStus = Status.RECORD_STOP;
            //文件合并

            try {
                recordItem.setElpased(event.getElpased());


                RecordHistoryBean recordHistoryBean = new RecordHistoryBean();

                recordHistoryBean.setClient_id(visitId);
                recordHistoryBean.setCloud(1);
                recordHistoryBean.setFile_elpased(recordItem.getElpased());


                recordHistoryBean.setEmployee_id(UserSp.getInstances().getUser().getEmployee_id());
                recordHistoryBean.setFile_path(recordItem.getRootFilePath() + "/" + recordItem.getFileName() + ".mp3");


                Log.i(TAG, "bean :" + recordHistoryBean.toString());
                RecordHistoryDao testDao = AbstractAppDatabase.getDbDateHelper().getRecordHistoryDao();
                testDao.insert(recordHistoryBean);
            } catch (NumberFormatException e) {
                Log.i(TAG, "e :" + e);
            }


        }
        if ("finish".equals(event.getStatus())) {

            if (uploadDialog != null) {
                uploadDialog.setProgress(100);

            }
            Toast.makeText(this, "文件保存完成>>>>" + recordItem.getRootFilePath() + "/" + recordItem.getFileName() + ".mp3", Toast.LENGTH_LONG).show();


            newFile = new File(recordItem.getRootFilePath() + "/" + recordItem.getFileName() + ".mp3");


            nowStus = Status.MP3DONE;


            if (uploadDialog != null) {
                uploadDialog.dismiss();
            }
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
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
