package com.example.huwang.carouselfigure;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.huwang.carouselfigure.view.CarouselFrameLayout;

import static com.example.huwang.carouselfigure.util.WindowUtils.initBitmaps;

public class MainActivity extends Activity implements CarouselFrameLayout.CarouselFrameListener {

    private CarouselFrameLayout mGroup;

    private int[] ids = new int[] {
            R.drawable.girl1,
            R.drawable.girl2,
            R.drawable.girl3,
            R.drawable.girl4,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGroup = (CarouselFrameLayout) findViewById(R.id.image_group);

        mGroup.addBitmaps(initBitmaps(ids, this), this);
        mGroup.setCarouselFrameListener(this);
    }



    @Override
    public void clickImageIndex(int pos) {
        Toast.makeText(this, "pos = " + pos, Toast.LENGTH_SHORT).show();
    }
}
