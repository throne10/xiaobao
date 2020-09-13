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

    @Query("SELECT * FROM RecordHistoryBean WHERE client_id=:visitId")
    List<RecordHistoryBean> getAllByVisitId(int visitId);

    @Query("SELECT * FROM RecordHistoryBean WHERE :path = file_path")
    List<RecordHistoryBean> getSpecificPath(String path);

    @Query("DELETE  FROM RecordHistoryBean")
    int deleteAll();

    @Query("DELETE  FROM RecordHistoryBean WHERE file_path =:filePath")
    int deleteByFilePath(String filePath);

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

    @Query("UPDATE RecordHistoryBean SET cloud= :cloud WHERE file_path = :file_path")
    public abstract int updateCloudStatus(int cloud, String file_path);
}
