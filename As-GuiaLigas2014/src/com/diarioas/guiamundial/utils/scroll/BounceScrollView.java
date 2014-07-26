package com.diarioas.guiamundial.utils.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class BounceScrollView extends ScrollView {

	private static final int BOTTOM_LIMIT_DEFAULT = 0;
	private int bottomLimit = BOTTOM_LIMIT_DEFAULT;

	public interface onChangeScrollListener {
		public void onChangeScroll(int x, int y, int oldX, int oldY);
	}

	private int mMaxYOverscrollDistance = 0;
	private onChangeScrollListener mOnChangeScrollListener = null;
	private boolean allowBounceBottom = true;

	public BounceScrollView(Context context) {
		super(context);
	}

	public BounceScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BounceScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// This is where the magic happens, we have replaced the incoming
		// maxOverScrollY with our own custom variable mMaxYOverscrollDistance;

		// Log.v("Scroll position", "Esto es la deltaX: " + deltaX
		// + " esta es la deltaY: " + deltaY + " Esta es la scrollY: "
		// + scrollY + " Esta es la scrollRangeY: " + scrollRangeY
		// + " maxOverScrollY : " + maxOverScrollY);

		if ((scrollY >= bottomLimit) && (deltaY > 0)) {
			if (allowBounceBottom) {
				return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
						scrollRangeX, scrollRangeY, maxOverScrollX,
						mMaxYOverscrollDistance, isTouchEvent);
			} else {
				return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
						scrollRangeX, scrollRangeY, maxOverScrollX, 0,
						isTouchEvent);
			}
		} else {
			// deltaY / 2 to decrese the speed
			return super.overScrollBy(deltaX, deltaY / 2, scrollX, scrollY,
					scrollRangeX, scrollRangeY, maxOverScrollX,
					mMaxYOverscrollDistance, isTouchEvent);
		}

	}

	//
	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent event) {
	// int pointerIndex = 0;
	// event.getY(pointerIndex);
	// switch (event.getAction()) {
	// case MotionEvent.ACTION_DOWN:
	// Log.d("SCROLL", "Y: " + pointerIndex);
	// // if (event.getY())
	// break;
	// case MotionEvent.ACTION_SCROLL:
	// float pos = this.getScrollY();
	// Log.d("SCROLL", "Y: " + this.getScrollY());
	// break;
	// }
	// return super.onInterceptTouchEvent(event);
	// }

	public int getMaxYOverscrollDistance() {
		return mMaxYOverscrollDistance;
	}

	public void setMaxYOverscrollDistance(int maxYOverscrollDistance) {
		this.mMaxYOverscrollDistance = maxYOverscrollDistance;
	}

	public onChangeScrollListener getOnChangeScrollListener() {
		return mOnChangeScrollListener;
	}

	public void setOnChangeScrollListener(
			onChangeScrollListener onChangeScrollListener) {
		this.mOnChangeScrollListener = onChangeScrollListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldX, int oldY) {
		super.onScrollChanged(x, y, oldX, oldY);

		if (mOnChangeScrollListener != null) {
			mOnChangeScrollListener.onChangeScroll(x, y, oldX, oldY);
		}
	}

	public boolean isAllowBounceBottom() {
		return allowBounceBottom;
	}

	public void setAllowBounceBottom(boolean allowBounceBottom) {
		this.allowBounceBottom = allowBounceBottom;
	}

	/**
	 * @param bottom_limit
	 *            the bottom_limit to set
	 */
	public void setBottomLimit(int bottom_limit) {
		bottomLimit = bottom_limit;
	}

}
