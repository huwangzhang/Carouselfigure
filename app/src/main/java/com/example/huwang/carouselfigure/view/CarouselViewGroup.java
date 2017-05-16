package com.example.huwang.carouselfigure.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by huwang on 2017/5/16.
 * 实现图片轮播
 */

public class CarouselViewGroup extends ViewGroup {

    private int count; // 子视图个数

    private int childheight;  // 子视图宽度

    private int childwidth; // 子视图高度

    private int x; //第一次按下位置的横坐标，移动之前的位置

    private int index; // 索引

    private Scroller mScroller;

    private boolean isAuto = true; // 默认开启启动轮播

    private boolean isClicked;// 是否可点击，点击开关

    private CarouselViewListener mListener;

    public CarouselViewListener getListener() {
        return mListener;
    }

    public void setListener(CarouselViewListener listener) {
        mListener = listener;
    }

    private Timer mTimer = new Timer();
    private TimerTask mTask;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (++index >= count) {
                        index = 0;
                    }
                    scrollTo(childwidth * index, 0);
                    mCarouselViewGroupListener.selectImage(index);
                    break;
            }
        }
    };

    /**
     * 提供的接口，外部点击使用
     */
    public interface CarouselViewListener {
        void clickImageIndex(int pos);
    }

    private void startAuto() {
        isAuto = true;
    }

    private void stopAuto() {
        isAuto = false;
    }

    public CarouselViewGroup(Context context) {
        this(context, null);
    }

    public CarouselViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        mTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        };
        mTimer.schedule(mTask, 100, 3000);
    }

    /**
     * 利用Scroller对象必须实现的方法
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            invalidate();
        }
    }

    /**
     * 事件拦截方法，该方法的返回值为true，自定义的ViewGroup容器就会处理拦截事件
     * 返回true，OntouchEvent方法（真正处理事件的方法）
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * scrollBy, scrollTo
     * Scroller对象
     * 滑动图片就是就是子视图的移动过程
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                index = (scrollX + childwidth / 2) / childwidth;
                if (index < 0) {  // 最左边
                    index = 0;
                } else if (index > count - 1) {  // 最右边
                    index = count - 1;
                }
                if (isClicked) {
                    mListener.clickImageIndex(index);
                } else {
//                scrollTo(index * childwidth, 0);
                    int dx = index * childwidth - scrollX;
                    mScroller.startScroll(scrollX, 0, dx, 0);
                    postInvalidate();
                    mCarouselViewGroupListener.selectImage(index);
                }
                startAuto();
                break;
            case MotionEvent.ACTION_DOWN:
                stopAuto();
                if (mScroller.computeScrollOffset()) {
                    mScroller.abortAnimation();
                }
                isClicked = true;
                x = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int distance = moveX - x;
                scrollBy(-distance, 0);
                x = moveX;
                isClicked = false;
                break;
        }
        return true; // 自己消费掉，不继续传递
    }

    /**
     * 容器内子控件的绘制，采用系统默认的即可
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    // 主要过程：测量->布局->绘制
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 1. 子视图的个数
        count = getChildCount();
        if (0 == count) {
            setMeasuredDimension(0, 0);
        } else {
            // 2. 测量子视图的宽度和高度
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            // ViewGroup的高度为第一个子视图的高度,宽度为所有子视图宽度之和
            View view = getChildAt(0);
            // 3. 求出ViewGroup的高度和宽度

            childheight = view.getMeasuredHeight();
            childwidth = view.getMeasuredWidth();
            int width = childwidth * count; // 所有子视图宽度之和
            setMeasuredDimension(width, childheight);
        }
    }

    /**
     * 自定义ViewGroup中必须实现
     *
     * @param changed 当ViewGroup布局位置发生改变时候为true
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int leftbase = 0;
            for (int i = 0; i < count; i++) {
                View view = getChildAt(i);
                view.layout(leftbase, t, leftbase + childwidth, childheight);
                leftbase += childwidth;
            }
        }
    }

    private CarouselViewGroupListener mCarouselViewGroupListener;

    public CarouselViewGroupListener getCarouselViewGroupListener() {
        return mCarouselViewGroupListener;
    }

    public void setCarouselViewGroupListener(CarouselViewGroupListener carouselViewGroupListener) {
        mCarouselViewGroupListener = carouselViewGroupListener;
    }

    /**
     * 通知自定义布局实现圆点的切换
     */
    public interface CarouselViewGroupListener{
        void selectImage(int pos);
    }
}
