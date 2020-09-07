package com.xiaobao.good.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Yuxd created at 2020/2/8 12:55
 */
@Entity(tableName = "TestBean")
public class TestBean {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "action")
    private String action;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", action='" + action + '\'' +
                '}';
    }
}
