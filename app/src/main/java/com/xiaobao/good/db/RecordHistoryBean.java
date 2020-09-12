package com.xiaobao.good.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "RecordHistoryBean")
public class RecordHistoryBean {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;


    @ColumnInfo(name = "employee_id")
    private int employee_id;


    @ColumnInfo(name = "client_id")
    private int client_id;


    @ColumnInfo(name = "file_path")
    private String file_path;

    @ColumnInfo(name = "cloud")
    private int cloud;


    @ColumnInfo(name = "file_elpased")
    private long file_elpased;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public int getCloud() {
        return cloud;
    }

    public void setCloud(int cloud) {
        this.cloud = cloud;
    }

    public long getFile_elpased() {
        return file_elpased;
    }

    public void setFile_elpased(long file_elpased) {
        this.file_elpased = file_elpased;
    }


    @Override
    public String toString() {
        return "RecordHistoryBean{" +
                "id=" + id +
                ", employee_id=" + employee_id +
                ", client_id=" + client_id +
                ", file_path='" + file_path + '\'' +
                ", cloud=" + cloud +
                ", file_elpased=" + file_elpased +
                '}';
    }
}
