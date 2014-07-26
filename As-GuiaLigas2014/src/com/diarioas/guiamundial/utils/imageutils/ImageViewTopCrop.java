package com.diarioas.guiamundial.utils.imageutils;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @author doriancussen
 */
public class ImageViewTopCrop extends ImageViewCrop {
	public ImageViewTopCrop(Context context) {
		super(context);
		setScaleType(ScaleType.MATRIX);
		isTop = true;
	}

	public ImageViewTopCrop(Context context, AttributeSet attrs) {
		super(context, attrs);
		setScaleType(ScaleType.MATRIX);
		isTop = true;
	}

	public ImageViewTopCrop(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setScaleType(ScaleType.MATRIX);
		isTop = true;
	}

}
