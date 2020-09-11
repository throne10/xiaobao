package com.xiaobao.good.record;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

/**
 * 录音的 Service
 */

public class RecordingService extends Service {

    private static final String LOG_TAG = "X_RecordingService";

    private String mFileName = null;
    private String mFilePath = null;

//    private MediaRecorder mRecorder = null;

    private AudioRecorder audioRecorder;

    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;

    private RecordItem item;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        audioRecorder = AudioRecorder.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.hasExtra("start")) {
            startRecording(intent.getParcelableExtra("item"));
        } else if (intent.hasExtra("stop")) {
            Log.i(LOG_TAG, "stop");
            stopRecording();
            notifyRecordActivity("stop");
        } else if (intent.hasExtra("pause")) {
            Log.i(LOG_TAG, "pause");

            stopRecording();
            notifyRecordActivity("pause");
        }


        return START_STICKY;
    }

    private void notifyRecordActivity(String status) {
        item.setStatus(status);
        EventBus.getDefault().post(
                item);
    }

    @Override
    public void onDestroy() {
        if (audioRecorder != null) {
            audioRecorder.release();
        }
        super.onDestroy();
    }

    public void startRecording(RecordItem item) {
        this.item = item;
        audioRecorder.setMp3Path(item.getRootFilePath());
        Log.i(LOG_TAG, "item :" + item.toString());
        setFileNameAndPath();
        try {
            if (audioRecorder.getStatus() == AudioRecorder.Status.STATUS_NO_READY) {
                //初始化录音
                audioRecorder.createDefaultAudio(mFileName);
                audioRecorder.startRecord(null);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mStartingTimeMillis = System.currentTimeMillis();

    }

    public void setFileNameAndPath() {
        mFileName = item.getFileName() + ".pcm";
        Log.i(LOG_TAG, "file name :" + mFileName);
    }

    public void stopRecording() {
        audioRecorder.stopRecord();
        mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);

        getSharedPreferences("sp_name_audio", MODE_PRIVATE)
                .edit()
                .putString("audio_path", mFilePath)
                .putLong("elpased", mElapsedMillis)
                .apply();

    }

}
