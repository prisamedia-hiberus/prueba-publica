package com.prisadigital.realmedia.adlib.animations;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ExpandAnimation extends Animation {
    private View view;
    private int  fromHeight;
    private int  toHeight;

    public ExpandAnimation(View view, float fromY, float toY) {
        // Save view reference
        this.view = view;

        // Calculate sizes
        fromHeight = (int) (view.getHeight() * fromY);
        toHeight = (int) (view.getHeight() * toY);
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        // Calculate new height
        int newHeight = (int) (fromHeight + (toHeight - fromHeight) * interpolatedTime);

        // Update view and layout
        view.getLayoutParams().height = newHeight;
        view.requestLayout();
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
