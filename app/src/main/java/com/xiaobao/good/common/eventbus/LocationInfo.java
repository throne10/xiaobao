package com.xiaobao.good.common.eventbus;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class LocationInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    //纬度
    @ColumnInfo(name = "latitude")
    private String latitude;
    //经度
    @ColumnInfo(name = "lontitude")
    private String lontitude;

    @ColumnInfo(name = "locType")
    private String locType;

    @ColumnInfo(name = "locTypedescription")
    private String locTypedescription;

    @ColumnInfo(name = "addr")
    private String addr;

    @ColumnInfo(name = "describe")
    private String describe;

    @ColumnInfo(name = "coorType")
    private String coorType;

    @ColumnInfo(name = "scanSpan")
    private String scanSpan;

    @ColumnInfo(name = "source")
    private String source;

    @ColumnInfo(name = "sourceSyn")
    private boolean sourceSyn;

    @ColumnInfo(name = "time")
    private String time;

    @ColumnInfo(name = "locationTime")
    private String locationTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLontitude() {
        return lontitude;
    }

    public void setLontitude(String lontitude) {
        this.lontitude = lontitude;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    public String getLocTypedescription() {
        return locTypedescription;
    }

    public void setLocTypedescription(String locTypedescription) {
        this.locTypedescription = locTypedescription;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getCoorType() {
        return coorType;
    }

    public void setCoorType(String coorType) {
        this.coorType = coorType;
    }

    public String getScanSpan() {
        return scanSpan;
    }

    public void setScanSpan(String scanSpan) {
        this.scanSpan = scanSpan;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isSourceSyn() {
        return sourceSyn;
    }

    public void setSourceSyn(boolean sourceSyn) {
        this.sourceSyn = sourceSyn;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
                "id=" + id +
                ", latitude='" + latitude + '\'' +
                ", lontitude='" + lontitude + '\'' +
                ", locType='" + locType + '\'' +
                ", locTypedescription='" + locTypedescription + '\'' +
                ", addr='" + addr + '\'' +
                ", describe='" + describe + '\'' +
                ", coorType='" + coorType + '\'' +
                ", scanSpan='" + scanSpan + '\'' +
                ", source='" + source + '\'' +
                ", sourceSyn=" + sourceSyn +
                ", time='" + time + '\'' +
                ", locationTime='" + locationTime + '\'' +
                '}' + "\n" + "\n";
    }

    public String getLocationTime() {
        return locationTime;
    }

    public void setLocationTime(String locationTime) {
        this.locationTime = locationTime;
    }
}