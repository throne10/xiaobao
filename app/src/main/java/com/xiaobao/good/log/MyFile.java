package com.xiaobao.good.log;

import android.os.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MyFile {
    private File addressTxt;
    private String zipName;
    private String path;

    public MyFile() {
        path = Environment.getExternalStorageDirectory() + "/XiaoBao";
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        String fileName = null;
        fileName = fileName();
        addressTxt = new File(path, fileName);
    }

    private String fileName() {
        String fileName = null;
        TimeNow time = new TimeNow();

        String day = time.getDay();
        int week = time.getWeek();
        String data = day;
        fileName = "xiaobao" + week + "_" + data + "_log.txt";
        return fileName;
    }

    private String startFileName() {
        String fileName = null;
        TimeNow time = new TimeNow();
        int week = time.getWeek();
        fileName = "te" + week + "";
        return fileName;
    }

    public long getFileSize() {
        long size = 0;
        if (addressTxt.exists()) {
            size = addressTxt.length();
        }
        return size;
    }

    public void deleteOldFile() {

        File file = new File(path);

        String startFileName = null;
        startFileName = path + startFileName() + "";

        String nowFileName = null;
        nowFileName = path + fileName() + "";

        if (file.isDirectory()) {
            File[] fileArray = file.listFiles();
            if (null != fileArray && 0 != fileArray.length) {
                for (int i = 0; i < fileArray.length; i++) {
                    String fileName = null;
                    fileName = fileArray[i] + "";
                    if (fileName.startsWith(startFileName)) {
                        if (!fileName.equals(nowFileName)) {
                            deleteFile(fileName);
                        }
                    }
                }
            }
        }
    }

    private void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }

    public void saveAddressTxt(String log) {
        try {
            if (!addressTxt.exists()) {
                addressTxt.createNewFile();
            }
            FileWriter fw = new FileWriter(addressTxt, true);
            fw.write(log + "\r\n");
            fw.close();

            //  saveAddressZip();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getZipName() {
        return zipName;
    }

}