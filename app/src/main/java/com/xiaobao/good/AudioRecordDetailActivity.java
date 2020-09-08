package com.xiaobao.good;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class AudioRecordDetailActivity extends FragmentActivity {


    ViewPager pager;
//    private List<MyFragment> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_detail);

        //初始化Fragment数组
        pager = (ViewPager) this.findViewById(R.id.pager);
//        list = new ArrayList<MyFragment>();
//        list.add(new MyFragment());
//        list.add(new MyFragment());
//
//
//        //创建自定的适配器对象
//        MyAdapter adapter = new MyAdapter(getSupportFragmentManager(), list);
//        pager.setAdapter(adapter);
//
//
//        textView[0].setBackgroundColor(Color.YELLOW);//初始化默认第一个为选中状态
//
//
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            /**
             * pagerView被切换后自动执行的方法
             */
            public void onPageSelected(int position) {


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
