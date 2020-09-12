package com.xiaobao.good.record;

import android.os.Parcel;
import android.os.Parcelable;

public class RecordDetailItem implements Parcelable {


    public RecordDetailItem() {

    }

    protected RecordDetailItem(Parcel in) {
        filePath = in.readString();
        position = in.readInt();
        type = in.readString();
        file_elpased = in.readLong();
        extra = in.readString();
    }

    public static final Creator<RecordDetailItem> CREATOR = new Creator<RecordDetailItem>() {
        @Override
        public RecordDetailItem createFromParcel(Parcel in) {
            return new RecordDetailItem(in);
        }

        @Override
        public RecordDetailItem[] newArray(int size) {
            return new RecordDetailItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(filePath);
        dest.writeInt(position);
        dest.writeString(type);
        dest.writeLong(file_elpased);
        dest.writeString(extra);
    }

    private String filePath;
    private int position;
    private String type;
    private long file_elpased;

    String extra;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getFile_elpased() {
        return file_elpased;
    }

    public void setFile_elpased(long file_elpased) {
        this.file_elpased = file_elpased;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
