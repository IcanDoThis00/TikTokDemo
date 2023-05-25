package com.example.tiktokdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CustomLayoutManager extends LinearLayoutManager implements RecyclerView.OnChildAttachStateChangeListener {
    private int mDrift;//位移，用来判断移动方向

    private PagerSnapHelper mPagerSnapHelper;
    private OnPageSlideListener mOnPageSlideListener;

    public CustomLayoutManager(Context context) {
        super(context);
    }

    public CustomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        mPagerSnapHelper = new PagerSnapHelper();
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        //在 RecyclerView 中，如果重写了该方法，主要是为了在 RecyclerView 视图被附加到窗口时触发一些处理操作
        view.addOnChildAttachStateChangeListener(this);
        mPagerSnapHelper.attachToRecyclerView(view);//实现对对齐屏幕
        super.onAttachedToWindow(view);
    }

    //Item添加进来
//    RecyclerView 的子视图被添加到窗口时被调用，
//    在这个方法中可以完成一些动画效果的初始化或者启动逻辑，
//    以及其它的监听器、事件等注册。
    @Override
    public void onChildViewAttachedToWindow(@NonNull View view) {//Item添加进来
        //播放视频操作，判断将要播放的是上一个视频，还是下一个视频
        if (mDrift > 0) { //向上
            if (mOnPageSlideListener != null)
                mOnPageSlideListener.onPageSelected(getPosition(view), true);
        } else { //向下
            if (mOnPageSlideListener != null)
                mOnPageSlideListener.onPageSelected(getPosition(view), false);
        }
    }

    //Item移除出去
    @Override
    public void onChildViewDetachedFromWindow(@NonNull View view) {
        //暂停播放操作
        if (mDrift >= 0) {
            if (mOnPageSlideListener != null)
                mOnPageSlideListener.onPageRelease(true, getPosition(view));
        } else {
            if (mOnPageSlideListener != null)
                mOnPageSlideListener.onPageRelease(false, getPosition(view));
        }

    }

    @Override
    public void onScrollStateChanged(int state) { //滑动状态监听
        switch (state) {
            case RecyclerView.SCROLL_STATE_IDLE:
                View view = mPagerSnapHelper.findSnapView(this);
                int position = getPosition(view);
                if (mOnPageSlideListener != null) {
                    mOnPageSlideListener.onPageSelected(position, position == getItemCount() - 1);
                }
                break;
        }
    }

//    监听位移变化的方法：scrollVerticallyBy()，这里dy的值为正数是往上滑，负数是往下滑
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.mDrift = dy;
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    //接口注入
    public void setOnPageSlideListener(OnPageSlideListener mOnViewPagerListener) {
        this.mOnPageSlideListener = mOnViewPagerListener;
    }
}


/*PagerSnapHelper 是一个 RecyclerView 辅助类，
用于帮助实现平滑的滚动效果和自动对齐到特定位置，例如 ViewPager。
它可以将 RecyclerView 的 Item 对齐到某个位置上，
即让 RecyclerView 自动展示任意一页的内容，而不是停留在中间，
从而实现类似ViewPager的滚动效果

*/