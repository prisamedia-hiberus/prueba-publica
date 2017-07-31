package com.diarioas.guialigas.utils.imageutils;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author doriancussen
 */
public class ImageViewBottomCrop extends ImageViewCrop {
	public ImageViewBottomCrop(Context context) {
		super(context);
		setScaleType(ScaleType.MATRIX);
		isTop = false;
	}

	public ImageViewBottomCrop(Context context, AttributeSet attrs) {
		super(context, attrs);
		setScaleType(ScaleType.MATRIX);
		isTop = false;
	}

	public ImageViewBottomCrop(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setScaleType(ScaleType.MATRIX);
		isTop = false;
	}

}
