package com.example.xyzreader.ui.behavior;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class DynamicHeightImageView extends ImageView {
    private float mAspectRatio = 1.3f;


    public DynamicHeightImageView(Context context) {
        super(context);
    }

    public DynamicHeightImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicHeightImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasure, int heightMeasure) {
        super.onMeasure(widthMeasure, heightMeasure);
        int measureWidth = getMeasuredWidth();
        setMeasuredDimension(measureWidth, (int)(measureWidth / mAspectRatio));
    }

    public void setAspectRation(float aspectRation) {
        mAspectRatio = aspectRation;
        requestLayout();
    }

    public void setImageHeight() {

    }
}
