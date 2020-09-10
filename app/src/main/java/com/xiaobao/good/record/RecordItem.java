package com.xiaobao.good.record;

import android.os.Parcel;
import android.os.Parcelable;

import com.xiaobao.good.AudioRecordActivity;

public class RecordItem implements Parcelable {


    protected RecordItem(Parcel in) {
        fileName = in.readString();
        fileCount = in.readInt();
        rootFilePath = in.readString();
        status = in.readString();
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
    }

    public RecordItem() {

    }


    private String fileName;
    private int fileCount = 1;
    private String rootFilePath;
    private String status;

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

    @Override
    public String toString() {
        return "RecordItem{" +
                "fileName='" + fileName + '\'' +
                ", fileCount=" + fileCount +
                ", rootFilePath='" + rootFilePath + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
