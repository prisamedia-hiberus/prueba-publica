package com.diarioas.guialigas.utils.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public abstract class CustomMagneticHorizontalScroll extends HorizontalScrollView {

	public interface ScrollEndListener {

		void onScrollEnd(int x);

		void onItemClicked(int position);

	}

	private static final String TAG = "CUSTOMMAGNETICHORIZONTALSCROLL";

	private ScrollEndListener mOnScrollEndListener;

	// private final Context mContext;

	private boolean currentlyScrolling;
	private boolean currentlyTouching;

	private int currentPosition = 0;
	private int currentOldPosition = -1;

	private int itemWidth;

	private int offset=0;

	public CustomMagneticHorizontalScroll(Context context) {
		super(context);
		// mContext = context;
		init();
	}

	public CustomMagneticHorizontalScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		// mContext = context;
		init();
	}

	public CustomMagneticHorizontalScroll(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// mContext = context;
		init();
	}

	private void init() {

	}

	public void setmOnScrollEndListener(ScrollEndListener scrollEndListener) {
		this.mOnScrollEndListener = scrollEndListener;
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		Log.v("SCROLL", "Esto es la deltaX" + deltaX + " esta es la deltaY "
				+ deltaY + " Esta es la scrollY " + scrollY
				+ " Esta es la scrollRangeY " + scrollRangeY
				+ "maxOverScrollY : " + maxOverScrollY);
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY,
				isTouchEvent);
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);

		if (Math.abs(x - oldx) > 1) {
			currentlyScrolling = true;
		} else {
			currentlyScrolling = false;
			if (!currentlyTouching) {
				onScrollFinish(x, y, oldx, oldy);
			}
		}
		super.onScrollChanged(x, y, oldx, oldy);

	}

	private void onScrollFinish(int x, int y, int oldx, int oldy) {
		calculatePosition();
	}

	private void calculatePosition() {
		int position = this.getScrollX() / itemWidth;
		if ((this.getScrollX() % itemWidth) > itemWidth / 2)
			position++;

		if (currentOldPosition != position) {
			goToPosition(position);
		}
	}



	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			currentlyTouching = true;
			break;
		}

		return super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			currentlyTouching = false;
			if (!currentlyScrolling) {
				// I handle the release from a drag here
				// return true;
				currentOldPosition = -1;
				calculatePosition();
			}
		}
		return super.onTouchEvent(event);
	}

	public void setItemWidth(int itemHeight) {
		this.itemWidth = itemHeight;
	}
	
	protected void goToPosition(int position) {
		currentPosition = position ;
		Log.d(TAG, "goToPosition: "+position);
		currentOldPosition = currentPosition;
		smoothScrollTo(currentPosition);
		if (mOnScrollEndListener != null) {
			mOnScrollEndListener.onScrollEnd(currentPosition);
		}
	}

	public void smoothScrollTo(int itemSelected) {
		this.smoothScrollTo(itemWidth * (itemSelected) + offset, 0);
		resetHeader(itemSelected);
	}

	protected abstract void resetHeader(int itemSelected);


	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

}
