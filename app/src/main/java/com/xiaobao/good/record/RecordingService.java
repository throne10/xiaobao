package com.xiaobao.good.record;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.xiaobao.good.R;
import com.xiaobao.good.common.NotificationUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 录音的 Service
 */

public class RecordingService extends Service {

    private static final String LOG_TAG = "X_RecordingService";

    private String mFileName = null;
    private String mFilePath = null;


    private AudioRecorder audioRecorder;

    private long mStartingTimeMillis = 0;
    private long mElapsedMillis = 0;

    private RecordItem item;
    private Notification notification;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationUtils mNotificationUtils = new NotificationUtils(this);
            Notification.Builder builder2 = mNotificationUtils.getAndroidChannelNotification
                    ("小宝一号录音中", "录音中");
            notification = builder2.build();
        } else {
            //获取一个Notification构造器
            Notification.Builder builder = new Notification.Builder(this);
            Intent nfIntent = new Intent();

            builder.setContentIntent(PendingIntent.
                    getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setContentTitle("小宝一号录音中") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("录音中") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

            notification = builder.build(); // 获取构建好的Notification
        }

        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音

        startForeground(4418, notification);// 开始前台服务
        audioRecorder = AudioRecorder.getInstance();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.hasExtra("start")) {
                Log.i(LOG_TAG, "getParcelableExtra>>>" + intent.getParcelableExtra("item"));
                startRecording(intent.getParcelableExtra("item"));
            } else if (intent.hasExtra("stop")) {
                Log.i(LOG_TAG, "stop");
                stopRecording();
                notifyRecordActivity("stop");
                stopSelf();
            } else if (intent.hasExtra("pause")) {
                Log.i(LOG_TAG, "pause");
                pauseRecord();
                notifyRecordActivity("pause");
            } else if (intent.hasExtra("restart")) {
                Log.i(LOG_TAG, "restart");
                audioRecorder.startRecord(null);
            }

        }
        return START_STICKY;
    }

    private void notifyRecordActivity(String status) {
        if (item != null) {
            item.setStatus(status);
            EventBus.getDefault().post(
                    item);
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        super.onDestroy();
    }

    public void startRecording(RecordItem item) {
        this.item = item;
        audioRecorder.setMp3Path(item.getRootFilePath());
        Log.i(LOG_TAG, "item :" + item.toString());
        mFileName = item.getFileName() + ".pcm";
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


    public void pauseRecord() {
        audioRecorder.pauseRecord();
    }

    public void stopRecording() {
        if (item != null) {
            try {
                audioRecorder.stopRecord();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mElapsedMillis = (System.currentTimeMillis() - mStartingTimeMillis);


            item.setElpased(mElapsedMillis);
        }

    }

}
