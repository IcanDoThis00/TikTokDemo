package com.example.tiktokdemo;

public interface OnPageSlideListener {
    //释放监听
    void onPageRelease(boolean isNext,int position);
    //选中的监听以及判断是否滑动到底部
    void onPageSelected(int position,boolean isBottom);
}
