package com.xiaobao.good.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xiaobao.good.db.RecordHistoryBean;
import com.xiaobao.good.db.TestBean;

import java.util.List;


@Dao
public interface RecordHistoryDao {

    @Query("SELECT * FROM RecordHistoryBean")
    List<RecordHistoryBean> getAll();

    @Query("DELETE  FROM RecordHistoryBean")
    int deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(RecordHistoryBean... testBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(RecordHistoryBean testBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<RecordHistoryBean> testBeans);

    @Update
    int update(RecordHistoryBean... testBeans);

    @Update()
    int updateAll(RecordHistoryBean... testBeans);

    @Update()
    int updateAll(List<RecordHistoryBean> testBeans);

    @Delete
    int delete(RecordHistoryBean testBean);

    @Delete
    int deleteAll(List<RecordHistoryBean> testBeans);

    @Delete
    int deleteAll(RecordHistoryBean... testBeans);
}
