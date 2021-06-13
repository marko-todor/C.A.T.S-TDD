package com.example.cats;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class PlayerMotionGestureListener extends GestureDetector.SimpleOnGestureListener {

    private CustomView customView;

    public PlayerMotionGestureListener(CustomView customView) {
        this.customView = customView;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        customView.shootRocket = true;
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        customView.bladeSpinning = true;
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}
