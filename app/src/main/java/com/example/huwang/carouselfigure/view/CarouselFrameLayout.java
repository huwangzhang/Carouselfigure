package com.example.huwang.carouselfigure.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.huwang.carouselfigure.R;
import com.example.huwang.carouselfigure.util.WindowUtils;

import java.util.List;

/**
 * Created by huwang on 2017/5/16.
 */

public class CarouselFrameLayout extends FrameLayout implements CarouselViewGroup.CarouselViewGroupListener, CarouselViewGroup.CarouselViewListener{

    private CarouselViewGroup viewGroup;
    private Activity mActivity;
    private LinearLayout linearLayout;

    public CarouselFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public CarouselFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCarouselView();
        initDotView();
    }


    /**
     * 初始底部圆点布局
     */
    private void initDotView() {
        // 圆点布局实质就是一个LinearLayout
        linearLayout = new LinearLayout(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 40);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.TRANSPARENT);
        addView(linearLayout);

        FrameLayout.LayoutParams layoutParams = (LayoutParams) linearLayout.getLayoutParams();
        // 可以修改圆点位置
        layoutParams.gravity = Gravity.BOTTOM;

        linearLayout.setLayoutParams(layoutParams);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            linearLayout.setAlpha(0.5f);
        } else {
            linearLayout.getBackground().setAlpha(100);
        }
    }

    /**
     * 初始化自定义的图片轮播核心类
     */
    private void initCarouselView() {
        viewGroup = new CarouselViewGroup(getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.setLayoutParams(params);
        viewGroup.setListener(this);
        viewGroup.setCarouselViewGroupListener(this);  // 实现圆点切换
        addView(viewGroup);
    }

    public void addBitmaps(List<Bitmap> list, Activity activity) {
        mActivity = activity;
        for (int i = 0; i < list.size(); i++) {
            Bitmap bitmap = list.get(i);
            addBitmapToCarouselViewGroup(bitmap);
            addDotToLinearLayout();
        }
    }

    private void addDotToLinearLayout() {
        ImageView iv = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5,5,5,5);
        iv.setLayoutParams(layoutParams);
        iv.setImageResource(R.drawable.dot_normal);
        linearLayout.addView(iv);
    }

    private void addBitmapToCarouselViewGroup(Bitmap bitmap) {
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(WindowUtils.getScreenWidth(mActivity), ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView iv = new ImageView(getContext());
        iv.setLayoutParams(lp);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setImageBitmap(bitmap);
        viewGroup.addView(iv);
    }

    @Override
    public void selectImage(int pos) {
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView iv = (ImageView) linearLayout.getChildAt(i);
            if (i == pos) {
                iv.setImageResource(R.drawable.dot_select);
            } else {
                iv.setImageResource(R.drawable.dot_normal);
            }
        }
    }

    public CarouselFrameListener getCarouselFrameListener() {
        return mCarouselFrameListener;
    }

    public void setCarouselFrameListener(CarouselFrameListener carouselFrameListener) {
        mCarouselFrameListener = carouselFrameListener;
    }

    @Override
    public void clickImageIndex(int pos) {
        mCarouselFrameListener.clickImageIndex(pos);
    }

    public interface CarouselFrameListener {
        void clickImageIndex(int pos);
    }
    private CarouselFrameListener mCarouselFrameListener; // 自定义FrameLayout布局的监听器，提供给外部
}
