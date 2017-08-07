/**
 * 
 */
package com.diarioas.guialigas.utils.viewpager;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author robertosanchez
 * 
 */
public class CustomViewPagerMain extends ViewPager {

	protected int activeFragment;
	protected final ArrayList<Integer> childIds;
	protected final Context mContext;

	public CustomViewPagerMain(Context context, AttributeSet attrs) {
		super(context, attrs);
		childIds = new ArrayList<Integer>();
		mContext = context;
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 * 
	 */
	protected boolean findChild(float x, float y) {
		for (Integer child : childIds) {
			View scroll = findViewById(child);
			if (scroll != null) {
				// Log.d("CVP", "Encontrado Scroll: " + scroll.getId()
				// + " CoordeandasRaw-> X: " + x + " Y: " + y);
				if (inRegion(x, y, scroll)) {
					// Log.d("CVP", "Efectivo Scroll: " + scroll.getId());
					return true;
				}

			}
		}
		return false;
	}

	private boolean inRegion(float x, float y, View v) {
		int[] mCoordBuffer = new int[2];
		v.getLocationOnScreen(mCoordBuffer);
		// Log.d("CVP",
		// "Rect2: " + mCoordBuffer[0] + " "
		// + (mCoordBuffer[0] + v.getWidth()) + " "
		// + mCoordBuffer[1] + " "
		// + (mCoordBuffer[1] + v.getHeight()));
		return mCoordBuffer[0] + v.getWidth() > x && // right edge
				mCoordBuffer[1] + v.getHeight() > y && // bottom edge
				mCoordBuffer[0] < x && // left edge
				mCoordBuffer[1] < y; // top edge
	}

	public void setActiveFragment(int active) {
		this.activeFragment = active;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.ViewPager#setCurrentItem(int)
	 */
	@Override
	public void setCurrentItem(int item) {
		// TODO Auto-generated method stub
		super.setCurrentItem(item);
		setActiveFragment(item);
	}
}
