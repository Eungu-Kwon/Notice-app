package com.eungu.notice.List_Menu.fragments;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class DaySquareButton extends AppCompatButton {
    public DaySquareButton(Context context) {
        super(context);
    }

    public DaySquareButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DaySquareButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        width = Math.min(width, height);
        height = width;

        setMeasuredDimension(width, height);
    }
}