package com.xiaobao.good.record;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jaygoo.library.converter.Mp3Converter;

/**
 * 实现录音
 *
 * @author chenmy0709
 * @version V001R001C01B001
 */
public class AudioRecorder {
    //音频输入-麦克风
    private final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    //采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级
    private final static int AUDIO_SAMPLE_RATE = 44100;
    //声道 单声道
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_STEREO;
    //编码
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    // 缓冲区字节大小
    private int bufferSizeInBytes = 0;

    //录音对象
    private AudioRecord audioRecord;

    //录音状态
    private Status status = Status.STATUS_NO_READY;

    //文件名
    private String fileName;

    //录音文件
    private List<String> filesName = new ArrayList<>();


    //文件名
    private String mp3Path;

    public String getMp3Path() {
        return mp3Path;
    }

    public void setMp3Path(String mp3Path) {
        this.mp3Path = mp3Path;
    }


    /**
     * 类级的内部类，也就是静态类的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用时才会装载，从而实现了延迟加载
     */
    private static class AudioRecorderHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static AudioRecorder instance = new AudioRecorder();
    }

    private AudioRecorder() {
    }

    public static AudioRecorder getInstance() {
        return AudioRecorderHolder.instance;
    }

    /**
     * 创建录音对象
     */
    public void createAudio(String fileName, int audioSource, int sampleRateInHz, int channelConfig, int audioFormat) {
        // 获得缓冲区字节大小
        bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, channelConfig);
        audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes);
        this.fileName = fileName;
    }

    /**
     * 创建默认的录音对象
     *
     * @param fileName 文件名
     */
    public void createDefaultAudio(String fileName) {
        // 获得缓冲区字节大小
        bufferSizeInBytes = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE,
                AUDIO_CHANNEL, AUDIO_ENCODING);
        audioRecord = new AudioRecord(AUDIO_INPUT, AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING, bufferSizeInBytes);
        this.fileName = fileName;
        status = Status.STATUS_READY;
    }


    /**
     * 开始录音
     *
     * @param listener 音频流的监听
     */
    public void startRecord(final RecordStreamListener listener) {

        if (status == Status.STATUS_NO_READY || TextUtils.isEmpty(fileName)) {
            throw new IllegalStateException("录音尚未初始化,请检查是否禁止了录音权限~");
        }
        if (status == Status.STATUS_START) {
            throw new IllegalStateException("正在录音");
        }
        Log.d("AudioRecorder", "===startRecord===" + audioRecord.getState());
        audioRecord.startRecording();

        new Thread(new Runnable() {
            @Override
            public void run() {
                writeDataTOFile(listener);
            }
        }).start();
    }

    /**
     * 暂停录音
     */
    public void pauseRecord() {
        Log.d("AudioRecorder", "===pauseRecord===");
        if (status != Status.STATUS_START) {
            throw new IllegalStateException("没有在录音");
        } else {
            audioRecord.stop();
            status = Status.STATUS_PAUSE;
        }
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        Log.d("AudioRecorder", "===stopRecord===");
        if (status == Status.STATUS_NO_READY || status == Status.STATUS_READY) {
            throw new IllegalStateException("录音尚未开始");
        } else {
            audioRecord.stop();
            status = Status.STATUS_STOP;
            release();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        Log.d("AudioRecorder", "===release===");
        //假如有暂停录音
        try {
            if (filesName.size() > 0) {
                List<String> filePaths = new ArrayList<>();
                for (String fileName : filesName) {
                    filePaths.add(FileUtil.getPcmFileAbsolutePath(fileName));
                }
                //清除
                filesName.clear();
                //将多个pcm文件转化为wav文件
                PCMFilesToMp3ile(filePaths);

//                mergePCMFilesToWAVFile(filePaths);
            } else {
                //这里由于只要录音过filesName.size都会大于0,没录音时fileName为null
                //会报空指针 NullPointerException
                // 将单个pcm文件转化为wav文件
                //Log.d("AudioRecorder", "=====makePCMFileToWAVFile======");
                //makePCMFileToWAVFile();
            }
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }

        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }

        status = Status.STATUS_NO_READY;
    }

    /**
     * 取消录音
     */
    public void canel() {
        filesName.clear();
        fileName = null;
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }

        status = Status.STATUS_NO_READY;
    }


    /**
     * 将音频信息写入文件
     *
     * @param listener 音频流的监听
     */
    private void writeDataTOFile(RecordStreamListener listener) {
        // new一个byte数组用来存一些字节数据，大小为缓冲区大小
        byte[] audiodata = new byte[bufferSizeInBytes];

        FileOutputStream fos = null;
        int readsize = 0;
        try {
            String currentFileName = fileName;
            if (status == Status.STATUS_PAUSE) {
                //假如是暂停录音 将文件名后面加个数字,防止重名文件内容被覆盖
                currentFileName += filesName.size();

            }
            filesName.add(currentFileName);
            File file = new File(FileUtil.getPcmFileAbsolutePath(currentFileName));
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);// 建立一个可存取字节的文件
        } catch (IllegalStateException e) {
            Log.e("AudioRecorder", e.getMessage());
            throw new IllegalStateException(e.getMessage());
        } catch (FileNotFoundException e) {
            Log.e("AudioRecorder", e.getMessage());

        }
        //将录音状态设置成正在录音状态
        status = Status.STATUS_START;
        while (status == Status.STATUS_START) {
            readsize = audioRecord.read(audiodata, 0, bufferSizeInBytes);
            if (AudioRecord.ERROR_INVALID_OPERATION != readsize && fos != null) {
                try {
                    fos.write(audiodata);
                    if (listener != null) {
                        //用于拓展业务
                        listener.recordOfByte(audiodata, 0, audiodata.length);
                    }
                } catch (IOException e) {
                    Log.e("AudioRecorder", e.getMessage());
                }
            }
        }
        try {
            if (fos != null) {
                fos.close();// 关闭写入流
            }
        } catch (IOException e) {
            Log.e("AudioRecorder", e.getMessage());
        }
    }

    /**
     * 可支持多文件合并，暂停录音，但现在应该不需要，只读文件0
     *
     * @param filePaths
     */

    private void PCMFilesToMp3ile(final List<String> filePaths) {
        new Thread(() -> {
            String pcmName = mergePCMFilesToPCM(filePaths);

            String mp3file = mp3Path + "/" + fileName.replace(".pcm", ".mp3");
            Log.i("AudioRecorder", "pcmName>>>" + pcmName + "   mp3file>>>" + mp3file);
            Mp3Converter.init(44100, 1, 1, 22050, 128 * 1024, 0);
            new Thread(() -> Mp3Converter.convertMp3(pcmName, mp3file)).start();
            Runnable runnable = () -> {
                while (true) {
                    long bytes = Mp3Converter.getConvertBytes();
                    Log.i("AudioRecorder", "convert bytes>>>" + bytes);
                    if (bytes == -1) {
                        Log.i("AudioRecorder", "convert progress 100");
                        clearFiles(filePaths);
                        clearFile(pcmName);
                        RecordItem item = new RecordItem();
                        item.setStatus("finish");
                        EventBus.getDefault().post(
                                item);
                        break;
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            new Thread(runnable).start();
            fileName = null;
        }).start();
    }

    private String mergePCMFilesToPCM(List<String> filePaths) {
        byte buffer[] = null;
        String destinationPath = null;
        try {
            if (filePaths.size() == 1) {
                return filePaths.get(0);
            }
            destinationPath = filePaths.get(0) + ".m";

            BufferedOutputStream ouStream = new BufferedOutputStream(new FileOutputStream(
                    destinationPath));
            buffer = new byte[1024 * 4]; // Length of All Files, Total Size

            for (int j = 0; j < filePaths.size(); j++) {
                Log.i("AudioRecorder", "merge>>>" + filePaths.get(j));
                BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(new File(filePaths.get(j))));
                int size;
                while ((size = inStream.read(buffer)) != -1) {
                    ouStream.write(buffer, 0, size);
                }
                inStream.close();
            }
            ouStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return destinationPath;
    }

    /**
     * 将pcm合并成wav
     *
     * @param filePaths
     */
    private void mergePCMFilesToWAVFile(final List<String> filePaths) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String wavName = FileUtil.getWavFileAbsolutePath(fileName);
                Log.e("AudioRecorder", "filePaths " + filePaths.size());
                if (PcmToWav.mergePCMFilesToWAVFile(filePaths, wavName)) {
//                操作成功
                } else {
                    //操作失败
                    Log.e("AudioRecorder", "mergePCMFilesToWAVFile fail");
                    throw new IllegalStateException("mergePCMFilesToWAVFile fail");
                }
                fileName = null;
            }
        }).start();
    }


    /**
     * 将单个pcm文件转化为wav文件
     */
    private void makePCMFileToWAVFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (PcmToWav.makePCMFileToWAVFile(FileUtil.getPcmFileAbsolutePath(fileName), FileUtil.getWavFileAbsolutePath(fileName), true)) {
                    //操作成功
                } else {
                    //操作失败
                    Log.e("AudioRecorder", "makePCMFileToWAVFile fail");
                    throw new IllegalStateException("makePCMFileToWAVFile fail");
                }
                fileName = null;
            }
        }).start();
    }

    /**
     * 获取录音对象的状态
     *
     * @return
     */
    public Status getStatus() {
        return status;
    }

    /**
     * 获取本次录音文件的个数
     *
     * @return
     */
    public int getPcmFilesCount() {
        return filesName.size();
    }

    /**
     * 录音对象的状态
     */
    public enum Status {
        //未开始
        STATUS_NO_READY,
        //预备
        STATUS_READY,
        //录音
        STATUS_START,
        //暂停
        STATUS_PAUSE,
        //停止
        STATUS_STOP
    }

    /**
     * 清除文件
     *
     * @param filePathList
     */
    private static void clearFiles(List<String> filePathList) {
        for (int i = 0; i < filePathList.size(); i++) {
            File file = new File(filePathList.get(i));
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private static void clearFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}