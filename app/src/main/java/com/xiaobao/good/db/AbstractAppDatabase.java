package com.xiaobao.good.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.xiaobao.good.InitApplication;
import com.xiaobao.good.db.dao.TestDao;

@Database(
        entities = {
                TestBean.class,
        },
        version = 1,
        exportSchema = false)
public abstract class AbstractAppDatabase extends RoomDatabase {
    private static AbstractAppDatabase db;
    public static final String DB_NAME = "xiaobao.db";
    public static String TAG = "AbstractAppDatabase";

    public static AbstractAppDatabase getDbDateHelper() {
        if (db == null && InitApplication.getContext() != null) {
            initDb(InitApplication.getContext());
        }

        return db;
    }
    public static void initDb(Context context) {
        db =
                Room.databaseBuilder(
                        context,
                        AbstractAppDatabase.class,
                        DB_NAME)
                        .allowMainThreadQueries()
                        .build();
        Log.i(TAG, "initDb success");
    }

    public abstract TestDao getTestDao();
}


