package com.xiaobao.good.widget.recyclerview;


class CheckRecyclerAdapter {
    static boolean checkInRange(int size, int position) {
        return (position >= 0 && position < size);
    }

    static boolean checkExits(int position) {
        return (position != -1);
    }

    static boolean haveYouRegistered(int type) {
        return (type == -1);
    }
}
