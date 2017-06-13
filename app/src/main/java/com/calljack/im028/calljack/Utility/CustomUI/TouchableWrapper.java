package com.calljack.im028.calljack.Utility.CustomUI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.calljack.im028.calljack.Utility.InterfaceClasses.TouchInterface;


/**
 * Created by IM028 on 5/24/16.
 */
public class TouchableWrapper extends FrameLayout {
    private TouchInterface touchInterface;

    public TouchableWrapper(Context context) {
        super(context);
    }

    public TouchableWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchableWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public TouchableWrapper(Context context, TouchInterface touchInterface) {
        super(context);
        this.touchInterface = touchInterface;
    }

    public void setTouchInterface(TouchInterface touchInterface) {
        this.touchInterface = touchInterface;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (touchInterface != null)
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchInterface.onPressed();
                    break;

                case MotionEvent.ACTION_UP:
                    touchInterface.onReleased();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchInterface.onMove();
                    break;
            }

        return super.onInterceptTouchEvent(ev);
    }
}


