package com.diarioas.guialigas.utils.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.diarioas.guialigas.utils.DimenUtils;

public class CustomScrollView extends ScrollView {

	private int heightTop = 0;
	private int heightBottom = 0;

	private boolean scrollTo;
	private boolean scrollOldTo;
	private boolean canStop = true;
	private int offset;

	public CustomScrollView(Context context) {
		super(context);
		init();
	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// mContext = context;
		init();
	}

	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// mContext = context;
		init();
	}

	private void init() {
		offset = DimenUtils.getRegularPixelFromDp(getContext(), 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View#onScrollChanged(int, int, int, int)
	 */
	@Override
	protected void onScrollChanged(int x, int y, int oldX, int oldY) {

		// Detectar si va hacia arriba o hacia abajo
		if (y > oldY + offset && y >= 1.5 * heightTop) {
			scrollTo = false;
		} else if (y < oldY - offset
				&& y <= getHeightTotal() - 1.5 * heightBottom) {
			scrollTo = true;
		}

		// Cambia el sentido -> reseteo
		if (scrollOldTo != scrollTo) {
			canStop = true;
			scrollOldTo = scrollTo;
		}

		if (canStop && scrollTo && haveStopOnTopView(y)) {
			// Puede parar, va hacia arriba y debe pararse
			this.scrollTo(0, heightTop - 1);

		} else if (canStop && !scrollTo && haveStopOnBottomView(y)) {
			// Puede parar, va hacia abajo y debe pararse
			this.scrollTo(0, getHeightTotal() - heightBottom + 1);
		} else
			// Ejecución normal
			super.onScrollChanged(x, y, oldX, oldY);

		// Se ha parado-> permitimos acceder a las vistas ocultas
		if (y - oldY == 0) {
			canStop = false;
		}
	}

	private boolean haveStopOnTopView(int y) {
		return y < heightTop;
	}

	private boolean haveStopOnBottomView(int y) {
		if (y > getHeightTotal() - heightBottom)
			return true;
		else
			return false;
	}

	/**
	 * @param heightTop
	 *            the heightTop to set
	 */
	public void setHeightTop(int heightTop) {
		this.heightTop = heightTop;
	}

	/**
	 * @param heightBottom
	 *            the heightBottom to set
	 */
	public void setHeightBottom(int heightBottom) {
		this.heightBottom = heightBottom;
	}

	/**
	 * @param i
	 */
	public int getHeightTotal() {
		return this.getChildAt(0).getHeight() - getHeight();

	}

}
