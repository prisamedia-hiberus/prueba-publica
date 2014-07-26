/**
 * 
 */
package com.diarioas.guiamundial.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * @author Rover
 * 
 */
public class CustomHorizontalScrollView extends HorizontalScrollView {

	/**
	 * @param context
	 */
	public CustomHorizontalScrollView(Context context) {
		super(context);

	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CustomHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public CustomHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;
	}

}
