package com.xiaobao.good.log;

import android.util.Log;

import com.xiaobao.good.common.DateUtil;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class LogUtil {

    private static ExecutorService executor;

    private static int DEBUG_LEVEL = 1;
    private static int INFO_LEVEL = 2;
    private static int WARN_LEVEL = 3;
    private static int ERROR_LEVEL = 4;

    private static int LogLevel = DEBUG_LEVEL;

    private static synchronized ExecutorService getExecutor() {
        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
            try {
                LogLevel = 1;

            } catch (Exception e) {
            }
        }
        return executor;
    }

    public static String makeLogTag(Class<?> cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static String makeLogTag(String cls) {
        return "te" + cls;
    }

    public static boolean assertLogLevel(int l) {
        return l >= LogLevel;
    }

    /**
     * debug
     * 
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {

        if (!assertLogLevel(DEBUG_LEVEL))
            return;

        Log.d(tag, msg);
        saveNote(tag, msg);
    }

    /**
     * info
     * 
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (!assertLogLevel(INFO_LEVEL))
            return;

        Log.i(tag, msg);
        saveNote(tag, msg);
    }

    /**
     * warn
     * 
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
        if (!assertLogLevel(WARN_LEVEL))
            return;

        Log.w(tag, msg);
        saveNote(tag, msg);
    }

    /**
     * error
     * 
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (!assertLogLevel(ERROR_LEVEL))
            return;

        Log.e(tag, msg);
        saveNote(tag, msg);
    }

    public static void e(String tag, String msg, Throwable e) {
        if (!assertLogLevel(ERROR_LEVEL))
            return;

        Log.e(tag, msg, e);
        saveNote(tag, msg);
    }

    private static void saveNote(final String tag, final String msg) {
        getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                    MyFile file = new MyFile();
                    file.saveAddressTxt(DateUtil.DateToStringHMS(new Date()) + " " + tag + " " + msg);
                }
            }
        });
    }

    //文件保存有效天数
    private static final int FILE_VALID_TIME = 7;

}
