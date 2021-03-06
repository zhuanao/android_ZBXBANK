package com.pub.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.pub.R;

/**
 * Created by Leader on 2017/5/22.
 *
 */
public class MyLetterListView extends View {

    OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    String[] b = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;

    public MyLetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyLetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    public MyLetterListView(Context context) {
        super(context);
    }


    private int height;
    private int width;
    private int singleHeight;
    private int startY;
    private int wordHeight;

    public void setWord(String[] word) {
        b = word;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.parseColor("#00000000"));
        }


        height = getHeight();
        width = getWidth();
        singleHeight = height / 27;
        startY = (height - singleHeight * b.length) / 2;
        Rect bounds = new Rect();
        paint.getTextBounds("A", 0, 1, bounds);
        wordHeight = bounds.bottom + bounds.height();
        for (int i = 0; i < b.length; i++) {
            paint.setColor(getResources().getColor(R.color.tvc9));
            //paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(24f);
            if (i == choose) {
                paint.setColor(getResources().getColor(R.color.main_tab_text_blue));
                paint.setFakeBoldText(true);//中文仿粗体
            }
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = startY + singleHeight * i + (singleHeight / 2 - wordHeight / 2);
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
//        final int c = (int) (y / getHeight() * b.length);
        final int c = (int) ((y - startY) / singleHeight);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < b.length) {
                        listener.onTouchingLetterChanged(b[c]);
                        choose = c;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c && listener != null) {
                    if (c >= 0 && c < b.length) {
                        listener.onTouchingLetterChanged(b[c]);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

}
