/**
 * 
 */
package com.diarioas.guialigas.utils.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author robertosanchez
 * 
 */
public class CustomViewPagerStadium extends ViewPager {

	protected final Context mContext;
	private float lastX;
	private boolean slidingLeft;
	private boolean slidingRight;

	public CustomViewPagerStadium(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	@Override
	protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
		if (v != this && v instanceof ViewPager) {
			int currentItem = ((ViewPager) v).getCurrentItem();
			int countItem = ((ViewPager) v).getAdapter().getCount();
			if ((currentItem == (countItem - 1) && dx < 0)
					|| (currentItem == 0 && dx > 0)) {
				return false;
			}
			return true;
		}
		return super.canScroll(v, checkV, dx, x, y);
	}
}
