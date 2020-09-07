package com.xiaobao.good.log;

import android.text.format.Time;

import com.xiaobao.good.common.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class TimeNow {

    public void getTimeNow() {

    }

    public String getDay() {
        return StringUtils.format(new Date(), "MM-dd");
    }

    public int getWeek() {
        int week = 0;
        Time time = new Time();
        time.setToNow(); // 取得系统时间。  
        week = time.weekDay;
        return week;
    }


    public String getNow() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()); //执行时间
        String dateNow = sDateFormat.format(new Date());
        return dateNow;
    }

}
