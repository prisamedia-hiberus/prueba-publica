/**
 * 
 */
package com.diarioas.guiamundial.utils.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author robertosanchez
 * 
 */
// public class CustomViewPagerLeague extends ViewPager {
public class CustomViewPagerLeague extends CustomViewPagerMain {
	//
	//
	public CustomViewPagerLeague(Context context, AttributeSet attrs) {
		super(context, attrs);
		// childIds.add(R.id.teamGrid);
		// childIds.add(1000);
		// childIds.add(700);
		// childIds.add(1300);
		// childIds.add(1200);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {

		// if (activeFragment == TARGET_FRAGMENT) {

		if (findChild(event.getRawX(), event.getRawY()))
			return false;
		// }
		return super.onInterceptTouchEvent(event);
	}

	/**
	 * @param id
	 */
	public void addChildId(int id) {
		childIds.add(id);

	}

}
