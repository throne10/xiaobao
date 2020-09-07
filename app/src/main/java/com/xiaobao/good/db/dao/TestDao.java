package com.xiaobao.good.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.xiaobao.good.db.TestBean;

import java.util.List;

/**
 * @author Yuxd created at 2020/2/8 13:00
 */
@Dao
public interface TestDao {

    @Query("SELECT * FROM TestBean")
    List<TestBean> getAll();

    @Query("DELETE  FROM TestBean")
    int deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(TestBean... testBeans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(TestBean testBean);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<TestBean> testBeans);

    @Update
    int update(TestBean... testBeans);

    @Update()
    int updateAll(TestBean... testBeans);

    @Update()
    int updateAll(List<TestBean> testBeans);

    @Delete
    int delete(TestBean testBean);

    @Delete
    int deleteAll(List<TestBean> testBeans);

    @Delete
    int deleteAll(TestBean... testBeans);
}
