package com.xiaobao.good.record;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordItem implements Parcelable {


    protected RecordItem(Parcel in) {
        fileName = in.readString();
        fileCount = in.readInt();
        rootFilePath = in.readString();
        status = in.readString();
        elpased = in.readLong();
        process = in.readInt();
    }

    public static final Creator<RecordItem> CREATOR = new Creator<RecordItem>() {
        @Override
        public RecordItem createFromParcel(Parcel in) {
            return new RecordItem(in);
        }

        @Override
        public RecordItem[] newArray(int size) {
            return new RecordItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeInt(fileCount);
        dest.writeString(rootFilePath);
        dest.writeString(status);
        dest.writeLong(elpased);
        dest.writeInt(process);
    }

    public RecordItem() {

    }


    private String fileName;
    private int fileCount = 1;
    private String rootFilePath;
    private String status;
    private long elpased;
    private int process;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    public String getRootFilePath() {
        return rootFilePath;
    }

    public void setRootFilePath(String rootFilePath) {
        this.rootFilePath = rootFilePath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Creator<RecordItem> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "RecordItem{" +
                "fileName='" + fileName + '\'' +
                ", fileCount=" + fileCount +
                ", rootFilePath='" + rootFilePath + '\'' +
                ", status='" + status + '\'' +
                ", elpased=" + elpased +
                '}';
    }

    public long getElpased() {
        return elpased;
    }

    public void setElpased(long elpased) {
        this.elpased = elpased;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }
}
